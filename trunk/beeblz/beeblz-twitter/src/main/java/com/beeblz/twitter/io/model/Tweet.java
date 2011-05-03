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
	private long userNo;
	private String text;
	private String url;
	private String retweetedUser;
	private long retweetedUserNo;
	private String mentionedUser;
	private long mentionedUserNo;
	private String replyUser;
	private long replyUserNo;
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
	public long getUserNo() {
		return userNo;
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
	public long getRetweetedUserNo() {
		return retweetedUserNo;
	}
	public String getMentionedUser() {
		return mentionedUser;
	}
	public long getMentionedUserNo() {
		return mentionedUserNo;
	}
	public String getReplyUser() {
		return replyUser;
	}
	public long getReplyUserNo() {
		return replyUserNo;
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
	public void setUserNo(long userNo) {
		this.userNo = userNo;
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
	public void setRetweetedUserNo(long retweetedUserNo) {
		this.retweetedUserNo = retweetedUserNo;
	}
	public void setMentionedUser(String mentionedUser) {
		this.mentionedUser = mentionedUser;
	}
	public void setMentionedUserNo(long mentionedUserNo) {
		this.mentionedUserNo = mentionedUserNo;
	}
	public void setReplyUser(String replyUser) {
		this.replyUser = replyUser;
	}
	public void setReplyUserNo(long replyUserNo) {
		this.replyUserNo = replyUserNo;
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
