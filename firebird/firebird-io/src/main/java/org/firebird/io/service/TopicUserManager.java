/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.service;

import java.util.List;

import org.firebird.common.service.GenericManager;
import org.firebird.io.model.TopicUser;

/**
 * A interface for topic user manager.
 * 
 * @author Young-Gue Bae
 */
public interface TopicUserManager extends GenericManager {

    /**
     * Gets the topic users.
     *
     * @param websiteId the website id
     * @param topicId the topic id
     * @return List<TopicUser> the list of topic user
     */
	public List<TopicUser> getUsers(int websiteId, int topicId);
	
	/**
     * Adds a topic user.
     *
     * @param topicUser the topic user
     */
	public void addUser(TopicUser topicUser);
	
	/**
     * Deletes topic users.
     *
     * @param websiteId the website id
     */
	public void deleteUsers(int websiteId);
	
	/**
     * Gets the topic users in the specific cluster.
     *
     * @param websiteId the website id
     * @param topic the topic id
     * @param cluster the cluster
     * @return List<TopicUser> the list of topic user
     */
	public List<TopicUser> getUsersInCluster(int websiteId, int topic, int cluster);
	
}
