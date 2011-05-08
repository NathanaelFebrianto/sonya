package com.beeblz.twitter.collector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.beeblz.common.Config;
import com.beeblz.common.JobLogger;
import com.beeblz.common.TwitterUtil;
import com.beeblz.liwc.PersonalityRecognizer;
import com.beeblz.liwc.Utils;
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

public class TwitterCollector {
	
	// logger
	private static JobLogger logger = JobLogger.getLogger(TwitterCollector.class, "twitter-collect.log");
	
	private static String AT_SIGNS_CHARS = "@\uFF20";
	
	public static final Pattern AT_SIGNS = Pattern.compile("[" + AT_SIGNS_CHARS
			+ "]");
	
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
		
		this.writeTweetColumns();	// write tweet columns into log file
		
		for (int i = 0; i < targetUsers.length; i++) {
			try {
				String qStr1 = "from:" + targetUsers[i];	//tweets by target user				
	        	String qStr2 = "@" + targetUsers[i];	//mention target user
				
	        	// query 1
	        	Query query1 = new Query(qStr1);
	        	query1.page(1);
	        	query1.rpp(100);
	        	//query1.setSince("2011-05-01");      	
	        	System.out.println("query1 = " + query1.getQuery());
	        	logger.info("query1 = " + query1.getQuery());
	            QueryResult result1 = twitter.search(query1);
	            System.out.println("query1 result size = " + result1.getTweets().size());
	            logger.info("query1 result size = " + result1.getTweets().size());
	            
	            // query 2
	        	Query query2 = new Query(qStr2);
	        	query2.page(1);
	        	query2.rpp(100);
	        	System.out.println("query2 = " + query2.getQuery());
	        	logger.info("query2 = " + query2.getQuery());
	            QueryResult result2 = twitter.search(query2);
	            System.out.println("query2 result size = " + result2.getTweets().size());
	            logger.info("query2 result size = " + result2.getTweets().size());

	            List<twitter4j.Tweet> tweets1 = result1.getTweets();
	            List<twitter4j.Tweet> tweets2 = result2.getTweets();
	            
	            addTweets(tweets1, null, targetUsers);
	            addTweets(tweets2, targetUsers[i], targetUsers);
	            
			} catch (TwitterException te) {
				System.out.println(te.getMessage());
			}
		}
	}
	
	public void addTweets(List<twitter4j.Tweet> tweets, String targetUser, String[] targetUsers) {		
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
        		
        		String attitude = TwitterUtil.extractPositiveOrNegativeAttitude(text);
            	
        		if (attitude != null && attitude.equalsIgnoreCase("positive"))
            		ntweet.setPositiveAttitude(true);
            	else if (attitude != null && attitude.equalsIgnoreCase("negative"))
            		ntweet.setNegativeAttitude(true);
        		
           		// insert tweet
        		List<Tweet> existTweets = tweetManager.getTweets(ntweet);
           		if (existTweets.size() == 0) {
           			// analyze LIWC features
           			Map<String,Double> liwcFeatures = analyzeLIWCFeatures(text);
           			ntweet.setLIWCFeatures(liwcFeatures);
           			
           			// write tweet data into log file
           			this.writeTweetData(ntweet);	
           			tweetManager.addTweet(ntweet); 
           			
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
               			
               			if (ntweet.getPositiveAttitude())
               				relation.setPositiveAttitudeCountByUser2(1);
               			else if (ntweet.getNegativeAttitude())
               				relation.setNegativeAttitudeCountByUser2(1);
               			
               			//set LIWC features
               			relation.setLIWCFeatures(liwcFeatures);
               			
               			this.addRelationship(relation);
               		}
               		
               		// insert tweet mentioned user list
               		List<String> mentionedUserList = TwitterUtil.extractMentionedUserList(text, excludeMentionedUsers);
               		this.addTweetMentionedUsers(ntweet, mentionedUserList, targetUsers, liwcFeatures);
           		}
           		else if (existTweets.size() > 0) {
           			//tweetManager.setTweet(ntweet);
           		}
        	} catch (Exception e) {
        		System.out.println(e.getMessage());
        		e.printStackTrace();
        	}
        }		
	}
	
	private void addTweetMentionedUsers(Tweet tweet, List<String> mentionedUserList, String[] targetUsers, Map<String,Double> liwcFeatures) {
		for (String mentionedUser : mentionedUserList) {
			TweetMentionedUser tweetMentionedUser = new TweetMentionedUser();
			tweetMentionedUser.setId(tweet.getId());
			tweetMentionedUser.setUser(tweet.getUser());
			tweetMentionedUser.setUserNo(tweet.getUserNo());
			tweetMentionedUser.setMentionedUser(mentionedUser);
			
   			Relationship relation = new Relationship();
   			relation.setId1(mentionedUser);
   			relation.setId2(tweet.getUser());
   			relation.setMentionedCountByUser2(1);
   			
   			//set LIWC features
   			relation.setLIWCFeatures(liwcFeatures);
   			
   			if (tweet.getPositiveAttitude())
   				relation.setPositiveAttitudeCountByUser2(1);
   			else if (tweet.getNegativeAttitude())
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
	
	private Map<String,Double> analyzeLIWCFeatures(String text) {
		try {
			File liwcCatFile = new File(Config.getProperty("liwcCatFile"));
			PersonalityRecognizer recognizer = new PersonalityRecognizer(liwcCatFile);

			// get feature counts from the input text
			//Map<String,Double> counts = recognizer.getFeatureCounts(text, true);
			Map<String,Double> counts = recognizer.getJustFeatureCounts(text, true);
			System.out.println("Total features computed: " + counts.size());
			
			System.out.println("Feature counts:");
			Utils.printMap(counts, System.out);
			
			return counts;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void writeTweetColumns() {
   		StringBuffer columns = new StringBuffer()
		.append("id").append("|")
		.append("user").append("|")
		.append("user_no").append("|")
		.append("tweet_text").append("|")
		.append("url").append("|")
		.append("tweet_type").append("|")
		.append("target_user").append("|")
		.append("reply_user").append("|")
		.append("reply_user_no").append("|")
		.append("retweeted_user").append("|")
		.append("mentioned_user").append("|")
		.append("mentioned_users").append("|")
		.append("positive_attitude").append("|")
		.append("negative_attitude").append("|")
		.append("create_date").append("");
   		
   		logger.info("---------------------------------------------------------------------------");
   		logger.info(columns.toString());
   		logger.info("---------------------------------------------------------------------------");
	}
	
	private void writeTweetData(Tweet tweet) {
   		StringBuffer data = new StringBuffer()
		.append(tweet.getId()).append("|")
		.append(tweet.getUser()).append("|")
		.append(tweet.getUserNo()).append("|")
		.append(tweet.getText()).append("|")
		.append(tweet.getUrl()).append("|")
		.append(tweet.getTweetType()).append("|")
		.append(tweet.getTargetUser()).append("|")
		.append(tweet.getReplyUser()).append("|")
		.append(tweet.getReplyUserNo()).append("|")
		.append(tweet.getRetweetedUser()).append("|")
		.append(tweet.getMentionedUser()).append("|")
		.append(tweet.getMentionedUsers()).append("|")
		.append(tweet.getPositiveAttitude()).append("|")
		.append(tweet.getNegativeAttitude()).append("|")
		.append(tweet.getCreateDate()).append("");
   		
   		logger.info(data.toString());
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
