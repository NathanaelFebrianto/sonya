package org.louie.api.youtube;

import com.google.api.client.util.Key;

/**
 * This class is player.
 * 
 * @author Younggue Bae
 */
public class Player {
	// "default" is a Java keyword, so need to specify the JSON key manually
	@Key("default")
	String defaultUrl;
}
