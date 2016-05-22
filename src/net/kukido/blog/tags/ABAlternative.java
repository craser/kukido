package net.kukido.blog.tags;

/**
 * Created by craser on 5/22/16.
 */
public class ABAlternative extends ABControl
{
    public int doStartTag() {
        return getTest().useAlternative()
                ? EVAL_BODY_INCLUDE
                : SKIP_BODY;
    }
}
