package net.kukido.servlet.filter;

import java.util.ArrayList;
import java.util.List;

public class UrlMapConfig 
{
	private List<UrlMapping> mappings;
	
	public UrlMapConfig() {
		this.mappings = new ArrayList<UrlMapping>();
	}
	
	public List<UrlMapping> getMappings() {
		return new ArrayList<UrlMapping>(mappings);
	}
	
	public void addMapping(UrlMapping mapping) {
		mappings.add(mapping);
	}

}
