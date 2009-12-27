package org.firebird.twitter;

import java.util.HashMap;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class TwitterDataCollector {
	
	int myId = 50900875;
	Twitter twitter = null;
	boolean isFollowing = false;
	boolean isFollower = false;
	int limitLevel = 1;
	int limitPeople = 999999999;
	int limitDegree = 1;
	int numFriends = 0;
	int numFollowers = 0;
	
	HashMap vertices = new HashMap();
	HashMap edges = new HashMap();
	
	public TwitterDataCollector() throws Exception {
		TwitterOAuthSupport twitterSupport = new TwitterOAuthSupport();
		twitter = twitterSupport.access(myId);
	}
	
	public void setCollectFollowingRelationship(boolean isFollowing) {
		this.isFollowing = isFollowing;
	}
	
	public void setCollectFollowerRelationship(boolean isFollower) {
		this.isFollower = isFollower;
	}

	public void setLimitLevel(int limitLevel) {
		this.limitLevel = limitLevel;
	}
	
	public void setLimitPeople(int limitPeople) {
		this.limitPeople = limitPeople;
	}
	
	public void setLimitDegree(int limitDegree) {
		this.limitDegree = limitDegree;
	}
		
	public void collectSocialNetwork(String screenName) {		
		System.out.println(">>>>>>>>>>>>>> option");		
		System.out.println("collect following relationship == " + isFollowing);
		System.out.println("collect follower relationship == " + isFollowing);
		System.out.println("level limit == " + limitLevel);
		System.out.println("people limit == " + limitPeople);
		
		User user = null;

		try {
			user = twitter.showUser(screenName);

			System.out.println("rate limit == " + user.getRateLimitLimit());
			System.out.println("rate limit remaining  == " + user.getRateLimitRemaining());			
			System.out.println(">>>>>>>>>>>>>> start");
			
			if (isFollowing)
				this.collectFriendsOfUser(user, 1);
			
			if (isFollower)
				this.collectFollowersOfUser(user, 1);			
		} catch (TwitterException te) {
			te.printStackTrace();
		}
		
		System.out.println(">>>>>>>>>>>>>> result");
		System.out.println("rate limit remaining  == " + user.getRateLimitRemaining());
		System.out.println("friends == " + numFriends);	
		System.out.println("followers == " + numFollowers);
		System.out.println("vertices == " + vertices.size());
		System.out.println("edges == " + edges.size());
	}
	
	private void collectFriendsOfUser(User user, int level) throws TwitterException {		
		if (level <= limitLevel && numFriends <= limitPeople) {
			// add vertex		
			addVertex(user);			
			
			List friends = twitter.getFriendsStatuses(String.valueOf(user.getId()));

			level++;			
			for (int i = 0; i < friends.size(); i++) {	
				if (i == limitDegree)	break;
				
				numFriends++;
				User friend = (User)friends.get(i);								
				
				// add vertex
				addVertex(friend);
				
				// add edge
				addEdge(user, friend);
				
				// recursive call
				if (isFollowing)
					this.collectFriendsOfUser(friend, level);
				if (isFollower)
					this.collectFollowersOfUser(friend, level);				
			}
		}
	}

	private void collectFollowersOfUser(User user, int level) throws TwitterException {		
		if (level <= limitLevel && numFollowers <= limitPeople) {
			// add vertex		
			addVertex(user);
			
			List followers = twitter.getFollowersStatuses(String.valueOf(user.getId()));
		
			level++;
			for (int i = 0; i < followers.size(); i++) {
				if (i == limitDegree)	break;
				
				numFollowers++;
				User follower = (User)followers.get(i);				
				
				// add vertex
				addVertex(follower);
				
				// add edge
				addEdge(follower, user);				
				
				// recursive call
				if (isFollowing)
					this.collectFriendsOfUser(follower, level);
				if (isFollower)
					this.collectFollowersOfUser(follower, level);	
			}
		}		
	}
	
	public void addVertex(User user) {
		String key = String.valueOf(user.getId());
		
		if (!vertices.containsKey(key)) {
			vertices.put(key, "");			
			//System.out.println("status text == " + user.getStatusText() + " " + user.getStatusCreatedAt());
		}			
	}
	
	public void addEdge(User user1, User user2) {
		String key = String.valueOf(user1.getId()) + "->" + String.valueOf(user2.getId());
		
		if (!edges.containsKey(key)) {
			edges.put(key, "following");			
			System.out.println(user1.getScreenName() + " -> " + user2.getScreenName());
		}		
	}
}
