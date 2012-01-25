package com.nhn.socialanalytics.me2day.parse;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Me2dayParser {
	
	public static String extractContent(String body, String type) {		
		body = body.replaceAll("\n", " ");
		body = body.replaceAll("\t", " ");
		body = body.replaceAll("&gt;", "");
		body = body.replaceAll("&lt;", "");
		body = body.replaceAll("&amp;", "");
		body = removeReservedWords(type, body);
		body = removeMe2dayUrls(body);
		body = stripHTML(body);
		
		Pattern EXTRACTION_PATTERN = Pattern.compile("http://(.*?)\\s|http://(.*?)\\Z");		
		StringBuffer buffer = new StringBuffer(body);		
		Matcher matcher = EXTRACTION_PATTERN.matcher(buffer);		
	
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                if (matcher.group(i) != null) {
                	if (i == 1)
                  		try { body = body.replaceAll("http://"+matcher.group(i)+"\\s", ""); } catch (Exception e) { e.printStackTrace(); }
                		
                	if (i == 2)
                		try { body = body.replaceAll("http://"+matcher.group(i)+"\\Z", ""); } catch (Exception e) { e.printStackTrace(); }
                }
            }            
        }
         
        return body;
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
