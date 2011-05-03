/*
 * Copyright (c) 2011, Young-Gue Bae
 * All rights reserved.
 */
package com.beeblz.twitter.io.service;

import java.util.List;

import com.beeblz.twitter.io.GenericManager;
import com.beeblz.twitter.io.model.Tweet;

/**
 * A interface for tweet manager.
 * 
 * @author Young-Gue Bae
 */
public interface TweetManager extends GenericManager {

	public List<Tweet> getTweets(Tweet tweet);
	
	public void addTweet(Tweet tweet);
	
	public void setTweet(Tweet tweet);
	
}
