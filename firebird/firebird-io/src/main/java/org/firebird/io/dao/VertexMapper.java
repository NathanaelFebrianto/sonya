/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.dao;

import java.util.List;

import org.firebird.common.ibatis.GenericMapper;
import org.firebird.io.model.Vertex;

/**
 * A interface for vertex mapper.
 * 
 * @author Young-Gue Bae
 */
public interface VertexMapper extends GenericMapper {

    /**
     * Selects vertex list(vertices).
     *
     * @param websiteId the websiteId
     * @return List<Vertex> the vertex list
     */
	public List<Vertex> selectVertices(int websiteId);
	
	 /**
     * Gets the vertex id list by scoring condition.
     *
     * @param vertex the vertex
     * @return List<String> the vertex id list
     */
	public List<String> selectVertexIdsByScoringCondition(Vertex vertex);
	
    /**
     * Inserts a vertex.
     *
     * @param vertex the vertex
     */
	public void insertVertex(Vertex vertex);

    /**
     * Deletes a vertex.
     *
     * @param vertex the vertex
     */
	public void deleteVertex(Vertex vertex);
	
	/**
     * Updates a vertex score.
     *
     * @param vertex the vertex score
     */
	public void updateVertexScore(Vertex vertex);

	/**
     * Updates a vertex cluster.
     *
     * @param vertex the vertex cluster
     */
	public void updateVertexCluster(Vertex vertex);
}
