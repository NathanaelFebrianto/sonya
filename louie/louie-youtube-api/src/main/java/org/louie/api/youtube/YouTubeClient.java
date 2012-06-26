package org.louie.api.youtube;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.json.JsonCParser;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.xml.atom.AtomParser;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.xml.XmlNamespaceDictionary;

/**
 * This class is a YouTube client.
 * http://code.google.com/p/google-api-java-client/wiki/DeveloperGuide
 * http://javadoc.google-api-java-client.googlecode.com/hg/1.10.2-beta/com/google/api/client/googleapis/xml/atom/package-summary.html
 * 
 * @author Younggue Bae
 */
public class YouTubeClient {

	private final JsonFactory jsonFactory = new JacksonFactory();

	private final HttpTransport transport = new NetHttpTransport();

	private final HttpRequestFactory requestFactory;
	
	private final static int MAX_ITEM_SIZE = 1000;

	/**
	 * Constructor.
	 */
	public YouTubeClient() {
		final JsonCParser jsoncParser = new JsonCParser(jsonFactory);
		final AtomParser atomParser = new AtomParser(new XmlNamespaceDictionary());
		
		requestFactory = transport.createRequestFactory(new HttpRequestInitializer() {
			
			@SuppressWarnings("deprecation")
			public void initialize(HttpRequest request) throws IOException {
				// headers
				GoogleHeaders headers = new GoogleHeaders();
				headers.setApplicationName("Google-YouTubeSample/1.0");
				headers.gdataVersion = "2";
				
				request.setHeaders(headers);
				request.addParser(jsoncParser);
				request.addParser(atomParser);
			}
		});
	}
	
	/**
	 * Gets all video feed by the specified url parameters.
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public List<VideoFeed> executeGetAllVideoFeed(YouTubeUrl url) throws IOException {
		List<VideoFeed> feedList = new ArrayList<VideoFeed>();
		
		int totalItemSize = 0;
		int maxResults = 50;
		if (url.get("max-results") != null) {
			maxResults = (Integer) url.get("max-results");
		}
		else {
			url.set("max-results", maxResults);
		}
		
		VideoFeed feed = executeGetVideoFeed(url);
		totalItemSize = feed.getTotalItemsSize();
		
		int maxPage = 0;
		if (totalItemSize <= MAX_ITEM_SIZE) {
			maxPage = totalItemSize / maxResults;
		}
		else {
			maxPage = MAX_ITEM_SIZE / maxResults;
		}
		
		if (feed.getItems() != null) {
			feedList.add(feed);
		}
		
		System.out.println("url[" + 0 + "/" + maxPage + "page, total size = " + totalItemSize + "] == " + url.toString());
		System.out.println("response[" + 0 + "/" + maxPage + "page, total size = " + totalItemSize + "] == " + feed.getItems());
		
		for (int page = 1 ; page <= maxPage; page++) {
			url.set("start-index", maxResults * page + 1);
			VideoFeed nextFeed = executeGetVideoFeed(url);
			
			System.out.println("url[" + page + "/" + maxPage + "page, total size = " + totalItemSize + "] == " + url.toString());
			System.out.println("response[" + page + "/" + maxPage + "page, total size = " + totalItemSize + "] == " + nextFeed.getItems());
			
			if (nextFeed.getItems() != null)
				feedList.add(nextFeed);
			
			// waiting for 1 second
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return feedList;
	}
	
	/**
	 * Gets all video comment feed by the specified url parameters.
	 * 
	 * @param url
	 * @param maxPage
	 * @return
	 * @throws IOException
	 */
	public List<CommentFeed> executeGetAllCommentFeed(YouTubeUrl url, int maxPage) throws IOException {
		List<CommentFeed> feedList = new ArrayList<CommentFeed>();
		
		int totalItemSize = 0;
		int maxResults = 50;
		if (url.get("max-results") != null) {
			maxResults = (Integer) url.get("max-results");
		}
		else {
			url.set("max-results", maxResults);
		}
		
		int maxItemSize = maxResults * maxPage;
		
		CommentFeed feed = executeGetVideoCommentFeed(url);
		totalItemSize = feed.getTotalItemsSize();

		if (totalItemSize <= MAX_ITEM_SIZE && maxItemSize > MAX_ITEM_SIZE) {
			maxPage = totalItemSize / maxResults;
		}
		else if (totalItemSize > MAX_ITEM_SIZE || maxItemSize > MAX_ITEM_SIZE){
			maxPage = MAX_ITEM_SIZE / maxResults;
		}
		
		if (feed.getItems() != null) {
			feedList.add(feed);
		}
		
		System.out.println("url[" + 0 + "/" + maxPage + "page, total size = " + totalItemSize + "] == " + url.toString());
		System.out.println("response[" + 0 + "/" + maxPage + "page, total size = " + totalItemSize + "] == " + feed.getItems());
		
		for (int page = 1 ; page <= maxPage; page++) {
			url.set("start-index", maxResults * page + 1);
			CommentFeed nextFeed = executeGetVideoCommentFeed(url);
			
			System.out.println("url[" + page + "/" + maxPage + "page, total size = " + totalItemSize + "] == " + url.toString());
			System.out.println("response[" + page + "/" + maxPage + "page, total size = " + totalItemSize + "] == " + nextFeed.getItems());
			
			if (nextFeed.getItems() != null)
				feedList.add(nextFeed);
			
			// waiting for 1 second
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return feedList;
	}
	
	/**
	 * Gets a feed of videos.
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public VideoFeed executeGetVideoFeed(YouTubeUrl url) throws IOException {
		return executeGetFeed(url, VideoFeed.class);
	}
	
	/**
	 * Gets a video entry.
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public VideoEntry executeGetVideoEntry(YouTubeUrl url) throws IOException {
		return executeGetEntry(url, VideoEntry.class);
	}
	
	/**
	 * Gets a feed of video comments.
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public CommentFeed executeGetVideoCommentFeed(YouTubeUrl url) throws IOException {
		return executeGetFeed(url, CommentFeed.class);
	}
	
	/**
	 * Gets a user entry.
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public UserEntry executeGetUserEntry(YouTubeUrl url) throws IOException {
		return executeGetEntry(url, UserEntry.class);
	}
	
	/**
	 * Gets a string of response.
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public String executeGetAsString(YouTubeUrl url) throws IOException {
		HttpRequest request = requestFactory.buildGetRequest(url);

		return request.execute().parseAsString();
	}

	/**
	 * Executes feed to get.
	 * 
	 * @param url
	 * @param feedClass
	 * @return
	 * @throws IOException
	 */
	private <F extends Feed<? extends Item>> F executeGetFeed(YouTubeUrl url,
			Class<F> feedClass) throws IOException {
		HttpRequest request = requestFactory.buildGetRequest(url);
		
		return request.execute().parseAs(feedClass);
	}
	
	/**
	 * Executes feed to get entry.
	 * 
	 * @param url
	 * @param feedClass
	 * @return
	 * @throws IOException
	 */
	private <F extends Entry> F executeGetEntry(YouTubeUrl url,
			Class<F> feedClass) throws IOException {
		HttpRequest request = requestFactory.buildGetRequest(url);
		
		//System.out.println("response == " + request.execute().parseAsString());
		
		return request.execute().parseAs(feedClass);
	}
}
