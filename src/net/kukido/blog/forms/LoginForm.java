package net.kukido.blog.forms;

/**
 * LoginForm bean to handle logging in.
 *
 * @author  craser
 **/

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import net.kukido.blog.dataaccess.*;

public class LoginForm extends org.apache.struts.action.ActionForm {
    
    private String username;    
    private String password;
    private boolean remember;

    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setRemember(boolean remember)
    {
	this.remember = remember;
    }

    public boolean getRemember()
    {
	return remember;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest req) 
    {
        ActionErrors errors = new ActionErrors();
        
        try { new UserDao().findByUserNamePassword(username, password); }
        catch (DataAccessException e)
	{
            errors.add("username", new ActionMessage("error.invalid.login"));
        }
      
        return errors;
    }
    
    public void reset(ActionMapping actionMapping, HttpServletRequest req) 
    {
        setUsername(null);
        setPassword(null);
    }
    
}
