/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.service;

import java.util.List;
import java.util.Set;

import org.firebird.common.service.GenericManager;
import org.firebird.io.model.TopicUserCluster;

/**
 * A interface for topic user cluster manager.
 * 
 * @author Young-Gue Bae
 */
public interface TopicUserClusterManager extends GenericManager {

    /**
     * Gets the topic users.
     *
     * @param websiteId the website id
     * @param topicId the topic id
     * @param topUserNum the top user number
     * @param orderByColumn the orderby column
     * @return List<TopicUserCluster> the list of topic user cluster
     */
	public List<TopicUserCluster> getUsers(int websiteId, int topicId, int topUserNum, String orderByColumn);
	
    /**
     * Gets the topic users.
     *
     * @param websiteId the website id
     * @param topicId the topic id
     * @return List<TopicUserCluster> the list of topic user cluster
     */
	public List<TopicUserCluster> getUsers(int websiteId, int topicId);
	
	/**
     * Adds a topic user cluster.
     *
     * @param topicUser the topic user cluster
     */
	public void addUser(TopicUserCluster topicUser);
	
	/**
     * Deletes topic users cluster.
     *
     * @param websiteId the website id
     */
	public void deleteUsers(int websiteId);

	/**
     * Gets the topic cluster set.
     *
     * @param websiteId the website id
     * @return Set<Set<String>> the cluster set with vertex id
     */
	public Set<Set<String>> getTopicClusterSet(int websiteId);
}
