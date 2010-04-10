/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.service;

import java.util.List;

import org.firebird.common.service.GenericManager;
import org.firebird.io.model.Edge;

/**
 * A interface for edge manager.
 * 
 * @author Young-Gue Bae
 */
public interface EdgeManager extends GenericManager {

    /**
     * Gets the egde list.
     *
     * @param websiteId1 the websiteId1
     * @param websiteId2 the websiteId2
     * @return List<Edge> the edge list
     */
	public List<Edge> getEdges(int websiteId1, int websiteId2);
	
    /**
     * Adds a edge.
     *
     * @param edge the edge
     */
	public void addEdge(Edge edge);

    /**
     * Deletes a edge.
     *
     * @param edge the edge
     */
	public void deleteEdge(Edge edge);
	
	/**
     * Updates a edge score.
     *
     * @param edge the edge score
     */
	public void setEdgeScore(Edge edge);
	
    /**
     * Gets the edge.
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
			String relationship);
}
