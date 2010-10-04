/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.firebird.analyzer.graph.clustering.Clusterer;
import org.firebird.analyzer.graph.scoring.ScoringConfig;
import org.firebird.analyzer.service.AnalysisManager;
import org.firebird.analyzer.service.impl.AnalysisManagerImpl;
import org.firebird.collector.CollectorConfig;
import org.firebird.collector.service.CollectManager;
import org.firebird.collector.service.impl.CollectManagerImpl;
import org.firebird.graph.service.GraphService;
import org.firebird.io.model.Edge;
import org.firebird.io.model.TopicTerm;
import org.firebird.io.model.TopicUser;
import org.firebird.io.model.TopicUserCluster;
import org.firebird.io.model.UserTerm;
import org.firebird.io.model.Vertex;
import org.firebird.io.service.EdgeManager;
import org.firebird.io.service.TopicTermManager;
import org.firebird.io.service.TopicUserClusterManager;
import org.firebird.io.service.TopicUserManager;
import org.firebird.io.service.UserTermManager;
import org.firebird.io.service.VertexManager;
import org.firebird.io.service.impl.EdgeManagerImpl;
import org.firebird.io.service.impl.TopicTermManagerImpl;
import org.firebird.io.service.impl.TopicUserClusterManagerImpl;
import org.firebird.io.service.impl.TopicUserManagerImpl;
import org.firebird.io.service.impl.UserTermManagerImpl;
import org.firebird.io.service.impl.VertexManagerImpl;

/**
 * Remote service implementation for graph.
 * 
 * @author Young-Gue Bae
 */
public class GraphServiceImpl implements GraphService {

	private VertexManager vertexManager;
	private EdgeManager edgeManager;
	private CollectManager collectManager;
	private AnalysisManager analysisManager;
	private TopicTermManager topicManager;
	private UserTermManager userTermManager;
	private TopicUserManager topicUserManager;
	private TopicUserClusterManager topicClusterManager;
	
