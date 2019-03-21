package net.kukido.blog.tags;

import net.kukido.blog.config.DmgConfig;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class GoogleMapScript extends TagSupport
{

    private final String apiKey;
    private final String apiVersion;

    public GoogleMapScript() {
        DmgConfig config = new DmgConfig();
        this.apiKey = config.getProperty("google.api.key");
        this.apiVersion = config.getProperty("google.map.api.version");
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            String tag = new StringBuffer()
                    .append("<script ")
                    .append("src=\"").append(buildUrl()).append("\"")
                    .append("></script>")
                    .toString();
            pageContext.getOut().write(tag);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    private String buildUrl() {
        String url = new StringBuffer()
                .append("https://maps.googleapis.com/maps/api/js")
                .append("?v=").append(apiVersion)
                .append("&key=").append(apiKey)
                .toString();
        return url;
    }
}
