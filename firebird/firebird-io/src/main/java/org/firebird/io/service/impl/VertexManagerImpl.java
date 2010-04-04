/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.firebird.common.service.GenericManagerImpl;
import org.firebird.io.dao.VertexMapper;
import org.firebird.io.model.Vertex;
import org.firebird.io.service.VertexManager;

/**
 * A implementation for vertex manager.
 * 
 * @author Young-Gue Bae
 */
public class VertexManagerImpl extends GenericManagerImpl implements VertexManager {

	//private VertexMapper mapper;

	/**
     * Constructor.
     *
     */
    public VertexManagerImpl() {
    }
	
	/**
     * Constructor.
     *
     * @param mapper the vertex mapper
     */
    public VertexManagerImpl(VertexMapper mapper) {
        //this.mapper = mapper;
    }

    /**
     * Gets the vertex list.
     *
     * @param websiteId the websiteId
     * @return List<Vertex> the vertex list
     */
	public List<Vertex> getVertices(int websiteId) {
		// mapper.selectVertices(websiteId);
		
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		VertexMapper mapper = session.getMapper(VertexMapper.class);
    		List<Vertex> vertices = mapper.selectVertices(websiteId);
    		return vertices;
    	} finally {
    		session.close();
    	}
	}
	
	 /**
     * Gets the vertex id list by scoring condition.
     *
     * @param vertex the vertex
     * @return List<String> the vertex id list
     */
	public List<String> getVertexIdsByScoringCondition(Vertex vertex) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		VertexMapper mapper = session.getMapper(VertexMapper.class);
    		List<String> vertices = mapper.selectVertexIdsByScoringCondition(vertex);
    		return vertices;
    	} finally {
    		session.close();
    	}
	}
    
    /**
     * Adds a vertex.
     *
     * @param vertex the vertex
     */
	public void addVertex(Vertex vertex) {
		//mapper.insertVertex(vertex);

		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		VertexMapper mapper = session.getMapper(VertexMapper.class);
    		mapper.insertVertex(vertex);
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
		//mapper.deleteVertex(vertex);
		
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		VertexMapper mapper = session.getMapper(VertexMapper.class);
    		mapper.deleteVertex(vertex);
    		session.commit();
    	} finally {
    		session.close();
    	}
	}
	
	/**
     * Updates a vertex score.
     *
     * @param vertex the vertex score
     */
	public void setVertexScore(Vertex vertex) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		VertexMapper mapper = session.getMapper(VertexMapper.class);
    		mapper.updateVertexScore(vertex);
    		session.commit();
    	} finally {
    		session.close();
    	}		
	}
	
	/**
     * Updates a vertex cluster.
     *
     * @param vertex the vertex cluster
     */
	public void setVertexCluster(Vertex vertex) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		VertexMapper mapper = session.getMapper(VertexMapper.class);
    		mapper.updateVertexCluster(vertex);
    		session.commit();
    	} finally {
    		session.close();
    	}		
	}
}
