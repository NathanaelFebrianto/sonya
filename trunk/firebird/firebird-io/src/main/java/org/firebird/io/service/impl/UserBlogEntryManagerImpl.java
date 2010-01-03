/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.service.impl;

import org.firebird.io.dao.UserBlogEntryDao;
import org.firebird.io.model.UserBlogEntry;
import org.firebird.io.service.UserBlogEntryManager;
import org.firebird.service.impl.GenericManagerImpl;

/**
 * A implementation for user blog entry manager.
 * 
 * @author Young-Gue Bae
 */
public class UserBlogEntryManagerImpl extends GenericManagerImpl implements UserBlogEntryManager {

	private UserBlogEntryDao userBlogEntryDao;

	/**
     * Constructor.
     *
     * @param vertexDao the vertex DAO
     */
    public UserBlogEntryManagerImpl(UserBlogEntryDao userBlogEntryDao) {
        super(userBlogEntryDao);
        this.userBlogEntryDao = userBlogEntryDao;
    }

    /**
     * Adds a user blog entry.
     *
     * @param userBlogEntry the user blog entry
     */
	public void addUserBlogEntry(UserBlogEntry userBlogEntry) {
		userBlogEntryDao.insertUserBlogEntry(userBlogEntry);
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
		
		userBlogEntryDao.deleteUserBlogEntry(userBlogEntry);
	}
}
