/*
Copyright (c) 2009-2010, Young-Gue Bae
All rights reserved.
*/
package org.firebird.io.dao.ibatis;

import org.apache.ibatis.session.SqlSession;
import org.firebird.dao.ibatis.GenericDaoiBatis;
import org.firebird.io.dao.VertexDao;
import org.firebird.io.model.Vertex;

/**
 * A implementation for vertex DAO.
 * 
 * @author Young-Gue Bae
 */
public class VertexDaoiBatis extends GenericDaoiBatis implements VertexDao {

	public VertexDaoiBatis() {
	}

    /**
     * Inserts a vertex.
     *
     * @param vertex the vertex
     */
	public void insertVertex(Vertex vertex) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			session.insert("ibatis.sqlmap.Vertex.insertVertex", vertex);
			session.commit();
		} finally {
			session.close();
		}
	}
	
    /**
     * Deletes a vertex.
     *
     * @param vertex the vertex
     */
	public void deleteVertex(Vertex vertex) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			session.insert("ibatis.sqlmap.Vertex.deleteVertex", vertex);
			session.commit();
		} finally {
			session.close();
		}		
	}
}
