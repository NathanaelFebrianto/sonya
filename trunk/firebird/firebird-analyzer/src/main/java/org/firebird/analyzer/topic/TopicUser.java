/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.topic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
	private String userName;
	private List<TopicTerm> topicTerms = null;
	private List<UserTerm> userMatchTerms = null;
	private String topicTermsString;
	private String userMatchTermsString;
	private int userMatchTermsCount;
	private float score;
	private Date createDate;
	private Date lastUpdateDate;
	
	public int getTopicId() {
		return topicId;
	}
	public String getUserId() {
		return userId;
	}
	public String getUserName() {
		return userName;
	}
	public List<TopicTerm> getTopicTerms() {
		return topicTerms;
	}
	public List<UserTerm> getUserMatchTerms() {
		return userMatchTerms;
	}
	public String getTopicTermsString() {
		return topicTermsString;
	}
	public String getUserMatchTermsString() {
		return userMatchTermsString;
	}
	public int getUserMatchTermsCount() {
		return userMatchTermsCount;
	}
	public float getScore() {
		return score;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setTopicTerms(List<TopicTerm> topicTerms) {
		this.topicTerms = topicTerms;
	}
	public void setUserMatchTerms(List<UserTerm> userMatchTerms) {
		this.userMatchTerms = userMatchTerms;
	}
	public void setTopicTermsString(String topicTermsString) {
		this.topicTermsString = topicTermsString;
	}
	public void setUserMatchTermsString(String userMatchTermsString) {
		this.userMatchTermsString = userMatchTermsString;
	}
	public void setUserMatchTermsCount(int userMatchTermsCount) {
		this.userMatchTermsCount = userMatchTermsCount;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	public List<UserTerm> findUserMatchTerms(List<UserTerm> userTerms) throws Exception {
		if (topicTerms == null) {
			throw new Exception("Topic Terms is null.");
		}
		
		List<UserTerm> result = new ArrayList<UserTerm>();
		
		for (int i = 0; i < userTerms.size(); i++) {
			UserTerm userTerm = (UserTerm)userTerms.get(i);
			if (matchWithTopicTerm(userTerm)) {
				result.add(userTerm);
			}
		}
		this.userMatchTerms = result;
		return this.userMatchTerms;
	}

	public float calculateScore() throws Exception {
		if (topicTerms == null) {
			throw new Exception("Topic Terms is null.");
		}
		if (userMatchTerms == null) {
			throw new Exception("User Match Terms is null.");
		}
		
		float sumScore = 0.0f;
		for (int i = 0; i < userMatchTerms.size(); i++) {
			UserTerm userTerm = (UserTerm)userMatchTerms.get(i);
			float score = userTerm.getTermFreq() * Float.valueOf(String.valueOf(getTopicTerm(userTerm.getTerm()).getScore()));
			sumScore += score;
		}		
		this.score = sumScore;
		
		return this.score;
	}
	
	public String toTopicTermsString() {
		StringBuffer sb = new StringBuffer();
		
		for (TopicTerm topicTerm : topicTerms) {
			sb.append(topicTerm.getTerm())
			  .append("(")
			  .append(convertScale(topicTerm.getScore(), 2))
			  .append(") ");
		}		
		return sb.toString();
	}
	
	public String toUserMatchTermsString() {		
		List<UserTermScore> sortedList = new ArrayList<UserTermScore>();
		for (int i = 0; i < userMatchTerms.size(); i++) {
			UserTerm userTerm = (UserTerm)userMatchTerms.get(i);
			//float score = userTerm.getTermFreq() * Float.valueOf(String.valueOf(getTopicTerm(userTerm.getTerm()).getScore()));
			UserTermScore userTermScore = new UserTermScore(userTerm, getTopicTerm(userTerm.getTerm()));
			sortedList.add(userTermScore);
		}
		// sort by score
		Collections.sort(sortedList);
		
		StringBuffer sb = new StringBuffer();
		for (UserTermScore userTermScore : sortedList) {
			sb.append(userTermScore.getUserTerm().getTerm())
			  .append("(")
			  .append(convertScale(userTermScore.getScore(), 2))
			  .append(") ");			
		}
		
		return sb.toString();
	}
	
	private double convertScale(double d, int scale) {
		return new BigDecimal(d).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	private boolean matchWithTopicTerm(UserTerm userTerm) {
		if (userTerm == null)	return false;
		
		for (TopicTerm topicTerm : topicTerms) {
			if (topicTerm.getTerm().equals(userTerm.getTerm())) {
				return true;
			}
		}		
		return false;
	}
	
	private TopicTerm getTopicTerm(String term) {
		for (TopicTerm topicTerm : topicTerms) {
			if (topicTerm.getTerm().equals(term)) {
				return topicTerm;
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
	
	private class UserTermScore implements Comparable<UserTermScore> {
		UserTerm userTerm;
		TopicTerm topicTerm;
		float score = 0.0f;
		
		UserTermScore(UserTerm userTerm, TopicTerm topicTerm) {
			this.userTerm = userTerm;
			this.topicTerm = topicTerm;
		}
		
		private UserTerm getUserTerm() {
			return this.userTerm;
		}
		private float getScore() {
			this.score = userTerm.getTermFreq() * Float.valueOf(String.valueOf(topicTerm.getScore()));
			return this.score;
		}

		@Override
		public int compareTo(UserTermScore other) {
			//return Float.compare(score, other.getScore());
			//order by desc
			return Float.compare(other.getScore(), score);
		}
	}
}
