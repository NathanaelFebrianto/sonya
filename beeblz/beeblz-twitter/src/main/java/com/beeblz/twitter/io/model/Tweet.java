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

	private int id;
	private String user;
	private int userId;
	private String text;
	private String url;
	private String geoLocation;
	private String location;
	private String sourceTweet;
	private String retweetFromUser;
	private int retweetFromUserId;
	private String mentionToUser;
	private int mentionToUserId;
	private String replyToUser;
	private int replyToUserId;
	private Date createDate;
	private Date colCreateDate;
	private Date colUpdateDate;
	
	public int getId() {
		return id;
	}
	public String getUser() {
		return user;
	}
	public int getUserId() {
		return userId;
	}
	public String getText() {
		return text;
	}
	public String getUrl() {
		return url;
	}
	public String getGeoLocation() {
		return geoLocation;
	}
	public String getLocation() {
		return location;
	}
	public String getSourceTweet() {
		return sourceTweet;
	}
	public String getRetweetFromUser() {
		return retweetFromUser;
	}
	public int getRetweetFromUserId() {
		return retweetFromUserId;
	}
	public String getMentionToUser() {
		return mentionToUser;
	}
	public int getMentionToUserId() {
		return mentionToUserId;
	}
	public String getReplyToUser() {
		return replyToUser;
	}
	public int getReplyToUserId() {
		return replyToUserId;
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
	public void setId(int id) {
		this.id = id;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public void setText(String text) {
		this.text = text;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setGeoLocation(String geoLocation) {
		this.geoLocation = geoLocation;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public void setSourceTweet(String sourceTweet) {
		this.sourceTweet = sourceTweet;
	}
	public void setRetweetFromUser(String retweetFromUser) {
		this.retweetFromUser = retweetFromUser;
	}
	public void setRetweetFromUserId(int retweetFromUserId) {
		this.retweetFromUserId = retweetFromUserId;
	}
	public void setMentionToUser(String mentionToUser) {
		this.mentionToUser = mentionToUser;
	}
	public void setMentionToUserId(int mentionToUserId) {
		this.mentionToUserId = mentionToUserId;
	}
	public void setReplyToUser(String replyToUser) {
		this.replyToUser = replyToUser;
	}
	public void setReplyToUserId(int replyToUserId) {
		this.replyToUserId = replyToUserId;
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
