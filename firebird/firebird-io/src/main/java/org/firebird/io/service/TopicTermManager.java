/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.service;

import java.util.List;

import org.firebird.common.service.GenericManager;
import org.firebird.io.model.TopicTerm;

/**
 * A interface for topic term manager.
 * 
 * @author Young-Gue Bae
 */
public interface TopicTermManager extends GenericManager {

    /**
     * Gets the topics.
     *
     * @param websiteId the websiteId
     * @return List<Integer> the list of topic id
     */
	public List<Integer> getTopics(int websiteId);
	
	/**
     * Gets the topic terms.
     *
     * @param websiteId the website id
     * @param topicId the topic id
     * @return List<TopicTerm> the list of topic term
     */
	public List<TopicTerm> getTerms(int websiteId, int topicId);
	
	/**
     * Adds a topic term.
     *
     * @param topicTerm the topic term
     */
	public void addTerm(TopicTerm topicTerm);
	
	/**
     * Deletes topics.
     *
     * @param websiteId the website id
     */
	public void deleteTopics(int websiteId);
	
}
