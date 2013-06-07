package net.kukido.blog.test.servlet.filter;

import java.io.IOException;
import net.kukido.servlet.filter.UrlMapConfig;
import net.kukido.servlet.filter.UrlMapConfigParser;

import org.junit.*;
import static org.junit.Assert.*;

public class UrlMapConfigParserTest {

	@Test
	public void test_parse() throws IOException 
	{
		String fileName = "/Users/craser/Development/workspace/KukidoBlogTemp/WebContent/WEB-INF/url-map.xml";
		UrlMapConfigParser parser = new UrlMapConfigParser();
		UrlMapConfig conf = parser.parse(fileName);
		assertFalse(conf.getMappings().isEmpty());
	}
}
