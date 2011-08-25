package com.nhn.socialbuzz.me2day.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.mahout.df.data.Data;

/**
 * Data model for a comment.
 * 
 * @author Younggue Bae
 */
public class Comment implements Serializable {

	private String programId;
	private String postId;
	private String commentId;
	private String authorId;
	private String authorNickname;
	private String body;
	private String textBody;
	private Date publishDate;
	private String authorProfileImage;
	private String authorMe2dayHome;
	private double positive;
	private double negative;
	private Date registerDate;
	private Data updateDate;
	
	public String getProgramId() {
		return programId;
	}
	public String getPostId() {
		return postId;
	}
	public String getCommentId() {
		return commentId;
	}
	public String getAuthorId() {
		return authorId;
	}
	public String getAuthorNickname() {
		return authorNickname;
	}
	public String getBody() {
		return body;
	}
	public String getTextBody() {
		return textBody;
	}
	public Date getPublishDate() {
		return publishDate;
	}
	public String getAuthorProfileImage() {
		return authorProfileImage;
	}
	public String getAuthorMe2dayHome() {
		return authorMe2dayHome;
	}
	public double getPositive() {
		return positive;
	}
	public double getNegative() {
		return negative;
	}
	public Date getRegisterDate() {
		return registerDate;
	}
	public Data getUpdateDate() {
		return updateDate;
	}
	public void setProgramId(String programId) {
		this.programId = programId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public void setAuthorNickname(String authorNickname) {
		this.authorNickname = authorNickname;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public void setTextBody(String textBody) {
		this.textBody = textBody;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	public void setAuthorProfileImage(String authorProfileImage) {
		this.authorProfileImage = authorProfileImage;
	}
	public void setAuthorMe2dayHome(String authorMe2dayHome) {
		this.authorMe2dayHome = authorMe2dayHome;
	}
	public void setPositive(double positive) {
		this.positive = positive;
	}
	public void setNegative(double negative) {
		this.negative = negative;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	public void setUpdateDate(Data updateDate) {
		this.updateDate = updateDate;
	}

}
