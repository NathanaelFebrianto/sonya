/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.firebird.analyzer.graph.scoring.ScoringConfig;
import org.firebird.collector.CollectorConfig;
import org.firebird.collector.twitter.TwitterDataCollector;
import org.firebird.common.http.HttpCommunicate;
import org.firebird.common.http.HttpCommunicateClient;
import org.firebird.common.http.HttpCommunicateException;
import org.firebird.io.model.Edge;
import org.firebird.io.model.TopicTerm;
import org.firebird.io.model.TopicUser;
import org.firebird.io.model.TopicUserCluster;
import org.firebird.io.model.UserTerm;
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
	            "org.firebird.graph.service.impl.GraphServiceImpl", 
	            "getVertices",
	            methodParams);
	    return (List<Vertex>) httpClient.execute(comm);
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
	            "org.firebird.graph.service.impl.GraphServiceImpl", 
	            "getEdges",
	            methodParams);
	    return (List<Edge>) httpClient.execute(comm);		
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
	            "org.firebird.graph.service.impl.GraphServiceImpl", 
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

	/**
	 * Collects the twitter's blog entries data.
	 * 
	 * @param condition the condition
	 */
	public void collectTwitterBlogEntries(Vertex condition) throws HttpCommunicateException {
		Object[] methodParams = { condition };
	    HttpCommunicate comm = new HttpCommunicate(
	            "org.firebird.graph.service.impl.GraphServiceImpl", 
	            "collectTwitterBlogEntries",
	            methodParams);
	    httpClient.execute(comm);
	}	
	
	/**
	 * Evaluates the scores of the graph.
	 * such as HITS, Betweenness Centrality, Closeness Centrality, Degree etc.
	 * 
	 * @param config the scoring config
	 * @param websiteId1 the website id1
	 * @param websiteId2 the website id2
	 */
	public void scoringGraph(ScoringConfig config, int websiteId1, int websiteId2) throws HttpCommunicateException {
		Object[] methodParams = { config, websiteId1, websiteId2 };
	    HttpCommunicate comm = new HttpCommunicate(
	            "org.firebird.graph.service.impl.GraphServiceImpl", 
	            "scoringGraph",
	            methodParams);
	    httpClient.execute(comm);
	}
	
	/**
     * Gets the cluster set.
     *
     * @param websiteId the website id
     * @return Set<Set<String>> the cluster set with vertex id
     */
	public Set<Set<String>> getClusterSet(int websiteId) throws HttpCommunicateException {
		Object[] methodParams = { websiteId };
	    HttpCommunicate comm = new HttpCommunicate(
	            "org.firebird.graph.service.impl.GraphServiceImpl", 
	            "getClusterSet",
	            methodParams);
	    return (Set<Set<String>>) httpClient.execute(comm);
	}

	/**
     * Gets the topic cluster set.
     *
     * @param websiteId the website id
     * @return Set<Set<String>> the cluster set with vertex id
     */
	public Set<Set<String>> getTopicClusterSet(int websiteId) throws HttpCommunicateException {
		Object[] methodParams = { websiteId };
	    HttpCommunicate comm = new HttpCommunicate(
	            "org.firebird.graph.service.impl.GraphServiceImpl", 
	            "getTopicClusterSet",
	            methodParams);
	    return (Set<Set<String>>) httpClient.execute(comm);
	}
	
    /**
     * Gets the topics.
     *
     * @param websiteId the websiteId
     * @return List<Integer> the list of topic id
     */
	public List<Integer> getTopics(int websiteId) throws HttpCommunicateException {
		Object[] methodParams = { websiteId };
	    HttpCommunicate comm = new HttpCommunicate(
	            "org.firebird.graph.service.impl.GraphServiceImpl", 
	            "getTopics",
	            methodParams);
	    return (List<Integer>) httpClient.execute(comm);		
	}
	
	/**
     * Gets the terms by the specific topic.
     *
     * @param websiteId the website id
     * @param topicId the topic id
     * @return List<TopicTerm> the list of topic term
     */
	public List<TopicTerm> getTermsByTopic(int websiteId, int topicId) throws HttpCommunicateException {
		Object[] methodParams = { websiteId, topicId };
	    HttpCommunicate comm = new HttpCommunicate(
	            "org.firebird.graph.service.impl.GraphServiceImpl", 
	            "getTermsByTopic",
	            methodParams);
	    return (List<TopicTerm>) httpClient.execute(comm);		
	}
	
    /**
     * Gets the users by the specific term.
     *
     * @param websiteId the website id
     * @param term the term
     * @return List<UserTerm> the list of user term
     */
	public List<UserTerm> getUsersByTerm(int websiteId, String term) throws HttpCommunicateException {
		Object[] methodParams = { websiteId, term };
	    HttpCommunicate comm = new HttpCommunicate(
	            "org.firebird.graph.service.impl.GraphServiceImpl", 
	            "getUsersByTerm",
	            methodParams);
	    return (List<UserTerm>) httpClient.execute(comm);			
	}
	
    /**
     * Gets the users by the specific topic.
     *
     * @param websiteId the website id
     * @param topicId the topic id
     * @return List<TopicUser> the list of topic user
     */
	public List<TopicUser> getUsersByTopic(int websiteId, int topicId) throws HttpCommunicateException {
		Object[] methodParams = { websiteId, topicId };
	    HttpCommunicate comm = new HttpCommunicate(
	            "org.firebird.graph.service.impl.GraphServiceImpl", 
	            "getUsersByTopic",
	            methodParams);
	    return (List<TopicUser>) httpClient.execute(comm);		
	}
	
    /**
     * Gets the topic-based clustered users by the specified topic.
     *
     * @param websiteId the website id
     * @param topicId the topic id
     * @return List<TopicUserCluster> the list of topic user cluster
     */
	public List<TopicUserCluster> getClusteredUsersByTopic(int websiteId, int topicId) throws HttpCommunicateException {
		Object[] methodParams = { websiteId, topicId };
	    HttpCommunicate comm = new HttpCommunicate(
	            "org.firebird.graph.service.impl.GraphServiceImpl", 
	            "getClusteredUsersByTopic",
	            methodParams);
	    return (List<TopicUserCluster>) httpClient.execute(comm);		
	}
	
}
