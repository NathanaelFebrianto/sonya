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
	
	/*
	public static final String removeJapaneseUnsupportedCharacters(String str) {
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			Character.UnicodeBlock unicodeBlock = Character.UnicodeBlock.of(ch);
			
			if ( !(Character.isDigit(ch)
					|| UnicodeBlock.HIRAGANA.equals(unicodeBlock)
					|| UnicodeBlock.KATAKANA.equals(unicodeBlock)
					|| UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS.equals(unicodeBlock)
					|| UnicodeBlock.KANBUN.equals(unicodeBlock)
					|| UnicodeBlock.KANGXI_RADICALS .equals(unicodeBlock)
					|| UnicodeBlock.BASIC_LATIN.equals(unicodeBlock)) 
				) {
				str = str.replace(ch, ' ');
			}
			
			str = str.replaceAll("ᆢ", "");
			str = str.replaceAll("'", "");
		}
		
		return str;
	}
	*/
}
