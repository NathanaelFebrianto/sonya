/*
 * Copyright (c) 2011, Young-Gue Bae
 * All rights reserved.
 */
package com.beeblz.twitter.io.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.beeblz.twitter.io.GenericManagerImpl;
import com.beeblz.twitter.io.dao.TweetMentionedUserMapper;
import com.beeblz.twitter.io.model.TweetMentionedUser;

/**
 * A implementation for tweet mentioned user manager.
 * 
 * @author Young-Gue Bae
 */
public class TweetMentionedUserManagerImpl extends GenericManagerImpl implements TweetMentionedUserManager {

    public TweetMentionedUserManagerImpl() { }
	
    public List<TweetMentionedUser> getTweetMentionedUsers(TweetMentionedUser tweetMentionedUser) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TweetMentionedUserMapper mapper = session.getMapper(TweetMentionedUserMapper.class);
    		List<TweetMentionedUser> result = mapper.selectTweetMentionedUsers(tweetMentionedUser);
    		return result;
    	} finally {
    		session.close();
    	}
	}

    public void addTweetMentionedUser(TweetMentionedUser tweetMentionedUser) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TweetMentionedUserMapper mapper = session.getMapper(TweetMentionedUserMapper.class);
    		mapper.insertTweetMentionedUser(tweetMentionedUser);
    		session.commit();
    	} finally {
    		session.close();
    	}
	}

}
