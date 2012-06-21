package org.louie.api.youtube;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

/**
 * This class is item.
 * 
 * @author Younggue Bae
 */
public class Item {

	@Key
	String title;

	@Key
	DateTime updated;

}
