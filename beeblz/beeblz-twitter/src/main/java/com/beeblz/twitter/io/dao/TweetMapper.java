/*
 * Copyright (c) 2011, Young-Gue Bae
 * All rights reserved.
 */
package com.beeblz.twitter.io.dao;

import com.beeblz.twitter.io.GenericMapper;
import com.beeblz.twitter.io.model.Tweet;

/**
 * A interface for tweet mapper.
 * 
 * @author Young-Gue Bae
 */
public interface TweetMapper extends GenericMapper {

    /**
     * Inserts a tweet.
     *
     * @param tweet the tweet
     */
	public void insertTweet(Tweet tweet);

}
