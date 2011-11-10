package com.nhn.socialanalytics.me2day.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Data model for a metoo.
 * 
 * @author Younggue Bae
 */
@SuppressWarnings("serial")
public class Metoo implements Serializable {

	private String objectId;
	private String postId;
	private String authorId;
	private String authorNickname;
	private String authorProfileImage;
	private String authorMe2dayHome;
	private Date publishDate;
	private Date colCreaerDate;
	private Date colUpdateDate;
	
	private Date publishStartDate;
	private Date publishEndDate;
	
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public String getAuthorNickname() {
		return authorNickname;
	}
	public void setAuthorNickname(String authorNickname) {
		this.authorNickname = authorNickname;
	}
	public String getAuthorProfileImage() {
		return authorProfileImage;
	}
	public void setAuthorProfileImage(String authorProfileImage) {
		this.authorProfileImage = authorProfileImage;
	}
	public String getAuthorMe2dayHome() {
		return authorMe2dayHome;
	}
	public void setAuthorMe2dayHome(String authorMe2dayHome) {
		this.authorMe2dayHome = authorMe2dayHome;
	}
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	public Date getColCreaerDate() {
		return colCreaerDate;
	}
	public void setColCreaerDate(Date colCreaerDate) {
		this.colCreaerDate = colCreaerDate;
	}
	public Date getColUpdateDate() {
		return colUpdateDate;
	}
	public void setColUpdateDate(Date colUpdateDate) {
		this.colUpdateDate = colUpdateDate;
	}
	public Date getPublishStartDate() {
		return publishStartDate;
	}
	public void setPublishStartDate(Date publishStartDate) {
		this.publishStartDate = publishStartDate;
	}
	public Date getPublishEndDate() {
		return publishEndDate;
	}
	public void setPublishEndDate(Date publishEndDate) {
		this.publishEndDate = publishEndDate;
	}
	
}
