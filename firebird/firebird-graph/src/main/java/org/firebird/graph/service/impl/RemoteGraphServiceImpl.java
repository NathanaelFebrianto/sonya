/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.service.impl;

import java.util.List;

import org.firebird.collector.twitter.TwitterDataCollector;
import org.firebird.graph.service.RemoteGraphService;
import org.firebird.io.model.Edge;
import org.firebird.io.model.Vertex;
import org.firebird.io.service.EdgeManager;
import org.firebird.io.service.VertexManager;
import org.firebird.io.service.impl.EdgeManagerImpl;
import org.firebird.io.service.impl.VertexManagerImpl;

/**
 * Remote service implementation for graph.
 * 
 * @author Young-Gue Bae
 */
public class RemoteGraphServiceImpl implements RemoteGraphService {

	private VertexManager vertexManager;
	private EdgeManager edgeManager;
	private TwitterDataCollector twitterCollector;
	
	/**
     * Creates a remote query manager.
     * 
     * @exception Exception
     */
    public RemoteGraphServiceImpl() throws Exception {
    	System.out.println("Class[RemoteGraphServiceImpl] called by client.");
    	this.vertexManager = new VertexManagerImpl();
    	this.edgeManager = new EdgeManagerImpl();
    	this.twitterCollector = new TwitterDataCollector();    	
    }
	
	/**
	 * Gets the vertices.
	 * 
	 * @param websiteId the website id
	 * @return List<Vertex> the vertex list
	 */
	public List<Vertex> getVertices(int websiteId) throws Exception {
		return vertexManager.getVertices(websiteId);
	}

	/**
	 * Gets the edges.
	 * 
	 * @param websiteId1 the website id1
	 * @param websiteId2 the website id2
	 * @return List<Edge> the edge list
	 */
	public List<Edge> getEdges(int websiteId1, int websiteId2) throws Exception {
		return edgeManager.getEdges(websiteId1, websiteId2);		
	}

	/**
	 * Collects the twitter data.
	 * 
	 * @param screenName the user's screen name
	 */
	public void collectTwitter(String screenName) throws Exception {
		System.out.println("Method[collectTwitter] called by client.");
		
		twitterCollector.setDBStorageMode(true);
		twitterCollector.setLevelLimit(3);
		twitterCollector.setPeopleLimit(1000);
		twitterCollector.setDegreeLimit(100);
		twitterCollector.setCollectFriendRelationship(true);
		twitterCollector.setCollectFollowerRelationship(true);
		twitterCollector.setCollectUserBlogEntry(false);

		twitterCollector.collectSocialNetwork(screenName);
	}
	
}