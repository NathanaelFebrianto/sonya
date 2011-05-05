/*
 * Copyright (c) 2011, Young-Gue Bae
 * All rights reserved.
 */
package com.beeblz.twitter.io.dao;

import java.util.List;

import com.beeblz.twitter.io.GenericMapper;
import com.beeblz.twitter.io.model.TweetMentionedUser;

/**
 * A interface for tweet mentioned user mapper.
 * 
 * @author Young-Gue Bae
 */
public interface TweetMentionedUserMapper extends GenericMapper {

	public List<TweetMentionedUser> selectTweetMentionedUsers(TweetMentionedUser tweetMentionedUser);
	
	public void insertTweetMentionedUser(TweetMentionedUser tweetMentionedUser);

}
