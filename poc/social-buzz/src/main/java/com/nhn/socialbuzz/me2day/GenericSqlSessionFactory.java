package com.nhn.socialbuzz.me2day;

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
			String resource = "mybatis/Configuration.xml";
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