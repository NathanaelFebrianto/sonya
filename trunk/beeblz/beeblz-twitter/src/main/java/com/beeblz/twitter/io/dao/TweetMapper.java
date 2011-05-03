/*
 * Copyright (c) 2011, Young-Gue Bae
 * All rights reserved.
 */
package com.beeblz.twitter.io.dao;

import java.util.List;

import com.beeblz.twitter.io.GenericMapper;
import com.beeblz.twitter.io.model.Tweet;

/**
 * A interface for tweet mapper.
 * 
 * @author Young-Gue Bae
 */
public interface TweetMapper extends GenericMapper {

	public List<Tweet> selectTweets(Tweet tweet);
	
	public void insertTweet(Tweet tweet);
	
	public void updateTweet(Tweet tweet);

}
