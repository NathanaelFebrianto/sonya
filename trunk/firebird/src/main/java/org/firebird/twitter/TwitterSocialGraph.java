package org.firebird.twitter;

import twitter4j.IDs;
import twitter4j.Twitter;

public class TwitterSocialGraph {
	
	int myId = 50900875;
	Twitter twitter = null;
	
	public TwitterSocialGraph() throws Exception {
		TwitterOAuthSupport twitterSupport = new TwitterOAuthSupport();
		twitter = twitterSupport.access(myId);	
		
		IDs friendsIDs = twitter.getFriendsIDs(myId);
		IDs followersIDs = twitter.getFollowersIDs(myId);		
	}

}
