package com.nhn.socialbuzz.twitter.collector;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.nhn.socialbuzz.common.CommonUtil;
import com.nhn.socialbuzz.common.JobLogger;
import com.nhn.socialbuzz.me2day.collector.Me2dayDataCollector;
import com.nhn.socialbuzz.me2day.model.TvProgram;
import com.nhn.socialbuzz.me2day.model.TvProgram.SearchQuery;
import com.nhn.socialbuzz.me2day.service.TvProgramManager;
import com.nhn.socialbuzz.me2day.service.TvProgramManagerImpl;
import com.nhn.socialbuzz.twitter.model.Tweet;
import com.nhn.socialbuzz.twitter.service.TweetManager;
import com.nhn.socialbuzz.twitter.service.TweetManagerImpl;

public class TwitterDataCollector {	
	// logger
	private static JobLogger logger = JobLogger.getLogger(Me2dayDataCollector.class, "twitter-collect.log");
	
	private TvProgramManager tvProgramManager;
	private TweetManager tweetManager;
	private Twitter twitter;
	
	/**
	 * Constructor
	 */
	public TwitterDataCollector() {		
		tvProgramManager = new TvProgramManagerImpl();
		tweetManager = new TweetManagerImpl();
		twitter = new TwitterFactory().getInstance();
	}

	public List<twitter4j.Tweet> searchTweets(String programId, String strQuery, String createStartDate, String createEndDate, int maxPage) {
		
		List<twitter4j.Tweet> allList = new ArrayList<twitter4j.Tweet>();		
		
		try {
        	List<twitter4j.Tweet> tweets = new ArrayList<twitter4j.Tweet>();
         	
        	Query query = new Query(strQuery);
        	query.rpp(100);
        	query.setSince(createStartDate);
        	
        	System.out.println("------------------------------------------------");
        	System.out.println("program id: " + programId);
        	System.out.println("query = " + query.getQuery() + " since: " + createStartDate + " page: " + maxPage);
        	
        	logger.info("------------------------------------------------");
        	logger.info("program id: " + programId);
        	logger.info("query = " + query.getQuery() + " since: " + createStartDate + " page: " + maxPage);
         	
        	for (int page = 1; page <= maxPage; page++) {
	        	query.page(page);		        	
	            QueryResult result = twitter.search(query);
	            
	            System.out.println("query result size[page:" + page + "] = " + result.getTweets().size());
	            logger.info("query result size[page:" + page + "] = " + result.getTweets().size());
	            tweets.addAll(result.getTweets());
        	}

        	int addCount = addTweets(tweets, programId);
             
            System.out.println("@added tweet count for query = " + addCount);  
            logger.info("@added tweet count for query = " + addCount);
            
		} catch (TwitterException te) {
			System.out.println(te.getMessage());
			logger.error(te.getMessage(), te);
		}		
		
		return allList;
	}
	
	public int addTweets(List<twitter4j.Tweet> tweets, String programId) {	
		int count = 0;
        for (twitter4j.Tweet tweet : tweets) {
        	try {
        		String text = tweet.getText();
        		String source = tweet.getFromUser();
        		String replyUser = tweet.getToUser();
        		String target = null;
        		String tweetType = "";
        		
        		Tweet ntweet = new Tweet(); 
        		ntweet.setProgramId(programId);
        		ntweet.setTweetId(String.valueOf(tweet.getId()));
        		ntweet.setUser(source);
        		ntweet.setUserNo(String.valueOf(tweet.getFromUserId()));
        		ntweet.setTweetText(text);
            	ntweet.setCreateDate(tweet.getCreatedAt());
            	ntweet.setLocation(tweet.getLocation());
            	ntweet.setProfileImage(tweet.getProfileImageUrl());
 
           		// insert tweet
        		List<Tweet> existTweets = tweetManager.getTweets(ntweet);
           		if (existTweets.size() == 0) {
           			tweetManager.addTweet(ntweet); 
           			count++;           			
           		}
           		else if (existTweets.size() > 0) {
           			//tweetManager.setTweet(ntweet);
           		}
        	} catch (Exception e) {
        		System.out.println(e.getMessage());
        		e.printStackTrace();
        		logger.error(e.getMessage(), e);
        	}
        }
        
        return count;
	}	
	
	public void collectTweets(TvProgram program, String createStartdDate, String createEndDate) {
		
		try {
			
			String programId = program.getProgramId();
			
			System.out.println("================================================");
			System.out.println("\ntitle == " + program.getTitle());
			System.out.println("search queries == " + program.getTwitterSearchKeywords());
			
			logger.info("================================================");
			logger.info("\ntitle == " + program.getTitle());
			logger.info("search queries == " + program.getTwitterSearchKeywords());
			
			List<SearchQuery> searchQueries = program.extractTwitterSearchKeywords();
			
			System.out.println("search query size == " + searchQueries.size());
			logger.info("search query size == " + searchQueries.size());
			
			for (SearchQuery searchQuery : searchQueries) {	
				String keyword = searchQuery.getKeyword();
				int maxResultPage = searchQuery.getMaxResultPage();
				this.searchTweets(programId, keyword, createStartdDate, createEndDate, maxResultPage);					
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public static void main(String[] args) {
		try {
			TwitterDataCollector collector = new TwitterDataCollector();
			
			TvProgramManager programManager = new TvProgramManagerImpl();
			TvProgram param = new TvProgram();
			param.setStatus("open");
			//param.setNation("US");
			//param.setProgramId("cbs_bigbrother");
			List<TvProgram> programs = programManager.getPrograms(param);
			
			String createStartDate = CommonUtil.convertDateToString("yyyy-MM-dd", CommonUtil.addDay(new Date(), -1));
			
			for (int i = 0; i <programs.size(); i++) {
				//collector.collectTweets(programs.get(i), createStartDate, "");
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
