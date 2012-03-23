package net.kukido.servlet.filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
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
        File cacheFile = null;

        // If there's a problem creating/opening the cache file, bail to non-cached mode.
        try {
            cacheFile = getCacheFile(req);
            if (isStale(cacheFile) || debug) {
                updateCache(cacheFile, req, res, chain);
            }
            outputCacheFile(cacheFile, res);
        }
        catch (Exception e) {
            chain.doFilter(request, response);
            return;
        }
    }
    
    private void outputCacheFile(File cacheFile, HttpServletResponse res) throws IOException
    {
        FileInputStream in = null;
        OutputStream out = null;
        try {
            res.setContentType(mimeType);
            in = new FileInputStream(cacheFile);
            out = res.getOutputStream();
            byte[] buf = new byte[1024]; // 1k
            for (int read = in.read(buf); read > 0; read = in.read(buf)) {
                out.write(buf, 0, read);
            }
        }
        finally {
            try { in.close(); } catch (Exception ignored) {}
            try { out.close(); } catch (Exception ignored) {}
        }
    }
    
    private void updateCache(File cacheFile, HttpServletRequest req, HttpServletResponse res, FilterChain chain)
        throws IOException, ServletException
    {
        final FileOutputStream fileOut = new FileOutputStream(cacheFile);
        final PrintWriter fileWriter = new PrintWriter(cacheFile);
        try {
            HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(res) {
                public ServletOutputStream getOutputStream() {
                    return new ServletOutputStream() {
                        public void write(int arg0) throws IOException {
                            fileOut.write(arg0);
                        }
                        
                        public void write(byte[] bytes) throws IOException {
                            fileOut.write(bytes);
                        }
                        
                        public void write(byte[] bytes, int start, int len) throws IOException {
                            fileOut.write(bytes, start, len);
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
        String url = req.getRequestURL().toString();
        int hashCode = url.hashCode();
        String fileName = "cache_" + hashCode;
        File cache = new File(req.getSession().getServletContext().getRealPath(cacheDir));
        
        File file = new File(cache, fileName);
        if (file.exists() && !file.canWrite()) {
            throw new IOException("Unable to write to cache file: " + file.getCanonicalPath());
        }
        
        return file;        
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
