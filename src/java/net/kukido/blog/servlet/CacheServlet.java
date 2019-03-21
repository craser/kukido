/*
 * Blogroll.java
 *
 * Created on January 2, 2005, 1:57 PM
 */

package net.kukido.blog.servlet;

import net.kukido.blog.log.Logging;
import net.kukido.blog.servlet.ssl.*;
import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.util.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.Logger;

import java.security.*;

/**
 * @author  craser
 * @version
 */
public class CacheServlet extends HttpServlet
{
	Logger log = Logging.getLogger(getClass());
    
    private String getParameter(String param, ServletRequest req)
    {
        String val = req.getParameter(param);
        if (val == null) {
            val = getInitParameter(param);
        }
        return val;
    }
    
    /**
     * Identical to doGet()
     **/
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        doGet(req, res);
    }
    
    /**
     * Handles the HTTP GET method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
    {      

        String cacheUrl = getParameter("cacheUrl", req);
        boolean isPasswordProtected = new Boolean(getParameter("isPasswordProtected", req)).booleanValue();
        boolean debug = new Boolean(getParameter("debug", req)).booleanValue();
        String username = getParameter("username", req);
        String password = getParameter("password", req);
        String fileName = getParameter("fileName", req);
        String resourcePath = getParameter("resourcePath", req);
        String xsltTemplate = getParameter("xsltTemplate", req);
        int timeout = Integer.parseInt(getParameter("timeout", req)); // Time interval in seconds
        
            
        try
        {

            
            String localPath = getServletContext().getRealPath(fileName);
            File localFile = new File(localPath);            
            if (localFile.exists() && !localFile.canWrite()) 
                throw new IOException("Cannot write to file " + localFile.getCanonicalPath());

            // Check for changes <timeout> seconds after the last time we cached the file.
            Calendar check = Calendar.getInstance();
            check.setTime(new Date(localFile.lastModified()));
            check.add(Calendar.SECOND, timeout);

            // Figure out what time it is now, and see if we need to check again.
            Calendar now = Calendar.getInstance(); // Right now.
            try { 
                if (now.after(check) || debug) 
                    downloadFreshCopy(req, res, localFile, cacheUrl, isPasswordProtected, username, password, xsltTemplate);
            }
            catch (Exception e)
            {
                try { printStackTraceAsComment(e, new PrintWriter(res.getOutputStream())); }
                catch (IllegalStateException ise) { printStackTraceAsComment(e, new PrintWriter(res.getOutputStream())); }
                finally { log("Caught while trying to cache " + cacheUrl, e); }
            }
            
            if (localFile.exists())
            {
                RequestDispatcher disp = req.getRequestDispatcher(resourcePath);
                disp.include(req, res);
            }
        }
        catch (Exception e)
        {
            try { printStackTraceAsComment(e, new PrintWriter(res.getOutputStream())); }
            catch (IllegalStateException ise) { 
                try { printStackTraceAsComment(e, new PrintWriter(res.getOutputStream())); }
                catch (Exception ignored) {}
            }
            finally { log.error("Caught while trying to cache " + cacheUrl, e); }
        }
    }
    
    /**
     * Outputs the error within an HTML comment to the response's output
     * stream/writer.  This is here because my fascist ISP grants access
     * to access and error logs, but evidently not to output logs.  (And
     * why doesn't calling log(String, Throwable) output to System.err? 
     * No clue.  Something to read up on.
     */
    private void printStackTraceAsComment(Throwable t, PrintWriter out)
    {
        out.write("<!--\n");
        t.printStackTrace(out);
        out.write("\n--!>");
        out.flush();
    }
    
    private void printComment(String s, OutputStream out)
        throws IOException
    {
        out.write("<!-- ".getBytes());
        out.write(s.getBytes());
        out.write(" -->\n".getBytes());
    }
    
    private void downloadFreshCopy(HttpServletRequest req, HttpServletResponse res, File localFile
                                    ,String cacheUrl, boolean isPasswordProtected, String username
                                    ,String password, String xsltTemplate)
	throws ServletException, IOException
    {
	InputStream urlIn = null;
        OutputStream fileOut = null;
        File tempLocalFile = null;
	try
	{
            buildUrlStreamHandler();
            URL url = new URL(cacheUrl);
            URLConnection urlConnection = url.openConnection();

            if (isPasswordProtected) 
            {
                // Fascists!
                //this.registerPasswordAuthenticator();
                
                String credentials = username + ":" + password;
                String encodedCredentials = new sun.misc.BASE64Encoder().encode(credentials.getBytes());
                urlConnection.setRequestProperty("Authorization", "Basic " + encodedCredentials);
            }
            
            tempLocalFile = File.createTempFile(localFile.getName(), "temp", localFile.getParentFile());
            tempLocalFile.deleteOnExit();
	    urlIn = urlConnection.getInputStream();
            fileOut = new FileOutputStream(tempLocalFile);
            
            fileOut.write(("<!-- Last updated " + new Date() + " -->\n").getBytes());
            if (new Boolean(getParameter("debug", req)).booleanValue())
            {
                printComment("cacheUrl: " + getParameter("cacheUrl", req), fileOut);
                printComment("isPasswordProtected: " + getParameter("isPasswordProtected", req), fileOut);
                printComment("debug: " + getParameter("debug", req), fileOut);
                printComment("username: " + getParameter("username", req), fileOut);
                printComment("password: " + getParameter("password", req), fileOut);
                printComment("fileName: " + getParameter("fileName", req), fileOut);
                printComment("resourcePath: " + getParameter("resourcePath", req), fileOut);
                printComment("xsltTemplate: " + getParameter("xsltTemplate", req), fileOut);
                printComment("timeout: " + getParameter("timeout", req), fileOut);
            }
            
            if (xsltTemplate != null)
            {
                writeXmlFile(urlIn, fileOut, xsltTemplate);
            }
            else 
            {
                byte[] buf = new byte[256];
                for (int read = urlIn.read(buf); read > 0; read = urlIn.read(buf)) {
                    fileOut.write(buf, 0, read);
                }
            }
            
            // This was just here to test the error handling.
            //if (true) throw new IOException("FIXME!");
            if (localFile.exists()) localFile.delete();
            tempLocalFile.renameTo(localFile);
	}
        catch (ServletException e)
        {
            throw e;
        }
        catch (IOException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new ServletException(e);
        }
	finally
	{
            try { tempLocalFile.delete(); } catch (Exception ignored) {}
	    try { urlIn.close(); }          catch (Exception ignored) {}
            try { fileOut.flush(); }        catch (Exception ignored) {}
            try { fileOut.close(); }        catch (Exception ignored) {}
	}
    }
    
    private void writeXmlFile(InputStream in, OutputStream out, String xsltTemplate)
        throws ServletException
    {
        try
        {
            //byte[] b = new byte[256];
            //int r = 0;
            //while ((r = in.read(b)) > 0) { System.out.write(b, 0, r); }
            
            String templatePath = getServletContext().getRealPath(xsltTemplate);
            Source xslt = new StreamSource(new FileInputStream(templatePath));
            Source xml = new StreamSource(in);
            Result result = new StreamResult(out);
            Transformer transformer = TransformerFactory.newInstance().newTransformer(xslt);
            transformer.transform(xml, result);
            out.flush();
        }
        catch (Exception t)
        {
            log.error(t);
            throw new ServletException(t);
        }
    }
    
    /**
     * This code works, but my fascist ISP won't let me register a new Authenticator.
     * So I'll have to find a way around.
     **/
    private void registerPasswordAuthenticator(final String username, final String password)
    {
        Authenticator pwAuth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }            
        };
        Authenticator.setDefault(pwAuth);
    }
    
    static private void buildUrlStreamHandler()
        throws NoSuchAlgorithmException, KeyManagementException
    {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[] { new PromiscuousTrustManager() }, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }
}
