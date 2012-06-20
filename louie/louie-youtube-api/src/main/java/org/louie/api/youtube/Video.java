package org.louie.api.youtube;

import java.util.ArrayList;
import java.util.List;

import com.google.api.client.util.Key;

public class Video extends Item {

	@Key
	String description;

	@Key
	List<String> tags = new ArrayList<String>();

	@Key
	Player player;
	
	
	
	@Key
	int viewCount;
	
	@Key
	String category;
}
