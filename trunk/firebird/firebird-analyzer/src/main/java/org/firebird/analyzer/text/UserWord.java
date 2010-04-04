/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.text;

import java.io.Serializable;

/**
 * Data model for a user topic word.
 * 
 * @author Young-Gue Bae
 */
public class UserWord implements Serializable, Comparable<UserWord> {

	private static final long serialVersionUID = 215000208015378469L;

	private String userId;
	private int docId;
	private String word;
	private int termFreq;
	private float tf;
	private float idf;
	private String timeline;
	
	public String getUserId() {
		return userId;
	}
	public int getDocId() {
		return docId;
	}
	public String getWord() {
		return word;
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
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setDocId(int docId) {
		this.docId = docId;
	}
	public void setWord(String word) {
		this.word = word;
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
	
	@Override
	public int compareTo(UserWord other) {
		//return Float.compare(tf, other.getTF());
		//order by desc
		return Float.compare(other.getTF(), tf);
	}
}
