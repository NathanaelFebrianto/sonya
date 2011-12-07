package com.nhn.socialanalytics.common.util;

import java.lang.Character.UnicodeBlock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	
	public static final String replaceStrings(String text, String regex, String newStr) {
		if (text == null) {
			return null;
		}
	
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

		Matcher matcher = pattern.matcher(text);
		return matcher.replaceAll(newStr);
	}
	
	public static String convertEmoticonToTag(String text) {		
		text = replaceStrings(text, "(\\?+)", " TAGQUESTION ");
		text = replaceStrings(text, "(\\^\\^+)", " TAGSMILE ");
		text = replaceStrings(text, "(ㅋ+)", " TAGSMILE ");
		text = replaceStrings(text, "(ㅎ+)", " TAGSMILE ");
		text = replaceStrings(text, "(ㅜ+)", " TAGCRY ");
		text = replaceStrings(text, "(ㅠ+)", " TAGCRY ");
		text = replaceStrings(text, "(ㅡㅡ)", " TAGCRY ");
		text = replaceStrings(text, "(♡+)", " TAGLOVE ");
		text = replaceStrings(text, "(♥+)", " TAGLOVE ");
		text = replaceStrings(text, "(!+)", " TAGEXCLAMATION ");		
		
		return text;
	}

	public static final String removeKoreanUnsupportedCharacters(String str) {
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			Character.UnicodeBlock unicodeBlock = Character.UnicodeBlock.of(ch);
			
			if ( !(Character.isDigit(ch)
					|| UnicodeBlock.HANGUL_SYLLABLES.equals(unicodeBlock)
					|| UnicodeBlock.HANGUL_COMPATIBILITY_JAMO.equals(unicodeBlock)
					|| UnicodeBlock.HANGUL_JAMO.equals(unicodeBlock)
					|| UnicodeBlock.BASIC_LATIN.equals(unicodeBlock)) 
				) {
				str = str.replace(ch, ' ');
			}
			
			str = str.replaceAll("ᆢ", "");
			str = str.replaceAll("'", "");
		}
		
		return str;
	}
}
