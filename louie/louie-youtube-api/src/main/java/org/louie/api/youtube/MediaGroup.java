package org.louie.api.youtube;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

/**
 * This class is a media group.
 * 
 * @author Younggue Bae
 */
public class MediaGroup {

	@Key("media:category")
	String category;
	
	@Key("media:description")
	String description;
	
	@Key("media:keywords")
	String keywords;
	
	@Key("media:title")
	String title;
	
	@Key("yt:uploaded")
	DateTime uploaded;
	
	@Key("yt:uploaderId")
	String uploadId;
	
	@Key("yt:videoid")
	String videoId;

	@Override
	public String toString() {
		return "MediaGroup [category=" + category + ", description="
				+ description + ", keywords=" + keywords + ", title=" + title
				+ ", uploaded=" + uploaded + ", uploadId=" + uploadId
				+ ", videoId=" + videoId + "]";
	}
	
}
