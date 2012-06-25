package org.louie.api.youtube;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

/**
 * This class is a user feed. 
 * https://developers.google.com/youtube/2.0/developers_guide_protocol_profiles
 * 
 * @author Younggue Bae
 */
public class UserEntry extends Entry {

	@Key
	DateTime published;
	
	@Key
	DateTime updated;
	
	@Key
	String title;

	@Key("yt:userId")
	String id;

	@Key("author")
	Author author;

	@Key("yt:age")
	int age;

	@Key("yt:gender")
	String gender;

	@Key("yt:location")
	String location;

	@Key("yt:username")
	String username;

	@Override
	public String toString() {
		return "UserFeed [title=" + title + ", id=" + id + ", author=" + author
				+ ", age=" + age + ", gender=" + gender + ", location="
				+ location + ", username=" + username + ", updated=" + updated
				+ ", published=" + published + "]";
	}
}