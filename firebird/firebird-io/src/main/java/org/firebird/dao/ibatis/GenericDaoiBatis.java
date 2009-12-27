/**
 * 
 */
package org.firebird.dao.ibatis;

import org.apache.ibatis.session.SqlSessionFactory;
import org.firebird.dao.FirebirdSqlSession;
import org.firebird.dao.GenericDao;

/**
 * @author louie
 *
 */
public class GenericDaoiBatis implements GenericDao {
	
	protected SqlSessionFactory sqlSessionFactory = null;
	
	public GenericDaoiBatis() {
		sqlSessionFactory = FirebirdSqlSession.getSqlSessionFactory();
	}
}
