/*
 * Copyright (c) 2011, Young-Gue Bae
 * All rights reserved.
 */
package com.beeblz.twitter.io.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.beeblz.twitter.io.GenericManagerImpl;
import com.beeblz.twitter.io.dao.TweetMapper;
import com.beeblz.twitter.io.model.Tweet;

/**
 * A implementation for tweet manager.
 * 
 * @author Young-Gue Bae
 */
public class TweetManagerImpl extends GenericManagerImpl implements TweetManager {

    public TweetManagerImpl() { }
	
	public List<Tweet> getTweets(Tweet tweet) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TweetMapper mapper = session.getMapper(TweetMapper.class);
    		List<Tweet> result = mapper.selectTweets(tweet);
    		return result;
    	} finally {
    		session.close();
    	}
	}

	public void addTweet(Tweet tweet) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TweetMapper mapper = session.getMapper(TweetMapper.class);
    		mapper.insertTweet(tweet);
    		session.commit();
    	} finally {
    		session.close();
    	}
	}
	
}
