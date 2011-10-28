package com.nhn.socialbuzz.twitter.dao;

import java.util.List;

import com.nhn.socialbuzz.common.GenericMapper;
import com.nhn.socialbuzz.twitter.model.Tweet;

/**
 * A interface for tweet mapper.
 * 
 * @author Younggue Bae
 */
public interface TweetMapper extends GenericMapper {

	public List<Tweet> selectTweets(Tweet tweet);
	
	public void insertTweet(Tweet tweet);
	
	public void updateTweet(Tweet tweet);

}
