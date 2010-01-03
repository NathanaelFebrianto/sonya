/*
Copyright (c) 2009-2010, Young-Gue Bae
All rights reserved.
*/
package org.firebird.io.dao.ibatis;

import java.util.List;

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

	/**
     * Constructor.
     *
     */
	public VertexDaoiBatis() {
	}

    /**
     * Selects vertex list(vertices).
     *
     * @param websiteId the websiteId
     * @return List<Vertex> the vertex list
     */
	public List<Vertex> selectVertices(int websiteId) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			List<Vertex> vertices = session.selectList("ibatis.sqlmap.Vertex.selectVertices", websiteId);
			return vertices;			
		} finally {
			session.close();
		}		
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
