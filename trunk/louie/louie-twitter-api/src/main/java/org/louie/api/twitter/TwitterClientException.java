package org.louie.api.twitter;

/**
 * This class is a Twitter api exception.
 * 
 * @author Younggue Bae
 */
@SuppressWarnings("serial")
public class TwitterClientException extends Exception {

	public TwitterClientException() {
		
	}
	
	public TwitterClientException(String message) {
		super(message);
	}
	
	public TwitterClientException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public TwitterClientException(Throwable cause) {
		super(cause);
	}
}
