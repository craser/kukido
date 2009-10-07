/*
 * UserUpdate.java
 *
 * Created on September 3, 2007, 3:39 PM
 */

package net.kukido.blog.action;


import net.kukido.blog.dataaccess.*;
import net.kukido.blog.datamodel.*;
import net.kukido.blog.forms.*;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;

/**
 *
 * @author  craser
 */
public class UserUpdate extends Action 
{
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws ServletException
    {
        try
        {
            UserSettingsForm userForm = (UserSettingsForm)form;
            UserDao userDao = new UserDao();
            User user = userForm.getUser();
            user = userDao.update(user, userForm.getPassword());
            req.getSession().setAttribute("user", user);
            
            return mapping.findForward("success");
        }
        catch (Exception e)
        {
            throw new ServletException(e);
        }
    }
    
    /** Creates a new instance of UserUpdate */
    public UserUpdate() {
    }
    
}