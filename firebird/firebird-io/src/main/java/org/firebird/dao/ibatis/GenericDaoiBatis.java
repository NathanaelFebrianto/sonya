/*
Copyright (c) 2009-2010, Young-Gue Bae
All rights reserved.
*/
package org.firebird.dao.ibatis;

import org.apache.ibatis.session.SqlSessionFactory;
import org.firebird.dao.FirebirdSqlSession;
import org.firebird.dao.GenericDao;

/**
 * A generic DAO iBatis.
 * 
 * @author Young-Gue Bae
 */
public class GenericDaoiBatis implements GenericDao {
	
	/** SQL Session factory */
	protected SqlSessionFactory sqlSessionFactory = null;

	/**
	 * Constructor.
	 * 
	 */
	public GenericDaoiBatis() {
		sqlSessionFactory = FirebirdSqlSession.getSqlSessionFactory();
	}
}
