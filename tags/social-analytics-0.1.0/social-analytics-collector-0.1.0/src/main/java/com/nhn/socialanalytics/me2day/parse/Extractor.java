package com.nhn.socialanalytics.me2day.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class to extract usernames, lists, hashtags and URLs from Tweet text.
 */
public class Extractor {

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
			String matchText = matcher.group();
			if (!matchText.isEmpty()) {
				htmls.add(matcher.group());
			}
		}

		return htmls;
	}
	
	public String stripHTML(String text) {
		Matcher matcher1 = Regex.HTML_TAG_HREF_START.matcher(text);
		text = matcher1.replaceAll("");
		Matcher matcher2 = Regex.HTML_TAG_HREF_END.matcher(text);
		text = matcher2.replaceAll("");
		
		Matcher matcher3 = Regex.HTML_TAG.matcher(text);
		return matcher3.replaceAll("");
	}
	
	public String replaceStrings(String text, String regex, String newStr) {
		if (text == null) {
			return null;
		}
	
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

		Matcher matcher = pattern.matcher(text);
		return matcher.replaceAll(newStr);
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
	public List<String> extractList(Pattern pattern, String text, int groupNumber) {
		List<String> extracted = new ArrayList<String>();
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			extracted.add(matcher.group(groupNumber));
		}
		return extracted;
	}
	
	public static void main(String[] args) {
		
		Extractor extractor = new Extractor();
	
//		String text = "공주의 남자今天OST：백지영 (Baek Ji Young) 1- 오늘도 사랑해 也愛你";
//		String text = "แอร๊กกกกกก แม่ลูกฉลองวันเกิด คิมฮีและคิมคิ กูจะร้องไห้ คิดถึงเมิงมากมายบอมมี่";
//		String text = "ทำไมไม่เอาหมวยไปด้วยอ่า";
//		String text = "긍정돼지 저게요 밑에 은근히 많이 깔려서 배부르답니다ㅋㅋ누군가를 좋아하게 되니 저절로 소식하고 싶다는ᆢ뭐래?;;탕수육 맛나게 드셔요~규일님^^";
//		String text = "<a href='http://blog.naver.com/GoPost.nhn?blogId=love05741&logNo=60138129232'>[기사] 우결 권리세, 데이비드오에 “2PM 택연이 좋아” 고백</a><a href='http://blog.naver.com/GoPost.nhn?blogId=love05741&logNo=60138129232'>[기사] 우결 권리세, 데이비드오에 “2PM 택연이 좋아” 고백</a>";
//		System.out.println("before == " + text);		
//		List<String> list = extractor.extractHrefHTMLs(text);
//		System.out.println("list == " + list);
//		
//		text = extractor.stripHTML(text);
//		System.out.println("after == " + text);
		
		// <\\s*a[^>]*>(.*?)<\\s*/\\s*a>
		// a href='http://me2day.net
		String text1 = "<a href='http://me2day.net/enigma2k'>드래슬리</a> 전 좋다기보단 넘 멋쪄요<a href='http://me2day.net/enigma2k'>라라라</a>";
		Pattern pattern = Pattern.compile("<\\s*a href='http://me2day.net[^>]*>(.*?)<\\s*/\\s*a>", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(text1);
		while (matcher.find()) {
			System.out.println(matcher.group());
		}
		
		String text2 = "나방금까지 잠안와서^^공주의?남자 메이킹 봣음&gt;&lt;ㅋㅋㅋ멋져잉♥ㅋㅋㅋㅋ아…..5시임&gt;&lt;^^^^어케함??!";
		System.out.println(extractor.replaceStrings(text2, "(ㅋ+)", "TAGSMILE"));
		System.out.println(extractor.replaceStrings(text2, "(\\?+)", "TAGQUESTION"));
		System.out.println(extractor.replaceStrings(text2, "(\\^\\^+)", "TAGSMILE"));
		
	}

}
