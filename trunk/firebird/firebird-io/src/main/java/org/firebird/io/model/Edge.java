/*
Copyright (c) 2009-2010, Young-Gue Bae
All rights reserved.
*/
package org.firebird.io.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Data model for a edge.
 * 
 * @author Young-Gue Bae
 */
public class Edge implements Serializable {	
	private static final long serialVersionUID = -1346539592439752207L;
	private int websiteId;
	private String vertex1;
	private String vertex2;
	private int vertexNo1;
	private int vertexNo2;
	private String color;
	private int width;
	private int opacity;
	private boolean directed;
	private String relationship;
	private long closeness;
	private int replyCount;
	private int dmCount;
	private int referCount;
	private Date lastReplyDate;
	private Date lastDmDate;
	private Date lastReferDate;
	private Date createDate;
	private Date lastUpdateDate;
	private Date colCreateDate;
	private Date colLastUpdateDate;
	
	public int getWebsiteId() {
		return websiteId;
	}
	public String getVertex1() {
		return vertex1;
	}
	public String getVertex2() {
		return vertex2;
	}
	public int getVertexNo1() {
		return vertexNo1;
	}
	public int getVertexNo2() {
		return vertexNo2;
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
	public boolean getDirected() {
		return directed;
	}
	public String getRelationship() {
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
	public Date getLastReplyDate() {
		return lastReplyDate;
	}
	public Date getLastDmDate() {
		return lastDmDate;
	}
	public Date getLastReferDate() {
		return lastReferDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public Date getColCreateDate() {
		return colCreateDate;
	}
	public Date getColLastUpdateDate() {
		return colLastUpdateDate;
	}
	public void setWebsiteId(int websiteId) {
		this.websiteId = websiteId;
	}
	public void setVertex1(String vertex1) {
		this.vertex1 = vertex1;
	}
	public void setVertex2(String vertex2) {
		this.vertex2 = vertex2;
	}
	public void setVertexNo1(int vertexNo1) {
		this.vertexNo1 = vertexNo1;
	}
	public void setVertexNo2(int vertexNo2) {
		this.vertexNo2 = vertexNo2;
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
	public void setDirected(boolean directed) {
		this.directed = directed;
	}
	public void setRelationship(String relationship) {
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
	public void setLastReplyDate(Date lastReplyDate) {
		this.lastReplyDate = lastReplyDate;
	}
	public void setLastDmDate(Date lastDmDate) {
		this.lastDmDate = lastDmDate;
	}
	public void setLastReferDate(Date lastReferDate) {
		this.lastReferDate = lastReferDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public void setColCreateDate(Date colCreateDate) {
		this.colCreateDate = colCreateDate;
	}
	public void setColLastUpdateDate(Date colLastUpdateDate) {
		this.colLastUpdateDate = colLastUpdateDate;
	}
	
}
