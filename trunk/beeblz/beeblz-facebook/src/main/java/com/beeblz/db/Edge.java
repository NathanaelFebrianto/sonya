/**
 * Copyright (c) 2010, beeblz.com
 * All rights reserved.
 */
package com.beeblz.db;

import java.io.Serializable;

/**
 * This is a edge model.
 * 
 * @author YoungGue Bae
 */
public class Edge implements Serializable {
	
	private String id = "";
	private String id1 = "";
	private String id2 = "";
	private Boolean isMe = false;
	private Boolean isMyFriend = false;
	private Integer commentCount = 0;
	private Integer likeCount = 0;
	
	public String getId() {
		return id;
	}
	public String getId1() {
		return id1;
	}
	public String getId2() {
		return id2;
	}
	public Boolean getIsMe() {
		return isMe;
	}
	public Boolean getIsMyFriend() {
		return isMyFriend;
	}
	public Integer getCommentCount() {
		return commentCount;
	}
	public Integer getLikeCount() {
		return likeCount;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setId1(String id1) {
		this.id1 = id1;
	}
	public void setId2(String id2) {
		this.id2 = id2;
	}
	public void setIsMe(Boolean isMe) {
		this.isMe = isMe;
	}
	public void setIsMyFriend(Boolean isMyFriend) {
		this.isMyFriend = isMyFriend;
	}
	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}
	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}

}
