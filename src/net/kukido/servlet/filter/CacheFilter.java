package net.kukido.servlet.filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

public class CacheFilter implements Filter
{
    private String cacheDir;
    private String mimeType;
    private int timeout = 60; // Time interval in seconds.
    private boolean debug = false;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;

        // If there's a problem creating/opening the cache file, bail to non-cached mode.
        try {
            File cacheFile = getCacheFile(req);
            if (isStale(cacheFile) || debug) {
                int httpStatus = updateCache(cacheFile, req, res, chain);
                setPragmaHeaders(res, cacheFile, false);
                res.setContentType(getMimeType(req));
                int bytesWritten = outputCacheFile(cacheFile, res);
                
                if (httpStatus != HttpServletResponse.SC_OK) {
                    cacheFile.delete(); // Hackety-hack, don't talk back.
                }
            }
            else {
                setPragmaHeaders(res, cacheFile, true);
                res.setContentType(getMimeType(req));
                int bytesWritten = outputCacheFile(cacheFile, res);
            }
        }
        catch (Exception e) {
            chain.doFilter(request, response);
            return;
        }
    }
    
    private void setPragmaHeaders(HttpServletResponse res, File cacheFile, boolean cached)
    {
        //Pragma: dmg-cached=(true|false)
        res.setHeader("Pragma", "dmg-cached=" + cached + "; dmg-file-size=" + cacheFile.length());
    }
    
    private int outputCacheFile(File cacheFile, HttpServletResponse res) throws IOException
    {
        FileInputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(cacheFile);
            out = res.getOutputStream();
            byte[] buf = new byte[1024]; // 1k
            int numBytes = 0;
            for (int read = in.read(buf); read > 0; read = in.read(buf)) {
                out.write(buf, 0, read);
                numBytes += read;
            }
            out.flush();
            System.out.println("Wrote " + numBytes + " bytes.");
            return numBytes;
        }
        finally {
            try { in.close(); } catch (Exception ignored) {}
            try { out.close(); } catch (Exception ignored) {}
        }
    }
    
    private String getMimeType(HttpServletRequest req)
    {
        if ("auto".equalsIgnoreCase(this.mimeType)) {
            String path = req.getRequestURI();
            String fileName = (path.contains("/") ? path.substring(path.lastIndexOf("/")) : path);
            String mimeType = req.getSession().getServletContext().getMimeType(fileName);
            
            return mimeType;
        }
        else {
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
        final FileOutputStream fileOut = new FileOutputStream(cacheFile);
        final PrintWriter fileWriter = new PrintWriter(cacheFile);
        try {
            final int[] httpStatus = new int[] { HttpServletResponse.SC_OK }; // Hacking around Java's "final" requirement.
            HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(res) {
                public void sendError(int status) throws IOException {
                    httpStatus[0] = status;
                    super.sendError(status);
                }
                public void sendError(int status, String message) throws IOException {
                    httpStatus[0] = status;
                    super.sendError(status, message);
                }
                public void setStatus(int status) {
                    httpStatus[0] = status;
                    super.setStatus(status);
                }
                public void setStatus(int status, String message) {
                    httpStatus[0] = status;
                    super.setStatus(status, message);
                }
                public ServletOutputStream getOutputStream() {
                    return new ServletOutputStream() {
                        public void write(int arg0) throws IOException {
                            fileOut.write(arg0);
                            fileOut.flush();
                        }
                        
                        public void write(byte[] bytes) throws IOException {
                            fileOut.write(bytes);
                            fileOut.flush();
                        }
                        
                        public void write(byte[] bytes, int start, int len) throws IOException {
                            fileOut.write(bytes, start, len);
                            fileOut.flush();
                        }
                    };
                }
                
                public PrintWriter getWriter() {
                    return fileWriter;
                }
            };
            
            chain.doFilter(req, responseWrapper);
            fileOut.flush();
            fileWriter.flush();
            return httpStatus[0];
        }
        catch (Exception e) {
            throw new ServletException(e);
        }
        finally {
            try { fileOut.close(); } catch (Exception ignored) {}
            try { fileWriter.close(); } catch (Exception ignored) {}
        }
    }
    
    private boolean isStale(File cacheFile) {
        
        if (!cacheFile.exists()) {
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
        
        return stale;
    }

    private File getCacheFile(HttpServletRequest req) throws IOException
    {
        int hashId = getCacheFileIdentifier(req);
        String fileName = "cache_" + hashId;
        File cache = new File(req.getSession().getServletContext().getRealPath(cacheDir));
        
        File file = new File(cache, fileName);
        if (file.exists() && !file.canWrite()) {
            throw new IOException("Unable to write to cache file: " + file.getCanonicalPath());
        }
        
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
        this.cacheDir = conf.getInitParameter("cacheDir");
        this.mimeType = conf.getInitParameter("mimeType");
        this.timeout = Integer.parseInt(conf.getInitParameter("timeout"));
    }

    public void destroy()
    {
        // TODO Auto-generated method stub

    }
}
