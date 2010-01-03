/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.service.impl;

import java.util.List;

import org.firebird.io.dao.EdgeDao;
import org.firebird.io.model.Edge;
import org.firebird.io.service.EdgeManager;
import org.firebird.service.impl.GenericManagerImpl;

/**
 * A implementation for edge manager.
 * 
 * @author Young-Gue Bae
 */
public class EdgeManagerImpl extends GenericManagerImpl implements EdgeManager {

	private EdgeDao edgeDao;

	/**
     * Constructor.
     *
     * @param edgeDao the edge DAO
     */
    public EdgeManagerImpl(EdgeDao edgeDao) {
        super(edgeDao);
        this.edgeDao = edgeDao;
    }

    /**
     * Gets the egde list.
     *
     * @param websiteId the websiteId
     * @return List<Edge> the edge list
     */
	public List<Edge> getEdges(int websiteId) {
		return edgeDao.selectEdges(websiteId);
	}
    
    /**
     * Adds a edge.
     *
     * @param edge the edge
     */
	public void addEdge(Edge edge) {
		edgeDao.insertEdge(edge);
	}
	
    /**
     * Deletes a edge.
     *
     * @param edge the edge
     */
	public void deleteEdge(Edge edge) {
		edgeDao.deleteEdge(edge);
	}
}
