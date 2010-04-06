/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.topic;

import java.io.Serializable;

/**
 * Data model for a topic word.
 * 
 * @author Young-Gue Bae
 */
public class TopicWord implements Serializable, Comparable<TopicWord> {

	private static final long serialVersionUID = 4796609737710136397L;
	
	private int topicId;
	private String word;
	private double score;
	
	public int getTopicId() {
		return topicId;
	}
	public String getWord() {
		return word;
	}
	public double getScore() {
		return score;
	}	
	
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public void setScore(double score) {
		this.score = score;
	}
	
	@Override
	public int compareTo(TopicWord other) {
		//return Double.compare(score, other.getScore());
		//order by desc
		return Double.compare(other.getScore(), score);
	}
}
