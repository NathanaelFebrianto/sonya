package com.nhn.socialanalytics.me2day.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Data model for a post.
 * 
 * @author Younggue Bae
 */
@SuppressWarnings("serial")
public class Post implements Serializable {

	private String objectId;
	private String postId;
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
	public String getPermalink() {
		return permalink;
	}
	public void setPermalink(String permalink) {
		this.permalink = permalink;
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
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getTagText() {
		return tagText;
	}
	public void setTagText(String tagText) {
		this.tagText = tagText;
	}
	public String getMe2dayPage() {
		return me2dayPage;
	}
	public void setMe2dayPage(String me2dayPage) {
		this.me2dayPage = me2dayPage;
	}
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public int getMetooCount() {
		return metooCount;
	}
	public void setMetooCount(int metooCount) {
		this.metooCount = metooCount;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getCallbackUrl() {
		return callbackUrl;
	}
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
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
	public String getAuthorLocation() {
		return authorLocation;
	}
	public void setAuthorLocation(String authorLocation) {
		this.authorLocation = authorLocation;
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
