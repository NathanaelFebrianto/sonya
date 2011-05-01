/*
 * Copyright (c) 2011, Young-Gue Bae
 * All rights reserved.
 */
package com.beeblz.twitter.io.service;

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

	//private TweetMapper mapper;
	
	/**
     * Constructor.
     *
     */
    public TweetManagerImpl() {
    }
	
	/**
     * Constructor.
     *
     * @param mapper the tweet mapper
     */
    public TweetManagerImpl(TweetMapper mapper) {
        //this.mapper = mapper;
    }
    
    /**
     * Adds a tweet.
     *
     * @param tweet the tweet
     */
	public void addTweet(Tweet tweet) {
		//mapper.insertTweet(tweet);

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
