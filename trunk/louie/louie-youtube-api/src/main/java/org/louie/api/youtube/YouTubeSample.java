package org.louie.api.youtube;

import java.io.IOException;

/**
 * This class is a YouTube API test sample.
 * 
 * @author Younggue Bae
 */
public class YouTubeSample {

	private static void run() throws IOException {
		YouTubeClient client = new YouTubeClient();
		showVideos(client);
		showVideo(client);
		showVideoComments(client);
		showUser(client);
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
		// build URL for the video feed for the specified search parameters
		YouTubeUrl url = YouTubeUrl.forVideosFeed();
		
		System.out.println("url == " + url.toString());
		
		//url.author = "BIGBANG";
		url.set("q", "kpop");
		// execute GData request for the feed
		VideoFeed feed = client.executeGetVideosFeed(url);
		View.displayItems(feed);
		return feed;
	}
	
	private static VideoEntry showVideo(YouTubeClient client) throws IOException {
		View.header("Get Video");
		// build URL for the video feed for the specified search parameters
		String videoId = "8S5o0q5i9dU";
		YouTubeUrl url = YouTubeUrl.forVideoFeed(videoId);
		
		System.out.println("url == " + url.toString());
		
		// execute GData request for the entry
		VideoEntry entry = client.executeGetVideoFeed(url);
		View.displayEntry(entry);
		return entry;
	}
	
	private static CommentFeed showVideoComments(YouTubeClient client) throws IOException {
		View.header("Get Comments for video");
		// build URL for retrieving the comments for a video.
		String videoId = "8S5o0q5i9dU";
		YouTubeUrl url = YouTubeUrl.forVideoCommentsFeed(videoId);
		
		System.out.println("url == " + url.toString());
		
		// execute GData request for the feed
		CommentFeed feed = client.executeGetVideoCommentsFeed(url);
		View.displayItems(feed);
		
		return feed;
	}
	
	private static UserEntity showUser(YouTubeClient client) throws IOException {
		View.header("Get user profile");
		// build URL for retrieving the user profile.
		String userId = "babypallacita";
		YouTubeUrl url = YouTubeUrl.forUserFeed(userId);
		
		System.out.println("url == " + url.toString());
		
		// execute GData request for the entry
		UserEntity entry = client.executeGetUserFeed(url);
		View.displayEntry(entry);
		
		return entry;
	}
}
