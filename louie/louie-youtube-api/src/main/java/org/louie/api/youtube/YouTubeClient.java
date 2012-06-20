package org.louie.api.youtube;

import java.io.IOException;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.json.JsonCParser;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;


public class YouTubeClient {

	private final JsonFactory jsonFactory = new JacksonFactory();

	private final HttpTransport transport = new NetHttpTransport();

	private final HttpRequestFactory requestFactory;

	public YouTubeClient() {
		final JsonCParser parser = new JsonCParser(jsonFactory);
		requestFactory = transport.createRequestFactory(new HttpRequestInitializer() {
			
			public void initialize(HttpRequest request) throws IOException {
				// headers
				GoogleHeaders headers = new GoogleHeaders();
				headers.setApplicationName("Google-YouTubeSample/1.0");
				headers.gdataVersion = "2";
				request.setHeaders(headers);
				request.addParser(parser);
			}
		});
	}

	public VideoFeed executeGetVideoFeed(YouTubeUrl url) throws IOException {
		return executeGetFeed(url, VideoFeed.class);
	}

	private <F extends Feed<? extends Item>> F executeGetFeed(YouTubeUrl url,
			Class<F> feedClass) throws IOException {
		HttpRequest request = requestFactory.buildGetRequest(url);
		return request.execute().parseAs(feedClass);
	}
}
