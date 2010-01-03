/*
Copyright (c) 2009-2010, Young-Gue Bae
All rights reserved.
*/
package org.firebird.io.service;

import org.firebird.io.model.UserBlogEntry;
import org.firebird.io.model.Vertex;
import org.firebird.service.GenericManager;

/**
 * A interface for user blog entry manager.
 * 
 * @author Young-Gue Bae
 */
public interface UserBlogEntryManager extends GenericManager {

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
     */
	public void deleteUserBlogEntry(int websiteId, String userId);
}
