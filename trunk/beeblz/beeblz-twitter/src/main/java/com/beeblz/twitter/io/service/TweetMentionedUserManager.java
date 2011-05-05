/*
 * Copyright (c) 2011, Young-Gue Bae
 * All rights reserved.
 */
package com.beeblz.twitter.io.service;

import java.util.List;

import com.beeblz.twitter.io.GenericManager;
import com.beeblz.twitter.io.model.TweetMentionedUser;

/**
 * A interface for tweet mentioned user manager.
 * 
 * @author Young-Gue Bae
 */
public interface TweetMentionedUserManager extends GenericManager {

	public List<TweetMentionedUser> getTweetMentionedUsers(TweetMentionedUser tweetMentionedUser);
	
	public void addTweetMentionedUser(TweetMentionedUser tweetMentionedUser);
	
}
