/*
Copyright (c) 2009-2010, Young-Gue Bae
All rights reserved.
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
	private int id;
	private String name;
	private String color;
	private String shape;
	private int size;
	private int opacity;
	private String imageFile;
	private int inDegree;
	private int outDegree;
	private long betweenessCentrality;
	private long closenessCentrality;
	private long eigenvectorCentrality;
	private long clusteringCoefficient;
	private int following;
	private int followers;
	private int userNo;
	private String userId;
	private String userName;
	private String userUrl;
	private int blogEntryCount;
	private String lastBlogEntryId;
	private String lastBlogEntry;
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
	public int getId() {
		return id;
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
	public int getInDegree() {
		return inDegree;
	}
	public int getOutDegree() {
		return outDegree;
	}
	public long getBetweenessCentrality() {
		return betweenessCentrality;
	}
	public long getClosenessCentrality() {
		return closenessCentrality;
	}
	public long getEigenvectorCentrality() {
		return eigenvectorCentrality;
	}
	public long getClusteringCoefficient() {
		return clusteringCoefficient;
	}
	public int getFollowing() {
		return following;
	}
	public int getFollowers() {
		return followers;
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
	public String getLastBlogEntry() {
		return lastBlogEntry;
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
	public void setId(int id) {
		this.id = id;
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
	public void setInDegree(int inDegree) {
		this.inDegree = inDegree;
	}
	public void setOutDegree(int outDegree) {
		this.outDegree = outDegree;
	}
	public void setBetweenessCentrality(long betweenessCentrality) {
		this.betweenessCentrality = betweenessCentrality;
	}
	public void setClosenessCentrality(long closenessCentrality) {
		this.closenessCentrality = closenessCentrality;
	}
	public void setEigenvectorCentrality(long eigenvectorCentrality) {
		this.eigenvectorCentrality = eigenvectorCentrality;
	}
	public void setClusteringCoefficient(long clusteringCoefficient) {
		this.clusteringCoefficient = clusteringCoefficient;
	}
	public void setFollowing(int following) {
		this.following = following;
	}
	public void setFollowers(int followers) {
		this.followers = followers;
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
	public void setLastBlogEntry(String lastBlogEntry) {
		this.lastBlogEntry = lastBlogEntry;
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
