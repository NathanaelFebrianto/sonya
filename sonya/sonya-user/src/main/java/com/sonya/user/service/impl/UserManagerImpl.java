package com.sonya.user.service.impl;

import java.util.List;

import javax.persistence.EntityExistsException;

import org.acegisecurity.providers.dao.DaoAuthenticationProvider;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataIntegrityViolationException;

import com.sonya.service.impl.GenericManagerImpl;
import com.sonya.user.dao.UserDao;
import com.sonya.user.model.User;
import com.sonya.user.service.UserExistsException;
import com.sonya.user.service.UserManager;

/**
 * Implementation of UserManager interface.
 *
 * @author YoungGue Bae
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
        return (User) userDao.loadUserByUsername(username);
    }
    
    /**
     * {@inheritDoc}
     */
    public User saveUser(User user) throws UserExistsException {
        // get and prepare password management-related artifacts
        boolean passwordChanged = false;
        if (authenticationProvider != null) {
            PasswordEncoder passwordEncoder = authenticationProvider.getPasswordEncoder();

            if (passwordEncoder != null) {
                // check whether we have to encrypt (or re-encrypt) the password
                // existing user, check password in DB
                String currentPassword = userDao.getUserPassword(user.getUsername());
                if (currentPassword == null) {
                    passwordChanged = true;
                } else {
                    if (!currentPassword.equals(user.getPassword())) {
                        passwordChanged = true;
                    }
                }

                // if password was changed (or new user), encrypt it
                if (passwordChanged) {
                    user.setPassword(passwordEncoder.encodePassword(user.getPassword(), null));
                }
            } else {
                log.warn("PasswordEncoder not set on AuthenticationProvider, skipping password encryption...");
            }
        } else {
            log.warn("AuthenticationProvider not set, skipping password encryption...");

        }
        
        try {
            return userDao.saveUser(user);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            log.warn(e.getMessage());
            throw new UserExistsException("User '" + user.getUsername() + "' already exists!");
        } catch (EntityExistsException e) { // needed for JPA
            e.printStackTrace();
            log.warn(e.getMessage());
            throw new UserExistsException("User '" + user.getUsername() + "' already exists!");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void removeUser(String userId) {
        log.debug("removing user: " + userId);
        userDao.remove(userId);
    }
}
