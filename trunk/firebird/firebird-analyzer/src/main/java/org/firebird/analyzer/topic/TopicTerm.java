/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.topic;

import java.io.Serializable;
import java.util.Date;

/**
 * Data model for a topic term.
 * 
 * @author Young-Gue Bae
 */
public class TopicTerm implements Serializable, Comparable<TopicTerm> {

	private static final long serialVersionUID = 4796609737710136397L;
	
	private int topicId;
	private String term;
	private double score;
	private Date createDate;
	private Date lastUpdateDate;
	
	public int getTopicId() {
		return topicId;
	}
	public String getTerm() {
		return term;
	}
	public double getScore() {
		return score;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	@Override
	public int compareTo(TopicTerm other) {
		//return Double.compare(score, other.getScore());
		//order by desc
		return Double.compare(other.getScore(), score);
	}
}
