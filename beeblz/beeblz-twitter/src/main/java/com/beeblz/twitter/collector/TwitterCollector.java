package com.beeblz.twitter.collector;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.beeblz.twitter.common.TwitterUtil;
import com.beeblz.twitter.io.model.Relationship;
import com.beeblz.twitter.io.model.Tweet;
import com.beeblz.twitter.io.model.TweetMentionedUser;
import com.beeblz.twitter.io.model.User;
import com.beeblz.twitter.io.service.RelationshipManager;
import com.beeblz.twitter.io.service.RelationshipManagerImpl;
import com.beeblz.twitter.io.service.TweetManager;
import com.beeblz.twitter.io.service.TweetManagerImpl;
import com.beeblz.twitter.io.service.TweetMentionedUserManager;
import com.beeblz.twitter.io.service.TweetMentionedUserManagerImpl;
import com.beeblz.twitter.io.service.UserManager;
import com.beeblz.twitter.io.service.UserManagerImpl;
import com.twitter.Extractor;

public class TwitterCollector {
	
	private UserManager userManager;
	private TweetManager tweetManager;
	private TweetMentionedUserManager tweetMentionedUserManager;
	private RelationshipManager relationshipManager;
	
	public TwitterCollector() {
		userManager = new UserManagerImpl();
		tweetManager = new TweetManagerImpl();
		tweetMentionedUserManager = new TweetMentionedUserManagerImpl();
		relationshipManager = new RelationshipManagerImpl();
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
				String qStr1 = "from:" + targetUsers[i];		//tweets by target user				
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
	            
	            addTweets(tweets1, null, targetUsers, null);
	            addTweets(tweets2, targetUsers[i], targetUsers, "positive");
	            addTweets(tweets3, targetUsers[i], targetUsers, "negative");
	            
			} catch (TwitterException te) {
				System.out.println(te.getMessage());
			}
		}
	}
	
	public void addTweets(List<twitter4j.Tweet> tweets, String targetUser, String[] targetUsers, String attitude) {
		Extractor exractor = new Extractor();
		
        for (twitter4j.Tweet tweet : tweets) {
        	try {
        		String text = tweet.getText();
        		String source = tweet.getFromUser();
        		String target = null;
        		String tweetType = "";
        		
        		Tweet ntweet = new Tweet();        		
        		ntweet.setId(tweet.getId());
        		ntweet.setUser(source);
        		ntweet.setUserNo(tweet.getFromUserId());
        		ntweet.setText(text);
        		ntweet.setUrl(TwitterUtil.extractUrl(text));
            	ntweet.setCreateDate(tweet.getCreatedAt());
            	
            	ArrayList<String> excludeMentionedUsers = new ArrayList<String>();
            	
            	// priority1: replyUser
            	String replyUser = tweet.getToUser();
        		if (replyUser != null) {
                	ntweet.setReplyUser(replyUser);
                	ntweet.setReplyUserNo(tweet.getToUserId());
                	excludeMentionedUsers.add(replyUser);
                	
        			target = replyUser;
        			tweetType = "REPLY";
        		}           	
            	
            	// priority2: retweetedUser
        		String retweetedUser = null;
        		if (replyUser == null) {
            		retweetedUser = TwitterUtil.extractRetweetedUser(text);
            		if (retweetedUser != null) {
                    	ntweet.setRetweetedUser(retweetedUser);
                    	excludeMentionedUsers.add(retweetedUser);

                    	target = retweetedUser;
                    	tweetType = "RETWEET";
            		}            			
        		}     		
 
            	// priority4: mentionedUser
            	String mentionedUser = TwitterUtil.extractMentionedUser(text, excludeMentionedUsers);
            	if (mentionedUser != null) {
            		ntweet.setMentionedUser(mentionedUser);
            	}
        		
            	// priority3: if the mentioned user is a target user
            	if (targetUser != null && !targetUser.equalsIgnoreCase(replyUser) && !targetUser.equalsIgnoreCase(retweetedUser)) {            		
            		ntweet.setMentionedUser(targetUser);
            		
            		target = targetUser;
            		tweetType = "MENTION";
            	} 
            	
            	if (mentionedUser != null && targetUser == null && replyUser == null && retweetedUser == null) {
            		target = mentionedUser;
            		tweetType = "MENTION";
            	}
        		
            	// priority5: mentionedUsers for the others
        		String mentionedUsers = TwitterUtil.extractMentionedUsers(text, excludeMentionedUsers);        		
        		if (mentionedUsers != null) {
        			ntweet.setMentionedUsers(mentionedUsers);
        		}
        			
        		ntweet.setTargetUser(target);
        		ntweet.setTweetType(tweetType);
            	
            	if (attitude != null && attitude.equalsIgnoreCase("positive"))
            		ntweet.setPositiveAttitude(true);
            	else if (attitude != null && attitude.equalsIgnoreCase("negative"))
            		ntweet.setNegativeAttitude(true);
            	
           		System.out.println(
    				"id = " + tweet.getId() 
    				+ " | createdAt = " + tweet.getCreatedAt() 
     				+ " | @From:" + source + " -> @To:" + target + " - " + text);
           		
           		List<Tweet> existTweets = tweetManager.getTweets(ntweet);
           		if (existTweets.size() == 0) {
           			tweetManager.addTweet(ntweet);
           		}
           		else if (existTweets.size() > 0 && attitude != null) {
           			//tweetManager.setTweet(ntweet);
           		}
           		
           		// insert relationship
           		if (tweetType.equalsIgnoreCase("REPLY") || tweetType.equalsIgnoreCase("RETWEET")) {
           			Relationship relation = new Relationship();
           			relation.setId1(target);
           			relation.setId2(source);
           			relation.setUserNo1(tweet.getToUserId());
           			relation.setUserNo2(tweet.getFromUserId());
           			
           			if (tweetType.equalsIgnoreCase("REPLY"))
           				relation.setReplyedCountByUser2(1);
           			else if (tweetType.equalsIgnoreCase("RETWEET"))
           				relation.setRetweetedCountByUser2(1);
           			
           			if (ntweet.isPositiveAttitude())
           				relation.setPositiveAttitudeCountByUser2(1);
           			else if (ntweet.isNegativeAttitude())
           				relation.setNegativeAttitudeCountByUser2(1);
           			
           			this.addRelationship(relation);
           		}
           		
           		// insert tweet mentioned user list
           		List<String> mentionedUserList = TwitterUtil.extractMentionedUserList(text, excludeMentionedUsers);
           		this.addTweetMentionedUsers(ntweet, mentionedUserList, targetUsers);
           		
        	} catch (Exception e) {
        		System.out.println(e.getMessage());
        		e.printStackTrace();
        	}
        }		
	}
	
	private void addTweetMentionedUsers(Tweet tweet, List<String> mentionedUserList, String[] targetUsers) {
		for (int i = 0 ; i < mentionedUserList.size(); i++) {
			String mentionedUser = (String) mentionedUserList.get(i);
			
			TweetMentionedUser tweetMentionedUser = new TweetMentionedUser();
			tweetMentionedUser.setId(tweet.getId());
			tweetMentionedUser.setUser(tweet.getUser());
			tweetMentionedUser.setUserNo(tweet.getUserNo());
			tweetMentionedUser.setMentionedUser(mentionedUser);
			
   			Relationship relation = new Relationship();
   			relation.setId1(mentionedUser);
   			relation.setId2(tweet.getUser());
   			relation.setMentionedCountByUser2(1);
   			
   			if (tweet.isPositiveAttitude())
   				relation.setPositiveAttitudeCountByUser2(1);
   			else if (tweet.isNegativeAttitude())
   				relation.setNegativeAttitudeCountByUser2(1);
			
			// if source user is a target user, adds all mentioned users.
			if (this.isTargetUser(tweet.getUser(), targetUsers)) {
       			this.addRelationship(relation);
			}
			else {
				if (this.isTargetUser(mentionedUser, targetUsers)) {
					tweetMentionedUser.setIsTarget(true);
	       			this.addRelationship(relation);					
				}
			}
			List<TweetMentionedUser> exist = tweetMentionedUserManager.getTweetMentionedUsers(tweetMentionedUser);
			if (exist == null || exist.size() == 0)
				tweetMentionedUserManager.addTweetMentionedUser(tweetMentionedUser);
		} 
	}
	
	private boolean isTargetUser(String user, String[] targetUsers) {
		for (int i = 0 ; i < targetUsers.length; i++) {
			if (user.equalsIgnoreCase(targetUsers[i]))
				return true;
		}
		return false;
	}
	
	private void addRelationship(Relationship relationship) {
		List<Relationship> existRelations = relationshipManager.getRelationships(relationship);
		
		if (existRelations.size() == 0) {
			relationshipManager.addRelationship(relationship);
   		}
   		else if (existRelations.size() > 0) {
   			relationshipManager.setRelationship(relationship);
   		}   		
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
				"aplusk",		//
				"DalaiLama",
				"TechCrunch",
				"mashable",
				"cnnbrk",		//
				"BBCBreaking"	//
			};
		
		TwitterCollector collector = new TwitterCollector();
		//collector.collectUsers(targetUsers, true);
		collector.collectTweets(targetUsers);
    }	
}
