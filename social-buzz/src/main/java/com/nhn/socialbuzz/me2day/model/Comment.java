package com.nhn.socialbuzz.me2day.model;

import java.io.Serializable;
import java.util.Date;

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
	private double liwcNegation;
	private double liwcQuantifier;
	private double liwcSwear;
	private double liwcQmark;
	private double liwcExclam;
	private double liwcSmile;
	private double liwcCry;
	private double liwcLove;
	private double liwcPositive;
	private double liwcNegative;
	private double liwcAnger;
	private double liwcAnxiety;
	private double liwcSadness;
	private Date registerDate;
	private Date updateDate;
	
	private Date publishStartDate;
	private Date publishEndDate;
	
	
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
	public double getLiwcNegation() {
		return liwcNegation;
	}
	public double getLiwcQuantifier() {
		return liwcQuantifier;
	}
	public double getLiwcSwear() {
		return liwcSwear;
	}
	public double getLiwcQmark() {
		return liwcQmark;
	}
	public double getLiwcExclam() {
		return liwcExclam;
	}
	public double getLiwcSmile() {
		return liwcSmile;
	}
	public double getLiwcCry() {
		return liwcCry;
	}
	public double getLiwcLove() {
		return liwcLove;
	}
	public double getLiwcPositive() {
		return liwcPositive;
	}
	public double getLiwcNegative() {
		return liwcNegative;
	}
	public double getLiwcAnger() {
		return liwcAnger;
	}
	public double getLiwcAnxiety() {
		return liwcAnxiety;
	}
	public double getLiwcSadness() {
		return liwcSadness;
	}
	public Date getRegisterDate() {
		return registerDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public Date getPublishStartDate() {
		return publishStartDate;
	}
	public Date getPublishEndDate() {
		return publishEndDate;
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
	public void setLiwcNegation(double liwcNegation) {
		this.liwcNegation = liwcNegation;
	}
	public void setLiwcQuantifier(double liwcQuantifier) {
		this.liwcQuantifier = liwcQuantifier;
	}
	public void setLiwcSwear(double liwcSwear) {
		this.liwcSwear = liwcSwear;
	}
	public void setLiwcQmark(double liwcQmark) {
		this.liwcQmark = liwcQmark;
	}
	public void setLiwcExclam(double liwcExclam) {
		this.liwcExclam = liwcExclam;
	}
	public void setLiwcSmile(double liwcSmile) {
		this.liwcSmile = liwcSmile;
	}
	public void setLiwcCry(double liwcCry) {
		this.liwcCry = liwcCry;
	}
	public void setLiwcLove(double liwcLove) {
		this.liwcLove = liwcLove;
	}
	public void setLiwcPositive(double liwcPositive) {
		this.liwcPositive = liwcPositive;
	}
	public void setLiwcNegative(double liwcNegative) {
		this.liwcNegative = liwcNegative;
	}
	public void setLiwcAnger(double liwcAnger) {
		this.liwcAnger = liwcAnger;
	}
	public void setLiwcAnxiety(double liwcAnxiety) {
		this.liwcAnxiety = liwcAnxiety;
	}
	public void setLiwcSadness(double liwcSadness) {
		this.liwcSadness = liwcSadness;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public void setPublishStartDate(Date publishStartDate) {
		this.publishStartDate = publishStartDate;
	}
	public void setPublishEndDate(Date publishEndDate) {
		this.publishEndDate = publishEndDate;
	}

}
