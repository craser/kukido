package net.kukido.blog.test.action;

import javax.servlet.ServletException;

import net.kukido.blog.action.ThrowAction;
import org.apache.struts.action.Action;
import org.apache.struts.mock.MockHttpServletRequest;
import org.apache.struts.mock.MockHttpServletResponse;
import org.junit.*;
import static org.junit.Assert.*;

public class ThrowActionTest
{
    @Test
    public void test_execute() throws Exception
    {
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        Action throwAction = new ThrowAction();
        try { 
            throwAction.execute(null, null, req, res);
            fail(); // Should never get here.
        }
        catch (ServletException e) {
            assertTrue(ThrowAction.EXCEPTION_MESSAGE.equals(e.getMessage()));
        }
    }

}
