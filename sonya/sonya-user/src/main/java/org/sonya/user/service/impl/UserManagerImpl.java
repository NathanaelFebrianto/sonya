package org.sonya.user.service.impl;

import java.util.List;

import org.acegisecurity.providers.dao.DaoAuthenticationProvider;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.sonya.service.impl.GenericManagerImpl;
import org.sonya.user.dao.UserDao;
import org.sonya.user.model.User;
import org.sonya.user.service.UserExistsException;
import org.sonya.user.service.UserManager;
import org.springframework.beans.factory.annotation.Required;

/**
 * Implementation of UserManager interface.
 *
 * @author YoungGue Bae (Louie)
 */
public class UserManagerImpl extends GenericManagerImpl<User, String> implements UserManager {
    private UserDao userDao;
    private DaoAuthenticationProvider authenticationProvider;

    /**
     * Constructor that sets user manager.
     */
    public UserManagerImpl(UserDao userDao) {
        super(userDao);
        this.userDao = userDao;
    }    

    /**
     * Set the Dao for communication with the data layer.
     * 
     * @param dao the UserDao that communicates with the database
     */
    @Required
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
    
    /**
     * Set the DaoAuthenticationProvider object that will provide both the
     * PasswordEncoder and the SaltSource which will be used for password
     * encryption when necessary.
     * 
     * @param authenticationProvider the DaoAuthenticationProvider object
     */
    @Required
    public void setAuthenticationProvider(DaoAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    /**
     * {@inheritDoc}
     */
    public List<User> getUsers(User user) {
    	return userDao.getUsers();
    }
    
    /**
     * {@inheritDoc}
     */
    public User getUser(String userId) {
        return userDao.get(userId);
    }
    
    /**
     * {@inheritDoc}
     * 
     * @param username the login name of the human
     * @return User the populated user object
     * @throws UsernameNotFoundException thrown when username not found
     */
    public User getUserByUsername(String username) throws UsernameNotFoundException {
        return (User)userDao.loadUserByUsername(username);
    }
    
    /**
     * {@inheritDoc}
     */
    public User saveUser(User user) throws UserExistsException {
    	return null;
    }

    /**
     * {@inheritDoc}
     */
    public void removeUser(String userId) {
        log.debug("removing user: " + userId);
        userDao.remove(userId);
    }
}
