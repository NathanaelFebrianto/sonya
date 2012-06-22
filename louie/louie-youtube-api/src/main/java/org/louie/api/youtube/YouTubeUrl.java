package org.louie.api.youtube;

import com.google.api.client.googleapis.GoogleUrl;
import com.google.api.client.util.Key;

/**
 * This class defines a YouTube url.
 * 
 * @author Younggue Bae
 * @see https://developers.google.com/youtube/2.0/developers_guide_protocol_audience
 * @see https://developers.google.com/youtube/2.0/developers_guide_protocol_api_query_parameters#qsp
 */
public class YouTubeUrl extends GoogleUrl {

	/** 
	 * Whether to pretty print HTTP requests and responses.
	 * The prettyprint parameter lets you request an XML response formatted with indentations and line breaks. 
	 * Set the parameter's value to true to have the response formatted as such. 
	 */
	private static final boolean PRETTY_PRINT = true;

	static final String ROOT_URL = "https://gdata.youtube.com/feeds/api";

	@Key
	String author;
	
	@Key("v")
	Integer version = 2;

	/**
	 * The max-results parameter specifies the maximum number of results that should be included in the result set. 
	 * This parameter works in conjunction with the start-index parameter to determine which results to return. 
	 * For example, to request the second set of 10 results – i.e. results 11-20 – set the max-results parameter 
	 * to 10 and the start-index parameter to 11. The default value of this parameter is 25, and the maximum value is 50. 
	 * However, for displaying lists of videos, we recommend that you set the max-results parameter to 10.
	 */
	//@Key("max-results")
	//Integer maxResults = 50;	//2;

	YouTubeUrl(String encodedUrl) {
		super(encodedUrl);
		
		/**
		 *  The alt parameter specifies the format of the feed to be returned. 
		 *  Valid values for this parameter are atom, rss, json, json-in-script, and jsonc. 
		 */
		setAlt("jsonc");
		setPrettyPrint(PRETTY_PRINT);
	}

	private static YouTubeUrl root() {
		return new YouTubeUrl(ROOT_URL);
	}

	/**
	 * Returns url for video search feeds list videos that match parameter values, 
	 * such as a query term or video duration.
	 * https://developers.google.com/youtube/2.0/developers_guide_protocol_api_query_parameters#qsp
	 * 
	 * @return YouTubeUrl
	 */
	static YouTubeUrl forVideosFeed() {
		YouTubeUrl result = root();
		result.getPathParts().add("videos");
		result.setAlt("jsonc");
		result.set("max-results", 50);
		//result.version = 2;
		
		return result;
	}
	
	/**
	 * Returns url for retrieving a video.
	 * 
	 * @param videoId
	 * @return
	 */
	static YouTubeUrl forVideoFeed(String videoId) {
		YouTubeUrl result = root();
		result.getPathParts().add("videos");
		result.getPathParts().add(videoId);
		result.setAlt("atom");
		//result.setAlt("json");
		//result.version = 1;
		
		return result;
	}
	
	/**
	 * Returns url for retrieving comments for a video.
	 * 
	 * @param videoId
	 * @return
	 */
	static YouTubeUrl forVideoCommentsFeed(String videoId) {
		YouTubeUrl result = root();
		result.getPathParts().add("videos");
		result.getPathParts().add(videoId);
		result.getPathParts().add("comments");
		result.setAlt("atom");
		result.set("max-results", 50);
		//result.setAlt("json");
		//result.version = 1;
		
		return result;
	}
	
	/**
	 * Returns url for retrieving a user's profile.
	 * 
	 * @param userId
	 * @return
	 */
	static YouTubeUrl forUserFeed(String userId) {
		YouTubeUrl result = root();
		result.getPathParts().add("users");
		result.getPathParts().add(userId);
		result.setAlt("atom");
		//result.setAlt("json");
		//result.version = 1;
		
		return result;
	}
}