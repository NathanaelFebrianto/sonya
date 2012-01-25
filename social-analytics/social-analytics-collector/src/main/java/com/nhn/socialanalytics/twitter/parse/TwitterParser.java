package com.nhn.socialanalytics.twitter.parse;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TwitterParser {

	private static String AT_SIGNS_CHARS = "@\uFF20";
	
	public static final Pattern AT_SIGNS = Pattern.compile("[" + AT_SIGNS_CHARS	+ "]");

	public static final Pattern EXTRACT_RETWEET = Pattern.compile("^(?:["
			+ com.nhn.socialanalytics.twitter.parse.Spaces.getCharacterClass() + "])*" 
			+ "([Rr])([Tt])" 
			+ "([" + com.nhn.socialanalytics.twitter.parse.Spaces.getCharacterClass() + "])" 
			+ AT_SIGNS + "([a-z0-9_]{1,20}).*",
			Pattern.CASE_INSENSITIVE);
	
	public static final int EXTRACT_RETWEET_GROUP_USERNAME = 4;
	
	public static final Pattern POSITIVE_ATTITUDE = Pattern.compile("[:)]");
	public static final Pattern NEGATIVE_ATTITUDE = Pattern.compile("[:(]");
	public static final Pattern NEGATIVE_ATTITUDE1 = Pattern.compile("[:P]");

	public static final List<String> extractMentionedUserList(String text, List<String> excludeUsers) {
		Extractor exractor = new Extractor();
		List<String> mentionedUsers = exractor.extractMentionedScreennames(text);
		
		for (int i = 0 ; i < mentionedUsers.size(); i++) {
			String user = (String) mentionedUsers.get(i);
			for (int j = 0 ; j < excludeUsers.size(); j++) { 
				String excludeUser = (String) excludeUsers.get(j);
				if (user.equalsIgnoreCase(excludeUser))
					mentionedUsers.remove(user);
			}
		}
		
		return mentionedUsers;
	}
	
	public static final String extractMentionedUsers(String text, List<String> excludeUsers) {
		List<String> mentionedUsers = extractMentionedUserList(text, excludeUsers);
		
		StringBuffer users = new StringBuffer();
		for (int i = 0 ; i < mentionedUsers.size(); i++) {
			if (i == 0)
				users.append("|");
			
			users.append(mentionedUsers.get(i))
				 .append("|");
		}

		return users.toString();
	}
	
	public static final String extractMentionedUser(String text, List<String> excludeUsers) {
		List<String> mentionedUsers = extractMentionedUserList(text, excludeUsers);
		
		if (mentionedUsers != null && mentionedUsers.size() > 0)
			return (String) mentionedUsers.get(0);

		return null;
	}

	public static final String extractUrl(String text) {
		Extractor exractor = new Extractor();
		List<String> urls = exractor.extractURLs(text);
		if (urls != null && urls.size() > 0)
			return (String) urls.get(0);

		return null;
	}

	public static String extractRetweetedUser(String text) {
		if (text == null) {
			return null;
		}

		Matcher matcher = EXTRACT_RETWEET.matcher(text);
		if (matcher.matches()) {
			return matcher.group(EXTRACT_RETWEET_GROUP_USERNAME);
		} else {
			return null;
		}
	}
	
	public static String extractContent(String body) {
		Pattern EXTRACTION_PATTERN = Pattern.compile("@(.*?)\\s|@(.*?)\\Z|RT\\s@(.*?)\\s|http://(.*?)\\s|http://(.*?)\\Z");
		
		StringBuffer buffer = new StringBuffer(body);
		String out = body;
		
		Matcher matcher = EXTRACTION_PATTERN.matcher(buffer);		
	
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                if (matcher.group(i) != null) {

                	if (i == 1)
                		try { out = out.replaceAll("@"+matcher.group(i)+"\\s", ""); } catch (Exception e) { e.printStackTrace(); }
                		
                	if (i == 2)
                    	try { out = out.replaceAll("@"+matcher.group(i)+"\\Z", ""); } catch (Exception e) { e.printStackTrace(); }
                		
                	if (i == 3)
                		try { out = out.replaceAll("RT @"+matcher.group(i)+"\\s", ""); } catch (Exception e) { e.printStackTrace(); }

                	if (i == 4)
                		try { out = out.replaceAll("http://"+matcher.group(i)+"\\s", ""); } catch (Exception e) { e.printStackTrace(); }
                		
                	if (i == 5)
                		try { out = out.replaceAll("http://"+matcher.group(i)+"\\Z", ""); } catch (Exception e) { e.printStackTrace(); }
                }
            }            
        }
        
        out = out.replaceAll("\n", " ");
        out = out.replaceAll("\t", " ");
       
        return out;
	}

}
