package org.louie.api.twitter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.louie.common.util.DateUtil;
import org.louie.common.util.FileUtil;

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

/**
 * This class is a Twitter filter stream.
 * 
 * @author Younggue Bae
 */
public final class TwitterFilterStream extends StatusAdapter {
	
	/**
	 * Writes track filtered stream data into file with json format.
	 * 
	 * @param dir
	 * @param filter
	 * @throws TwitterClientException
	 */
	public static void writeTrackFilterStream(String dir, TwitterFilterQuery filter) throws TwitterClientException {
		try {
			final String strDir = dir;
			FileUtil.mkdirs(strDir);
			
			TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
			
			StatusListener listener = new StatusListener() {
				@Override
				public void onStatus(Status status) {
					String json = DataObjectFactory.getRawJSON(status);
					System.out.println(json);
	
					try {
						Date date = new Date();
						String subDir = DateUtil.convertDateToString("yyyyMMdd", date);
						String surfix = DateUtil.convertDateToString("yyyyMMddHH00", date);
						String filename = strDir + File.separator + subDir + File.separator + "twitter_" + surfix;
						FileUtil.mkdirsFromFullpath(filename);
						
						BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
								new FileOutputStream(filename, true), "UTF-8"));
						
						writer.write(json);
						writer.newLine();
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
	
				@Override
				public void onDeletionNotice(
						StatusDeletionNotice statusDeletionNotice) {
					System.out.println("Got a status deletion notice id:"
							+ statusDeletionNotice.getStatusId());
				}
	
				@Override
				public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
					System.out.println("Got track limitation notice:"
							+ numberOfLimitedStatuses);
				}
	
				@Override
				public void onScrubGeo(long userId, long upToStatusId) {
					System.out.println("Got scrub_geo event userId:" + userId
							+ " upToStatusId:" + upToStatusId);
				}
	
				@Override
				public void onException(Exception ex) {
					ex.printStackTrace();
				}
			};
			twitterStream.addListener(listener);
			
			FilterQuery query = new FilterQuery();
			query.setIncludeEntities(true);
			
			System.out.println("------------------------------------------------");
			System.out.println("@track size == " + filter.getTrack().length);
			System.out.println("@follow size == " + filter.getFollowScreenNames().length);
			System.out.println("@track == " + Arrays.asList(filter.getTrack()));
			System.out.println("@follow == " + Arrays.asList(filter.getFollowScreenNames()));
			System.out.println("------------------------------------------------");
			
			if (filter.getTrack().length > 0)
				query.track(filter.getTrack());
			
			if (filter.getFollowScreenNames().length > 0)
				query.follow(convertFollowQuery(filter.getFollowScreenNames()));
			
			twitterStream.filter(query);
			
		} catch (TwitterException e) {
			e.printStackTrace();
			throw new TwitterClientException(e);
		}
	}
		
	private static long[] convertFollowQuery(String[] screenNames) throws TwitterException {
		List<Long> follow = new ArrayList<Long>();
		
		Twitter twitter = new TwitterFactory().getInstance();
		
		List<String[]> screenNameList = new ArrayList<String[]>();
		List<String> itemArray = new ArrayList<String>();
		for (String screenName : screenNames) {
			itemArray.add(screenName);

			if (itemArray.size() == 100) {
				screenNameList.add(itemArray.toArray(new String[itemArray.size()]));
				itemArray.clear();
			}
		}
		
		if (itemArray.size() > 0) {
			screenNameList.add(itemArray.toArray(new String[itemArray.size()]));
		}
		
		System.out.println("list size == " + screenNameList.size());
		
		for (String[] screenNameArray : screenNameList) {
			System.out.println("array size of screenNames == " + screenNameArray.length);
			
			ResponseList<User> users = twitter.lookupUsers(screenNameArray);
			for (User user : users) {
				long id = user.getId();
				follow.add(id);
			}			
		}
		
		long[] result = new long[follow.size()];
		for (int i = 0; i < follow.size(); i++) {
			result[i] = (long) follow.get(i);
		}
		
		return result;
	}
	
	public static void main(String[] args) throws Exception {
		String[] track = { "kpop" };
		String[] follow = { "mjjeje" };
		TwitterFilterQuery filter = new TwitterFilterQuery();
		filter.setTrack(track);
		filter.setFollowScreenNames(follow);
		
		TwitterFilterStream.writeTrackFilterStream("./data/test", filter);

	}

}
