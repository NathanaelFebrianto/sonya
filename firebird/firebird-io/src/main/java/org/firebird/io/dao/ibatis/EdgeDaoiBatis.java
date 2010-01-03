/*
Copyright (c) 2009-2010, Young-Gue Bae
All rights reserved.
*/
package org.firebird.io.dao.ibatis;

import org.apache.ibatis.session.SqlSession;
import org.firebird.dao.ibatis.GenericDaoiBatis;
import org.firebird.io.dao.EdgeDao;
import org.firebird.io.model.Edge;

/**
 * A implementation for edge DAO.
 * 
 * @author Young-Gue Bae
 */
public class EdgeDaoiBatis extends GenericDaoiBatis implements EdgeDao {

	public EdgeDaoiBatis() {
	}

    /**
     * Inserts a edge.
     *
     * @param edge the edge
     */
	public void insertEdge(Edge edge) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			session.insert("ibatis.sqlmap.Edge.insertEdge", edge);
			session.commit();
		} finally {
			session.close();
		}
	}
	
    /**
     * Deletes a edge.
     *
     * @param edge the edge
     */
	public void deleteEdge(Edge edge) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			session.insert("ibatis.sqlmap.Edge.deleteEdge", edge);
			session.commit();
		} finally {
			session.close();
		}		
	}
}
