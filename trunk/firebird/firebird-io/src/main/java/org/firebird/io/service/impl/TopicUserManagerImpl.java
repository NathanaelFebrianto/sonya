/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.firebird.common.service.GenericManagerImpl;
import org.firebird.io.dao.TopicUserMapper;
import org.firebird.io.model.TopicUser;
import org.firebird.io.service.TopicUserManager;

/**
 * A implementation for topic user manager.
 * 
 * @author Young-Gue Bae
 */
public class TopicUserManagerImpl extends GenericManagerImpl implements TopicUserManager {

	/**
     * Constructor.
     *
     */
    public TopicUserManagerImpl() {
    }

    /**
     * Gets the topic users.
     *
     * @param websiteId the website id
     * @param topicId the topic id
     * @param topUserNum the top user number
     * @return List<TopicUser> the list of topic user
     */
	public List<TopicUser> getUsers(int websiteId, int topicId, int topUserNum) {
		TopicUser param = new TopicUser();
		param.setWebsiteId(websiteId);
		param.setTopicId(topicId);
		param.setTopUserNum(topUserNum);
		
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TopicUserMapper mapper = session.getMapper(TopicUserMapper.class);
    		List<TopicUser> topicUsers = mapper.selectUsers(param);
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
     * @return List<TopicUser> the list of topic user
     */
	public List<TopicUser> getUsers(int websiteId, int topicId) {
		TopicUser param = new TopicUser();
		param.setWebsiteId(websiteId);
		param.setTopicId(topicId);
		
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TopicUserMapper mapper = session.getMapper(TopicUserMapper.class);
    		List<TopicUser> topicUsers = mapper.selectUsers(param);
    		return topicUsers;
    	} finally {
    		session.close();
    	}		
	}
	
	/**
     * Adds a topic user.
     *
     * @param topicUser the topic user
     */
	public void addUser(TopicUser topicUser) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TopicUserMapper mapper = session.getMapper(TopicUserMapper.class);
    		mapper.insertUser(topicUser);
    		session.commit();
    	} finally {
    		session.close();
    	}			
	}
	
	/**
     * Deletes topic users.
     *
     * @param websiteId the website id
     */
	public void deleteUsers(int websiteId) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TopicUserMapper mapper = session.getMapper(TopicUserMapper.class);
    		mapper.deleteUsers(websiteId);
    		session.commit();
    	} finally {
    		session.close();
    	}			
	}
	
	/**
     * Gets the topic users in the specific edge betweenness cluster.
     *
     * @param websiteId the website id
     * @param topic the topic id
     * @param cluster the cluster
     * @return List<TopicUser> the list of topic user
     */
	public List<TopicUser> getUsersInEdgeBetweennessCluster(int websiteId, int topic, int cluster) {
		TopicUser param = new TopicUser();
		param.setWebsiteId(websiteId);
		param.setTopicId(topic);
		param.setCluster(cluster);
		
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TopicUserMapper mapper = session.getMapper(TopicUserMapper.class);
    		List<TopicUser> topicUsers = mapper.selectUsersInEdgeBetweennessCluster(param);
    		return topicUsers;
    	} finally {
    		session.close();
    	}			
	}
	
	/**
     * Gets the topic users in the specific edge betweenness cluster.
     *
     * @param websiteId the website id
     * @param topic the topic id
     * @param cluster the cluster
     * @param minScore the minimum score
     * @param topUserNum the top user number
     * @return List<TopicUser> the list of topic user
     */
	public List<TopicUser> getUsersInEdgeBetweennessCluster(int websiteId, int topic, int cluster, float minScore, int topUserNum) {
		TopicUser param = new TopicUser();
		param.setWebsiteId(websiteId);
		param.setTopicId(topic);
		param.setCluster(cluster);
		param.setScore(minScore);
		param.setTopUserNum(topUserNum);
		
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TopicUserMapper mapper = session.getMapper(TopicUserMapper.class);
    		List<TopicUser> topicUsers = mapper.selectUsersInEdgeBetweennessCluster(param);
    		return topicUsers;
    	} finally {
    		session.close();
    	}		
	}
	
	/**
     * Gets the topic users in the specific voltage cluster.
     *
     * @param websiteId the website id
     * @param topic the topic id
     * @param cluster the cluster
     * @return List<TopicUser> the list of topic user
     */
	public List<TopicUser> getUsersInVoltageCluster(int websiteId, int topic, int cluster) {
		TopicUser param = new TopicUser();
		param.setWebsiteId(websiteId);
		param.setTopicId(topic);
		param.setCluster(cluster);
		
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TopicUserMapper mapper = session.getMapper(TopicUserMapper.class);
    		List<TopicUser> topicUsers = mapper.selectUsersInVoltageCluster(param);
    		return topicUsers;
    	} finally {
    		session.close();
    	}			
	}
	
	/**
     * Gets the topic users in the specific voltage cluster.
     *
     * @param websiteId the website id
     * @param topic the topic id
     * @param cluster the cluster
     * @param minScore the minimum score
     * @param topUserNum the top user number
     * @return List<TopicUser> the list of topic user
     */
	public List<TopicUser> getUsersInVoltageCluster(int websiteId, int topic, int cluster, float minScore, int topUserNum) {
		TopicUser param = new TopicUser();
		param.setWebsiteId(websiteId);
		param.setTopicId(topic);
		param.setCluster(cluster);
		param.setScore(minScore);
		param.setTopUserNum(topUserNum);
		
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TopicUserMapper mapper = session.getMapper(TopicUserMapper.class);
    		List<TopicUser> topicUsers = mapper.selectUsersInVoltageCluster(param);
    		return topicUsers;
    	} finally {
    		session.close();
    	}				
	}
    
}
