package org.louie.api.twitter;

/**
 * This class is a Twitter api exception.
 * 
 * @author Younggue Bae
 */
@SuppressWarnings("serial")
public class TwitterApiException extends Exception {

	public TwitterApiException() {
		
	}
	
	public TwitterApiException(String message) {
		super(message);
	}
	
	public TwitterApiException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public TwitterApiException(Throwable cause) {
		super(cause);
	}
}
