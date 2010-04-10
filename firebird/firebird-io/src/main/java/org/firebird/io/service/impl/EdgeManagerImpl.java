/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.firebird.common.service.GenericManagerImpl;
import org.firebird.io.dao.EdgeMapper;
import org.firebird.io.model.Edge;
import org.firebird.io.service.EdgeManager;

/**
 * A implementation for edge manager.
 * 
 * @author Young-Gue Bae
 */
public class EdgeManagerImpl extends GenericManagerImpl implements EdgeManager {

	//private EdgeMapper mapper;

	/**
     * Constructor.
     *
     */
    public EdgeManagerImpl() {
    }
	
	/**
     * Constructor.
     *
     * @param mapper the edge mapper
     */
    public EdgeManagerImpl(EdgeMapper mapper) {
        //this.mapper = mapper;
    }

    /**
     * Gets the egde list.
     *
     * @param websiteId1 the websiteId1
     * @param websiteId2 the websiteId2
     * @return List<Edge> the edge list
     */
	public List<Edge> getEdges(int websiteId1, int websiteId2) {
		Edge edge = new Edge();
		edge.setWebsiteId1(websiteId1);
		edge.setWebsiteId2(websiteId2);		
		
		//return mapper.selectEdges(edge); 		
    	
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		EdgeMapper mapper = session.getMapper(EdgeMapper.class);
    		List<Edge> edges = mapper.selectEdges(edge);
    		return edges;
    	} finally {
    		session.close();
    	}
	}
    
    /**
     * Adds a edge.
     *
     * @param edge the edge
     */
	public void addEdge(Edge edge) {		
		//mapper.insertEdge(edge);
		
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		EdgeMapper mapper = session.getMapper(EdgeMapper.class);
    		mapper.insertEdge(edge);
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
		//mapper.deleteEdge(edge);
		
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		EdgeMapper mapper = session.getMapper(EdgeMapper.class);
    		mapper.deleteEdge(edge);
    		session.commit();
    	} finally {
    		session.close();
    	}
	}
	
	/**
     * Updates a edge score.
     *
     * @param edge the edge score
     */
	public void setEdgeScore(Edge edge) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		EdgeMapper mapper = session.getMapper(EdgeMapper.class);
    		mapper.updateEdgeScore(edge);
    		session.commit();
    	} finally {
    		session.close();
    	}		
	}
	
    /**
     * Gets the following edge.
     *
     * @param websiteId1 the websiteId1
     * @param websiteId2 the websiteId2
     * @param vertex1 the vertex from
     * @param vertex2 the vertex to
     * @param relationship the relationship
     * @return Edge the edge
     */
	public Edge getEdge(
			int websiteId1, 
			int websiteId2, 
			String vertex1, 
			String vertex2, 
			String relationship) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		EdgeMapper mapper = session.getMapper(EdgeMapper.class);
    		
    		Edge param = new Edge();
    		param.setWebsiteId1(websiteId1);
    		param.setWebsiteId2(websiteId2);
    		param.setVertex1(vertex1);
    		param.setVertex2(vertex2);
    		param.setRelationship(relationship);
    		
    		Edge edge = mapper.selectEdge(param);
    		return edge;
    	} finally {
    		session.close();
    	}		
	}
}
