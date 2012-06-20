package org.louie.api.twitter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

import org.louie.common.util.DateUtils;

import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.json.DataObjectFactory;

public final class TwitterFilterStream extends StatusAdapter {

	public static void main(String[] args) throws TwitterException, Exception {
		File dir = new File("./data/twitter/collect/");
		if (!dir.exists()) {
			dir.mkdirs();
		}

		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();

		/*
		AccessToken acessToken = loadAccessToken();
		twitterStream.setOAuthConsumer("*******************", "*******************");
		twitterStream.setOAuthAccessToken(acessToken);
		*/

		StatusListener listener = new StatusListener() {
			public void onStatus(Status status) {
				String json = DataObjectFactory.getRawJSON(status);
				System.out.println(json);

				try {
					String dateHour = DateUtils.convertDateToString("yyyyMMddHH00", new Date());
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
							new FileOutputStream("./data/twitter/collect/twitter_" + dateHour + ".txt", true), "UTF-8"));
					
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
		
		String[] track = { "kpop" };
		FilterQuery query = new FilterQuery();
		query.setIncludeEntities(true);
		query.track(track);
		twitterStream.filter(query);
	}

	/*
	private static AccessToken loadAccessToken() {
		String token = "*******************";
		String tokenSecret = "*******************";
		return new AccessToken(token, tokenSecret);
	}
	*/

}
