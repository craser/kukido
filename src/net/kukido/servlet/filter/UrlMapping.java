package net.kukido.servlet.filter;

import java.util.regex.*;

public class UrlMapping
{
    private Pattern pattern;
    private String template;

    public UrlMapping(String pattern, String template)
    {
	this.pattern = Pattern.compile(pattern);
	this.template = template;
    }

    public boolean matches(String uri)
    {
	return pattern.matcher(uri).matches();
    }

    public String getResource(String uri)
    {
	return pattern.matcher(uri).replaceFirst(template);
    }
    
    public String toString()
    {
        return "UrlMapping[\"" + pattern + "\" --> \"" + template + "\"]";
    }
	    
}
