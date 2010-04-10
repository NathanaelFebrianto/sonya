/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.firebird.common.service.GenericManagerImpl;
import org.firebird.io.dao.UserBlogEntryMapper;
import org.firebird.io.model.UserBlogEntry;
import org.firebird.io.service.UserBlogEntryManager;

/**
 * A implementation for user blog entry manager.
 * 
 * @author Young-Gue Bae
 */
public class UserBlogEntryManagerImpl extends GenericManagerImpl implements UserBlogEntryManager {

	//private UserBlogEntryMapper mapper;

	/**
     * Constructor.
     *
     */
    public UserBlogEntryManagerImpl() {
    }
	
	/**
     * Constructor.
     *
     * @param mapper the user blog entry mapper
     */
    public UserBlogEntryManagerImpl(UserBlogEntryMapper mapper) {
        //this.mapper = mapper;
    }

    /**
     * Gets the distinct users.
     *
     * @return List<String> the list of user id
     */
	public List<String> getDistinctUsers() {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		UserBlogEntryMapper mapper = session.getMapper(UserBlogEntryMapper.class);
    		List<String> userIds = mapper.selectDistinctUsers();
    		return userIds;
    	} finally {
    		session.close();
    	}
	}
	
    /**
     * Gets the user blog entries.
     *
     * @param userId the user id
     * @return List<UserBlogEntry> the list of user blog entry
     */
	public List<UserBlogEntry> getUserBlogEntries(String userId) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		UserBlogEntryMapper mapper = session.getMapper(UserBlogEntryMapper.class);
    		List<UserBlogEntry> blogEntries = mapper.selectUserBlogEntries(userId);
    		return blogEntries;
    	} finally {
    		session.close();
    	}		
	}
    
    /**
     * Adds a user blog entry.
     *
     * @param userBlogEntry the user blog entry
     */
	public void addUserBlogEntry(UserBlogEntry userBlogEntry) {
		//mapper.insertUserBlogEntry(userBlogEntry);
		
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		UserBlogEntryMapper mapper = session.getMapper(UserBlogEntryMapper.class);
    		mapper.insertUserBlogEntry(userBlogEntry);
    		session.commit();
    	} finally {
    		session.close();
    	}
	}

    /**
     * Deletes user blog entries.
     *
     * @param websiteId the website id
     * @param userId the user id
     * @param blogEntryId the blog entry id
     */
	public void deleteUserBlogEntry(int websiteId, String userId, String blogEntryId) {
		UserBlogEntry userBlogEntry = new UserBlogEntry();
		userBlogEntry.setWebsiteId(websiteId);
		userBlogEntry.setUserId(userId);
		userBlogEntry.setBlogEntryId(blogEntryId);
		
		//mapper.deleteUserBlogEntry(userBlogEntry);
		
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		UserBlogEntryMapper mapper = session.getMapper(UserBlogEntryMapper.class);
    		mapper.deleteUserBlogEntry(userBlogEntry);
    		session.commit();
    	} finally {
    		session.close();
    	}
	}
}
