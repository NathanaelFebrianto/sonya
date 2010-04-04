/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data model for a topic-user.
 * 
 * @author Young-Gue Bae
 */
public class TopicUser implements Serializable ,Comparable<TopicUser> {

	private static final long serialVersionUID = 6075397439259576211L;
	
	private int topicId;
	private String userId;	
	private List<TopicWord> topicWords = null;
	private List<UserWord> userMatchWords = null;
	private int userMatchWordsCount;
	private float score;
	
	public int getTopicId() {
		return topicId;
	}
	public String getUserId() {
		return userId;
	}
	public List<TopicWord> getTopicWords() {
		return topicWords;
	}
	public List<UserWord> getUserMatchWords() {
		return userMatchWords;
	}
	public int getUserMatchWordsCount() {
		return userMatchWordsCount;
	}
	public float getScore() {
		return score;
	}
	
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setTopicWords(List<TopicWord> topicWords) {
		this.topicWords = topicWords;
	}
	public void setUserMatchWords(List<UserWord> userMatchWords) {
		this.userMatchWords = userMatchWords;
	}
	public void setUserMatchWordsCount(int userMatchWordsCount) {
		this.userMatchWordsCount = userMatchWordsCount;
	}
	public void setScore(float score) {
		this.score = score;
	}
	
	public List<UserWord> findUserMatchWords(List<UserWord> userWords) throws Exception {
		if (topicWords == null) {
			throw new Exception("Topic Words is null.");
		}
		
		List<UserWord> result = new ArrayList<UserWord>();
		
		for (int i = 0; i < userWords.size(); i++) {
			UserWord userWord = (UserWord)userWords.get(i);
			if (matchWithTopicWord(userWord)) {
				result.add(userWord);
			}
		}
		this.userMatchWords = result;
		return this.userMatchWords;
	}

	public float calculateScore() throws Exception {
		if (topicWords == null) {
			throw new Exception("Topic Words is null.");
		}
		if (userMatchWords == null) {
			throw new Exception("User Match Words is null.");
		}
		
		float sumScore = 0.0f;
		for (int i = 0; i < userMatchWords.size(); i++) {
			UserWord userWord = (UserWord)userMatchWords.get(i);
			float score = userWord.getTermFreq() * Float.valueOf(String.valueOf(getTopicWord(userWord.getWord()).getScore()));
			sumScore += score;
		}		
		this.score = sumScore;
		
		return this.score;
	}
	
	public String toTopicWordsString() {
		StringBuffer sb = new StringBuffer();
		
		for (TopicWord topicWord : topicWords) {
			sb.append(topicWord.getWord())
			  .append("(")
			  .append(topicWord.getScore())
			  .append(") ");
		}		
		return sb.toString();
	}
	
	public String toUserMatchWordsString() {		
		List<UserWordScore> sortedList = new ArrayList<UserWordScore>();
		for (int i = 0; i < userMatchWords.size(); i++) {
			UserWord userWord = (UserWord)userMatchWords.get(i);
			//float score = userWord.getTermFreq() * Float.valueOf(String.valueOf(getTopicWord(userWord.getWord()).getScore()));
			UserWordScore userWordScore = new UserWordScore(userWord, getTopicWord(userWord.getWord()));
			sortedList.add(userWordScore);
		}
		// sort by score
		Collections.sort(sortedList);
		
		StringBuffer sb = new StringBuffer();
		for (UserWordScore userWordScore : sortedList) {
			sb.append(userWordScore.getUserWord().getWord())
			  .append("(")
			  .append(userWordScore.getScore())
			  .append(") ");			
		}
		
		return sb.toString();
	}
	
	private boolean matchWithTopicWord(UserWord userWord) {
		if (userWord == null)	return false;
		
		for (TopicWord topicWord : topicWords) {
			if (topicWord.getWord().equals(userWord.getWord())) {
				return true;
			}
		}		
		return false;
	}
	
	private TopicWord getTopicWord(String word) {
		for (TopicWord topicWord : topicWords) {
			if (topicWord.getWord().equals(word)) {
				return topicWord;
			}
		}
		return null;
	}
	
	@Override
	public int compareTo(TopicUser other) {
		//return Float.compare(score, other.getScore());
		//order by desc
		return Float.compare(other.getScore(), score);
	}
	
	private class UserWordScore implements Comparable<UserWordScore> {
		UserWord userWord;
		TopicWord topicWord;
		float score = 0.0f;
		
		UserWordScore(UserWord userWord, TopicWord topicWord) {
			this.userWord = userWord;
			this.topicWord = topicWord;
		}
		
		private UserWord getUserWord() {
			return this.userWord;
		}
		private float getScore() {
			this.score = userWord.getTermFreq() * Float.valueOf(String.valueOf(topicWord.getScore()));
			return this.score;
		}

		@Override
		public int compareTo(UserWordScore other) {
			//return Float.compare(score, other.getScore());
			//order by desc
			return Float.compare(other.getScore(), score);
		}
	}
}
