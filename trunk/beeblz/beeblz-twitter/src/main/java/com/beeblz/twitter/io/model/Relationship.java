/*
 * Copyright (c) 2011, Young-Gue Bae
 * All rights reserved.
 */
package com.beeblz.twitter.io.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Data model for a relationship.
 * 
 * @author Young-Gue Bae
 */
public class Relationship implements Serializable {

	private int id1;
	private int id2;
	private String user1;
	private String user2;
	private boolean isFollowedByUser2;
	private int retweetedCountByUser2;
	private int replyedCountByUser2;
	private int mentionedCountByUser2;
	private Date colCreateDate;
	private Date colUpdateDate;
	
	public int getId1() {
		return id1;
	}
	public int getId2() {
		return id2;
	}
	public String getUser1() {
		return user1;
	}
	public String getUser2() {
		return user2;
	}
	public boolean isFollowedByUser2() {
		return isFollowedByUser2;
	}
	public int getRetweetedCountByUser2() {
		return retweetedCountByUser2;
	}
	public int getReplyedCountByUser2() {
		return replyedCountByUser2;
	}
	public int getMentionedCountByUser2() {
		return mentionedCountByUser2;
	}
	public Date getColCreateDate() {
		return colCreateDate;
	}
	public Date getColUpdateDate() {
		return colUpdateDate;
	}
	public void setId1(int id1) {
		this.id1 = id1;
	}
	public void setId2(int id2) {
		this.id2 = id2;
	}
	public void setUser1(String user1) {
		this.user1 = user1;
	}
	public void setUser2(String user2) {
		this.user2 = user2;
	}
	public void setFollowedByUser2(boolean isFollowedByUser2) {
		this.isFollowedByUser2 = isFollowedByUser2;
	}
	public void setRetweetedCountByUser2(int retweetedCountByUser2) {
		this.retweetedCountByUser2 = retweetedCountByUser2;
	}
	public void setReplyedCountByUser2(int replyedCountByUser2) {
		this.replyedCountByUser2 = replyedCountByUser2;
	}
	public void setMentionedCountByUser2(int mentionedCountByUser2) {
		this.mentionedCountByUser2 = mentionedCountByUser2;
	}
	public void setColCreateDate(Date colCreateDate) {
		this.colCreateDate = colCreateDate;
	}
	public void setColUpdateDate(Date colUpdateDate) {
		this.colUpdateDate = colUpdateDate;
	}
	
	
}
