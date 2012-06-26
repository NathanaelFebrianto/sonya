package org.louie.api.youtube;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

/**
 * This class is a media group.
 * 
 * @author Younggue Bae
 */
public class MediaGroup {

	@Key("media:title")
	public String title;
	
	@Key("media:category")
	public String category;
	
	@Key("media:description")
	public String description;
	
	@Key("media:keywords")
	public String keywords;
	
	@Key("yt:uploaded")
	public DateTime uploaded;
	
	@Key("yt:uploaderId")
	public String uploaderId;
	
	@Key("yt:videoid")
	public String videoId;
	
	@Key("media:player")
	public Player player;

	public static class Player {
		@Key("@url")
		public String url;

		@Override
		public String toString() {
			return "Player [url=" + url + "]";
		}
		
	}

	@Override
	public String toString() {
		return "MediaGroup [title=" + title + ", category=" + category
				+ ", description=" + description + ", keywords=" + keywords
				+ ", uploaded=" + uploaded + ", uploaderId=" + uploaderId
				+ ", videoId=" + videoId + ", player=" + player + "]";
	}
	
}
