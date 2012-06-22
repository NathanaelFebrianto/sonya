package org.louie.api.youtube;

import com.google.api.client.util.Key;

/**
 * This class is a category.
 * 
 * @author Younggue Bae
 */
public class Category {

	@Key("@scheme")
	public String scheme;

	@Key("@term")
	public String term;

	public static Category newKind(String kind) {
		Category category = new Category();
		category.scheme = "http://schemas.google.com/g/2005#kind";
		category.term = "http://schemas.google.com/photos/2007#" + kind;
		return category;
	}

}
