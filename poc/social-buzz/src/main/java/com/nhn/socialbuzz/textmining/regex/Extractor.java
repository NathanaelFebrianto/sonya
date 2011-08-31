package com.nhn.socialbuzz.textmining.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class to extract usernames, lists, hashtags and URLs from Tweet text.
 */
public class Extractor {
	public static class Entity {
		protected Integer start = null;
		protected Integer end = null;
		protected String value = null;
		protected String type = null;

		public Entity(Matcher matcher, String valueType, Integer groupNumber) {
			this.start = matcher.start(groupNumber) - 1; // 0-indexed.
			this.end = matcher.end(groupNumber);
			this.value = matcher.group(groupNumber);
			this.type = valueType;
		}

		/** Constructor used from conformance data */
		public Entity(Map<String, Object> config, String valueType) {
			this.type = valueType;
			this.value = (String) config.get(valueType);
			List<Integer> indices = (List<Integer>) config.get("indices");
			this.start = indices.get(0);
			this.end = indices.get(1);
		}

		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof Entity)) {
				System.out.println("incorrect type");
				return false;
			}

			Entity other = (Entity) obj;

			if (this.type.equals(other.type) && this.start.equals(other.start)
					&& this.end.equals(other.end)
					&& this.value.equals(other.value)) {
				return true;
			} else {
				return false;
			}
		}

		public int hashCode() {
			return this.type.hashCode() + this.value.hashCode() + this.start
					+ this.end;
		}
	}

	/**
	 * Create a new extractor.
	 */
	public Extractor() {
	}

	/**
	 * Extract URL references from text.
	 * 
	 * @param text
	 *            of the text from which to extract URLs
	 * @return List of URLs referenced.
	 */
	public List<String> extractURLs(String text) {
		if (text == null) {
			return null;
		}

		List<String> urls = new ArrayList<String>();

		Matcher matcher = Regex.VALID_URL.matcher(text);
		while (matcher.find()) {
			String protocol = matcher.group(Regex.VALID_URL_GROUP_PROTOCOL);
			if (!protocol.isEmpty()) {
				urls.add(matcher.group(Regex.VALID_URL_GROUP_URL));
			}
		}

		return urls;
	}
	
	/**
	 * Extract <a href...></a> htmls references from  text.-> Regexp 잘못 정의되었는지 작동 안하고 있음. 나중에 다시 봐야 함.
	 * 
	 * @param text
	 *            of the text from which to extract HTMLs
	 * @return List of HTMLs referenced.
	 */
	public List<String> extractHrefHTMLs(String text) {
		if (text == null) {
			return null;
		}

		List<String> htmls = new ArrayList<String>();

		Matcher matcher = Regex.HTML_TAG_HREF.matcher(text);
		while (matcher.find()) {
			String matchText = matcher.group(1);
			if (!matchText.isEmpty()) {
				System.out.println("html == " + matcher.group(1));
				htmls.add(matcher.group(1));
			}
		}

		return htmls;
	}
	
	/**
	 * Extract htmls references from  text. -> Regexp 잘못 정의되었는지 작동 안하고 있음. 나중에 다시 봐야 함.
	 * 
	 * @param text
	 *            of the text from which to extract HTMLs
	 * @return List of HTMLs referenced.
	 */
	public List<String> extractHTMLs(String text) {
		if (text == null) {
			return null;
		}

		List<String> htmls = new ArrayList<String>();

		Matcher matcher = Regex.HTML_TAG.matcher(text);
		while (matcher.find()) {
			String matchText = matcher.group(1);
			if (!matchText.isEmpty()) {
				htmls.add(matcher.group(1));
			}
		}

		return htmls;
	}
	
	public List<String> extractSameCharacters(String text, String ch) {
		if (text == null) {
			return null;
		}

		List<String> chars = new ArrayList<String>();
		
		Pattern pattern = Pattern.compile("("+ ch + "+)", Pattern.CASE_INSENSITIVE);

		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			String matchText = matcher.group(1);
			if (!matchText.isEmpty()) {
				chars.add(matcher.group(1));
			}
		}

		return chars;		
	}

	/**
	 * Helper method for extracting multiple matches from text.
	 * 
	 * @param pattern
	 *            to match and use for extraction
	 * @param text
	 *            of the text to extract from
	 * @param groupNumber
	 *            the capturing group of the pattern that should be added to the
	 *            list.
	 * @return list of extracted values, or an empty list if there were none.
	 */
	private List<String> extractList(Pattern pattern, String text, int groupNumber) {
		List<String> extracted = new ArrayList<String>();
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			extracted.add(matcher.group(groupNumber));
		}
		return extracted;
	}

}
