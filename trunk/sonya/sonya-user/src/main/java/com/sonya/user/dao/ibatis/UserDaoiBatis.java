package com.sonya.user.dao.ibatis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.orm.ObjectRetrievalFailureException;

import com.sonya.dao.ibatis.GenericDaoiBatis;
import com.sonya.user.dao.UserDao;
import com.sonya.user.model.Role;
import com.sonya.user.model.User;

/**
 * User Data Access Object for iBatis.
 * 
 * @author YoungGue Bae
 */
public class UserDaoiBatis extends GenericDaoiBatis<User, String> implements UserDao, UserDetailsService {

	/**
     * Constructor that sets the entity to User.class.
     */
    public UserDaoiBatis() {
        super(User.class);
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
        User user = (User) getSqlMapClientTemplate().queryForObject("getUser", userId);

        if (user == null) {
            logger.warn("uh oh, user not found...");
            throw new ObjectRetrievalFailureException(User.class, userId);
        } else {
            List roles = getSqlMapClientTemplate().queryForList("getUserRoles", user);
            user.setRoles(new HashSet<Role>(roles));
        }

        return user;
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        List users = getSqlMapClientTemplate().queryForList("getUsers", null);

        // get the roles for each user
        for (int i = 0; i < users.size(); i++) {
            User user = (User) users.get(i);

            List roles =  getSqlMapClientTemplate().queryForList("getUserRoles", user);
            user.setRoles(new HashSet<Role>(roles));
            users.set(i, user);
        }

        return users;
    }

    /**
     * Convenience method to delete roles
     * @param userId the id of the user to delete
     */
    private void deleteUserRoles(final String userId) {
        getSqlMapClientTemplate().update("deleteUserRoles", userId);
    }

    private void addUserRoles(final User user) {
        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                Map<String, String> newRole = new HashMap<String, String>();
                newRole.put("userId", user.getId());
                newRole.put("roleId", role.getId());

                List userRoles = getSqlMapClientTemplate().queryForList("getUserRoles", user);
                if (userRoles.isEmpty()) {
                    getSqlMapClientTemplate().update("addUserRole", newRole);
                }
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public User saveUser(final User user) {
        //iBatisDaoUtils.prepareObjectForSaveOrUpdate(user);

        if (user.getId() == null) {
            String id = (String) getSqlMapClientTemplate().insert("addUser", user);
            user.setId(id);
            addUserRoles(user);
        } else {
            getSqlMapClientTemplate().update("updateUser", user);
            deleteUserRoles(user.getId());
            addUserRoles(user);
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
         User user = (User) getSqlMapClientTemplate().queryForObject("getUserByUsername", username);

         if (user == null) {
             logger.warn("uh oh, user not found...");
             throw new UsernameNotFoundException("user '" + username + "' not found...");
         } else {
        	 try {
             List roles = getSqlMapClientTemplate().queryForList("getUserRoles", user);
             user.setRoles(new HashSet<Role>(roles));
        	 } catch (Exception e) {
        		 logger.error(e);
        	 }
         }
         
         return user;
     }

    /**
     * {@inheritDoc}
     */
    public String getUserPassword(String username) {
        return (String) getSqlMapClientTemplate().queryForObject("getUserPassword", username);
    }
}
