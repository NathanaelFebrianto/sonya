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
     * Gets the topic users in the specific cluster.
     *
     * @param websiteId the website id
     * @param cluster the cluster
     * @return List<TopicUser> the list of topic user
     */
	public List<TopicUser> getUsersInCluster(int websiteId, int cluster) {
		TopicUser param = new TopicUser();
		param.setWebsiteId(websiteId);
		param.setCluster(cluster);
		
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TopicUserMapper mapper = session.getMapper(TopicUserMapper.class);
    		List<TopicUser> topicUsers = mapper.selectUsersInCluster(param);
    		return topicUsers;
    	} finally {
    		session.close();
    	}			
	}
    
}
