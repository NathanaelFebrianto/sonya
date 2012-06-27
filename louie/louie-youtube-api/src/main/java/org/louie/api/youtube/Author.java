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
	
	@Key("uri")
	public String uri;
	
	public String getUsername() {
		if (uri != null && !uri.equals("")) {
			int pos = uri.lastIndexOf("/");
			String username = uri.substring(pos + 1);
			return username;
		} 
		else {
			return name;
		}
	}

	@Override
	public String toString() {
		return "Author [name=" + name + ", userId=" + userId + ", uri=" + uri
				+ "]";
	}

}
