/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.dao;

import java.util.List;

import org.firebird.common.ibatis.GenericMapper;
import org.firebird.io.model.TopicTerm;

/**
 * A interface for topic term mapper.
 * 
 * @author Young-Gue Bae
 */
public interface TopicTermMapper extends GenericMapper {

    /**
     * Selects topics.
     *
     * @param websiteId the websiteId
     * @return List<Integer> the list of topic id
     */
	public List<Integer> selectTopics(int websiteId);
	
	/**
     * Selects topic terms.
     *
     * @param param the topic term
     * @return List<TopicTerm> the list of topic term
     */
	public List<TopicTerm> selectTerms(TopicTerm param);
	
	/**
     * Inserts a topic term.
     *
     * @param topicTerm the topic term
     */
	public void insertTerm(TopicTerm topicTerm);
	
	/**
     * Deletes topics.
     *
     * @param websiteId the website id
     */
	public void deleteTopics(int websiteId);
	
}
