package org.firebird.twitter;

import java.util.ResourceBundle;
import java.util.StringTokenizer;

import twitter4j.Twitter;
import twitter4j.http.AccessToken;

public class TwitterOAuthSupport {

	private ResourceBundle oauthInfo = null;
	
	public TwitterOAuthSupport() {
		oauthInfo = ResourceBundle.getBundle("oauth"); 
	}

	public Twitter access(int userId) throws Exception {
		Twitter twitter = new Twitter();
		
		String[] userInfo = getUserOAuthInfo(userId);
		
		twitter.setOAuthConsumer(userInfo[0], userInfo[1]);
		System.out.println("Set OAuth consummer for user [" + userId + "]");
		AccessToken accessToken = loadAccessToken(userId);
		twitter.setOAuthAccessToken(accessToken);
		System.out.println("Set OAuth access token for user [" + userId + "]");

		return twitter;
	}

	/*
	 * Loads access token from a persistent store.
	 */
	private AccessToken loadAccessToken(int userId) {		
		String[] userInfo = getUserOAuthInfo(userId);
		String token = userInfo[2];
		String tokenSecret = userInfo[3];
		return new AccessToken(token, tokenSecret);
	}
	
	private String[] getUserOAuthInfo(int userId) {
		StringTokenizer st = new StringTokenizer(oauthInfo.getString(String.valueOf(userId)), ",");
		String[] userInfo = new String[st.countTokens()];
		int i = 0;
		while (st.hasMoreTokens()) {
			userInfo[i] = st.nextToken();
			i++;
		}		
		return userInfo; 		
	}
	
	public static final void main(String[] args) throws Exception {
		TwitterOAuthSupport twitterSupport = new TwitterOAuthSupport();
		int userId = 50900875;
		Twitter twitter = twitterSupport.access(userId);
		twitter.updateStatus("Test by Louiezzang from Firebird!!!");
	}
}
