/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.service;

import java.util.List;

import org.firebird.collector.CollectorConfig;
import org.firebird.io.model.Edge;
import org.firebird.io.model.Vertex;

/**
 * Remote service interface for graph.
 * 
 * @author Young-Gue Bae
 */
public interface RemoteGraphService {

	/**
	 * Gets the vertices.
	 * 
	 * @param websiteId the website id
	 * @return List<Vertex> the vertex list
	 */
	public List<Vertex> getVertices(int websiteId) throws Exception;

	/**
	 * Gets the edges.
	 * 
	 * @param websiteId1 the website id1
	 * @param websiteId2 the website id2
	 * @return List<Edge> the edge list
	 */
	public List<Edge> getEdges(int websiteId1, int websiteId2) throws Exception;

	/**
	 * Collects the twitter data.
	 * 
	 * @param config the collect config
	 * @param screenName the user's screen name
	 */
	public void collectTwitter(CollectorConfig config, String screenName) throws Exception;
}
