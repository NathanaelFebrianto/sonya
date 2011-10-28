package com.nhn.socialbuzz.twitter.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Data model for a tweet.
 * 
 * @author Younggue Bae
 */
public class Tweet implements Serializable {

	private String tweetId;
	private String programId;
	private String user;
	private String userNo;
	private String tweetText;
	private Date createDate;
	private String location;
	private String profileImage;
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
	
	private Date createStartDate;
	private Date createEndDate;
	
	public String getTweetId() {
		return tweetId;
	}
	public String getProgramId() {
		return programId;
	}
	public String getUser() {
		return user;
	}
	public String getUserNo() {
		return userNo;
	}
	public String getTweetText() {
		return tweetText;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public String getLocation() {
		return location;
	}
	public String getProfileImage() {
		return profileImage;
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
	public Date getCreateStartDate() {
		return createStartDate;
	}
	public Date getCreateEndDate() {
		return createEndDate;
	}
	public void setTweetId(String tweetId) {
		this.tweetId = tweetId;
	}
	public void setProgramId(String programId) {
		this.programId = programId;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public void setTweetText(String tweetText) {
		this.tweetText = tweetText;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
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
	public void setCreateStartDate(Date createStartDate) {
		this.createStartDate = createStartDate;
	}
	public void setCreateEndDate(Date createEndDate) {
		this.createEndDate = createEndDate;
	}
	
}
