package com.nhn.socialbuzz.textmining.analysis;
import java.io.IOException;
import java.io.StringReader;
import java.lang.Character.UnicodeBlock;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.kr.morph.AnalysisOutput;
import org.apache.lucene.analysis.kr.morph.MorphAnalyzer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Version;

import com.nhn.socialbuzz.textmining.regex.Extractor;

public class TextAnalyzer {
	
	public static final String[] STOP_WORDS = new String[]{
		"a", "an", "and", "are", "as", "at", "be", "but", "by",
		"for", "if", "in", "into", "is", "it",
		"no", "not", "of", "on", "or", "such",
		"that", "the", "their", "then", "there", "these",
		"they", "this", "to", "was", "will", "with"		  
	};
  
	public static final String[] KOR_STOP_WORDS = new String[]{
		"이","그","저","것","수","등","들"
	};	
	
	private Set<Object> stopSet;
	
	private Extractor extractor;
	
	private MorphAnalyzer morphAnalyzer;
	
	private StopAnalyzer stopAnalyzer;
	
	/**
	 * Creates a text analyzer.
	 * 
	 */
	public TextAnalyzer() {
		
		extractor = new Extractor();
		
		stopSet = StopFilter.makeStopSet(Version.LUCENE_CURRENT, STOP_WORDS, true);
		stopSet.addAll(StopFilter.makeStopSet(Version.LUCENE_CURRENT, KOR_STOP_WORDS, true));
		
		morphAnalyzer = new MorphAnalyzer();		
		stopAnalyzer = new StopAnalyzer(Version.LUCENE_CURRENT, stopSet);
		System.out.println("stop words == " + stopAnalyzer.getStopwordSet());
	}

	/**
	 * Extract terms by lucene.
	 * 
	 * @param type
	 * @param text
	 * @return
	 */
	public Vector<String> extractTerms(String text) {
		System.out.println("TextAnalyzer :: original text == " + text);
		
		///////////////////////////////////////////
		// normalize the text
		///////////////////////////////////////////
		text = this.removeUrls(text);
		text = this.stripHTML(text);
		text = this.convertEmoticonToTag(text);
		///////////////////////////////////////////
		
		System.out.println("TextAnalyzer :: normalized text == " + text);
		
		Vector<String> terms = new Vector<String>();
		
		TokenStream stream = stopAnalyzer.tokenStream("k", new StringReader(text));

		TermAttribute termAttr = stream.getAttribute(TermAttribute.class); 		
        OffsetAttribute offSetAttr = stream.getAttribute(OffsetAttribute.class);
		
		try {

			while (stream.incrementToken()) {
				try {
					morphAnalyzer.setExactCompound(false);					
					List<AnalysisOutput> results = morphAnalyzer.analyze(termAttr.term());

					for (AnalysisOutput o : results) {

						String metaWord = o.toString();						
						String term = o.getStem();
						
						String noun = "";
						String verb = "";
						if (metaWord.indexOf("(N)") >= 0) {
							noun = term;
							if (!noun.trim().equals(""))
								terms.add(noun);
						}
						if (metaWord.indexOf("(V)") >= 0) {
							verb = term;
							if (!verb.trim().equals(""))
							terms.add(verb + "다");
						}
						
						System.out.println("token = " + termAttr.term() + " => " + o.getStem() + ", noun = " + noun + ", verb = " + verb);
					}										

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		return terms;		
	}
	
	/**
	 * Convert terms into a delimited string.
	 * 
	 * @param terms
	 * @return
	 */
	public String convertTermsToStringWithoutUnsupportedChars(Vector<String> terms) {
		StringBuffer sbTerms = new StringBuffer();
		for (String term: terms) {
			term = this.removeUnsupportedCharacters(term);
			if(!term.trim().equals(""))
				sbTerms.append(term).append(" ");
		}
		
		return sbTerms.toString().trim();
	}
	
	/**
	 * Converts emoticons to tags.
	 * 
	 * @param type
	 * @param text
	 * @return
	 */
	public String convertEmoticonToTag(String text) {		
		
		text = this.replaceStrings(text, "(\\?+)", "TAGQUESTION");
		text = this.replaceStrings(text, "(\\^\\^+)", "TAGSMILE");
		text = this.replaceStrings(text, "(ㅋ+)", " TAGSMILE ");
		text = this.replaceStrings(text, "(ㅎ+)", " TAGSMILE ");
		text = this.replaceStrings(text, "(ㅜ+)", " TAGCRY ");
		text = this.replaceStrings(text, "(ㅠ+)", " TAGCRY ");
		text = this.replaceStrings(text, "(♡+)", " TAGLOVE ");
		text = this.replaceStrings(text, "(♥+)", " TAGLOVE ");
		text = this.replaceStrings(text, "(!+)", " TAGEXCLAMATION ");		
		
		return text;
	}
	
	/**
	 * Removes the urls from the source text.
	 * 
	 * @param text
	 * @return
	 */
	public String removeUrls(String text) {
		List<String> urls = extractor.extractURLs(text);
		
		if (urls.size() > 0) {
			//System.out.println("text before removing urls == " + text);
			//System.out.println("urls == " + urls.toString());
		}		
		
		for (String url : urls) {
			text = text.replaceAll(url, " ");
			//System.out.println("text after removing urls == " + text);
		}
				
		return text;		
	}
	
	/**
	 * Removes the HTML tags.
	 * 
	 * @param text
	 * @return
	 */
	public String stripHTML(String text) {	
		text = extractor.stripHTML(text);
		text = text.replaceAll("&amp;", "").replaceAll("&lt;", "").replaceAll("&gt;", "");
			
		return text;
	}	
	
	/**
	 * Replaces the old characters with the new characters.
	 * 
	 * @param text
	 * @param oldChar
	 * @param newChar
	 * @return
	 */
	public String replaceStrings(String text, String regexp, String newStr) {
		return extractor.replaceStrings(text, regexp, newStr);
	}
	
	/**
	 * Removes unsupported characters such as Chinese, Japanese, etc.
	 * 
	 * @param str
	 * @return
	 */
	public String removeUnsupportedCharacters(String str) {
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
		}
		
		return str;
	}
	
	public static void main(String[] args) {
		
		TextAnalyzer analyzer = new TextAnalyzer();
	
//		String text = "공주의 남자今天OST：백지영 (Baek Ji Young) 1- 오늘도 사랑해 也愛你";
//		String text = "แอร๊กกกกกก แม่ลูกฉลองวันเกิด คิมฮีและคิมคิ กูจะร้องไห้ คิดถึงเมิงมากมายบอมมี่";
//		String text = "ทำไมไม่เอาหมวยไปด้วยอ่า";
//		String text = "긍정돼지 저게요 밑에 은근히 많이 깔려서 배부르답니다ㅋㅋ누군가를 좋아하게 되니 저절로 소식하고 싶다는ᆢ뭐래?;;탕수육 맛나게 드셔요~규일님^^";
		String text = "<a href='http://blog.naver.com/GoPost.nhn?blogId=love05741&logNo=60138129232'>[기사] 우결 권리세, 데이비드오에 “2PM 택연이 좋아” 고백</a>";
		Vector<String> terms = analyzer.extractTerms(text);
		text = analyzer.convertTermsToStringWithoutUnsupportedChars(terms);
		System.out.println(text);
		
	}

}
