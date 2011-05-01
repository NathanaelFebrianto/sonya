/*
 * Copyright (c) 2011, Young-Gue Bae
 * All rights reserved.
 */
package com.beeblz.twitter.io.service;

import org.apache.ibatis.session.SqlSession;

import com.beeblz.twitter.io.GenericManagerImpl;
import com.beeblz.twitter.io.dao.UserMapper;
import com.beeblz.twitter.io.model.User;

/**
 * A implementation for user manager.
 * 
 * @author Young-Gue Bae
 */
public class UserManagerImpl extends GenericManagerImpl implements UserManager {

	//private UserMapper mapper;
	
	/**
     * Constructor.
     *
     */
    public UserManagerImpl() {
    }
	
	/**
     * Constructor.
     *
     * @param mapper the user mapper
     */
    public UserManagerImpl(UserMapper mapper) {
        //this.mapper = mapper;
    }
    
    /**
     * Adds a user.
     *
     * @param user the user
     */
	public void addUser(User user) {
		//mapper.insertUser(user);

		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		UserMapper mapper = session.getMapper(UserMapper.class);
    		mapper.insertUser(user);
    		session.commit();
    	} finally {
    		session.close();
    	}
	}
	
}
