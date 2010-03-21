/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.dao;

import java.util.List;

import org.firebird.common.ibatis.GenericMapper;
import org.firebird.io.model.UserBlogEntry;
import org.firebird.io.model.Vertex;

/**
 * A interface for user blog entry mapper.
 * 
 * @author Young-Gue Bae
 */
public interface UserBlogEntryMapper extends GenericMapper {

    /**
     * Selects the distinct users.
     *
     * @return List<String> the list of user id
     */
	public List<String> selectDistinctUsers();
	
    /**
     * Selects the user blog entries.
     *
     * @param userId the user id
     * @return List<UserBlogEntry> the list of user blog entry
     */
	public List<UserBlogEntry> selectUserBlogEntries(String userId);
	
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
