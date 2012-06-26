package org.louie.api.youtube;

import com.google.api.client.util.Key;

/**
 * This class is author.
 * 
 * @author Younggue Bae
 */
public class Author {

	@Key("name")
	public String name;
	
	@Key("yt:userId")
	public String userId;

	@Override
	public String toString() {
		return "Author [name=" + name + ", userId=" + userId + "]";
	}

}
