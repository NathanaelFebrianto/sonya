/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.bean;

import java.util.HashMap;
import java.util.List;

import org.firebird.collector.CollectorConfig;
import org.firebird.collector.twitter.TwitterDataCollector;
import org.firebird.common.http.HttpCommunicate;
import org.firebird.common.http.HttpCommunicateClient;
import org.firebird.common.http.HttpCommunicateException;
import org.firebird.io.model.Edge;
import org.firebird.io.model.Vertex;

/**
 * Client service handler for graph.
 * 
 * @author Young-Gue Bae
 */
public class GraphClientHandler {

	private HttpCommunicateClient httpClient;
	
	/**
     * Consturctor.
     * 
     * @exception Exception
     */
    public GraphClientHandler() {
        try {
        	httpClient = HttpCommunicateClient.getInstance();
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
    }

	/**
	 * Gets the vertices.
	 * 
	 * @param websiteId the website id
	 * @return List<Vertex> the vertex list
	 */
	public List<Vertex> getVertices(int websiteId) throws HttpCommunicateException {
		Object[] methodParams = { websiteId };
	    HttpCommunicate comm = new HttpCommunicate(
	            "org.firebird.graph.service.impl.RemoteGraphServiceImpl", 
	            "getVertices",
	            methodParams);
	    return (List<Vertex>)httpClient.execute(comm);
	}

	/**
	 * Gets the edges.
	 * 
	 * @param websiteId1 the website id1
	 * @param websiteId2 the website id2
	 * @return List<Edge> the edge list
	 */
	public List<Edge> getEdges(int websiteId1, int websiteId2) throws HttpCommunicateException {
		Object[] methodParams = { websiteId1, websiteId2 };
	    HttpCommunicate comm = new HttpCommunicate(
	            "org.firebird.graph.service.impl.RemoteGraphServiceImpl", 
	            "getEdges",
	            methodParams);
	    return (List<Edge>)httpClient.execute(comm);		
	}

	/**
	 * Collects the twitter data into database.
	 * 
	 * @param config the collector config
	 * @param screenName the user's screen name
	 */
	public void collectTwitter(CollectorConfig config, String screenName) throws HttpCommunicateException {
		Object[] methodParams = { config, screenName };
	    HttpCommunicate comm = new HttpCommunicate(
	            "org.firebird.graph.service.impl.RemoteGraphServiceImpl", 
	            "collectTwitter",
	            methodParams);
	    httpClient.execute(comm);		
	}	
	
	/**
	 * Collects the twitter data in real-time.
	 * 
	 * @param config the collector config
	 * @param screenName the user's screen name
	 * @return HashMap the hash map with vertices and edges
	 */
	public HashMap<String, List> collectRealtimeTwitter(CollectorConfig config, String screenName) throws Exception {
		HashMap<String, List> result = new HashMap<String, List>();
		
		TwitterDataCollector collector = new TwitterDataCollector(config);

		collector.collect(screenName);    	
		List<Vertex> vertices = collector.getVertices();
		List<Edge> edges = collector.getEdges();
		
		result.put("vertices", vertices);
		result.put("edges", edges);
		
		return result;
		
	}
}
