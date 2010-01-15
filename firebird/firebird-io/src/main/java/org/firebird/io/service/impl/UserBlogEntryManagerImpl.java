/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.service.impl;

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
     */
	public void deleteUserBlogEntry(int websiteId, String userId) {
		UserBlogEntry userBlogEntry = new UserBlogEntry();
		userBlogEntry.setWebsiteId(websiteId);
		userBlogEntry.setUserId(userId);
		
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
