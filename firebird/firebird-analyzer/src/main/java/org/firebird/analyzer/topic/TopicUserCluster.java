/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.topic;

import java.io.Serializable;
import java.util.Date;

/**
 * Data model for a topic-user cluster.
 * 
 * @author Young-Gue Bae
 */
public class TopicUserCluster implements Serializable ,Comparable<TopicUserCluster> {

	private static final long serialVersionUID = 2609827170483209455L;
	private int topicId;
	private String userId;	
	private boolean isTopicUser;
	private int cluster;
	private float score;
	private Date createDate;
	private Date lastUpdateDate;
	
	public int getTopicId() {
		return topicId;
	}
	public String getUserId() {
		return userId;
	}
	public boolean isTopicUser() {
		return isTopicUser;
	}
	public int getCluster() {
		return cluster;
	}
	public float getScore() {
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
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setTopicUser(boolean isTopicUser) {
		this.isTopicUser = isTopicUser;
	}
	public void setCluster(int cluster) {
		this.cluster = cluster;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	@Override
	public int compareTo(TopicUserCluster other) {
		//return Float.compare(score, other.getScore());
		//order by desc
		return Float.compare(other.getScore(), score);
	}

}
