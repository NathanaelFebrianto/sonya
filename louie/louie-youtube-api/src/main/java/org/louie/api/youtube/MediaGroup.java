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
	String title;
	
	@Key("media:category")
	String category;
	
	@Key("media:description")
	String description;
	
	@Key("media:keywords")
	String keywords;
	
	@Key("yt:uploaded")
	DateTime uploaded;
	
	@Key("yt:uploaderId")
	String uploaderId;
	
	@Key("yt:videoid")
	String videoId;
	
	@Key("media:player")
	Player player;

	public static class Player {
		@Key("@url")
		String url;

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
