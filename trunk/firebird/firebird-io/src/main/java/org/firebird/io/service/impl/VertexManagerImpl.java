/*
Copyright (c) 2009-2010, Young-Gue Bae
All rights reserved.
*/
package org.firebird.io.service.impl;

import org.firebird.io.dao.VertexDao;
import org.firebird.io.model.Vertex;
import org.firebird.io.service.VertexManager;
import org.firebird.service.impl.GenericManagerImpl;

/**
 * A implementation for vertex manager.
 * 
 * @author Young-Gue Bae
 */
public class VertexManagerImpl extends GenericManagerImpl implements VertexManager {

	private VertexDao vertexDao;

	/**
     * Constructor.
     *
     * @param vertexDao the vertex DAO
     */
    public VertexManagerImpl(VertexDao vertexDao) {
        super(vertexDao);
        this.vertexDao = vertexDao;
    }

    /**
     * Adds a vertex.
     *
     * @param vertex the vertex
     */
	public void addVertex(Vertex vertex) {
		vertexDao.insertVertex(vertex);
	}
	
    /**
     * Deletes a vertex.
     *
     * @param vertex the vertex
     */
	public void deleteVertex(Vertex vertex) {
		vertexDao.deleteVertex(vertex);
	}
}
