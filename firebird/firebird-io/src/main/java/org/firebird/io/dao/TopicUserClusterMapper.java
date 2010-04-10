/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.dao;

import java.util.List;

import org.firebird.common.ibatis.GenericMapper;
import org.firebird.io.model.TopicUserCluster;

/**
 * A interface for topic user cluster mapper.
 * 
 * @author Young-Gue Bae
 */
public interface TopicUserClusterMapper extends GenericMapper {

    /**
     * Selects topic users.
     *
     * @param param the topic user cluster
     * @return List<TopicUserCluster> the list of topic user cluster
     */
	public List<TopicUserCluster> selectUsers(TopicUserCluster param);
	
	/**
     * Inserts a topic user cluster.
     *
     * @param topicUser the topic user cluster
     */
	public void insertUser(TopicUserCluster topicUser);
	
	/**
     * Deletes topic users cluster.
     *
     * @param websiteId the website id
     */
	public void deleteUsers(int websiteId);

}
