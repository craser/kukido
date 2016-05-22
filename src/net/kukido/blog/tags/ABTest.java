package net.kukido.blog.tags;

import javax.servlet.jsp.tagext.TagSupport;
import java.util.Date;

/**
 * Created by craser on 5/22/16.
 */
public class ABTest extends TagSupport
{
    static public final int VARIANT_CONTROL = 0;
    static public final int VARIANT_ALTERNATIVE = 1;

    private String testId = null;
    private int variant = -1;

    public int doStartTag() {
        variant = ((new Date().getTime() % 2) == 0)
                ? VARIANT_CONTROL
                : VARIANT_ALTERNATIVE;

        return EVAL_BODY_INCLUDE;
    }

    public boolean useControl() {
        return variant == VARIANT_CONTROL;
    }

    public boolean useAlternative() {
        return variant == VARIANT_ALTERNATIVE;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }


}
