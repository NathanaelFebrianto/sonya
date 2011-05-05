/*
 * Copyright (c) 2011, Young-Gue Bae
 * All rights reserved.
 */
package com.beeblz.twitter.io.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Data model for a mentioned users of tweet.
 * 
 * @author Young-Gue Bae
 */
public class TweetMentionedUser implements Serializable {

	private long id;
	private String user;
	private String mentionedUser;
	private long userNo;
	private boolean isTarget;
	private Date colCreateDate;
	private Date colUpdateDate;
	
	public long getId() {
		return id;
	}
	public String getUser() {
		return user;
	}
	public String getMentionedUser() {
		return mentionedUser;
	}
	public long getUserNo() {
		return userNo;
	}
	public boolean getIsTarget() {
		return isTarget;
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
	public void setMentionedUser(String mentionedUser) {
		this.mentionedUser = mentionedUser;
	}
	public void setUserNo(long userNo) {
		this.userNo = userNo;
	}
	public void setIsTarget(boolean isTarget) {
		this.isTarget = isTarget;
	}
	public void setColCreateDate(Date colCreateDate) {
		this.colCreateDate = colCreateDate;
	}
	public void setColUpdateDate(Date colUpdateDate) {
		this.colUpdateDate = colUpdateDate;
	}

}
