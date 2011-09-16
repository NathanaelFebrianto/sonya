package com.nhn.socialbuzz.twitter.service;

import java.util.List;

import com.nhn.socialbuzz.common.GenericManager;
import com.nhn.socialbuzz.twitter.model.Tweet;

/**
 * A interface for tweet manager.
 * 
 * @author Younggue Bae
 */
public interface TweetManager extends GenericManager {

	public List<Tweet> getTweets(Tweet tweet);

	public void addTweet(Tweet tweet);
	
	public void setTweet(Tweet tweet);
	
}
