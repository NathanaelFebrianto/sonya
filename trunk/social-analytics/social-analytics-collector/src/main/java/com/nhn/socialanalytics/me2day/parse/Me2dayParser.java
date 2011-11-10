package com.nhn.socialanalytics.me2day.parse;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nhn.socialanalytics.common.util.StringUtil;
import com.nhn.socialanalytics.me2day.parse.Extractor;

public class Me2dayParser {

	
	public static String extractContent(String body, String type) {
		body = Me2dayParser.removeReservedWords(type, body);
		body = Me2dayParser.removeMe2dayUrls(body);
		body = StringUtil.removeUnsupportedCharacters(body);
		//body = StringUtil.convertEmoticonToTag(body);
        
		body = body.replaceAll("\n", " ");
		body = body.replaceAll("\t", " ");
         
        return body;
	}
	
	public static String convertEmoticonToTag(String text) {
		return StringUtil.convertEmoticonToTag(text);
	}
	
	/**
	 * Convert string to date.
	 * 
	 * @param publishDate
	 * @return
	 */
	public static final Date convertDate(String publishDate) {
		int year = Integer.parseInt(publishDate.substring(0, 4));
		int month = Integer.parseInt(publishDate.substring(5, 7))-1;
		int date = Integer.parseInt(publishDate.substring(8, 10));
		int hourOfDay = Integer.parseInt(publishDate.substring(11, 13));
		int minute = Integer.parseInt(publishDate.substring(14, 16));
		int second = Integer.parseInt(publishDate.substring(17, 19));
		Calendar cal = Calendar.getInstance(Locale.KOREA);
		cal.set(year, month, date, hourOfDay, minute, second);
		return cal.getTime();
	}
	
	/**
	 * Removes the reserved words.
	 * 
	 * @param type
	 * @param text
	 * @return
	 */
	public static final String removeReservedWords(String type, String text) {		
		if (type.equals("TAG")) {
			text = text.replaceAll("me2photo", ""); 
			text = text.replaceAll("me2mobile", "");
			text = text.replaceAll("me2mms", "");
			text = text.replaceAll("me2sms", "");
			text = text.replaceAll("me2music", "");
			text = text.replaceAll("me2movie", "");
			text = text.replaceAll("me2book", "");
			text = text.replaceAll("지식로그", "");
			text = text.replaceAll("네이버뉴스", "");
			
			if (text.indexOf("네이버블로그") >= 0) {
				return "";
			}				
		}
		
		if (type.equals("POST")) {
			text = text.replaceAll("지식로그", "");			
		}
		
		return text;
	}
	
	/**
	 * Removes the me2day urls.
	 * 
	 * @param usernames
	 * @param text
	 * @return
	 */
	public static final String removeMe2dayUrls(String text) {
		Pattern pattern = Pattern.compile("<\\s*a href='http://me2day.net[^>]*>(.*?)<\\s*/\\s*a>", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(text);
		text = matcher.replaceAll("");
		text = text.replaceAll("\\[", "").replaceAll("\\]", "");
		
		return text;
	}
	
	public static final String extractUrl(String text) {
		Extractor exractor = new Extractor();
		List<String> urls = exractor.extractURLs(text);
		if (urls != null && urls.size() > 0)
			return (String) urls.get(0);

		return null;
	}
	
	public static final String stripHTML(String text) {
		Extractor extractor = new Extractor();
		text = extractor.stripHTML(text);
		
		return text;
	}
}
