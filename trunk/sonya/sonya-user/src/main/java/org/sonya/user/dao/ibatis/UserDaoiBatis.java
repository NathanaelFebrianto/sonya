package org.sonya.user.dao.ibatis;

import java.util.List;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.sonya.dao.ibatis.GenericDaoiBatis;
import org.sonya.sample.model.Person;
import org.sonya.user.dao.UserDao;
import org.sonya.user.model.User;
import org.springframework.orm.ObjectRetrievalFailureException;

/**
 * User Data Access Object for iBatis.
 * 
 * @author YoungGue Bae (Louie)
 */
public class UserDaoiBatis extends GenericDaoiBatis<User, String> implements UserDao, UserDetailsService {

	/**
     * Constructor that sets the entity to User.class.
     */
    public UserDaoiBatis() {
        super(User.class);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        List users = (List<User>)getSqlMapClientTemplate().queryForList("getUsers", null);

        return users;
    }
    
    /**
     * Get user by id.
     *
     * @param userId the user's id
     * @return a populated user object
     */
    @SuppressWarnings("unchecked")
    @Override
    public User get(String userId) {
        User user = (User)getSqlMapClientTemplate().queryForObject("getUser", userId);

        if (user == null) {
            logger.warn("uh oh, user not found...");
            throw new ObjectRetrievalFailureException(User.class, userId);
        }

        return user;
    }
    
    /**
     * {@inheritDoc}
     */
    public User saveUser(final User user) {
        //iBatisDaoUtils.prepareObjectForSaveOrUpdate(user);

        if (user.getId() == null) {
        	String id = (String)getSqlMapClientTemplate().insert("addUser", user);
            user.setId(id);
        }

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
    @Override
    public void remove(String userId) {
        getSqlMapClientTemplate().update("deleteUser", userId);
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         User user = (User)getSqlMapClientTemplate().queryForObject("getUserByUsername", username);

         if (user == null) {
             logger.warn("uh oh, user not found...");
             throw new UsernameNotFoundException("user '" + username + "' not found...");
         }
         
         return user;
     }

    /**
     * {@inheritDoc}
     */
    public String getUserPassword(String username) {
        return (String)getSqlMapClientTemplate().queryForObject("getUserPassword", username);
    }
}
