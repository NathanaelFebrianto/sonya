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

	private String id1;
	private String id2;
	private long userNo1;
	private long userNo2;
	private boolean isFollowedByUser2;
	private int retweetedCountByUser2;
	private int replyedCountByUser2;
	private int mentionedCountByUser2;
	private Date colCreateDate;
	private Date colUpdateDate;
	
	public String getId1() {
		return id1;
	}
	public String getId2() {
		return id2;
	}
	public long getUserNo1() {
		return userNo1;
	}
	public long getUserNo2() {
		return userNo2;
	}
	public boolean getIsFollowedByUser2() {
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
	public void setId1(String id1) {
		this.id1 = id1;
	}
	public void setId2(String id2) {
		this.id2 = id2;
	}
	public void setUserNo1(long userNo1) {
		this.userNo1 = userNo1;
	}
	public void setUserNo2(long userNo2) {
		this.userNo2 = userNo2;
	}
	public void setIsFollowedByUser2(boolean isFollowedByUser2) {
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
