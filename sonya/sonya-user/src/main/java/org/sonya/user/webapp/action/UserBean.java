package org.sonya.user.webapp.action;

import java.io.Serializable;
import java.util.List;

import org.sonya.service.GenericManager;
import org.sonya.webapp.action.BasePage;

/**
 * JSF Page class to handle user.
 *
 * @author mraible
 */
public class UserBean extends BasePage implements Serializable {
	private GenericManager userManager;
	
	public void setPersonManager(GenericManager manager) {
	        this.userManager = manager;
	}
	
    public List getUsers() {
        return sort(userManager.getAll());
    }
}
