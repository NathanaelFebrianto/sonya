/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.topic;

import java.io.Serializable;
import java.util.Date;

/**
 * Data model for a dictionary.
 * 
 * @author Young-Gue Bae
 */
public class Dictionary implements Serializable, Comparable<Dictionary> {
	
	private static final long serialVersionUID = -5108673345619615396L;
	
	private int websiteId;
	private int seq;
	private String term;
	private int docFreq;
	private Date createDate;
	private Date lastUpdateDate;
	
	public int getWebsiteId() {
		return websiteId;
	}
	public int getSeq() {
		return seq;
	}
	public String getTerm() {
		return term;
	}
	public int getDocFreq() {
		return docFreq;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	
	public void setWebsiteId(int websiteId) {
		this.websiteId = websiteId;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public void setDocFreq(int docFreq) {
		this.docFreq = docFreq;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	@Override
	public int compareTo(Dictionary other) {
		//return Integer.valueOf(docFreq).compareTo(Integer.valueOf(other.getDocFreq()));
		//order by desc
		return Integer.valueOf(other.getDocFreq()).compareTo(Integer.valueOf(docFreq));
	}	
}
