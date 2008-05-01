package org.sonya.user.dao.ibatis;

import java.util.List;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.sonya.dao.ibatis.GenericDaoiBatis;
import org.sonya.user.dao.UserDao;
import org.sonya.user.model.User;

/**
 * User Data Access Object for iBatis.
 * 
 * @author YoungGue Bae (Louie)
 */
public class UserDaoiBatis extends GenericDaoiBatis<User, String> implements UserDao {

	/**
     * Constructor that sets the entity to User.class.
     */
    public UserDaoiBatis() {
        super(User.class);
    }

    /**
     * {@inheritDoc}
     */
     public List<User> getUsers() {
    	 return null;
    }
    
    /**
     * Saves a user's information.
     * 
     * @param user the object to be saved
     * @return the user object
     */
    public User saveUser(User user) {
    	log.debug("user's id: " + user.getId());
    	return user;
    }
    
    /**
     * Overridden simply to call the saveUser method. This is happenening 
     * because saveUser flushes the session.
     *
     * @param user the user to save
     * @return the modified user (with a primary key set if they're new)
     */
    @Override
    public User save(User user) {
        return this.saveUser(user);
    }

    /** 
     * {@inheritDoc}
    */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List users = null;
        if (users == null || users.isEmpty()) {
            throw new UsernameNotFoundException("user '" + username + "' not found...");
        } else {
            return (UserDetails) users.get(0);
        }
    }
}
