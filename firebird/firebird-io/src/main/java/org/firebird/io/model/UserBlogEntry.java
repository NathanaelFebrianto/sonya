/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Data model for a user blog entry.
 * 
 * @author Young-Gue Bae
 */
public class UserBlogEntry implements Serializable {
	private static final long serialVersionUID = 5765114194107546966L;
	private int websiteId;
	private String userId;
	private int userNo;
	private String blogEntryId;
	private String title;
	private String body;
	private int sourceWebsiteId;
	private String blogEntryType;
	private String permaLinkUrl;
	private String userLinkUrl;
	private String replyTo;
	private String dmTo;
	private String referFrom;
	private Date createDate;
	private Date lastUpdateDate;
	private Date colCreateDate;
	private Date colLastUpdateDate;
	
	public int getWebsiteId() {
		return websiteId;
	}
	public String getUserId() {
		return userId;
	}
	public int getUserNo() {
		return userNo;
	}
	public String getBlogEntryId() {
		return blogEntryId;
	}
	public String getTitle() {
		return title;
	}
	public String getBody() {
		return body;
	}
	public int getSourceWebsiteId() {
		return sourceWebsiteId;
	}
	public String getBlogEntryType() {
		return blogEntryType;
	}
	public String getPermaLinkUrl() {
		return permaLinkUrl;
	}
	public String getUserLinkUrl() {
		return userLinkUrl;
	}
	public String getReplyTo() {
		return replyTo;
	}
	public String getDmTo() {
		return dmTo;
	}
	public String getReferFrom() {
		return referFrom;
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
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}
	public void setBlogEntryId(String blogEntryId) {
		this.blogEntryId = blogEntryId;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public void setSourceWebsiteId(int sourceWebsiteId) {
		this.sourceWebsiteId = sourceWebsiteId;
	}
	public void setBlogEntryType(String blogEntryType) {
		this.blogEntryType = blogEntryType;
	}
	public void setPermaLinkUrl(String permaLinkUrl) {
		this.permaLinkUrl = permaLinkUrl;
	}
	public void setUserLinkUrl(String userLinkUrl) {
		this.userLinkUrl = userLinkUrl;
	}
	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}
	public void setDmTo(String dmTo) {
		this.dmTo = dmTo;
	}
	public void setReferFrom(String referFrom) {
		this.referFrom = referFrom;
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
