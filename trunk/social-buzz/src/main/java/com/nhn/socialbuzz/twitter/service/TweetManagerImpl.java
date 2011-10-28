package com.nhn.socialbuzz.twitter.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.nhn.socialbuzz.common.GenericManagerImpl;
import com.nhn.socialbuzz.twitter.dao.TweetMapper;
import com.nhn.socialbuzz.twitter.model.Tweet;

/**
 * A implementation for tweet manager.
 * 
 * @author Younggue Bae
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
	
	public void setTweet(Tweet tweet) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TweetMapper mapper = session.getMapper(TweetMapper.class);
    		mapper.updateTweet(tweet);
    		session.commit();
    	} finally {
    		session.close();
    	}		
	}
}
