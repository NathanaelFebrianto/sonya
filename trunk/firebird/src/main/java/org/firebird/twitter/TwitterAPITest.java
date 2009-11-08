package org.firebird.twitter;

import java.util.List;

import twitter4j.IDs;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class TwitterAPITest {

	public TwitterAPITest() {

	}

	/**
	 * @param args
	 */
	public static final void main(String[] args) {
		Twitter twitter = new Twitter("louiezzang@hotmail.com", "bae7214");

		try {
			List<User> followers = twitter.getFollowersStatuses();
			List<User> friends = twitter.getFriendsStatuses();

			/*
			for (int i = 0; i < followers.size(); i++) {
				User follower = (User)followers.get(i);
				System.out.println("follower == " + follower);
			}
			*/
			for (User follower: followers) {
				System.out.println("follower == " + follower);
				//System.out.println("name == " + follower.getName());
				IDs fos = twitter.getFollowersIDs(follower.getId());
				IDs frs = twitter.getFriendsIDs(follower.getId());
				int[] foss = fos.getIDs();
				for (int j = 0; j < foss.length; j++) {
					System.out.println("foss == " + foss[j]);
				}
				int[] frss = fos.getIDs();
				for (int j = 0; j < frss.length; j++) {
					System.out.println("frss == " + frss[j]);
				}
			}
			
		} catch (TwitterException te) {
			te.printStackTrace();
		}
	}
}
