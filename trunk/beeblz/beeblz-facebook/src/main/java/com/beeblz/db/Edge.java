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
	
	private String id;
	private String id1;
	private String id2;
	private Integer commentId1ToId2;
	private Integer likeId1ToId2;
	private Integer commentId2ToId1;
	private Integer likeId2ToId1;
	private Integer closenessId1ToId2;
	private Integer closenessId2ToId1;
	private Integer mutualCloseness;
	
	public String getId() {
		return id;
	}
	public String getId1() {
		return id1;
	}
	public String getId2() {
		return id2;
	}
	public Integer getCommentId1ToId2() {
		return commentId1ToId2;
	}
	public Integer getLikeId1ToId2() {
		return likeId1ToId2;
	}
	public Integer getCommentId2ToId1() {
		return commentId2ToId1;
	}
	public Integer getLikeId2ToId1() {
		return likeId2ToId1;
	}
	public Integer getClosenessId1ToId2() {
		return closenessId1ToId2;
	}
	public Integer getClosenessId2ToId1() {
		return closenessId2ToId1;
	}
	public Integer getMutualCloseness() {
		return mutualCloseness;
	}
	public void setId(String id) {
		this.id1 = id;
	}
	public void setId1(String id1) {
		this.id1 = id1;
	}
	public void setId2(String id2) {
		this.id2 = id2;
	}
	public void setCommentId1ToId2(Integer commentId1ToId2) {
		this.commentId1ToId2 = commentId1ToId2;
	}
	public void setLikeId1ToId2(Integer likeId1ToId2) {
		this.likeId1ToId2 = likeId1ToId2;
	}
	public void setCommentId2ToId1(Integer commentId2ToId1) {
		this.commentId2ToId1 = commentId2ToId1;
	}
	public void setLikeId2ToId1(Integer likeId2ToId1) {
		this.likeId2ToId1 = likeId2ToId1;
	}
	public void setClosenessId1ToId2(Integer closenessId1ToId2) {
		this.closenessId1ToId2 = closenessId1ToId2;
	}
	public void setClosenessId2ToId1(Integer closenessId2ToId1) {
		this.closenessId2ToId1 = closenessId2ToId1;
	}
	public void setMutualCloseness(Integer mutualCloseness) {
		this.mutualCloseness = mutualCloseness;
	}

}
