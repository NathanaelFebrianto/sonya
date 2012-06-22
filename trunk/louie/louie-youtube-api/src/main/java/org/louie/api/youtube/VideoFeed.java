package org.louie.api.youtube;

import java.util.List;

import com.google.api.client.util.Key;


/**
 * This class is videos feed.
 * 
 * @author Younggue Bae
 */
public class VideoFeed extends ListFeed<Video> {

	@Key("items")
	List<Video> items;

	@Key
	int totalItems;

	@Override
	public List<Video> getItems() {
		return items;
	}

	@Override
	public int getTotalItemsSize() {
		return totalItems;
	}
	
}