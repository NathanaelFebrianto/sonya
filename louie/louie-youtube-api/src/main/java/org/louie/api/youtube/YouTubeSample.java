package org.louie.api.youtube;

import java.io.IOException;

public class YouTubeSample {

	private static void run() throws IOException {
		YouTubeClient client = new YouTubeClient();
		showVideos(client);
	}

	public static void main(String[] args) {
		try {
			try {
				run();
				return;
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		System.exit(1);
	}

	private static VideoFeed showVideos(YouTubeClient client) throws IOException {
		View.header("Get Videos");
		// build URL for the video feed for "search stories"
		YouTubeUrl url = YouTubeUrl.forVideosFeed();
		//url.author = "searchstories";
		//url.author = "BIGBANG";
		url.set("q", "kpop");
		// execute GData request for the feed
		VideoFeed feed = client.executeGetVideoFeed(url);
		View.display(feed);
		return feed;
	}
}
