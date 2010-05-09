/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.service.impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.firebird.common.service.GenericManagerImpl;
import org.firebird.io.dao.TopicUserClusterMapper;
import org.firebird.io.model.TopicUserCluster;
import org.firebird.io.service.TopicTermManager;
import org.firebird.io.service.TopicUserClusterManager;

/**
 * A implementation for topic user cluster manager.
 * 
 * @author Young-Gue Bae
 */
public class TopicUserClusterManagerImpl extends GenericManagerImpl implements TopicUserClusterManager {

	/**
     * Constructor.
     *
     */
    public TopicUserClusterManagerImpl() {
    }

    /**
     * Gets the topic users.
     *
     * @param websiteId the website id
     * @param topicId the topic id
     * @param topUserNum the top user number
     * @param orderByColumn the orderby column
     * @return List<TopicUserCluster> the list of topic user cluster
     */
	public List<TopicUserCluster> getUsers(int websiteId, int topicId, int topUserNum, String orderByColumn) {
		TopicUserCluster param = new TopicUserCluster();
		param.setWebsiteId(websiteId);
		param.setTopicId(topicId);
		param.setTopUserNum(topUserNum);
		param.setOrderByColumn(orderByColumn);
		
		//System.out.println("order by column == " + orderByColumn);
		
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TopicUserClusterMapper mapper = session.getMapper(TopicUserClusterMapper.class);
    		List<TopicUserCluster> topicUsers = mapper.selectUsers(param);
    		return topicUsers;
    	} finally {
    		session.close();
    	}		
	}
	
    /**
     * Gets the topic users.
     *
     * @param websiteId the website id
     * @param topicId the topic id
     * @return List<TopicUserCluster> the list of topic user cluster
     */
	public List<TopicUserCluster> getUsers(int websiteId, int topicId) {
		TopicUserCluster param = new TopicUserCluster();
		param.setWebsiteId(websiteId);
		param.setTopicId(topicId);
		
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TopicUserClusterMapper mapper = session.getMapper(TopicUserClusterMapper.class);
    		List<TopicUserCluster> topicUsers = mapper.selectUsers(param);
    		return topicUsers;
    	} finally {
    		session.close();
    	}		
	}
	
	/**
     * Gets the topics by a user.
     *
     * @param websiteId the website id
     * @param userId the user id
     * @return List<TopicUserCluster> the list of topic user cluster
     */
	public List<TopicUserCluster> getTopicsByUser(int websiteId, String userId) {
		TopicUserCluster param = new TopicUserCluster();
		param.setWebsiteId(websiteId);
		param.setUserId(userId);
		
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TopicUserClusterMapper mapper = session.getMapper(TopicUserClusterMapper.class);
    		List<TopicUserCluster> topicUsers = mapper.selectTopicsByUser(param);
    		return topicUsers;
    	} finally {
    		session.close();
    	}		
	}
	
	/**
     * Gets the users to recommend.
     *
     * @param websiteId the website id
     * @param topicId the topic id
     * @param topUserNum the top user number
     * @param userId the user id
     * @param orderByColumn the orderby column
     * @return List<TopicUserCluster> the list of topic user cluster
     */
	public List<TopicUserCluster> getRecommendUsers(int websiteId, int topicId, 
			int topUserNum, String userId, String orderByColumn) {
		TopicUserCluster param = new TopicUserCluster();
		param.setWebsiteId(websiteId);
		param.setTopicId(topicId);
		param.setTopUserNum(topUserNum);
		param.setUserId(userId);
		param.setOrderByColumn(orderByColumn);
		
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TopicUserClusterMapper mapper = session.getMapper(TopicUserClusterMapper.class);
    		List<TopicUserCluster> topicUsers = mapper.selectRecommendUsers(param);
    		return topicUsers;
    	} finally {
    		session.close();
    	}			
	}
	
	/**
     * Adds a topic user cluster.
     *
     * @param topicUser the topic user cluster
     */
	public void addUser(TopicUserCluster topicUser) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TopicUserClusterMapper mapper = session.getMapper(TopicUserClusterMapper.class);
    		mapper.insertUser(topicUser);
    		session.commit();
    	} finally {
    		session.close();
    	}			
	}
	
	/**
     * Deletes topic users cluster.
     *
     * @param websiteId the website id
     */
	public void deleteUsers(int websiteId) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TopicUserClusterMapper mapper = session.getMapper(TopicUserClusterMapper.class);
    		mapper.deleteUsers(websiteId);
    		session.commit();
    	} finally {
    		session.close();
    	}			
	}
	
	/**
     * Gets the topic cluster set.
     *
     * @param websiteId the website id
     * @return Set<Set<String>> the cluster set with vertex id
     */
	public Set<Set<String>> getTopicClusterSet(int websiteId) {
		Set<Set<String>> clusterSet = new LinkedHashSet<Set<String>>();
		
		TopicTermManager topicTermManager = new TopicTermManagerImpl();		
		List<Integer> topics = topicTermManager.getTopics(websiteId);
		
		for (Integer topicId : topics) {
			Set<String> verticesSet = new LinkedHashSet<String>();			
			List<TopicUserCluster> users = this.getUsers(websiteId, topicId);		
			for (TopicUserCluster user : users) {
				verticesSet.add(user.getVertexId());
			}
			clusterSet.add(verticesSet);
		}		
		return clusterSet;
	}
    
}
