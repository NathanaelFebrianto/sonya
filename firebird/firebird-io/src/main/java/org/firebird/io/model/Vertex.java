/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Data model for a vertex.
 * 
 * @author Young-Gue Bae
 */
public class Vertex implements Serializable {
	private static final long serialVersionUID = -3745591316177805874L;
		
	private int websiteId;
	private String id;
	private int no;
	private String name;
	private String color;
	private String shape;
	private int size;
	private int opacity;
	private String imageFile;
	private int degree;
	private int inDegree;
	private int outDegree;
	private double pageRank;
	private double authority;
	private double hub;
	private double betweennessCentrality;
	private double closenessCentrality;
	private double eigenvectorCentrality;
	private int edgeBetweennessCluster;	
	private int voltageCluster;
	private int cnmCluster;
	private int friendsCount;
	private int followersCount;
	private int userNo;
	private String userId;
	private String userName;
	private String userUrl;
	private int blogEntryCount;
	private String lastBlogEntryId;
	private String lastBlogEntryBody;
	private String lastBlogEntryType;
	private Date lastBlogEntryCreateDate;
	private String lastBlogEntryReplyTo;
	private String lastBlogEntryDmTo;
	private String lastBlogEntryReferFrom;
	private Date createDate;
	private Date lastUpdateDate;
	private Date colCreateDate;
	private Date colLastUpdateDate;
	
	public int getWebsiteId() {
		return websiteId;
	}
	public String getId() {
		return id;
	}
	public int getNo() {
		return no;
	}
	public String getName() {
		return name;
	}
	public String getColor() {
		return color;
	}
	public String getShape() {
		return shape;
	}
	public int getSize() {
		return size;
	}
	public int getOpacity() {
		return opacity;
	}
	public String getImageFile() {
		return imageFile;
	}
	public int getDegree() {
		return degree;
	}
	public int getInDegree() {
		return inDegree;
	}
	public int getOutDegree() {
		return outDegree;
	}
	public double getPageRank() {
		return pageRank;
	}
	public double getAuthority() {
		return authority;
	}
	public double getHub() {
		return hub;
	}
	public double getBetweennessCentrality() {
		return betweennessCentrality;
	}
	public double getClosenessCentrality() {
		return closenessCentrality;
	}
	public double getEigenvectorCentrality() {
		return eigenvectorCentrality;
	}
	public int getEdgeBetweennessCluster() {
		return edgeBetweennessCluster;
	}
	public int getVoltageCluster() {
		return voltageCluster;
	}
	public int getCnmCluster() {
		return cnmCluster;
	}
	public int getFriendsCount() {
		return friendsCount;
	}
	public int getFollowersCount() {
		return followersCount;
	}
	public int getUserNo() {
		return userNo;
	}
	public String getUserId() {
		return userId;
	}
	public String getUserName() {
		return userName;
	}
	public String getUserUrl() {
		return userUrl;
	}
	public int getBlogEntryCount() {
		return blogEntryCount;
	}
	public String getLastBlogEntryId() {
		return lastBlogEntryId;
	}
	public String getLastBlogEntryBody() {
		return lastBlogEntryBody;
	}
	public String getLastBlogEntryType() {
		return lastBlogEntryType;
	}
	public Date getLastBlogEntryCreateDate() {
		return lastBlogEntryCreateDate;
	}
	public String getLastBlogEntryReplyTo() {
		return lastBlogEntryReplyTo;
	}
	public String getLastBlogEntryDmTo() {
		return lastBlogEntryDmTo;
	}
	public String getLastBlogEntryReferFrom() {
		return lastBlogEntryReferFrom;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public Date getColCreateDate() {
		return colCreateDate;
	}
	public Date getColLastUpdateDate() {
		return colLastUpdateDate;
	}
	
	public void setWebsiteId(int websiteId) {
		this.websiteId = websiteId;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public void setShape(String shape) {
		this.shape = shape;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public void setOpacity(int opacity) {
		this.opacity = opacity;
	}
	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	}
	public void setDegree(int degree) {
		this.degree = degree;
	}
	public void setInDegree(int inDegree) {
		this.inDegree = inDegree;
	}
	public void setOutDegree(int outDegree) {
		this.outDegree = outDegree;
	}
	public void setPageRank(double pageRank) {
		this.pageRank = pageRank;
	}
	public void setAuthority(double authority) {
		this.authority = authority;
	}
	public void setHub(double hub) {
		this.hub = hub;
	}
	public void setBetweennessCentrality(double betweennessCentrality) {
		this.betweennessCentrality = betweennessCentrality;
	}
	public void setClosenessCentrality(double closenessCentrality) {
		this.closenessCentrality = closenessCentrality;
	}
	public void setEigenvectorCentrality(double eigenvectorCentrality) {
		this.eigenvectorCentrality = eigenvectorCentrality;
	}
	public void setEdgeBetweennessCluster(int edgeBetweennessCluster) {
		this.edgeBetweennessCluster = edgeBetweennessCluster;
	}
	public void setVoltageCluster(int voltageCluster) {
		this.voltageCluster = voltageCluster;
	}
	public void setCnmCluster(int cnmCluster) {
		this.cnmCluster = cnmCluster;
	}
	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}
	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}
	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setUserUrl(String userUrl) {
		this.userUrl = userUrl;
	}
	public void setBlogEntryCount(int blogEntryCount) {
		this.blogEntryCount = blogEntryCount;
	}
	public void setLastBlogEntryId(String lastBlogEntryId) {
		this.lastBlogEntryId = lastBlogEntryId;
	}
	public void setLastBlogEntryBody(String lastBlogEntryBody) {
		this.lastBlogEntryBody = lastBlogEntryBody;
	}
	public void setLastBlogEntryType(String lastBlogEntryType) {
		this.lastBlogEntryType = lastBlogEntryType;
	}
	public void setLastBlogEntryCreateDate(Date lastBlogEntryCreateDate) {
		this.lastBlogEntryCreateDate = lastBlogEntryCreateDate;
	}
	public void setLastBlogEntryReplyTo(String lastBlogEntryReplyTo) {
		this.lastBlogEntryReplyTo = lastBlogEntryReplyTo;
	}
	public void setLastBlogEntryDmTo(String lastBlogEntryDmTo) {
		this.lastBlogEntryDmTo = lastBlogEntryDmTo;
	}
	public void setLastBlogEntryReferFrom(String lastBlogEntryReferFrom) {
		this.lastBlogEntryReferFrom = lastBlogEntryReferFrom;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public void setColCreateDate(Date colCreateDate) {
		this.colCreateDate = colCreateDate;
	}
	public void setColLastUpdateDate(Date colLastUpdateDate) {
		this.colLastUpdateDate = colLastUpdateDate;
	}
	
}
