package org.louie.api.twitter;

import java.util.ArrayList;
import java.util.List;

import twitter4j.FilterQuery;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.json.DataObjectFactory;

public final class TwitterUserStream extends StatusAdapter {

	public static void main(String[] args) throws TwitterException, Exception {
		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();

		StatusListener listener = new StatusListener() {
			public void onStatus(Status status) {
				String json = DataObjectFactory.getRawJSON(status);
				System.out.println(json);
			}

			public void onDeletionNotice(
					StatusDeletionNotice statusDeletionNotice) {
				System.out.println("Got a status deletion notice id:"
						+ statusDeletionNotice.getStatusId());
			}

			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				System.out.println("Got track limitation notice:"
						+ numberOfLimitedStatuses);
			}

			public void onScrubGeo(long userId, long upToStatusId) {
				System.out.println("Got scrub_geo event userId:" + userId
						+ " upToStatusId:" + upToStatusId);
			}

			public void onException(Exception ex) {
				ex.printStackTrace();
			}
		};
		twitterStream.addListener(listener);
		
		long[] idList = getUserIDList();
		FilterQuery query = new FilterQuery();
		query.setIncludeEntities(true);
		query.follow(idList);
		twitterStream.filter(query);
	}
	
	private static long[] getUserIDList() throws TwitterException, Exception {
		List<Long> idList = new ArrayList<Long>();
		
		Twitter twitter = new TwitterFactory().getInstance();
		
		/////////////////////
		List<String[]> screenNames = null;
		
		System.out.println("list size == " + screenNames.size());
		
		for (String[] screenNameArray : screenNames) {
			System.out.println("array size of screenNames == " + screenNameArray.length);
			
			ResponseList<User> users = twitter.lookupUsers(screenNameArray);
			for (User user : users) {
				long id = user.getId();
				idList.add(id);
			}			
		}
		
		long[] lid = new long[idList.size()];
		for (int i = 0; i < idList.size(); i++) {
			lid[i] = (long) idList.get(i);
		}
		
		return lid;

	}

}
