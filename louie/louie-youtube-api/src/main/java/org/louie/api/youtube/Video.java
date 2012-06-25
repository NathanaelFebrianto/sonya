package org.louie.api.youtube;

import java.util.ArrayList;
import java.util.List;

import com.google.api.client.util.Key;

/**
 * This class is a video.
 * 
 * @author Younggue Bae
 */
public class Video extends Item {

	@Key
	String id;
	
	@Key
	String uploaded;
	
	@Key
	String updated;
	
	@Key
	String uploader;
	
	@Key
	String category;
	
	@Key
	String title;
	
	@Key
	String description;

	@Key
	List<String> tags = new ArrayList<String>();

	@Key
	Thumbnail thumbnail;
	
	@Key
	Player player;
	
	@Key
	double rating;
	
	@Key
	int likeCount;
	
	@Key
	int ratingCount;
	
	@Key
	int viewCount;
	
	@Key
	int favoriteCount;
	
	@Key
	int commentCount;


	public static class Player {
		// "default" is a Java keyword, so need to specify the JSON key manually
		@Key("default")
		String defaultUrl;

		@Override
		public String toString() {
			return "Player [defaultUrl=" + defaultUrl + "]";
		}
	}
	
	public static class Thumbnail {
		@Key("hqDefault")
		String defaultUrl;

		@Override
		public String toString() {
			return "Thumbnail [defaultUrl=" + defaultUrl + "]";
		}
	}
	
	@Override
	public String toString() {
		return "Video [id=" + id + ", uploaded=" + uploaded + ", updated="
				+ updated + ", uploader=" + uploader + ", category=" + category
				+ ", title=" + title + ", description=" + description
				+ ", tags=" + tags + ", thumbnail=" + thumbnail + ", player="
				+ player + ", rating=" + rating + ", likeCount=" + likeCount
				+ ", ratingCount=" + ratingCount + ", viewCount=" + viewCount
				+ ", favoriteCount=" + favoriteCount + ", commentCount="
				+ commentCount + "]";
	}

}
