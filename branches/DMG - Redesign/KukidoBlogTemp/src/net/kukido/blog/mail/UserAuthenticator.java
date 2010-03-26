/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.kukido.blog.mail;

import javax.mail.*;
import net.kukido.blog.datamodel.*;

/**
 *
 * @author craser
 */
public class UserAuthenticator extends Authenticator 
{
    private User user;
    
    public UserAuthenticator(User user)
    {
        this.user = user;
    }
    
    @Override
    public PasswordAuthentication getPasswordAuthentication()
    {
        // FIXME: Need to get these values from this.user.
        return new PasswordAuthentication("chris.raser", "c0r0gati0n");
    }
    
    

}
