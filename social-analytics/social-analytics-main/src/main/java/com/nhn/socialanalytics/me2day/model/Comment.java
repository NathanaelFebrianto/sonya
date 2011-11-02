package com.nhn.socialanalytics.me2day.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Data model for a comment.
 * 
 * @author Younggue Bae
 */
@SuppressWarnings("serial")
public class Comment implements Serializable {

	private String objectId;
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
	private Date colCreateDate;
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
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
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
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getTextBody() {
		return textBody;
	}
	public void setTextBody(String textBody) {
		this.textBody = textBody;
	}
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
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
	public double getLiwcNegation() {
		return liwcNegation;
	}
	public void setLiwcNegation(double liwcNegation) {
		this.liwcNegation = liwcNegation;
	}
	public double getLiwcQuantifier() {
		return liwcQuantifier;
	}
	public void setLiwcQuantifier(double liwcQuantifier) {
		this.liwcQuantifier = liwcQuantifier;
	}
	public double getLiwcSwear() {
		return liwcSwear;
	}
	public void setLiwcSwear(double liwcSwear) {
		this.liwcSwear = liwcSwear;
	}
	public double getLiwcQmark() {
		return liwcQmark;
	}
	public void setLiwcQmark(double liwcQmark) {
		this.liwcQmark = liwcQmark;
	}
	public double getLiwcExclam() {
		return liwcExclam;
	}
	public void setLiwcExclam(double liwcExclam) {
		this.liwcExclam = liwcExclam;
	}
	public double getLiwcSmile() {
		return liwcSmile;
	}
	public void setLiwcSmile(double liwcSmile) {
		this.liwcSmile = liwcSmile;
	}
	public double getLiwcCry() {
		return liwcCry;
	}
	public void setLiwcCry(double liwcCry) {
		this.liwcCry = liwcCry;
	}
	public double getLiwcLove() {
		return liwcLove;
	}
	public void setLiwcLove(double liwcLove) {
		this.liwcLove = liwcLove;
	}
	public double getLiwcPositive() {
		return liwcPositive;
	}
	public void setLiwcPositive(double liwcPositive) {
		this.liwcPositive = liwcPositive;
	}
	public double getLiwcNegative() {
		return liwcNegative;
	}
	public void setLiwcNegative(double liwcNegative) {
		this.liwcNegative = liwcNegative;
	}
	public double getLiwcAnger() {
		return liwcAnger;
	}
	public void setLiwcAnger(double liwcAnger) {
		this.liwcAnger = liwcAnger;
	}
	public double getLiwcAnxiety() {
		return liwcAnxiety;
	}
	public void setLiwcAnxiety(double liwcAnxiety) {
		this.liwcAnxiety = liwcAnxiety;
	}
	public double getLiwcSadness() {
		return liwcSadness;
	}
	public void setLiwcSadness(double liwcSadness) {
		this.liwcSadness = liwcSadness;
	}
	public Date getColCreateDate() {
		return colCreateDate;
	}
	public void setColCreateDate(Date colCreateDate) {
		this.colCreateDate = colCreateDate;
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
