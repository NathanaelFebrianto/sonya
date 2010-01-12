/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.dao;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * A generic SQL session.
 * 
 * @author Young-Gue Bae
 */
public class GenericSqlSession {

	private static final SqlSessionFactory sqlSessionFactory;

	static {
		try {
			String resource = "ibatis/Configuration.xml";
			Reader reader = Resources.getResourceAsReader(resource);
			sqlSessionFactory = new SqlSessionFactoryBuilder()
					.build(reader);
			//SqlSession session = sqlSessionFactory.openSession();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Error initializing MyAppSqlConfig class. Cause: " + e);
		}
	}
	
    /**
     * Gets the SQL session factory.
     *
     * @return SqlSessionFactory the SQL session factory
     */
	public static SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}
}