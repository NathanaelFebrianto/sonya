package org.louie.api.youtube;

import java.util.ArrayList;
import java.util.List;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

/**
 * This class is user feed. 
 * https://developers.google.com/youtube/2.0/developers_guide_protocol_profiles
 * 
 * @author Younggue Bae
 */
public class UserFeed extends Feed<User> {

	@Key
	String title;

	@Key
	DateTime updated;

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
	public List<User> getItems() {
		List<User> items = new ArrayList<User>();

		if (id != null) {
			User user = new User();
			user.title = title;
			user.updated = updated;
			user.id = id;
			user.author = author;
			user.age = age;
			user.gender = gender;
			user.location = location;
			user.username = username;
	
			items.add(user);
		}
		return items;
	}

	@Override
	public int getTotalItemsSize() {
		return getItems().size();
	}

}