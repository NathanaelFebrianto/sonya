package org.louie.api.youtube;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.json.JsonCParser;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponseException;
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
	
	private static YouTubeClient instance = null;

	private final JsonFactory jsonFactory = new JacksonFactory();

	private final HttpTransport transport = new NetHttpTransport();

	private final HttpRequestFactory requestFactory;
	
	private final static int MAX_ITEM_SIZE = 1000;
	
	//private final static int MAX_REQUEST_COUNT = 100;
	
	private final static long WAITING_SECONDS = 1000 * 20;//1000 * 60 * 5
	
	private int requestCount = 0;
	
	private Date lastCallErrorTime = new Date();

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
	 * Gets the singleton instance.
	 * @return
	 */
	public static final YouTubeClient getInstance() {
		if (instance == null) {
			instance = new YouTubeClient();
		}
		
		return instance;
	}
 	
	private void addRequestCount() {
		requestCount++;
		
		System.out.println("***************************requestCount == " + requestCount);
		
		/*
		if (totalRequestCount == TOTAL_MAX_REQUEST_COUNT) {
			long miliseconds = 1000 * 60 * 5;
			sleep(miliseconds);
			requestCount = 0;
			totalRequestCount = 0;
		}
		else if (requestCount == MAX_REQUEST_COUNT) {
			long miliseconds = 3000;
			sleep(miliseconds);
			requestCount = 0;
		}
		*/
	}
	
	private void sleep(long miliseconds) {
		try {
			int seconds = (int) miliseconds / 1000;
			System.err.println("................. waiting for " + seconds + " seconds .................");
			Thread.sleep(miliseconds);
			requestCount = 0;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets all video feed by the specified url parameters.
	 * 
	 * @param url
	 * @return
	 * @throws YouTubeClientException
	 */
	public List<VideoFeed> executeGetAllVideoFeed(YouTubeUrl url) throws YouTubeClientException {

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
			/*
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			*/
		}
		
		return feedList;

	}
	
	/**
	 * Gets all video comment feed by the specified url parameters.
	 * 
	 * @param url
	 * @param maxPage
	 * @return
	 * @throws YouTubeClientException
	 */
	public List<CommentFeed> executeGetAllCommentFeed(YouTubeUrl url, int maxPage) throws YouTubeClientException {
		List<CommentFeed> feedList = new ArrayList<CommentFeed>();
		
		int totalItemSize = 0;
		int maxResults = 50;
		if (url.get("max-results") != null) {
			maxResults = (Integer) url.get("max-results");
		}
		else {
			url.set("max-results", maxResults);
		}

		CommentFeed feed = executeGetVideoCommentFeed(url);
		totalItemSize = feed.getTotalItemsSize();
		
		if (totalItemSize <= maxResults) {
			maxPage = 0;
		}
		
		if (feed.getItems() != null) {
			feedList.add(feed);
		}
		
		System.out.println("url[" + 0 + "/" + maxPage + "page, total size = " + totalItemSize + "] == " + url.toString());
		//System.out.println("response[" + 0 + "/" + maxPage + "page, total size = " + totalItemSize + "] == " + feed.getItems());
		System.out.println("response[" + 0 + "/" + maxPage + "page, total size = " + totalItemSize + "]");
		
		for (int page = 1 ; page <= maxPage; page++) {
			url.set("start-index", maxResults * page + 1);
			CommentFeed nextFeed = executeGetVideoCommentFeed(url);

			System.out.println("url[" + page + "/" + maxPage + "page, total size = " + totalItemSize + "] == " + url.toString());
			//System.out.println("response[" + page + "/" + maxPage + "page, total size = " + totalItemSize + "] == " + nextFeed.getItems());
			System.out.println("response[" + page + "/" + maxPage + "page, total size = " + totalItemSize + "]");
		
			if (nextFeed.getItems() != null)
				feedList.add(nextFeed);
			
			// waiting for 1 second
			/*
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			*/
		}
		
		return feedList;
	}
	
	/**
	 * Gets a feed of videos.
	 * 
	 * @param url
	 * @return
	 * @throws YouTubeClientException
	 */
	public VideoFeed executeGetVideoFeed(YouTubeUrl url) throws YouTubeClientException {
		addRequestCount();
		return executeGetFeed(url, VideoFeed.class);
	}
	
	/**
	 * Gets a video entry.
	 * 
	 * @param url
	 * @return
	 * @throws YouTubeClientException
	 */
	public VideoEntry executeGetVideoEntry(YouTubeUrl url) throws YouTubeClientException {
		addRequestCount();
		return executeGetEntry(url, VideoEntry.class);
	}
	
	/**
	 * Gets a feed of video comments.
	 * 
	 * @param url
	 * @return
	 * @throws YouTubeClientException
	 */
	public CommentFeed executeGetVideoCommentFeed(YouTubeUrl url) throws YouTubeClientException {
		addRequestCount();
		return executeGetFeed(url, CommentFeed.class);
	}
	
	/**
	 * Gets a user entry.
	 * 
	 * @param url
	 * @return
	 * @throws YouTubeClientException
	 */
	public UserEntry executeGetUserEntry(YouTubeUrl url) throws YouTubeClientException {
		addRequestCount();
		return executeGetEntry(url, UserEntry.class);
	}
	
	/**
	 * Gets a string of response.
	 * 
	 * @param url
	 * @return
	 * @throws YouTubeClientException
	 */
	public String executeGetAsString(YouTubeUrl url) throws YouTubeClientException {
		try {
			addRequestCount();
			HttpRequest request = requestFactory.buildGetRequest(url);
			
			try {
				return request.execute().parseAsString();
			} catch (HttpResponseException e) {
				//e.printStackTrace();
				if (e.getMessage().indexOf("too_many_recent_calls") >= 0) {
					e.printStackTrace();
					
					Date current = new Date();
					long term = current.getTime() - lastCallErrorTime.getTime();
					System.out.println(term + " miliseconds have passed since the last error occurred: " + lastCallErrorTime + " -> " + current);
					lastCallErrorTime = current;
					long waitingTime = WAITING_SECONDS;
					if (term < 22000) {	// within 22 seconds
						waitingTime = waitingTime + term;
					}
					sleep(waitingTime);

					//return request.execute().parseAsString();
					return executeGetAsString(url);
				}
				else {
					System.err.println("error for url = " + url.toString());
					e.printStackTrace();
					throw new YouTubeClientException(e);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new YouTubeClientException(e);
		}
	}

	/**
	 * Executes feed to get.
	 * 
	 * @param url
	 * @param feedClass
	 * @return
	 * @throws YouTubeClientException
	 */
	private <F extends Feed<? extends Item>> F executeGetFeed(YouTubeUrl url, Class<F> feedClass) throws YouTubeClientException {
		try {
			HttpRequest request = requestFactory.buildGetRequest(url);
			
			try {
				return request.execute().parseAs(feedClass);
			} catch (HttpResponseException e) {
				//e.printStackTrace();
				if (e.getMessage().indexOf("too_many_recent_calls") >= 0) {
					e.printStackTrace();
					
					Date current = new Date();
					long term = current.getTime() - lastCallErrorTime.getTime();
					System.out.println(term + " miliseconds have passed since the last error occurred: " + lastCallErrorTime + " -> " + current);
					lastCallErrorTime = current;
					long waitingTime = WAITING_SECONDS;
					if (term < 22000) {	// within 22 seconds
						waitingTime = waitingTime + term;
					}
					sleep(waitingTime);
					
					//return request.execute().parseAs(feedClass);
					return executeGetFeed(url, feedClass);
				}
				else {
					System.err.println("error for url = " + url.toString());
					e.printStackTrace();
					throw new YouTubeClientException(e);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new YouTubeClientException(e);
		}
	}
	
	/**
	 * Executes feed to get entry.
	 * 
	 * @param url
	 * @param feedClass
	 * @return
	 * @throws YouTubeClientException
	 */
	private <F extends Entry> F executeGetEntry(YouTubeUrl url, Class<F> feedClass) throws YouTubeClientException {
		try {
			HttpRequest request = requestFactory.buildGetRequest(url);
			
			try {
				return request.execute().parseAs(feedClass);
			} catch (HttpResponseException e) {
				//e.printStackTrace();
				if (e.getMessage().indexOf("too_many_recent_calls") >= 0) {
					e.printStackTrace();
					
					Date current = new Date();
					long term = current.getTime() - lastCallErrorTime.getTime();
					System.out.println(term + " miliseconds have passed since the last error occurred: " + lastCallErrorTime + " -> " + current);
					lastCallErrorTime = current;
					long waitingTime = WAITING_SECONDS;
					if (term < 22000) {	// within 22 seconds
						waitingTime = waitingTime + term;
					}
					sleep(waitingTime);
					
					//return request.execute().parseAs(feedClass);
					return executeGetEntry(url, feedClass);
				}
				else if (e.getMessage().indexOf("<internalReason>Forbidden</internalReason>") >= 0) {
					System.err.println("error for url = " + url.toString());
					throw new YouTubeClientException("403 Forbidden");
				}
				else {
					System.err.println("error for url = " + url.toString());
					e.printStackTrace();
					throw new YouTubeClientException(e);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new YouTubeClientException(e);
		}
	}
}
