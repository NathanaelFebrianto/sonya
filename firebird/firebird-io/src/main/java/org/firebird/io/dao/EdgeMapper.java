/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.dao;

import java.util.List;

import org.firebird.common.ibatis.GenericMapper;
import org.firebird.io.model.Edge;
import org.firebird.io.model.Vertex;

/**
 * A interface for edge mapper.
 * 
 * @author Young-Gue Bae
 */
public interface EdgeMapper extends GenericMapper {

    /**
     * Selects edge list.
     *
     * @param edge the edge
      * @return List<Edge> the edge list
     */
	public List<Edge> selectEdges(Edge edge);
	
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
	
	/**
     * Updates a edge score.
     *
     * @param edge the edge score
     */
	public void updateEdgeScore(Edge edge);
}
