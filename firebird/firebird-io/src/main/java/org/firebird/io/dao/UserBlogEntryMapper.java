/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.dao;

import org.firebird.common.ibatis.GenericMapper;
import org.firebird.io.model.UserBlogEntry;

/**
 * A interface for user blog entry mapper.
 * 
 * @author Young-Gue Bae
 */
public interface UserBlogEntryMapper extends GenericMapper {

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
