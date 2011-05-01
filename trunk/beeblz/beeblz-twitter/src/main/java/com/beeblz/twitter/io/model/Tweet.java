/*
 * Copyright (c) 2011, Young-Gue Bae
 * All rights reserved.
 */
package com.beeblz.twitter.io.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Data model for a tweet.
 * 
 * @author Young-Gue Bae
 */
public class Tweet implements Serializable {

	private long id;
	private String user;
	private long userId;
	private String text;
	private String url;
	private String retweetedUser;
	private long retweetedUserId;
	private String mentionedUser;
	private long mentionedUserId;
	private String replyUser;
	private long replyUserId;
	private boolean positiveAttitude;
	private boolean negativeAttitude;
	private Date createDate;
	private Date colCreateDate;
	private Date colUpdateDate;
	
	public long getId() {
		return id;
	}
	public String getUser() {
		return user;
	}
	public long getUserId() {
		return userId;
	}
	public String getText() {
		return text;
	}
	public String getUrl() {
		return url;
	}
	public String getRetweetedUser() {
		return retweetedUser;
	}
	public long getRetweetedUserId() {
		return retweetedUserId;
	}
	public String getMentionedUser() {
		return mentionedUser;
	}
	public long getMentionedUserId() {
		return mentionedUserId;
	}
	public String getReplyUser() {
		return replyUser;
	}
	public long getReplyUserId() {
		return replyUserId;
	}
	public boolean getPositiveAttitude() {
		return positiveAttitude;
	}
	public boolean getNegativeAttitude() {
		return negativeAttitude;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public Date getColCreateDate() {
		return colCreateDate;
	}
	public Date getColUpdateDate() {
		return colUpdateDate;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public void setText(String text) {
		this.text = text;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setRetweetedUser(String retweetedUser) {
		this.retweetedUser = retweetedUser;
	}
	public void setRetweetedUserId(long retweetedUserId) {
		this.retweetedUserId = retweetedUserId;
	}
	public void setMentionedUser(String mentionedUser) {
		this.mentionedUser = mentionedUser;
	}
	public void setMentionedUserId(long mentionedUserId) {
		this.mentionedUserId = mentionedUserId;
	}
	public void setReplyUser(String replyUser) {
		this.replyUser = replyUser;
	}
	public void setReplyUserId(long replyUserId) {
		this.replyUserId = replyUserId;
	}
	public void setPositiveAttitude(boolean positiveAttitude) {
		this.positiveAttitude = positiveAttitude;
	}
	public void setNegativeAttitude(boolean negativeAttitude) {
		this.negativeAttitude = negativeAttitude;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setColCreateDate(Date colCreateDate) {
		this.colCreateDate = colCreateDate;
	}
	public void setColUpdateDate(Date colUpdateDate) {
		this.colUpdateDate = colUpdateDate;
	}
	
}
