package net.kukido.servlet.filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import net.kukido.blog.log.Logging;

import org.apache.log4j.Logger;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

public class CacheFilter implements Filter
{
    private enum CacheBehavior {CACHED, UNCACHED, BYPASSED};
    private Logger log;
    private String cacheDir;
    private String mimeType;
    private int timeout = 60; // Time interval in seconds.

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        log.info("URL: " + ((HttpServletRequest)request).getRequestURL());
        if ("true".equalsIgnoreCase(request.getParameter("nocache"))) {
            log.info("Request parameter \"nocache\" set to \"true\".  Bypassing caching filter.");
            chain.doFilter(request, response);
        }
        else if(((HttpServletRequest)request).getSession().getAttribute("user") != null) {
            log.info("User is logged in.  Bypassing caching filter.");
            chain.doFilter(request, response);
        }
        else {
            log.info("Caching: " + ((HttpServletRequest)request).getRequestURL());
            doFilterCached(request, response, chain);
        }
    }
    
    public void doFilterCached(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;
        File cacheFile = null;
        // If there's a problem creating/opening the cache file, bail to non-cached mode.
        try {
            cacheFile = getCacheFile(req);
            if (cacheFile.length() == 0 || isStale(cacheFile) || req.getParameter("nocache") == "true") {
                log.info("Refreshing cached file for URL: " + req.getRequestURL());
                int httpStatus = updateCache(cacheFile, req, res, chain);
                
                if (httpStatus != HttpServletResponse.SC_OK) {
                    log.debug("Deleting cache file: " + cacheFile.getName());
                    cacheFile.delete(); // Hackety-hack, don't talk back.
                }
            }
            else {
                log.info("Using cached output for URL: " + req.getRequestURL());
                res.setContentType(getMimeType(req));
                outputCacheFile(cacheFile, res);
            }
        }
        catch (Exception e) {
            log.info("Error in doFilterCached()", e);
            log.info("Deleting cache file " + cacheFile.getName());
            if (cacheFile != null) cacheFile.delete();
            throw new ServletException(e);
        }
    }
    
    private void outputCacheFile(File cacheFile, HttpServletResponse res) throws IOException
    {
        FileInputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(cacheFile);
            out = res.getOutputStream();
            byte[] buf = new byte[1024]; // 1k
            int bytesWritten = 0;
            for (int read = in.read(buf); read > 0; read = in.read(buf)) {
                out.write(buf, 0, read);
                bytesWritten += read;
            }
            out.flush();
            log.debug("Cache file size: " + cacheFile.length() + " bytes.");
            log.debug("Wrote " + bytesWritten + " bytes to response output stream.");
        }
        finally {
            try { in.close(); } catch (Exception ignored) {}
            try { 
                log.debug("Closing output stream.");
                out.close(); 
            } 
            catch (Exception ignored) {}
        }
    }
    
    private String getMimeType(HttpServletRequest req)
    {
        if ("auto".equalsIgnoreCase(this.mimeType)) {
            String path = req.getRequestURI();
            String fileName = (path.contains("/") ? path.substring(path.lastIndexOf("/")) : path);
            String mimeType = req.getSession().getServletContext().getMimeType(fileName);
            log.debug("Determined mime type \"" + mimeType + "\" from file name \"" + fileName + "\"");
            return mimeType;
        }
        else {
            log.debug("Configured mime type: \"" + mimeType + "\"");
            return this.mimeType;
        }
            
    }

    /**
     * 
     * @param cacheFile
     * @param req
     * @param res
     * @param chain
     * @return Returns the HTTP status code from the response.
     * @throws IOException
     * @throws ServletException
     */
    private int updateCache(File cacheFile, HttpServletRequest req, HttpServletResponse res, FilterChain chain)
        throws IOException, ServletException
    {
        FileOutputStream fileOut = new FileOutputStream(cacheFile);
        PrintWriter writer = new PrintWriter(fileOut);
        PrintStream stream = new PrintStream(cacheFile);
        try {
            CacheResponseWrapper wrapper = new CacheResponseWrapper(res, writer, stream);
            log.debug("Calling down the chain...");
            chain.doFilter(req, wrapper);
            log.debug("Cache update returned HTTP status " + wrapper.getHttpStatus());
            return wrapper.getHttpStatus();
        }
        catch (Exception e) {
            log.error("Error creating cache file " + cacheFile.getName(), e);
            throw new ServletException(e);
        }
        finally {
            try {
                stream.flush();
                stream.close();
            }
            catch (Exception ignored) {}
            try {
                log.debug("Closing FileWriter");
                writer.flush();
                writer.close(); 
            } 
            catch (Exception ignored) {}
        }
    }
    
    private boolean isStale(File cacheFile) {
        
        if (!cacheFile.exists()) {
            log.debug("Cache file " + cacheFile.getName() + " does not exist.");
            return true;
        }
        
        // Check for changes <timeout> seconds after the last time we cached
        // the file.
        Calendar check = Calendar.getInstance();
        check.setTime(new Date(cacheFile.lastModified()));
        check.add(Calendar.SECOND, timeout);

        // Figure out what time it is now, and see if we need to check
        // again.
        Calendar now = Calendar.getInstance(); // Right now.
        boolean stale = now.after(check);
        
        log.debug("Cache file " + cacheFile.getName() + " is " + (stale ? "" : "not") + " older than " + timeout + " seconds.");
        return stale;
    }

    private File getCacheFile(HttpServletRequest req) throws IOException
    {
        int cacheId = getCacheFileIdentifier(req);
        String fileName = "cache_" + cacheId;
        
        File file = new File(cacheDir, fileName);
        if (file.exists() && !file.canWrite()) {
            throw new IOException("Unable to write to cache file: " + file.getCanonicalPath());
        }
        
        log.debug("Cache file: " + file);
        return file;        
    }

    private int getCacheFileIdentifier(HttpServletRequest req)
    {
        StringBuffer url = new StringBuffer(req.getRequestURL().toString());
        List<String> paramNames = new ArrayList<String>(req.getParameterMap().keySet());
        Collections.sort(paramNames);
        url.append("?");
        for (String p : paramNames) {
            for (String v : req.getParameterValues(p)) {
                url.append(p).append("=").append(v).append("&");
            }
        }
        int hashCode = url.toString().hashCode(); // Not sure if it's strictly necessary to convert to String first.
        return hashCode;
    }

    public void init(FilterConfig conf) throws ServletException
    {
        this.cacheDir = conf.getServletContext().getRealPath(conf.getInitParameter("cacheDir"));
        this.mimeType = conf.getInitParameter("mimeType");
        this.timeout = Integer.parseInt(conf.getInitParameter("timeout"));
        
        log = Logging.getLogger(getClass().getName() + "." + conf.getFilterName());
        log.info("CacheFilter \"" + conf.getFilterName() + "\" initialized.");
        log.debug("cache dir: " + cacheDir);
        log.debug("mime type: " + mimeType);
        log.debug("timeout: " + timeout + " seconds");
    }

    public void destroy()
    {
        // TODO Auto-generated method stub

    }
}
