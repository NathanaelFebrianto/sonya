/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Data model for a topic-user cluster.
 * 
 * @author Young-Gue Bae
 */
public class TopicUserCluster implements Serializable ,Comparable<TopicUserCluster> {

	private static final long serialVersionUID = 2609827170483209455L;
	
	private int websiteId;
	private int topicId;
	private String userId;
	private String vertexId;
	private String userName;
	private String userProfileImage;
	private String userUrl;
	private boolean isTopicUser;
	private int cluster;
	private double authority;
	private float topicScore;
	private float authorityTopicScore;
	private Date createDate;
	private Date lastUpdateDate;
	private Integer topUserNum;
	private String orderByColumn;
	
	public int getWebsiteId() {
		return websiteId;
	}
	public int getTopicId() {
		return topicId;
	}
	public String getUserId() {
		return userId;
	}
	public String getVertexId() {
		return vertexId;
	}
	public String getUserName() {
		return userName;
	}
	public String getUserProfileImage() {
		return userProfileImage;
	}
	public String getUserUrl() {
		return userUrl;
	}
	public boolean isTopicUser() {
		return isTopicUser;
	}
	public int getCluster() {
		return cluster;
	}
	public double getAuthority() {
		return authority;
	}
	public float getTopicScore() {
		return topicScore;
	}
	public float getAuthorityTopicScore() {
		return authorityTopicScore;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public Integer getTopUserNum() {
		return topUserNum;
	}
	public String getOrderByColumn() {
		return orderByColumn;
	}

	public void setWebsiteId(int websiteId) {
		this.websiteId = websiteId;
	}
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setVertexId(String vertexId) {
		this.vertexId = vertexId;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setUserProfileImage(String userProfileImage) {
		this.userProfileImage = userProfileImage;
	}
	public void setUserUrl(String userUrl) {
		this.userUrl = userUrl;
	}
	public void setTopicUser(boolean isTopicUser) {
		this.isTopicUser = isTopicUser;
	}
	public void setCluster(int cluster) {
		this.cluster = cluster;
	}
	public void setAuthority(double authority) {
		this.authority = authority;
	}
	public void setTopicScore(float topicScore) {
		this.topicScore = topicScore;
	}
	public void setAuthorityTopicScore(float authorityTopicScore) {
		this.authorityTopicScore = authorityTopicScore;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public void setTopUserNum(Integer topUserNum) {
		this.topUserNum = topUserNum;
	}
	public void setOrderByColumn(String orderByColumn) {
		this.orderByColumn = orderByColumn;
	}

	@Override
	public int compareTo(TopicUserCluster other) {
		//return Float.compare(score, other.getScore());
		//order by desc
		return Float.compare(other.getTopicScore(), topicScore);
	}

}