	/**
     * Creates a remote query manager.
     * 
     */
    public GraphServiceImpl() {
    	this.vertexManager = new VertexManagerImpl();
    	this.edgeManager = new EdgeManagerImpl();
    	this.collectManager = new CollectManagerImpl();
    	this.analysisManager = new AnalysisManagerImpl();
    	this.topicManager = new TopicTermManagerImpl();
    	this.userTermManager = new UserTermManagerImpl();
    	this.topicUserManager = new TopicUserManagerImpl();
    	this.topicClusterManager = new TopicUserClusterManagerImpl();
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
	 * @param config the collect config
	 * @param screenName the user's screen name
	 */
	public void collectTwitter(CollectorConfig config, String screenName) throws Exception {
		collectManager.collectTwitter(config, screenName);
	}
	
	/**
	 * Collects the twitter's blog entries data.
	 * 
	 * @param condition the condition
	 */
	public void collectTwitterBlogEntries(Vertex condition) throws Exception {
		collectManager.collectTwitterBlogEntries(condition);
	}
	
	/**
	 * Evaluates the scores of the graph.
	 * such as HITS, Betweenness Centrality, Closeness Centrality, Degree etc.
	 * 
	 * @param config the scoring config
	 * @param websiteId1 the website id1
	 * @param websiteId2 the website id2
	 */
	public void scoringGraph(ScoringConfig config, int websiteId1, int websiteId2) throws Exception {
		analysisManager.scoringGraph(config, websiteId1, websiteId2);
	}
	
	/**
     * Gets the cluster set.
     *
     * @param clusterType the cluster type
     * @param websiteId the website id
     * @return Set<Set<String>> the cluster set with vertex id
     */
	public Set<Set<String>> getClusterSet(String clusterType, int websiteId) throws Exception {
		if (clusterType.equals(Clusterer.EDGE_BETWEENNESS_CLUSTER)) {
			return vertexManager.getEdgeBetweennessClusterSet(websiteId);
		}
		else if (clusterType.equals(Clusterer.VOLTAGE_CLUSTER)) {
			return vertexManager.getVoltageClusterSet(websiteId);
		}
		else if (clusterType.equals(Clusterer.CNM_CLUSTER)) {
			return vertexManager.getCnmClusterSet(websiteId);
		}
		
		return null;		
	}
	
	/**
     * Gets the cluster map.
     *
     * @param clusterType the cluster type
     * @param websiteId the website id
     * @return Map<Intger, Set<String>> the cluster set with vertices id
     */
	public Map<Integer, Set<String>> getClusterMap(String clusterType, int websiteId) throws Exception {
		if (clusterType.equals(Clusterer.EDGE_BETWEENNESS_CLUSTER)) {
			return vertexManager.getEdgeBetweennessClusterMap(websiteId);
		}
		else if (clusterType.equals(Clusterer.VOLTAGE_CLUSTER)) {
			return vertexManager.getVoltageClusterMap(websiteId);
		}
		else if (clusterType.equals(Clusterer.CNM_CLUSTER)) {
			return vertexManager.getCnmClusterMap(websiteId);
		}
		
		return null;
	}
	
	/**
     * Gets the topic cluster set.
     *
     * @param websiteId the website id
     * @return Set<Set<String>> the cluster set with vertex id
     */
	public Set<Set<String>> getTopicClusterSet(int websiteId) throws Exception {
		return topicClusterManager.getTopicClusterSet(websiteId);
	}
	
    /**
     * Gets the topics.
     *
     * @param websiteId the websiteId
     * @return List<Integer> the list of topic id
     */
	public List<Integer> getTopics(int websiteId) throws Exception {
		return topicManager.getTopics(websiteId);
	}
	
	/**
     * Gets the terms by the specific topic.
     *
     * @param websiteId the website id
     * @param topicId the topic id
     * @return List<TopicTerm> the list of topic term
     */
	public List<TopicTerm> getTermsByTopic(int websiteId, int topicId) throws Exception {
		return topicManager.getTerms(websiteId, topicId);
	}
	
    /**
     * Gets the users by the specific term.
     *
     * @param websiteId the website id
     * @param term the term
     * @return List<UserTerm> the list of user term
     */
	public List<UserTerm> getUsersByTerm(int websiteId, String term) {
		return userTermManager.getUsers(websiteId, term);
	}
	
    /**
     * Gets the users by the specific topic.
     *
     * @param websiteId the website id
     * @param topicId the topic id
     * @param topUserNum the top user number
     * @return List<TopicUser> the list of topic user
     */
	public List<TopicUser> getUsersByTopic(int websiteId, int topicId, int topUserNum) {
		return topicUserManager.getUsers(websiteId, topicId, topUserNum);
	}
	
    /**
     * Gets the users by the specific topic.
     *
     * @param websiteId the website id
     * @param topicId the topic id
     * @return List<TopicUser> the list of topic user
     */
	public List<TopicUser> getUsersByTopic(int websiteId, int topicId) {
		return topicUserManager.getUsers(websiteId, topicId);
	}
	
    /**
     * Gets the topic-based clustered users by the specified topic.
     *
     * @param websiteId the website id
     * @param topicId the topic id
     * @param topUserNum the top user number
     * @param orderByColumn the orderby column
     * @return List<TopicUserCluster> the list of topic user cluster
     */
	public List<TopicUserCluster> getClusteredUsersByTopic(int websiteId, int topicId, int topUserNum, String orderByColumn) {
		return topicClusterManager.getUsers(websiteId, topicId, topUserNum, orderByColumn);
	}
	
    /**
     * Gets the topic-based clustered users by the specified topic.
     *
     * @param websiteId the website id
     * @param topicId the topic id
     * @return List<TopicUserCluster> the list of topic user cluster
     */
	public List<TopicUserCluster> getClusteredUsersByTopic(int websiteId, int topicId) {
		return topicClusterManager.getUsers(websiteId, topicId);
	}
	
	/**
     * Gets the clustered user's topics by a user.
     *
     * @param websiteId the website id
     * @param userId the user id
     * @return List<TopicUserCluster> the list of topic user cluster
     */
	public List<TopicUserCluster> getTopicsByClusteredUser(int websiteId, String userId) {
		return topicClusterManager.getTopicsByUser(websiteId, userId);
	}
	
	/**
     * Gets the clustered users to recommend.
     *
     * @param websiteId the website id
     * @param topicId the topic id
     * @param topUserNum the top user number
     * @param userId the user id
     * @param orderByColumn the orderby column
     * @return List<TopicUserCluster> the list of topic user cluster
     */
	public List<TopicUserCluster> getRecommendClusteredUsers(int websiteId, int topicId, 
			int topUserNum, String userId, String orderByColumn) {
		return topicClusterManager.getRecommendUsers(websiteId, topicId, topUserNum, userId, orderByColumn);
	}
	
}
