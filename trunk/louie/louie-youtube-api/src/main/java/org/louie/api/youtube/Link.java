package org.louie.api.youtube;

import java.util.List;

import com.google.api.client.util.Key;

/**
 * This class is a link. 
 * 
 * @author Younggue Bae
 */
public class Link {

	@Key("@href")
	public String href;

	@Key("@rel")
	public String rel;

	public static String find(List<Link> links, String rel) {
		if (links != null) {
			for (Link link : links) {
				if (rel.equals(link.rel)) {
					return link.href;
				}
			}
		}
		return null;
	}

}
