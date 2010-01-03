/*
Copyright (c) 2009-2010, Young-Gue Bae
All rights reserved.
*/
package org.firebird.io.service;

import org.firebird.io.model.Vertex;
import org.firebird.service.GenericManager;

/**
 * A interface for vertex manager.
 * 
 * @author Young-Gue Bae
 */
public interface VertexManager extends GenericManager {

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
}
