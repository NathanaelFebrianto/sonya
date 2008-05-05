package org.sonya.user.webapp.action;

import java.io.Serializable;
import java.util.List;

import org.sonya.user.model.User;
import org.sonya.user.service.UserManager;
import org.sonya.webapp.action.BasePage;

/**
 * JSF Page class to handle user.
 *
 * @author mraible
 */
public class UserBean extends BasePage implements Serializable {
	private UserManager userManager;
	
	public void setUserManager(UserManager manager) {
	        this.userManager = manager;
	}
	
    public List getUsers() {
        return sort(userManager.getUsers(new User()));
    }
}
