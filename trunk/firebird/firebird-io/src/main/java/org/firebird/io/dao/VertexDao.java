/*
Copyright (c) 2009-2010, Young-Gue Bae
All rights reserved.
*/
package org.firebird.io.dao;

import org.firebird.dao.GenericDao;
import org.firebird.io.model.Vertex;

/**
 * A interface for vertex DAO.
 * 
 * @author Young-Gue Bae
 */
public interface VertexDao extends GenericDao {

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
