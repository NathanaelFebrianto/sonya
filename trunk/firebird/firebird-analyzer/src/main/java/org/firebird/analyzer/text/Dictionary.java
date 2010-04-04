/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.text;

import java.io.Serializable;

/**
 * Data model for a dictionary.
 * 
 * @author Young-Gue Bae
 */
public class Dictionary implements Serializable, Comparable<Dictionary> {
	
	private static final long serialVersionUID = -5108673345619615396L;
	
	private int seq;
	private String word;
	private int docFreq;
	
	public int getSeq() {
		return seq;
	}
	public String getWord() {
		return word;
	}
	public int getDocFreq() {
		return docFreq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public void setDocFreq(int docFreq) {
		this.docFreq = docFreq;
	}
	
	@Override
	public int compareTo(Dictionary other) {
		//return Integer.valueOf(docFreq).compareTo(Integer.valueOf(other.getDocFreq()));
		//order by desc
		return Integer.valueOf(other.getDocFreq()).compareTo(Integer.valueOf(docFreq));
	}	
}
