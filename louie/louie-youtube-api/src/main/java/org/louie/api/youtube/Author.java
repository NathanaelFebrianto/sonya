package org.louie.api.youtube;

import com.google.api.client.util.Key;

/**
 * This class is author.
 * 
 * @author Younggue Bae
 */
public class Author {

	@Key("name")
	String name;

	@Override
	public String toString() {
		return "Author [name=" + name + "]";
	}

}
