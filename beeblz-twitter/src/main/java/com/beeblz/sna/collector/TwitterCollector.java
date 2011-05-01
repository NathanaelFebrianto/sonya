package com.beeblz.sna.collector;

import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterCollector {
	
    public static void main(String[] args) {

        Twitter twitter = new TwitterFactory().getInstance();
        try {
        	String qStr = "sna";
        	Query query = new Query(qStr);
        	query.page(2);
        	query.rpp(5);
        	System.out.println("query = " + query.getQuery());
        	
            QueryResult result = twitter.search(query);
            System.out.println("result size = " + result.getTweets().size());
            int p = result.getResultsPerPage();
            
            List<Tweet> tweets = result.getTweets();
            for (Tweet tweet : tweets) {
            	String toUser = tweet.getToUser();
            	if (toUser != null) 
            		System.out.println("@From:" + tweet.getFromUser() + " -> @To:" + toUser + " - " + tweet.getText());
            	else
            		System.out.println("@From:" + tweet.getFromUser() + " - " + tweet.getText());
            }
            
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
    }
    
    
}
