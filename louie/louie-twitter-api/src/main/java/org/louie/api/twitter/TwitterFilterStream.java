package org.louie.api.twitter;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

import org.louie.common.util.DateUtils;
import org.louie.common.util.FileUtils;

import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
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
	 * @param file
	 * @param track
	 * @throws TwitterApiException
	 */
	public static void writeTrackFilterStream(String file, String[] track) throws TwitterApiException {
		final String filename = file;
		FileUtils.mkdirs(filename);
		
		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();

		StatusListener listener = new StatusListener() {
			public void onStatus(Status status) {
				String json = DataObjectFactory.getRawJSON(status);
				System.out.println(json);

				try {
					String dateHour = DateUtils.convertDateToString("yyyyMMddHH00", new Date());
					String fileWithSurfix = filename + "_" + dateHour;
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
							new FileOutputStream(fileWithSurfix, true), "UTF-8"));
					
					writer.write(json);
					writer.newLine();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
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
		
		FilterQuery query = new FilterQuery();
		query.setIncludeEntities(true);
		query.track(track);
		twitterStream.filter(query);
	}
	
	/**
	 * Writes follow filtered stream data into file with json format.
	 * 
	 * @param file
	 * @param follow
	 * @throws TwitterApiException
	 */
	public static void writeFollowFilterStream(String file, long[] follow) throws TwitterApiException {
		final String filename = file;
		FileUtils.mkdirs(filename);
		
		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();

		StatusListener listener = new StatusListener() {
			public void onStatus(Status status) {
				String json = DataObjectFactory.getRawJSON(status);
				System.out.println(json);

				try {
					String dateHour = DateUtils.convertDateToString("yyyyMMddHH00", new Date());
					String fileWithSurfix = filename + "_" + dateHour;
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
							new FileOutputStream(fileWithSurfix, true), "UTF-8"));
					
					writer.write(json);
					writer.newLine();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
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
		
		FilterQuery query = new FilterQuery();
		query.setIncludeEntities(true);
		query.follow(follow);
		twitterStream.filter(query);
	}
	
	public static void main(String[] args) throws Exception {
		String[] filter = { "kpop", "k-pop" };
		
		TwitterFilterStream.writeTrackFilterStream("./data/test", filter);
	}

}
