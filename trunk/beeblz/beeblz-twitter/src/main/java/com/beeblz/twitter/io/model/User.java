/*
 * Copyright (c) 2011, Young-Gue Bae
 * All rights reserved.
 */
package com.beeblz.twitter.io.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Data model for a user.
 * 
 * @author Young-Gue Bae
 */
public class User implements Serializable {

	private String id;
	private long userNo;
	private String name;
	private String location;
	private String profileImageUrl;
	private int followersCount;
	private int friendsCount;
	private int tweetsCount;
	private boolean isTarget;
	private Date colCreateDate;
	private Date colUpdateDate;
	
	public String getId() {
		return id;
	}
	public long getUserNo() {
		return userNo;
	}
	public String getName() {
		return name;
	}
	public String getLocation() {
		return location;
	}
	public String getProfileImageUrl() {
		return profileImageUrl;
	}
	public int getFollowersCount() {
		return followersCount;
	}
	public int getFriendsCount() {
		return friendsCount;
	}
	public int getTweetsCount() {
		return tweetsCount;
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
	public void setId(String id) {
		this.id = id;
	}
	public void setUserNo(long userNo) {
		this.userNo = userNo;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}
	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}
	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}
	public void setTweetsCount(int tweetsCount) {
		this.tweetsCount = tweetsCount;
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
