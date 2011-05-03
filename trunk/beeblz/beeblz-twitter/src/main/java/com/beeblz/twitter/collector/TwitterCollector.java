package com.beeblz.twitter.collector;

import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.beeblz.twitter.io.model.Tweet;
import com.beeblz.twitter.io.model.User;
import com.beeblz.twitter.io.service.TweetManager;
import com.beeblz.twitter.io.service.TweetManagerImpl;
import com.beeblz.twitter.io.service.UserManager;
import com.beeblz.twitter.io.service.UserManagerImpl;
import com.twitter.Extractor;

public class TwitterCollector {
	
	private UserManager userManager;
	private TweetManager tweetManager;
	
	public TwitterCollector() {
		userManager = new UserManagerImpl();
		tweetManager = new TweetManagerImpl();
	}
	
	public void collectUsers(String[] targetUsers, boolean isTarget) {
		
		Twitter twitter = new TwitterFactory().getInstance();
		
		for (int i = 0; i < targetUsers.length; i++) {
			try {
				twitter4j.User tuser = twitter.showUser(targetUsers[i]);
				if (tuser.getStatus() != null) {
					User user = new User();
					user.setId(tuser.getScreenName());
					user.setUserNo(tuser.getId());
					user.setName(tuser.getName());
					user.setLocation(tuser.getLocation());
					user.setProfileImageUrl(tuser.getProfileImageURL().toString());
					user.setFollowersCount(tuser.getFollowersCount());
					user.setFriendsCount(tuser.getFriendsCount());
					user.setTweetsCount(tuser.getStatusesCount());
					user.setIsTarget(isTarget);
					
					userManager.addUser(user);
					
					System.out.println("insert user: " + user.getId());
				} else {
					// the user is protected
				}
			} catch (TwitterException te) {
				System.out.println(te.getMessage());
			}
		}
	}
	
	public void collectTweets(String[] targetUsers) {
		Twitter twitter = new TwitterFactory().getInstance();
		
		for (int i = 0; i < targetUsers.length; i++) {
			try {
				String qStr1 = "from:" + targetUsers[i];	//tweets by target user				
	        	String qStr2 = "@" + targetUsers[i] + " :)";	//tweets with a positive attitude that mention or reply target user
	        	String qStr3 =  "@" + targetUsers[i] + " :(";	//tweets with a negative attitude that mention or reply target user
				
	        	// query 1
	        	Query query1 = new Query(qStr1);
	        	query1.page(1);
	        	query1.rpp(100);
	        	//query1.setSince("2011-05-01");      	
	        	System.out.println("query1 = " + query1.getQuery());	        	
	            QueryResult result1 = twitter.search(query1);
	            System.out.println("query1 result size = " + result1.getTweets().size());
	            
	            // query 2
	        	Query query2 = new Query(qStr2);
	        	query2.page(1);
	        	query2.rpp(100);
	        	System.out.println("query2 = " + query2.getQuery());	        	
	            QueryResult result2 = twitter.search(query2);
	            System.out.println("query2 result size = " + result2.getTweets().size());
	            
	            // query 3
	        	Query query3 = new Query(qStr3);
	        	query3.page(1);
	        	query3.rpp(100);
	        	System.out.println("query3 = " + query3.getQuery());	        	
	            QueryResult result3 = twitter.search(query3);
	            System.out.println("query3 result size = " + result3.getTweets().size());	            

	            List<twitter4j.Tweet> tweets1 = result1.getTweets();
	            List<twitter4j.Tweet> tweets2 = result2.getTweets();
	            List<twitter4j.Tweet> tweets3 = result3.getTweets();
	            
	            addTweets(tweets1, null, null);
	            addTweets(tweets2, targetUsers[i], "positive");
	            addTweets(tweets3, targetUsers[i], "negative");
	            
			} catch (TwitterException te) {
				System.out.println(te.getMessage());
			}
		}
	}
	
	public void addTweets(List<twitter4j.Tweet> tweets, String mentionedUser, String attitude) {
		Extractor exractor = new Extractor();
		
        for (twitter4j.Tweet tweet : tweets) {
        	try {
        		Tweet ntweet = new Tweet();
        		
        		ntweet.setId(tweet.getId());
        		ntweet.setUser(tweet.getFromUser());
        		ntweet.setUserNo(tweet.getFromUserId());
        		ntweet.setText(tweet.getText());
            	ntweet.setReplyUser(tweet.getToUser());
            	ntweet.setReplyUserNo(tweet.getToUserId());
            	ntweet.setCreateDate(tweet.getCreatedAt());
            	
            	if (mentionedUser ==  null) {
            		List<String> mentionedUsers = exractor.extractMentionedScreennames(tweet.getText());            		
            		if (mentionedUsers != null && mentionedUsers.size() > 0) {
            			String parsedMentionedUser = (String) mentionedUsers.get(0);
            			if (!parsedMentionedUser.equals(tweet.getToUser()))
            				ntweet.setMentionedUser(parsedMentionedUser);
            		}
             	}
            	
            	if (mentionedUser != null && !mentionedUser.equals(tweet.getToUser())) {
            		ntweet.setMentionedUser(mentionedUser);
            	}            		
            	
            	List<String> urls = exractor.extractURLs(tweet.getText());
            	if (urls != null && urls.size() > 0)
            		ntweet.setUrl((String)urls.get(0));

            	if (attitude != null && attitude.equalsIgnoreCase("positive"))
            		ntweet.setPositiveAttitude(true);
            	else if (attitude != null && attitude.equalsIgnoreCase("negative"))
            		ntweet.setNegativeAttitude(true);
            	
           		System.out.println(
    				"id = " + tweet.getId() 
    				+ " | createdAt = " + tweet.getCreatedAt() 
     				+ " | @From:" + tweet.getFromUser() + " -> @To:" + tweet.getToUser() + " - " + tweet.getText());
           		
           		List<Tweet> exists = tweetManager.getTweets(ntweet);
           		if (exists.size() == 0) {
           			tweetManager.addTweet(ntweet);
           		}
           		else if (exists.size() > 0 && attitude != null) {
           			//tweetManager.setTweet(ntweet);
           		}

            	// LIWC로 Sentiment 체크
            	// TweetText 모듈이용해서 MentionedUsers, RetweetedUser, ReplyUser, URLs 뽑아냄
            	
            	// Tweet테이블에 있는지 체크해서 없으면 Insert.
            	
        		// 1. Relationship 테이블에 있는지 체크해서 없으면 Insert.
        		// 2. Insert전에 user1, user2의 following관계를 체크함.
        		// 1. User테이블에 없으면 Insert.(업데이트는 안해도 되겠지? 있으면 업데이트 해줘? 해주면 좋지.)
           		
        	} catch (Exception e) {
        		System.out.println(e.getMessage());
        		e.printStackTrace();
        	}
        	
        	// Cron Job 생성.(매 30분 또는 1시간마다)
        }		
	}
	
	public void addRelationships() {
		
	}
	
	public static void main(String[] args) {

		String[] targetUsers = {
				"BarackObama",
				"realDonaldTrump",
				"BillGates",
				"Oprah",
				"kingsthings",
				"ladygaga",
				"britneyspears",
				"DalaiLama",
				"TechCrunch",
				"mashable"
			};
		
		TwitterCollector collector = new TwitterCollector();
		//collector.collectUsers(targetUsers, true);
		collector.collectTweets(targetUsers);
    }
	
}
