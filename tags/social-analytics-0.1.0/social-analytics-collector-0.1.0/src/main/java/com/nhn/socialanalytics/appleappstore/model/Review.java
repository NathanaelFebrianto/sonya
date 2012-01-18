package com.nhn.socialanalytics.appleappstore.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Review implements Serializable {
	
	private String appStoreId;
	private String country;
	private String reviewId;
	private String authorId;
	private String authorName;
	private String topic;
	private String text;
	private String version;
	private String createDate;
	private int rating;
	
	public String getAppStoreId() {
		return appStoreId;
	}
	public void setAppStoreId(String appStoreId) {
		this.appStoreId = appStoreId;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getReviewId() {
		return reviewId;
	}
	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	
}
