/*
 * Copyright (c) 2011, Young-Gue Bae
 * All rights reserved.
 */
package com.beeblz.twitter.io.service;

import com.beeblz.twitter.io.GenericManager;
import com.beeblz.twitter.io.model.Tweet;

/**
 * A interface for tweet manager.
 * 
 * @author Young-Gue Bae
 */
public interface TweetManager extends GenericManager {

     /**
     * Adds a tweet.
     *
     * @param tweet the tweet
     */
	public void addTweet(Tweet tweet);
	
}
