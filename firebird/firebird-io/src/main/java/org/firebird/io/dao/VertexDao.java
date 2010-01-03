/*
Copyright (c) 2009-2010, Young-Gue Bae
All rights reserved.
*/
package org.firebird.io.dao;

import java.util.List;

import org.firebird.dao.GenericDao;
import org.firebird.io.model.Edge;
import org.firebird.io.model.Vertex;

/**
 * A interface for vertex DAO.
 * 
 * @author Young-Gue Bae
 */
public interface VertexDao extends GenericDao {

    /**
     * Selects vertex list(vertices).
     *
     * @param websiteId the websiteId
     * @return List<Vertex> the vertex list
     */
	public List<Vertex> selectVertices(int websiteId);
	
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
}
