package org.louie.api.youtube;

import com.google.api.client.util.Key;

public class Player {
	// "default" is a Java keyword, so need to specify the JSON key manually
	@Key("default")
	String defaultUrl;
}
