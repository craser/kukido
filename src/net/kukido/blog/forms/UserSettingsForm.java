/*
 * UserSettingsForm.java
 *
 * Created on September 3, 2007, 12:58 PM
 */

package net.kukido.blog.forms;

import net.kukido.blog.datamodel.User;
import net.kukido.blog.dataaccess.*;
import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import javax.servlet.http.*;
import java.net.URL;

/**
 *
 * @author  craser
 */
public class UserSettingsForm extends ActionForm
{    
    private User user;
    private String password;
    private String newPassword;
    private String confirmNewPassword;
    
    /** Creates a new instance of UserSettingsForm */
    public UserSettingsForm() {
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest req)
    {
	ActionErrors errors = new ActionErrors();
        try
        {
            // First, validate the username and password.  This might throw
            // a DataAccessException, caught below.
            UserDao userDao = new UserDao();
            User validatedUser = userDao.findByUserNamePassword(user.getUserName(), password);
            
            // Now that we've validated the user, do some actual processing.
            if (newPassword != null && !"".equals(newPassword)) { 
                // If we're resetting the password, double check that the 
                // newPassword and confirmNewPassword values match.
                if (newPassword.equals(confirmNewPassword)) {
                    user.setPassword(newPassword);
                }
                else {
                    // If the password and confirmation don't match, indicate an error.
                    errors.add("newPassword", new ActionMessage("error.mismatched.passwords"));
                }
            }
            else {
                // If we're keeping the old password, then set that in the User bean.
                user.setPassword(password);
            }
        }
        catch (DataAccessException e)
        {
            errors.add("password", new ActionMessage("error.invalid.login"));
        }
        
	return errors;
    }
    
    public void reset(ActionMapping mapping, HttpServletRequest req)
    {
        // Always get the original User profile from the session.
        user = (User)req.getSession().getAttribute("user");
    }    
    
    /**
     * Getter for property user.
     * @return Value of property user.
     */
    public net.kukido.blog.datamodel.User getUser() {
        return user;
    }
    
    /**
     * Setter for property user.
     * @param user New value of property user.
     */
    public void setUser(net.kukido.blog.datamodel.User user) {
        this.user = user;
    }
    
    /**
     * Getter for property password.
     * @return Value of property password.
     */
    public java.lang.String getPassword() {
        return password;
    }
    
    /**
     * Setter for property password.
     * @param password New value of property password.
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }
    
    /**
     * Getter for property newPassword.
     * @return Value of property newPassword.
     */
    public java.lang.String getNewPassword() {
        return newPassword;
    }
    
    /**
     * Setter for property newPassword.
     * @param newPassword New value of property newPassword.
     */
    public void setNewPassword(java.lang.String newPassword) {
        this.newPassword = newPassword;
    }
    
    /**
     * Getter for property confirmNewPassword.
     * @return Value of property confirmNewPassword.
     */
    public java.lang.String getConfirmNewPassword() {
        return confirmNewPassword;
    }
    
    /**
     * Setter for property confirmNewPassword.
     * @param confirmNewPassword New value of property confirmNewPassword.
     */
    public void setConfirmNewPassword(java.lang.String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
    
}
