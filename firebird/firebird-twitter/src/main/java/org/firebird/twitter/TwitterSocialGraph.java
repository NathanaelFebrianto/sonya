package org.firebird.twitter;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.User;

public class TwitterSocialGraph {
	
	int myId = 50900875;
	Twitter twitter = null;
	
	public TwitterSocialGraph() throws Exception {
		TwitterOAuthSupport twitterSupport = new TwitterOAuthSupport();
		twitter = twitterSupport.access(myId);	
		
		IDs friendsIDs = twitter.getFriendsIDs(myId);
		IDs followersIDs = twitter.getFollowersIDs(myId);		
	}
	
	public void createDirctedGraph(String screenName) throws Exception {
		User user = twitter.showUser(screenName);
		IDs friendsIDs = twitter.getFriendsIDs(screenName);
		IDs followersIDs = twitter.getFollowersIDs(screenName);
		
		int[] fids = friendsIDs.getIDs();		
		System.out.println("number of friends == " + fids.length);
		
		for (int i = 0; i < fids.length; i++) {
			User fuser = twitter.showUser(String.valueOf(fids[i]));
			System.out.println("friend[" + i + "] == " + fuser.getScreenName() + ", " + fuser.getName());
		}
	}

}
