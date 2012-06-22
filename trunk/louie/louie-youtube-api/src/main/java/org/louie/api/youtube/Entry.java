package org.louie.api.youtube;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;


/**
 * This class is a single item feed.
 * 
 * @author Younggue Bae
 */
public abstract class Entry {
	
	@Key
	DateTime updated;
	
	@Key
	DateTime published;
}