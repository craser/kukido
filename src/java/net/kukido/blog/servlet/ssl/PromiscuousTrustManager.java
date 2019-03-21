/*
 * PromiscuousTrustManager.java
 *
 * Created on November 3, 2006, 9:46 PM
 */

package net.kukido.blog.servlet.ssl;

import javax.net.ssl.*;
import java.security.cert.*;

/**
 *
 * @author  craser
 */
public class PromiscuousTrustManager implements X509TrustManager
{
    
    /** Creates a new instance of PromiscuousTrustManager */
    public PromiscuousTrustManager() 
    {
    }
    
    public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificate, String str) 
        throws CertificateException 
    {
    }
    
    public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificate, String str) 
        throws CertificateException 
    {
    }
    
    public java.security.cert.X509Certificate[] getAcceptedIssuers() 
    {
        return new X509Certificate[0];
    }
    
}
