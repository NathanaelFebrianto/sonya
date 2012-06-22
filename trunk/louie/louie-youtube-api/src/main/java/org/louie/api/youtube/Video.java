package org.louie.api.youtube;

import java.util.ArrayList;
import java.util.List;

import com.google.api.client.util.Key;

/**
 * This class is video.
 * 
 * @author Younggue Bae
 */
public class Video extends Item {

	@Key
	String description;

	@Key
	List<String> tags = new ArrayList<String>();

	@Key
	Player player;
	
	@Key
	int viewCount;
	
	@Key
	String category;

	@Override
	public String toString() {
		return "Video [description=" + description + ", tags=" + tags
				+ ", player=" + player + ", viewCount=" + viewCount
				+ ", category=" + category + ", title=" + title + ", updated="
				+ updated + "]";
	}

}
