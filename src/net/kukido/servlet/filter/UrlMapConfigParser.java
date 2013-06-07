package net.kukido.servlet.filter;

import java.util.*;
import java.io.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.text.*;

/**
 * @author  craser
 */
public class UrlMapConfigParser extends DefaultHandler
{
    private Stack<String> state;
    private StringBuffer chars;
    private String pattern;
    private String template;
    private UrlMapConfig conf;
    
    /** Creates a new instance of GpxHandler */
    public UrlMapConfigParser() {
        state = new Stack<String>();
        chars = new StringBuffer();
        conf = new UrlMapConfig();
    }
    
    public UrlMapConfig parse(String fileName)
    	throws IOException
    {

    	FileInputStream in = null;
    	try {
    		File file = new File(fileName);
    		in = new FileInputStream(file);
    		return parse(in);
    	}
    	catch (Exception e) {
    		throw new IOException(e);
    	}
    	finally {
    		try { in.close(); } 
    		catch (Exception ignored) {}
    	}
    }
    
    public UrlMapConfig parse(InputStream in)
    	throws SAXException, IOException
    {
        //XMLReader reader = XMLReaderFactory.createXMLReader();
        // FIXME: This should be soft-coded using a property value
        // to set the class name of the appropriate factory, etc.
    	XMLReader reader = new org.apache.xerces.parsers.SAXParser();
    	reader.setContentHandler(this);
    	reader.parse(new InputSource(in));
    	
    	return conf;
    }
    
    public void startPrefixMapping(String prefix, String uri)
    {
        //System.out.println("Mapping prefix: " + prefix + " => " + uri);
    }
    
    public void error(SAXParseException e)
    {
        System.err.println("'Recoverable' error parsing UrlMapFilter config: " + e.getMessage());
        e.printStackTrace(System.err);
    }
    
    public void fatalError(SAXParseException e)
    {
        System.err.println("Fatal error parsing UrlMapFilter config: " + e.getMessage());
        e.printStackTrace(System.err);
    }
    
    public void warning(SAXParseException e)
    {
        System.err.println("Warning while UrlMapFilter config: " + e.getMessage());
        e.printStackTrace(System.err);
    }
    
    public void startDocument() 
        throws SAXException
    {
        //System.out.println("GpxParser: Starting Document");
    }
    
    public void startElement(String namespaceUrl, String locale, String name, Attributes atts)
        throws SAXException
    {
        //System.out.println("startElement(\"" + name + "\")");
        state.push(name);
        if ("pattern".equals(name)) {
            pattern = "";
            chars = new StringBuffer();
        }
        else if ("template".equals(name)) {
        	template = "";
            chars = new StringBuffer();
        }
    }
    
    public void endElement(String namespaceURI, String localName, String name)
        throws SAXException
    {
        String currentState = (String)state.pop();
        
        if (!currentState.equals(name)) {
            throw new IllegalStateException("ERG!  Expected end of \"" + state.peek() + "\","
                + " found \"" + name + "\"");
        }
        
        
        String val = chars.toString().trim();
        chars = new StringBuffer();
        
        if ("pattern".equals(currentState)) {
        	pattern = val;
        }
        else if ("template".equals(currentState)) {
        	template = val;
        }
        else if ("mapping".equals(currentState)) {
        	UrlMapping mapping = new UrlMapping(pattern, template);
        	conf.addMapping(mapping);
        }
    }
    
    public void characters(char[] buff, int start, int len)
        throws SAXException
    {
        chars.append(buff, start, len);      
    }
}

