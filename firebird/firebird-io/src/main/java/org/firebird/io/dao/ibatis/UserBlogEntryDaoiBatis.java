/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.dao.ibatis;

import org.apache.ibatis.session.SqlSession;
import org.firebird.dao.ibatis.GenericDaoiBatis;
import org.firebird.io.dao.UserBlogEntryDao;
import org.firebird.io.model.UserBlogEntry;

/**
 * A implementation for user blog entry DAO.
 * 
 * @author Young-Gue Bae
 */
public class UserBlogEntryDaoiBatis extends GenericDaoiBatis implements UserBlogEntryDao {

	/**
     * Constructor.
     *
     */
	public UserBlogEntryDaoiBatis() {
	}

    /**
     * Inserts a user blog entry.
     *
     * @param userBlogEntry the user blog entry
     */
	public void insertUserBlogEntry(UserBlogEntry userBlogEntry) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			session.insert("ibatis.sqlmap.UserBlogEntry.insertUserBlogEntry", userBlogEntry);
			session.commit();
		} finally {
			session.close();
		}
	}
	
    /**
     * Deletes user blog entries.
     *
     * @param userBlogEntry the user blog entry
     */
	public void deleteUserBlogEntry(UserBlogEntry userBlogEntry) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			session.insert("ibatis.sqlmap.UserBlogEntry.deleteUserBlogEntry", userBlogEntry);
			session.commit();
		} finally {
			session.close();
		}		
	}
}
