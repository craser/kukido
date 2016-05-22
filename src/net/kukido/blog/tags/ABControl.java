package net.kukido.blog.tags;

import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Created by craser on 5/22/16.
 */
public class ABControl extends TagSupport
{
    public int doStartTag() {
        return getTest().useControl()
                ? EVAL_BODY_INCLUDE
                : SKIP_BODY;
    }

    public ABTest getTest() {
        for (Tag p = this; p != null; p = p.getParent()) {
            if (p instanceof ABTest) {
                return (ABTest)p;
            }
        }
        throw new IllegalStateException("No parent ABTest found.");
    }
}
