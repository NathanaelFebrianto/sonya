/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.service;

import java.util.List;

import org.firebird.io.model.Edge;
import org.firebird.service.GenericManager;

/**
 * A interface for edge manager.
 * 
 * @author Young-Gue Bae
 */
public interface EdgeManager extends GenericManager {

    /**
     * Gets the egde list.
     *
     * @param websiteId the websiteId
     * @return List<Edge> the edge list
     */
	public List<Edge> getEdges(int websiteId);
	
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
}
