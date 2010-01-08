/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.dao;

import java.util.List;

import org.firebird.dao.GenericDao;
import org.firebird.io.model.Edge;

/**
 * A interface for edge DAO.
 * 
 * @author Young-Gue Bae
 */
public interface EdgeDao extends GenericDao {

    /**
     * Selects egde list.
     *
     * @param websiteId1 the websiteId1
     * @param websiteId2 the websiteId2
     * @return List<Edge> the edge list
     */
	public List<Edge> selectEdges(int websiteId1, int websiteId2);
	
	/**
     * Inserts a edge.
     *
     * @param edge the edge
     */
	public void insertEdge(Edge edge);

    /**
     * Deletes a edge.
     *
     * @param edge the edge
     */
	public void deleteEdge(Edge edge);
}
