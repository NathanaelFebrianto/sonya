package com.sonya.user.dao;

import java.util.List;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sonya.dao.GenericDao;
import com.sonya.user.model.User;

/**
 * User Data Access Object(GenericDao) interface.
 * 
 * @author YoungGue Bae
 */
public interface UserDao extends GenericDao<User, String> {

    /**
     * Gets a list of users ordered by the uppercase version of their username.
     *
     * @return List populated list of users
     */
    List<User> getUsers();
    
    /**
     * Saves a user's information.
     * 
     * @param user the object to be saved
     * @return the user object
     */
    User saveUser(User user);
    
    /**
     * Gets users information based on login name.
     * 
     * @param username the user's username
     * @return userDetails populated userDetails object
     * @throws org.acegisecurity.userdetails.UsernameNotFoundException thrown when user not found in database
     */
    @Transactional
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    /**
     * Retrieves the password in DB for a user.
     * 
     * @param username the user's username
     * @return the password in DB, if the user is already persisted
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    String getUserPassword(String username);
}
