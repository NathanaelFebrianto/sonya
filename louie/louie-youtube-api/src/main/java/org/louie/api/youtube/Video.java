package org.louie.api.youtube;

import java.util.ArrayList;
import java.util.List;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

/**
 * This class is a video.
 * 
 * @author Younggue Bae
 */
public class Video extends Item {

	@Key
	public String id;
	
	@Key
	public DateTime uploaded;
	
	@Key
	public DateTime updated;
	
	@Key
	public String uploader;
	
	@Key
	public String category;
	
	@Key
	public String title;
	
	@Key
	public String description;

	@Key
	public List<String> tags = new ArrayList<String>();

	@Key
	public Thumbnail thumbnail;
	
	@Key
	public Player player;
	
	@Key
	public double rating;
	
	@Key
	public int likeCount;
	
	@Key
	public int ratingCount;
	
	@Key
	public int viewCount;
	
	@Key
	public int favoriteCount;
	
	@Key
	public int commentCount;


	public static class Player {
		// "default" is a Java keyword, so need to specify the JSON key manually
		@Key("default")
		public String defaultUrl;

		@Override
		public String toString() {
			return "Player [defaultUrl=" + defaultUrl + "]";
		}
	}
	
	public static class Thumbnail {
		@Key("hqDefault")
		public String defaultUrl;

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
