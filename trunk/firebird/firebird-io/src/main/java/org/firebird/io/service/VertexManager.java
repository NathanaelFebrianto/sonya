/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.service;

import java.util.List;

import org.firebird.common.service.GenericManager;
import org.firebird.io.model.Vertex;

/**
 * A interface for vertex manager.
 * 
 * @author Young-Gue Bae
 */
public interface VertexManager extends GenericManager {

    /**
     * Gets the vertex list.
     *
     * @param websiteId the websiteId
     * @return List<Vertex> the vertex list
     */
	public List<Vertex> getVertices(int websiteId);
	
	 /**
     * Gets the vertex id list by scoring condition.
     *
     * @param vertex the vertex
     * @return List<String> the vertex id list
     */
	public List<String> getVertexIdsByScoringCondition(Vertex vertex);
	
    /**
     * Adds a vertex.
     *
     * @param vertex the vertex
     */
	public void addVertex(Vertex vertex);

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
	public void setVertexScore(Vertex vertex);
	
	/**
     * Updates a vertex cluster.
     *
     * @param vertex the vertex cluster
     */
	public void setVertexCluster(Vertex vertex);
}
