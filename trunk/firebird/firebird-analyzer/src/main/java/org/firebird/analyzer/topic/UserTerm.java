/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.topic;

import java.io.Serializable;
import java.util.Date;

/**
 * Data model for a user topic term.
 * 
 * @author Young-Gue Bae
 */
public class UserTerm implements Serializable, Comparable<UserTerm> {

	private static final long serialVersionUID = 215000208015378469L;

	private String userId;
	private int docId;
	private String term;
	private int termFreq;
	private float tf;
	private float idf;
	private String timeline;
	private Date createDate;
	private Date lastUpdateDate;
	
	public String getUserId() {
		return userId;
	}
	public int getDocId() {
		return docId;
	}
	public String getTerm() {
		return term;
	}
	public int getTermFreq() {
		return termFreq;
	}
	public float getTF() {
		return tf;
	}
	public float getIDF() {
		return idf;
	}
	public String getTimeline() {
		return timeline;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setDocId(int docId) {
		this.docId = docId;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public void setTermFreq(int termFreq) {
		this.termFreq = termFreq;
	}
	public void setTF(float tf) {
		this.tf = tf;
	}
	public void setIDF(float idf) {
		this.idf = idf;
	}
	public void setTimeline(String timeline) {
		this.timeline = timeline;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	@Override
	public int compareTo(UserTerm other) {
		//return Float.compare(tf, other.getTF());
		//order by desc
		return Float.compare(other.getTF(), tf);
	}
}
