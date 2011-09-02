package com.nhn.socialbuzz.me2day.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Data model for a post.
 * 
 * @author Younggue Bae
 */
public class Post implements Serializable {

	private String postId;
	private String programId;
	private String permalink;
	private String body;
	private String textBody;
	private String icon;
	private String tagText;
	private String me2dayPage;
	private Date publishDate;
	private int commentCount;
	private int metooCount;
	private String iconUrl;
	private String callbackUrl;
	private String authorId;
	private String authorNickname;
	private String authorProfileImage;
	private String authorMe2dayHome;
	private String authorLocation;	
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
	
	
	public String getPostId() {
		return postId;
	}
	public String getProgramId() {
		return programId;
	}
	public String getPermalink() {
		return permalink;
	}
	public String getBody() {
		return body;
	}
	public String getTextBody() {
		return textBody;
	}
	public String getIcon() {
		return icon;
	}
	public String getTagText() {
		return tagText;
	}
	public String getMe2dayPage() {
		return me2dayPage;
	}
	public Date getPublishDate() {
		return publishDate;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public int getMetooCount() {
		return metooCount;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public String getCallbackUrl() {
		return callbackUrl;
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
	public String getAuthorLocation() {
		return authorLocation;
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
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public void setProgramId(String programId) {
		this.programId = programId;
	}
	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public void setTextBody(String textBody) {
		this.textBody = textBody;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public void setTagText(String tagText) {
		this.tagText = tagText;
	}
	public void setMe2dayPage(String me2dayPage) {
		this.me2dayPage = me2dayPage;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public void setMetooCount(int metooCount) {
		this.metooCount = metooCount;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
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
	public void setAuthorLocation(String authorLocation) {
		this.authorLocation = authorLocation;
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
