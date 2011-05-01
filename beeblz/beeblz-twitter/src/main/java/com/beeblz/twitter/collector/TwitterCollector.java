package com.beeblz.twitter.collector;

import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.beeblz.twitter.io.model.User;
import com.beeblz.twitter.io.service.UserManager;
import com.beeblz.twitter.io.service.UserManagerImpl;

public class TwitterCollector {
	
	private UserManager userManager;
	
	public TwitterCollector() {
		userManager = new UserManagerImpl();
	}
	
	public void collectUsers(String[] targetUsers) {
		
		Twitter twitter = new TwitterFactory().getInstance();
		
		for (int i = 0; i < targetUsers.length; i++) {
			try {
				twitter4j.User tuser = twitter.showUser(targetUsers[i]);
				if (tuser.getStatus() != null) {
					User user = new User();
					user.setId(tuser.getId());
					user.setUser(tuser.getScreenName());
					user.setName(tuser.getName());
					user.setLocation(tuser.getLocation());
					user.setProfileImageUrl(tuser.getProfileImageURL().toString());
					user.setFollowersCount(tuser.getFollowersCount());
					user.setFriendsCount(tuser.getFriendsCount());
					user.setTweetsCount(tuser.getStatusesCount());
					user.setIsTarget(true);
					
					userManager.addUser(user);
					
					System.out.println("insert user: " + user.getUser());
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
	        	//String qStr = "from:" + targetUsers[i];
	        	//String qStr = "@" + targetUsers[i];
				
	        	//String qStr = "@" + targetUsers[i] + " :)";	//with a positive attitude
	        	String qStr =  "@" + targetUsers[i] + "  :(";	//with a negative attitude
				
	        	Query query = new Query(qStr);
	        	query.page(1);
	        	query.rpp(100);
	        	//query.setSince("2011-05-01");
	        	//query.setUntil("");
	        	
	        	System.out.println("==========================================");
	        	System.out.println("query = " + query.getQuery());
	        	
	            QueryResult result = twitter.search(query);
	            System.out.println("result size = " + result.getTweets().size());

	            List<twitter4j.Tweet> tweets = result.getTweets();
	            for (twitter4j.Tweet tweet : tweets) {
	            	String toUser = tweet.getToUser();
	            	if (toUser != null) 
	            		System.out.println(
	            				"id = " + tweet.getId() 
	            				+ " | createdAt = " + tweet.getCreatedAt() 
 	            				+ " | @From:" + tweet.getFromUser() + " -> @To:" + toUser + " - " + tweet.getText());
	            	else
	            		System.out.println(
	            				"id = " + tweet.getId() 
	            				+ " | createdAt = " + tweet.getCreatedAt() 
 	            				+ " | @From:" + tweet.getFromUser() + " - " + tweet.getText());
	            	// LIWC로 Sentiment 체크
	            	// TweetText 모듈이용해서 MentionedUsers, RetweetedUser, ReplyUser, URLs 뽑아냄
	            	
	            	// Tweet테이블에 있는지 체크해서 없으면 Insert.
	            	
	        		// 1. Relationship 테이블에 있는지 체크해서 없으면 Insert.
	        		// 2. Insert전에 user1, user2의 following관계를 체크함.
	        		// 1. User테이블에 없으면 Insert.(업데이트는 안해도 되겠지? 있으면 업데이트 해줘? 해주면 좋지.)
	            	
	            	// Cron Job 생성.(매 30분 또는 1시간마다)
	            }
			} catch (TwitterException te) {
				System.out.println(te.getMessage());
			}
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
		//collector.collectUsers(targetUsers);
		collector.collectTweets(targetUsers);
    }
    
    
}
