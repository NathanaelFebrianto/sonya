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
	public DateTime published;
	
	@Key
	public DateTime updated;
	
	@Key
	public String title;
	
	@Key
	public String content;
	
	@Key
	public List<String> link;

	@Key("author")
	public Author author;

	@Override
	public String toString() {
		return "CommentEntry [published=" + published + ", updated=" + updated
				+ ", title=" + title + ", content=" + content + ", link="
				+ link + ", author=" + author + "]";
	}

}
