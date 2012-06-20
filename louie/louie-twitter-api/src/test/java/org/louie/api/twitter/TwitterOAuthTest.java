package org.louie.api.twitter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TwitterOAuthTest extends TestCase {

	public TwitterOAuthTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(TwitterOAuthTest.class);
	}
	
	public void testOAuthTest() throws Exception {
		/*
		Twitter twitter = new TwitterFactory().getInstance();
		
		twitter.setOAuthConsumer("goBKSaqPvfEOZ1om3FOAw", "I9yvEXfHKYUqWltxU7QjkTi0PJi3FItIkWfKGBouRgo");
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
					accessToken = twitter.getOAuthAccessToken();
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
		
		//Status status = twitter.updateStatus(args[0]);
		//System.out.println("Successfully updated the status to [" + status.getText() + "].");
		System.exit(0);
		*/
	}
	
	/*
	private static void storeAccessToken(long userId, AccessToken accessToken) {
		// store accessToken.getToken()
		// store accessToken.getTokenSecret()
	}
	*/
}

