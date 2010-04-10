/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.service;

import java.util.List;

import org.firebird.common.service.GenericManager;
import org.firebird.io.model.UserBlogEntry;

/**
 * A interface for user blog entry manager.
 * 
 * @author Young-Gue Bae
 */
public interface UserBlogEntryManager extends GenericManager {

    /**
     * Gets the distinct users.
     *
     * @param websiteId the website id
     * @return List<String> the list of user id
     */
	public List<String> getDistinctUsers(int websiteId);
	
    /**
     * Gets the user blog entries.
     *
     * @param websiteId the website id
     * @param userId the user id
     * @return List<UserBlogEntry> the list of user blog entry
     */
	public List<UserBlogEntry> getUserBlogEntries(int websiteId, String userId);
	
    /**
     * Adds a user blog entry.
     *
     * @param userBlogEntry the user blog entry
     */
	public void addUserBlogEntry(UserBlogEntry userBlogEntry);

    /**
     * Deletes user blog entries.
     *
     * @param websiteId the website id
     * @param userId the user id
     * @param blogEntryId the blog entry id
     */
	public void deleteUserBlogEntry(int websiteId, String userId, String blogEntryId);
}
