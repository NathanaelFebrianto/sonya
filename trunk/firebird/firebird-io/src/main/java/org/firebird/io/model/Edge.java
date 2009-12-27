package org.firebird.io.model;

import java.io.Serializable;

public class Edge implements Serializable {	
	private static final long serialVersionUID = -1346539592439752207L;
	private int websiteId;
	private int vertex1;
	private int vertex2;
	private String color;
	private int width;
	private int opacity;
	private boolean isDirected;
	private int relationship;
	private long closeness;
	private int replyCount;
	private int dmCount;
	private int referCount;
	private String lastReplyDate;
	private String lastDmDate;
	private String lastReferDate;
	private String createDate;
	private String lastUpdateDate;
	private String colCreateDate;
	
	public int getWebsiteId() {
		return websiteId;
	}
	public int getVertex1() {
		return vertex1;
	}
	public int getVertex2() {
		return vertex2;
	}
	public String getColor() {
		return color;
	}
	public int getWidth() {
		return width;
	}
	public int getOpacity() {
		return opacity;
	}
	public boolean isDirected() {
		return isDirected;
	}
	public int getRelationship() {
		return relationship;
	}
	public long getCloseness() {
		return closeness;
	}
	public int getReplyCount() {
		return replyCount;
	}
	public int getDmCount() {
		return dmCount;
	}
	public int getReferCount() {
		return referCount;
	}
	public String getLastReplyDate() {
		return lastReplyDate;
	}
	public String getLastDmDate() {
		return lastDmDate;
	}
	public String getLastReferDate() {
		return lastReferDate;
	}
	public String getCreateDate() {
		return createDate;
	}
	public String getLastUpdateDate() {
		return lastUpdateDate;
	}
	public String getColCreateDate() {
		return colCreateDate;
	}
	public String getColLastUpdateDate() {
		return colLastUpdateDate;
	}
	public void setWebsiteId(int websiteId) {
		this.websiteId = websiteId;
	}
	public void setVertex1(int vertex1) {
		this.vertex1 = vertex1;
	}
	public void setVertex2(int vertex2) {
		this.vertex2 = vertex2;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public void setOpacity(int opacity) {
		this.opacity = opacity;
	}
	public void setDirected(boolean isDirected) {
		this.isDirected = isDirected;
	}
	public void setRelationship(int relationship) {
		this.relationship = relationship;
	}
	public void setCloseness(long closeness) {
		this.closeness = closeness;
	}
	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}
	public void setDmCount(int dmCount) {
		this.dmCount = dmCount;
	}
	public void setReferCount(int referCount) {
		this.referCount = referCount;
	}
	public void setLastReplyDate(String lastReplyDate) {
		this.lastReplyDate = lastReplyDate;
	}
	public void setLastDmDate(String lastDmDate) {
		this.lastDmDate = lastDmDate;
	}
	public void setLastReferDate(String lastReferDate) {
		this.lastReferDate = lastReferDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public void setColCreateDate(String colCreateDate) {
		this.colCreateDate = colCreateDate;
	}
	public void setColLastUpdateDate(String colLastUpdateDate) {
		this.colLastUpdateDate = colLastUpdateDate;
	}
	private String colLastUpdateDate;

}
