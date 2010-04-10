/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.firebird.common.service.GenericManagerImpl;
import org.firebird.io.dao.TopicTermMapper;
import org.firebird.io.model.TopicTerm;
import org.firebird.io.service.TopicTermManager;

/**
 * A implementation for topic term manager.
 * 
 * @author Young-Gue Bae
 */
public class TopicTermManagerImpl extends GenericManagerImpl implements TopicTermManager {

	/**
     * Constructor.
     *
     */
    public TopicTermManagerImpl() {
    }
    
    /**
     * Gets the topics.
     *
     * @param websiteId the websiteId
     * @return List<Integer> the list of topic id
     */
	public List<Integer> getTopics(int websiteId) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TopicTermMapper mapper = session.getMapper(TopicTermMapper.class);
    		List<Integer> topics = mapper.selectTopics(websiteId);
    		return topics;
    	} finally {
    		session.close();
    	}
	}
	
	/**
     * Gets the topic terms.
     *
     * @param websiteId the website id
     * @param topicId the topic id
     * @return List<TopicTerm> the list of topic term
     */
	public List<TopicTerm> getTerms(int websiteId, int topicId) {
		TopicTerm param = new TopicTerm();
		param.setWebsiteId(websiteId);
		param.setTopicId(topicId);
		
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TopicTermMapper mapper = session.getMapper(TopicTermMapper.class);
    		List<TopicTerm> topicTerms = mapper.selectTerms(param);
    		return topicTerms;
    	} finally {
    		session.close();
    	}		
	}
	
	/**
     * Adds a topic term.
     *
     * @param topicTerm the topic term
     */
	public void addTerm(TopicTerm topicTerm) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TopicTermMapper mapper = session.getMapper(TopicTermMapper.class);
    		mapper.insertTerm(topicTerm);
    		session.commit();
    	} finally {
    		session.close();
    	}		
	}
	
	/**
     * Deletes topics.
     *
     * @param websiteId the website id
     */
	public void deleteTopics(int websiteId) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TopicTermMapper mapper = session.getMapper(TopicTermMapper.class);
    		mapper.deleteTopics(websiteId);
    		session.commit();
    	} finally {
    		session.close();
    	}		
	}

}
