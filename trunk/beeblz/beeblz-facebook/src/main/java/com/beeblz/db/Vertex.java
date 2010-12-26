/**
 * Copyright (c) 2010, beeblz.com
 * All rights reserved.
 */
package com.beeblz.db;

import java.io.Serializable;

/**
 * This is a vertex model.
 * 
 * @author YoungGue Bae
 */
public class Vertex implements Serializable {
	
	private String id;
	private String name;
	private String email;
	private String picture;
	private Boolean isMe;
	private Boolean isMyFriend;
	private Integer mutualFriendCount = -1;
	private Integer cluster = -1;
	private Integer postStatusCount = -1;
	private Integer postLinkCount = -1;
	private Integer postPhotoCount = -1;
	private Integer postVideoCount = -1;
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getEmail() {
		return email;
	}
	public String getPicture() {
		return picture;
	}
	public Boolean getIsMe() {
		return isMe;
	}
	public Boolean getIsMyFriend() {
		return isMyFriend;
	}
	public Integer getMutualFriendCount() {
		return mutualFriendCount;
	}
	public Integer getCluster() {
		return cluster;
	}
	public Integer getPostStatusCount() {
		return postStatusCount;
	}
	public Integer getPostLinkCount() {
		return postLinkCount;
	}
	public Integer getPostPhotoCount() {
		return postPhotoCount;
	}
	public Integer getPostVideoCount() {
		return postVideoCount;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public void setIsMe(Boolean isMe) {
		this.isMe = isMe;
	}
	public void setIsMyFriend(Boolean isMyFriend) {
		this.isMyFriend = isMyFriend;
	}
	public void setMutualFriendCount(Integer mutualFriendCount) {
		this.mutualFriendCount = mutualFriendCount;
	}
	public void setCluster(Integer cluster) {
		this.cluster = cluster;
	}
	public void setPostStatusCount(Integer postStatusCount) {
		this.postStatusCount = postStatusCount;
	}
	public void setPostLinkCount(Integer postLinkCount) {
		this.postLinkCount = postLinkCount;
	}
	public void setPostPhotoCount(Integer postPhotoCount) {
		this.postPhotoCount = postPhotoCount;
	}
	public void setPostVideoCount(Integer postVideoCount) {
		this.postVideoCount = postVideoCount;
	}

}
