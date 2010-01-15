/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.common.ibatis;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * A generic SQL session.
 * 
 * @author Young-Gue Bae
 */
public class GenericSqlSessionFactory {

	private static final SqlSessionFactory sqlSessionFactory;

	static {
		try {
			String resource = "ibatis/Configuration.xml";
			Reader reader = Resources.getResourceAsReader(resource);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error initializing GenericSqlSessionFactory class. Cause: " + e);
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