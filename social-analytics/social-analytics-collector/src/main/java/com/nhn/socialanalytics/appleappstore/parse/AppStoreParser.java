package com.nhn.socialanalytics.appleappstore.parse;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nhn.socialanalytics.common.util.DateUtil;

public class AppStoreParser {
	
	public static String extractReviewId(String url) {
		Pattern pattern = Pattern.compile("(userReviewId=[0-9]+)", Pattern.CASE_INSENSITIVE);
		
		Matcher matcher = pattern.matcher(url);
		while (matcher.find()) {
			String matchText = matcher.group();
			if (!matchText.isEmpty()) {
				return String.valueOf(extractNumber(matchText));
			}
		}
		
		return "";
	}
	
	public static String extractAuthorId(String url) {
		Pattern pattern = Pattern.compile("(userProfileId=[0-9]+)", Pattern.CASE_INSENSITIVE);
		
		Matcher matcher = pattern.matcher(url);
		while (matcher.find()) {
			String matchText = matcher.group();
			if (!matchText.isEmpty()) {
				return  String.valueOf(extractNumber(matchText));
			}
		}
		
		return "";
	}
	
	public static String extractVersion(String text) {
		text = text.replaceAll(" ", "");
		text = text.replaceAll("\n", "");
		text = text.replaceAll("Version", "");	// English
		text = text.replaceAll("버전", "");	// Korea
		text = text.replaceAll("バージョン", "");	// Japan
		text = text.replaceAll("e", "");	// Italy
		text = text.replaceAll("Versie", "");	// Nederland
		text = text.replaceAll("Versión", "");	// Espana
		text = text.replaceAll("版本", "");	// China
		
		StringTokenizer st = new StringTokenizer(text, "-");
		int count = 1;
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (count == 1)
				return token;
		}
		
		return "";
	}
	
	public static String extractDate(String text) {
		String strDate = "";
		text = text.replaceAll(" ", "");
		text = text.replaceAll("\n", "");
		
		StringTokenizer st = new StringTokenizer(text, "-");
		StringBuffer sbDate = new StringBuffer();
		int totalTokens = st.countTokens();
		int count = 1;
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (count > 1 && count < totalTokens)
				sbDate.append(token).append("-");
			else if (count > 1 && count == totalTokens)
				sbDate.append(token);
			
			count++;
		}
		
		System.out.println("sbDate == " + sbDate.toString());

		try {
			Date date = DateUtil.convertStringToDate("dd-MMM-yyyy", sbDate.toString(), Locale.US);
			strDate = DateUtil.convertDateToString("yyyyMMdd", date);
		} catch (ParseException e) {
			e.printStackTrace();
			return sbDate.toString();
		}
		
		return strDate;
	}
	
	public static int extractNumber(String text) {
		System.out.println("text == " + text);
		
		Pattern pattern = Pattern.compile("([0-9]+)", Pattern.CASE_INSENSITIVE);
		try {
			Matcher matcher = pattern.matcher(text);
			while (matcher.find()) {
				String matchText = matcher.group();
				if (!matchText.isEmpty()) {
					return Integer.valueOf(matchText);
				}
			}			
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return 0;
		}
		return 0;		
	}

}
