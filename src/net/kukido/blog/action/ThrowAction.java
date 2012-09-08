package net.kukido.blog.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ThrowAction extends Action
{
    static public final String EXCEPTION_MESSAGE = "Damnit, Beavis!";
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        throw new ServletException(EXCEPTION_MESSAGE);
    }
}