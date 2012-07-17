package org.louie.common.util;

import java.lang.Character.UnicodeBlock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is string utilities.
 * 
 * @author Younggue Bae
 */
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
			str = str.replaceAll("ﾟ", "");
		}
		
		return str;
	}
	
	public static final boolean isEmpty(String str) {
		if (str == null || str.trim().equals("")) {
			return true;
		}
		
		return false;
	}
	
	public static final String escapeDelimiterChar(String str) {
		if (!isEmpty(str))
			return str.replaceAll("\n", "\\\\n").replaceAll("\r", "\\\\r").replaceAll("\t", "\\\\t");
		
		return str;
	}
	
	public static final boolean isAlphabet(char c) {
		if ((0x61 <= c && c <= 0x7A) || (0x41 <= c && c <= 0x5A))
			return true;
		
		return false;
	}
	
	public static final boolean isHangul(char c) {
		 if ((0xAC00 <= c && c <= 0xD7A3) || (0x3131 <= c && c <= 0x318E))
			return true;
		
		return false;
	}
	
	public static final boolean isNumber(char c) {
		 if (0x30 <= c && c <= 0x39)
			return true;
		
		return false;
	}
	
}
