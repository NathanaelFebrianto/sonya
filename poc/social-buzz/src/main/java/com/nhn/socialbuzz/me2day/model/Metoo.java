package com.nhn.socialbuzz.me2day.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.mahout.df.data.Data;

/**
 * Data model for a metoo.
 * 
 * @author Younggue Bae
 */
public class Metoo implements Serializable {

	private String postId;
	private String authorId;
	private String authorNickname;
	private String authorProfileImage;
	private String authorMe2dayHome;
	private Date publishDate;
	private Date registerDate;
	private Data updateDate;
	
	public String getPostId() {
		return postId;
	}
	public String getAuthorId() {
		return authorId;
	}
	public String getAuthorNickname() {
		return authorNickname;
	}
	public String getAuthorProfileImage() {
		return authorProfileImage;
	}
	public String getAuthorMe2dayHome() {
		return authorMe2dayHome;
	}
	public Date getPublishDate() {
		return publishDate;
	}
	public Date getRegisterDate() {
		return registerDate;
	}
	public Data getUpdateDate() {
		return updateDate;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public void setAuthorNickname(String authorNickname) {
		this.authorNickname = authorNickname;
	}
	public void setAuthorProfileImage(String authorProfileImage) {
		this.authorProfileImage = authorProfileImage;
	}
	public void setAuthorMe2dayHome(String authorMe2dayHome) {
		this.authorMe2dayHome = authorMe2dayHome;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	public void setUpdateDate(Data updateDate) {
		this.updateDate = updateDate;
	}

}