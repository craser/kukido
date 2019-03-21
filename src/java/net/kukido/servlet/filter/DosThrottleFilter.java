/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.kukido.servlet.filter;

import javax.servlet.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author craser
 */
public class DosThrottleFilter implements Filter 
{
    private Map<RequestProfile, RequestProfile> profileMap;
    private boolean debug = false;
    
    private long triggerThreshhold;
    private long maxDelay;
    private long delayIncrement;
    private String method; // GET, POST, ALL
    
    public DosThrottleFilter() {
        profileMap = new ConcurrentHashMap<RequestProfile, RequestProfile>();
    }
    
    public void destroy()
    {
    }
    
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
	throws java.io.IOException, javax.servlet.ServletException
    {
        Date now = new Date();
        try {
            if (matchesMethod(((HttpServletRequest)req).getMethod())) {
                RequestProfile rp = new RequestProfile(req);
                if (profileMap.containsKey(rp)) {
                    RequestProfile prev = profileMap.get(rp);
                    
                    if (now.getTime() < prev.getExpires()) { // Make sure it hasn't already expired.
                        long delay = prev.getDelay() + delayIncrement;
                        rp.setDelay(delay);
                        profileMap.put(rp, rp);
                        
                        if (delay > maxDelay) {
                            debug("Killing connection.");
                            return; // Short-circuit
                        }
                        
                        delay(now, rp.getDelay());
                    }
                    else {
                        debug("Already done: " + new Date(prev.getExpires()));
                    }
                }
                else {
                    rp.setDelay(triggerThreshhold);
                    profileMap.put(rp, rp);
                }
            }
            chain.doFilter(req, res);
        }
        finally 
        {
            for (RequestProfile rp : profileMap.keySet()) {
                if (now.getTime() > rp.getExpires()) {
                    debug("    expired: " + rp);
                    profileMap.remove(rp);
                }
            }
        }
    }
    
    private void delay(Date now, long delay)
    {
        //long targetTime = now.getTime() + totalDelay;
        debug("Sleeping for " + delay + "ms.");
        //while (new Date().getTime() < targetTime) {
            try { 
                Thread.sleep(delay); 
            }
            catch (InterruptedException e) {
                debug("Caught exception while trying to sleep.", e);
                //break; // Just for safety.  Might not be the Right Thing.
            }
        //}
        if (debug) {
            long newNow = new Date().getTime();
            long actualDelay = newNow - now.getTime();
            debug("Slept: " + actualDelay + "ms.");
        }
    }
    
    private boolean matchesMethod(String m)
    {
        return this.method.equalsIgnoreCase("ALL") || method.equalsIgnoreCase(m);
    }
    
    
    public void init(javax.servlet.FilterConfig config)
	throws javax.servlet.ServletException
    {
        try {
            debug = new Boolean(config.getInitParameter("debug")).booleanValue();
            triggerThreshhold = Long.parseLong(config.getInitParameter("triggerThreshhold"));
            maxDelay = Long.parseLong(config.getInitParameter("maxDelay"));
            delayIncrement = Long.parseLong(config.getInitParameter("delayIncrement"));
            method = config.getInitParameter("method");
            
            debug("Initialized DosThrottleFilter:");
            debug("    triggerThreshold: " + triggerThreshhold);
            debug("    delayIncrement  : " + delayIncrement);
            debug("    method          : " + method);
        }
        catch (Exception e)
        {
            debug("Error initializing DosThrottleFilter", e);
        }
    }

    private void debug(String message, Throwable t)
    {
	debug(message);
	if (debug) t.printStackTrace();
    }

    private void debug(String message)
    {
	if (debug) System.out.println(message);
    }

}
