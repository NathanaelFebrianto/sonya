package org.sonya.user.service;

import java.util.List;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.sonya.service.GenericManager;
import org.sonya.user.model.User;


/**
 * Business Service Interface to handle communication between web and
 * persistence layer.
 *
 * @author YoungGue Bae
 */
public interface UserManager extends GenericManager<User, String> {

    /**
     * Retrieves a list of users, filtering with parameters on a user object.
     * 
     * @param user parameters to filter on
     * @return List
     */
    List getUsers(User user);
    
	/**
     * Retrieves a user by userId.  An exception is thrown if user not found
     *
     * @param userId the identifier for the user
     * @return User
     */
    User getUser(String userId);

    /**
     * Finds a user by their username.
     * 
     * @param username the user's username used to login
     * @return User a user object
     * @throws org.acegisecurity.userdetails.UsernameNotFoundException
     *         exception thrown when user not found
     */
    User getUserByUsername(String username) throws UsernameNotFoundException;
    
    /**
     * Saves a user's information.
     *
     * @param user the user's information
     * @throws UserExistsException thrown when user already exists
     * @return user the updated user object
     */
    User saveUser(User user) throws UserExistsException;

    /**
     * Removes a user from the database by their userId
     *
     * @param userId the user's id
     */
    void removeUser(String userId);
}
