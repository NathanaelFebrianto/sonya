/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.firebird.analyzer.graph.scoring.ScoringConfig;
import org.firebird.collector.CollectorConfig;
import org.firebird.io.model.Edge;
import org.firebird.io.model.TopicTerm;
import org.firebird.io.model.TopicUser;
import org.firebird.io.model.TopicUserCluster;
import org.firebird.io.model.UserTerm;
import org.firebird.io.model.Vertex;

/**
 * Remote service interface for graph.
 * 
 * @author Young-Gue Bae
 */
public interface GraphService {

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

	/**
	 * Collects the twitter's blog entries data.
	 * 
	 * @param condition the condition
	 */
	public void collectTwitterBlogEntries(Vertex condition) throws Exception;
	
	/**
	 * Evaluates the scores of the graph.
	 * such as HITS, Betweenness Centrality, Closeness Centrality, Degree etc.
	 * 
	 * @param config the scoring config
	 * @param websiteId1 the website id1
	 * @param websiteId2 the website id2
	 */
	public void scoringGraph(ScoringConfig config, int websiteId1, int websiteId2) throws Exception;

	/**
     * Gets the cluster set.
     *
     * @param websiteId the website id
     * @return Set<Set<String>> the cluster set with vertex id
     */
	public Set<Set<String>> getClusterSet(String clusterType, int websiteId) throws Exception;
	
	/**
     * Gets the cluster map.
     *
     * @param clusterType the cluster type
     * @param websiteId the website id
     * @return Map<Intger, Set<String>> the cluster set with vertices id
     */
	public Map<Integer, Set<String>> getClusterMap(String clusterType, int websiteId) throws Exception;

	/**
     * Gets the topic cluster set.
     *
     * @param websiteId the website id
     * @return Set<Set<String>> the cluster set with vertex id
     */
	public Set<Set<String>> getTopicClusterSet(int websiteId) throws Exception;
	
    /**
     * Gets the topics.
     *
     * @param websiteId the websiteId
     * @return List<Integer> the list of topic id
     */
	public List<Integer> getTopics(int websiteId) throws Exception;
	
	/**
     * Gets the terms by the specific topic.
     *
     * @param websiteId the website id
     * @param topicId the topic id
     * @return List<TopicTerm> the list of topic term
     */
	public List<TopicTerm> getTermsByTopic(int websiteId, int topicId) throws Exception;
	
    /**
     * Gets the users by the specific term.
     *
     * @param websiteId the website id
     * @param term the term
     * @return List<UserTerm> the list of user term
     */
	public List<UserTerm> getUsersByTerm(int websiteId, String term);
	
    /**
     * Gets the users by the specific topic.
     *
     * @param websiteId the website id
     * @param topicId the topic id
     * @param topUserNum the top user number
     * @return List<TopicUser> the list of topic user
     */
	public List<TopicUser> getUsersByTopic(int websiteId, int topicId, int topUserNum);
	
    /**
     * Gets the users by the specific topic.
     *
     * @param websiteId the website id
     * @param topicId the topic id
     * @return List<TopicUser> the list of topic user
     */
	public List<TopicUser> getUsersByTopic(int websiteId, int topicId);
	
    /**
     * Gets the topic-based clustered users by the specified topic.
     *
     * @param websiteId the website id
     * @param topicId the topic id
     * @param topUserNum the top user number
     * @return List<TopicUserCluster> the list of topic user cluster
     */
	public List<TopicUserCluster> getClusteredUsersByTopic(int websiteId, int topicId, int topUserNum);
	
    /**
     * Gets the topic-based clustered users by the specified topic.
     *
     * @param websiteId the website id
     * @param topicId the topic id
     * @return List<TopicUserCluster> the list of topic user cluster
     */
	public List<TopicUserCluster> getClusteredUsersByTopic(int websiteId, int topicId);

}
