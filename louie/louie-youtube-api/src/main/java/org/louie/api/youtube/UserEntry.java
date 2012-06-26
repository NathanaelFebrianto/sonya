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
	public String id;
	
	@Key
	public DateTime published;
	
	@Key
	public DateTime updated;
	
	@Key
	public String title;
	
	@Key
	public String summary;

	@Key("author")
	public Author author;
	
	@Key("yt:firstName")
	public String firstName;

	@Key("yt:lastName")
	public String lastName;
	
	@Key("yt:aboutMe")
	public String aboutMe;
	
	@Key("yt:age")
	public int age;

	@Key("yt:gender")
	public String gender;
	
	@Key("yt:company")
	public String company;
	
	@Key("yt:hobbies")
	public String hobbies;
	
	@Key("yt:hometown")
	public String hometown;

	@Key("yt:location")
	public String location;
	
	@Key("yt:occupation")
	public String occupation;
	
	@Key("yt:school")
	public String school;

	@Override
	public String toString() {
		return "UserEntry [id=" + id + ", published=" + published
				+ ", updated=" + updated + ", title=" + title + ", summary="
				+ summary + ", author=" + author + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", aboutMe=" + aboutMe + ", age="
				+ age + ", gender=" + gender + ", company=" + company
				+ ", hobbies=" + hobbies + ", hometown=" + hometown
				+ ", location=" + location + ", occupation=" + occupation
				+ ", school=" + school + "]";
	}
	
}