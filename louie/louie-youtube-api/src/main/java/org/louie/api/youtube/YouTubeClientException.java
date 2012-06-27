package org.louie.api.youtube;

/**
 * This class is a YouTube client exception.
 * 
 * @author Younggue Bae
 */
@SuppressWarnings("serial")
public class YouTubeClientException extends Exception {

	public YouTubeClientException() {
		
	}
	
	public YouTubeClientException(String message) {
		super(message);
	}
	
	public YouTubeClientException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public YouTubeClientException(Throwable cause) {
		super(cause);
	}
}
