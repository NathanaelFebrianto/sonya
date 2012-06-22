package org.louie.api.youtube;

import java.io.IOException;

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

	public VideosFeed executeGetVideosFeed(YouTubeUrl url) throws IOException {
		return executeGetListFeed(url, VideosFeed.class);
	}
	
	public VideoFeed executeGetVideoFeed(YouTubeUrl url) throws IOException {
		return executeGetEntryFeed(url, VideoFeed.class);
	}
	
	public CommentsFeed executeGetVideoCommentsFeed(YouTubeUrl url) throws IOException {
		return executeGetListFeed(url, CommentsFeed.class);
	}
	
	public UserFeed executeGetUserFeed(YouTubeUrl url) throws IOException {
		return executeGetEntryFeed(url, UserFeed.class);
	}
	
	public String executeGetAsString(YouTubeUrl url) throws IOException {
		HttpRequest request = requestFactory.buildGetRequest(url);
		String response = request.execute().parseAsString();

		return response;
	}

	private <F extends ListFeed<? extends Item>> F executeGetListFeed(YouTubeUrl url,
			Class<F> feedClass) throws IOException {
		HttpRequest request = requestFactory.buildGetRequest(url);
		
		System.out.println("response == " + request.execute().parseAsString());
		
		return request.execute().parseAs(feedClass);
	}
	
	private <F extends EntryFeed> F executeGetEntryFeed(YouTubeUrl url,
			Class<F> feedClass) throws IOException {
		HttpRequest request = requestFactory.buildGetRequest(url);
		
		System.out.println("response == " + request.execute().parseAsString());
		
		return request.execute().parseAs(feedClass);
	}
}
