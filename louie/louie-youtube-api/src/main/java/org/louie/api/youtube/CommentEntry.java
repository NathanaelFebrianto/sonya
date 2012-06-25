package org.louie.api.youtube;

import java.util.List;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

/**
 * This class is comment.
 * 
 * @author Younggue Bae
 */
public class CommentEntry extends Item {

	@Key
	DateTime published;
	
	@Key
	DateTime updated;
	
	@Key
	String title;
	
	@Key
	String content;
	
	@Key
	List<String> link;

	@Key("author")
	Author author;

	@Override
	public String toString() {
		return "CommentEntry [published=" + published + ", updated=" + updated
				+ ", title=" + title + ", content=" + content + ", link="
				+ link + ", author=" + author + "]";
	}

}
