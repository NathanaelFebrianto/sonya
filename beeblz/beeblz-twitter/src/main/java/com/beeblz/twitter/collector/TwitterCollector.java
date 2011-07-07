package com.beeblz.twitter.collector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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
	
	public void collectTargetUsers(String targetUsersFile) {

		List<HashMap<String,Object>> users = new ArrayList<HashMap<String,Object>>();
		try {
			users = this.loadTargetUsers(targetUsersFile);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return;
		}		
		
		Twitter twitter = new TwitterFactory().getInstance();
		
		for (int i = 0; i < users.size(); i++) {
			try {
				HashMap<String,Object> userMap = (HashMap<String,Object>) users.get(i);
				String targetUser = (String) userMap.get("target_user");
				twitter4j.User tuser = twitter.showUser(targetUser);
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
					user.setIsTarget(true);
					
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
	
	public void collectTweets(String targetUsersFile) {
		
		List<HashMap<String,Object>> users = new ArrayList<HashMap<String,Object>>();
		try {
			users = this.loadTargetUsers(targetUsersFile);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return;
		}
		
		String[] targetUsers = new String[users.size()];
		for (int i = 0; i < users.size(); i++) {
			HashMap<String,Object> userMap = (HashMap<String,Object>) users.get(i);
			targetUsers[i] = (String) userMap.get("target_user");
		}
		
		Twitter twitter = new TwitterFactory().getInstance();
		
		this.writeTweetColumns();	// write tweet columns into log file
		
		String sinceDate = TwitterUtil.convertDateToString("yyyy-MM-dd", TwitterUtil.addDay(new Date(), -1));
		
		for (int i = 0; i < users.size(); i++) {
			try {
				HashMap<String,Object> userMap = (HashMap<String,Object>) users.get(i);
				String targetUser = (String) userMap.get("target_user");
				int pageQuery1 = (Integer) userMap.get("page_query_from");
				int pageQuery2 = (Integer) userMap.get("page_query_mention");
				
				String qStr1 = "from:" + targetUser;	//tweets by target user				
	        	String qStr2 = "@" + targetUser;	//mention target user
				
	        	List<twitter4j.Tweet> tweets1 = new ArrayList<twitter4j.Tweet>();
	        	List<twitter4j.Tweet> tweets2 = new ArrayList<twitter4j.Tweet>();
	        	
	        	// query 1
	        	Query query1 = new Query(qStr1);
	        	query1.rpp(100);
	        	query1.setSince(sinceDate);
	        	//query1.setSince("2011-06-17");
	        	//query1.setUntil("2011-06-17");
	        	
	        	System.out.println("------------------------------------------------");
	        	logger.info("------------------------------------------------");
	        	System.out.println("query1 = " + query1.getQuery() + " since: " + sinceDate + " page: " + pageQuery1);
	        	logger.info("query1 = " + query1.getQuery() + " since: " + sinceDate + " page: " + pageQuery1);
	        	
	        	for (int page = 1; page <= pageQuery1; page++) {
		        	query1.page(page);		        	
		            QueryResult result1 = twitter.search(query1);
		            
		            System.out.println("query1 result size[page:" + page + "] = " + result1.getTweets().size());
		            logger.info("query1 result size[page:" + page + "] = " + result1.getTweets().size());
		            tweets1.addAll(result1.getTweets());
	        	}
	            
	            // query 2
	        	Query query2 = new Query(qStr2);
	        	query2.rpp(100);
	        	query2.setSince(sinceDate);
	        	//query1.setSince("2011-06-17");
	        	//query1.setUntil("2011-06-17");
	        	
	        	System.out.println("query2 = " + query2.getQuery() + " since: " + sinceDate + " page: " + pageQuery2);
	        	logger.info("query2 = " + query2.getQuery() + " since: " + sinceDate + " page: " + pageQuery2);
	        	
	        	for (int page = 1; page <= pageQuery2; page++) {
		        	query2.page(page);		        	
		            QueryResult result2 = twitter.search(query2);
		            
		            System.out.println("query2 result size[page:" + page + "] = " + result2.getTweets().size());
		            logger.info("query2 result size[page:" + page + "] = " + result2.getTweets().size());
		            tweets2.addAll(result2.getTweets());
	        	}
  
	            int addedNum1 = addTweets(tweets1, null, targetUsers);
	            int addedNum2 = addTweets(tweets2, targetUser, targetUsers);
	            
	            System.out.println("------------------------------------------------");
	            logger.info("------------------------------------------------");
	            System.out.println("target user: " + targetUser);
	            logger.info("target user: " + targetUser);
	            System.out.println("@added tweet count for query1 = " + addedNum1);
	        	logger.info("@added tweet count for query1 = " + addedNum1);
	        	
	        	System.out.println("@added tweet count for query2 = " + addedNum2);
	        	logger.info("@added tweet count for query2 = " + addedNum2);
	            
			} catch (TwitterException te) {
				System.out.println(te.getMessage());
			}
		}
	}
	
	public int addTweets(List<twitter4j.Tweet> tweets, String targetUser, String[] targetUsers) {	
		int count = 0;
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
           			count++;
           			
               		// insert relationship
               		if (tweetType.equalsIgnoreCase("REPLY") || tweetType.equalsIgnoreCase("RETWEET")) {
               			Relationship relation = new Relationship();
               			relation.setId1(target);
               			relation.setId2(source);
               			relation.setUserNo1(tweet.getToUserId());
               			relation.setUserNo2(tweet.getFromUserId());
               			
               			if (tweetType.equalsIgnoreCase("REPLY"))
               				relation.setRepliedCountByUser2(1);
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
        
        return count;
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
	
	private List<HashMap<String,Object>> loadTargetUsers(String targetUsersFile) throws Exception {
		if (targetUsersFile == null || targetUsersFile.equals(""))
			throw new Exception("Can't load target users because file path is not defined in Config.properties!");
		
		File file = new File(targetUsersFile);
		if (!file.exists()) 
			throw new Exception("The target_users.dat file doesn't exit, check the file!");
		
		List<HashMap<String,Object>> targetUsers = new ArrayList<HashMap<String,Object>>();
		
		FileReader fileReader = new FileReader(file);		 
		BufferedReader reader = new BufferedReader(fileReader);

		StringTokenizer st = null;		 
		String line = null;
		
		int row = 0;
		while((line = reader.readLine()) != null) {
			HashMap<String,Object> map = new HashMap<String,Object>();
			
			if (row == 0) {
				System.out.println(line);
				logger.info(line);
			} else {
				if (line != null && !line.equals("")) {
					System.out.println(line);
					logger.info(line);
					
					st = new StringTokenizer(line, ",");
					int column = 0;
					while(st.hasMoreTokens()){		 
						String data = st.nextToken();
						if (column == 0)
							map.put("target_user", data);
						else if (column == 1)
							map.put("page_query_from", new Integer(data));
						else if (column == 2)
							map.put("page_query_mention", new Integer(data));
						
						column++;
					}
					targetUsers.add(map);
				}			
			}
			row++;
		}

		return targetUsers;
	}
	
	private void showFriendship(String sourceScreenName, String targetScreenName) {
		Twitter twitter = new TwitterFactory().getInstance();
		try {
			twitter4j.Relationship relation = twitter.showFriendship(sourceScreenName, targetScreenName);
			boolean isSourceBlockingTarget =  relation.isSourceBlockingTarget();
			boolean isSourceFollowedByTarget = relation.isSourceFollowedByTarget();
			
			System.out.println("[" + sourceScreenName + " - " + targetScreenName + "] source <-- target: " 
					+ isSourceFollowedByTarget + ", source blocks target: " + isSourceBlockingTarget);
		} catch (TwitterException te) {
			te.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		TwitterCollector collector = new TwitterCollector();
		//String targetUsersFile = Config.getProperty("targetUsersFile");
		//collector.collectTweets(targetUsersFile);
		//collector.collectTargetUsers(targetUsersFile);
		
		collector.showFriendship("BarackObama", "ozlemerdemm");
		
		
    }	
}
