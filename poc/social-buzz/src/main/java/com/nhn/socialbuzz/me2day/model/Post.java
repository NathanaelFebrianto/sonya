package com.nhn.socialbuzz.me2day.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.mahout.df.data.Data;

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
	private double postPositive;
	private double postNegative;
	private double tagPositive;
	private double tagNegative;
	private Date registerDate;
	private Data updateDate;
	
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
	public double getPostPositive() {
		return postPositive;
	}
	public double getPostNegative() {
		return postNegative;
	}
	public double getTagPositive() {
		return tagPositive;
	}
	public double getTagNegative() {
		return tagNegative;
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
	public void setPostPositive(double postPositive) {
		this.postPositive = postPositive;
	}
	public void setPostNegative(double postNegative) {
		this.postNegative = postNegative;
	}
	public void setTagPositive(double tagPositive) {
		this.tagPositive = tagPositive;
	}
	public void setTagNegative(double tagNegative) {
		this.tagNegative = tagNegative;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	public void setUpdateDate(Data updateDate) {
		this.updateDate = updateDate;
	}
	

}
