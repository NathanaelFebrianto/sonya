package org.firebird.collector.twitter;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;

public class TwitterOAuthTest {

	public TwitterOAuthTest() {
	}
	
	public static final void main(String[] args) throws Exception {
		Twitter twitter = new Twitter();
		//Twitter twitter = new Twitter("louiezzang@hotamil.com", "xxx");
		
		//twitter.setOAuthConsumer("[consumer key]", "[consumer secret]");
		RequestToken requestToken = twitter.getOAuthRequestToken();
		AccessToken accessToken = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (null == accessToken) {
			System.out.println("Open the following URL and grant access to your account:");
			System.out.println(requestToken.getAuthorizationURL());
			System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
			String pin = br.readLine();
			try {
				if (pin.length() > 0) {
					accessToken = twitter.getOAuthAccessToken(requestToken, pin);
				} else {
					accessToken = requestToken.getAccessToken();
				}
			} catch (TwitterException te) {
				if (401 == te.getStatusCode()) {
					System.out.println("Unable to get the access token.");					
				} else {
					te.printStackTrace();
				}
			}
		}
		// persist to the accessToken for future reference.
		//storeAccessToken(twitter.verifyCredentials().getId(), accessToken);
		
		System.out.println("id == " + twitter.verifyCredentials().getId());
		System.out.println("access token == " + accessToken.getToken());
		System.out.println("access token secret == " + accessToken.getTokenSecret());
		
		//Status status = twitter.updateStatus("Test by louiezzang");
		//System.out.println("Successfully updated the status to [" + status.getText() + "].");
		//System.exit(0);
	}
	
	/*
	private void storeAccessToken(int userId, AccessToken at) {
		// store at.getToken()
		// store at.getTokenSecret()
	}
	*/
}
