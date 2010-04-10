/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.dao;

import java.util.List;

import org.firebird.common.ibatis.GenericMapper;
import org.firebird.io.model.UserBlogEntry;

/**
 * A interface for user blog entry mapper.
 * 
 * @author Young-Gue Bae
 */
public interface UserBlogEntryMapper extends GenericMapper {

    /**
     * Selects the distinct users.
     *
     * @param websiteId the website id
     * @return List<String> the list of user id
     */
	public List<String> selectDistinctUsers(int websiteId);
	
    /**
     * Selects the user blog entries.
     *
     * @param userBlogEntry the user blog entry
     * @return List<UserBlogEntry> the list of user blog entry
     */
	public List<UserBlogEntry> selectUserBlogEntries(UserBlogEntry userBlogEntry);
	
    /**
     * Inserts a user blog entry.
     *
     * @param userBlogEntry the user blog entry
     */
	public void insertUserBlogEntry(UserBlogEntry userBlogEntry);
	
    /**
     * Deletes user blog entries.
     *
     * @param userBlogEntry the user blog entry
     */
	public void deleteUserBlogEntry(UserBlogEntry userBlogEntry);
}
