package org.louie.api.youtube;

import java.util.List;

import com.google.api.client.util.Key;


/**
 * This class is comments feed.
 * https://developers.google.com/youtube/2.0/developers_guide_protocol_comments#Retrieve_comments
 * 
 * @author Younggue Bae
 */
public class CommentFeed extends Feed<CommentEntry> {
	
	@Key("entry")
	List<CommentEntry> items;

	@Key("openSearch:totalResults")
	int totalItems;
	
	@Key("openSearch:itemsPerPage")
	int itemsPerPage;

	@Override
	public List<CommentEntry> getItems() {
		return items;
	}

	@Override
	public int getTotalItemsSize() {
		return totalItems;
	}
	
}