/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.firebird.common.service.GenericManagerImpl;
import org.firebird.io.dao.UserTermMapper;
import org.firebird.io.model.UserTerm;
import org.firebird.io.service.UserTermManager;

/**
 * A implementation for user term manager.
 * 
 * @author Young-Gue Bae
 */
public class UserTermManagerImpl extends GenericManagerImpl implements UserTermManager {

	/**
     * Constructor.
     *
     */
    public UserTermManagerImpl() {
    }

    /**
     * Gets the users.
     *
     * @param websiteId the website id
     * @param term the term
     * @return List<UserTerm> the list of user term
     */
	public List<UserTerm> getUsers(int websiteId, String term) {
		UserTerm param = new UserTerm();
		param.setWebsiteId(websiteId);
		param.setTerm(term);
		
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		UserTermMapper mapper = session.getMapper(UserTermMapper.class);
    		List<UserTerm> users = mapper.selectUsers(param);
    		return users;
    	} finally {
    		session.close();
    	}		
	}
	
	/**
     * Gets the user terms.
     *
     * @param websiteId the website id
     * @param userId the user id
     * @return List<UserTerm> the list of user term
     */
	public List<UserTerm> getTerms(int websiteId, String userId) {
		UserTerm param = new UserTerm();
		param.setWebsiteId(websiteId);
		param.setUserId(userId);
		
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		UserTermMapper mapper = session.getMapper(UserTermMapper.class);
    		List<UserTerm> userTerms = mapper.selectTerms(param);
    		return userTerms;
    	} finally {
    		session.close();
    	}		
	}
	
	/**
     * Adds a user term.
     *
     * @param userTerm the user term
     */
	public void addTerm(UserTerm userTerm) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		UserTermMapper mapper = session.getMapper(UserTermMapper.class);
    		mapper.insertTerm(userTerm);
    		session.commit();
    	} finally {
    		session.close();
    	}			
	}
	
	/**
     * Deletes users.
     *
     * @param websiteId the website id
     */
	public void deleteUsers(int websiteId) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		UserTermMapper mapper = session.getMapper(UserTermMapper.class);
    		mapper.deleteUsers(websiteId);
    		session.commit();
    	} finally {
    		session.close();
    	}		
	}
    
}
