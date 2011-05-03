/*
 * Copyright (c) 2011, Young-Gue Bae
 * All rights reserved.
 */
package com.beeblz.twitter.io.service;

import java.util.List;

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
	
    public UserManagerImpl() { }	

	public List<User> getUsers(User user) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		UserMapper mapper = session.getMapper(UserMapper.class);
    		List<User> result = mapper.selectUsers(user);
    		return result;
    	} finally {
    		session.close();
    	}
	}
    
	public void addUser(User user) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		UserMapper mapper = session.getMapper(UserMapper.class);
    		mapper.insertUser(user);
    		session.commit();
    	} finally {
    		session.close();
    	}
	}
	
	public void setUser(User user) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		UserMapper mapper = session.getMapper(UserMapper.class);
    		mapper.updateUser(user);
    		session.commit();
    	} finally {
    		session.close();
    	}		
	}	
}
