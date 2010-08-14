/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.dao;

import java.util.List;

import org.firebird.common.ibatis.GenericMapper;
import org.firebird.io.model.TopicUser;

/**
 * A interface for topic user mapper.
 * 
 * @author Young-Gue Bae
 */
public interface TopicUserMapper extends GenericMapper {

    /**
     * Selects topic users.
     *
     * @param param the topic user
     * @return List<TopicUser> the list of topic user
     */
	public List<TopicUser> selectUsers(TopicUser param);
	
	/**
     * Inserts a topic user.
     *
     * @param topicUser the topic user
     */
	public void insertUser(TopicUser topicUser);
	
	/**
     * Deletes topic users.
     *
     * @param websiteId the website id
     */
	public void deleteUsers(int websiteId);
	
	/**
     * Selects topic users in the specific edge betweenness cluster.
     *
     * @param param the topic user
     * @return List<TopicUser> the list of topic user
     */
	public List<TopicUser> selectUsersInEdgeBetweennessCluster(TopicUser param);
	
	/**
     * Selects topic users in the specific voltage cluster.
     *
     * @param param the topic user
     * @return List<TopicUser> the list of topic user
     */
	public List<TopicUser> selectUsersInVoltageCluster(TopicUser param);
	
	/**
     * Selects topic users in the specific CNM(Clauset-Newman-Moore) cluster.
     *
     * @param param the topic user
     * @return List<TopicUser> the list of topic user
     */
	public List<TopicUser> selectUsersInCnmCluster(TopicUser param);

}
