package com.nhn.socialanalytics.nlp.lang.ko;

import java.io.IOException;
import java.io.StringReader;
import java.lang.Character.UnicodeBlock;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.kr.KoreanAnalyzer;
import org.apache.lucene.analysis.kr.morph.AnalysisOutput;
import org.apache.lucene.analysis.kr.morph.MorphAnalyzer;
import org.apache.lucene.analysis.kr.morph.MorphException;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Version;

import com.nhn.socialanalytics.nlp.morpheme.MorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.morpheme.Sentence;

public class KoreanMorphemeAnalyzer implements MorphemeAnalyzer {

	public static final String[] STOP_WORDS = new String[]{
		"a", "an", "and", "are", "as", "at", "be", "but", "by",
		"for", "if", "in", "into", "is", "it",
		"no", "not", "of", "on", "or", "such",
		"that", "the", "their", "then", "there", "these",
		"they", "this", "to", "was", "will", "with"		  
	};
  
	public static final String[] KOR_STOP_WORDS = new String[]{
		//"이","그","저","것","수","등","들"
	};	
	
	private Set<Object> stopSet;
	
	private static KoreanMorphemeAnalyzer instance = null;
	
	private MorphAnalyzer morphAnalyzer;
	
	//private StopAnalyzer stopAnalyzer;
	
	private KoreanAnalyzer koreanAnalyzer;
	
	public KoreanMorphemeAnalyzer() {
		stopSet = StopFilter.makeStopSet(Version.LUCENE_CURRENT, STOP_WORDS, true);
		stopSet.addAll(StopFilter.makeStopSet(Version.LUCENE_CURRENT, KOR_STOP_WORDS, true));
		
		morphAnalyzer = new MorphAnalyzer();		
		//stopAnalyzer = new StopAnalyzer(Version.LUCENE_CURRENT, stopSet);
		koreanAnalyzer = new KoreanAnalyzer(Version.LUCENE_CURRENT, stopSet);
		koreanAnalyzer.setBigrammable(false);
		koreanAnalyzer.setHasOrigin(false);
		System.out.println("stop words == " + koreanAnalyzer.getStopwordSet());			
		
		try {
			MorphemeUtil.loadDictionary();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public static KoreanMorphemeAnalyzer getInstance() {
		if (instance == null)
			instance = new KoreanMorphemeAnalyzer();
		return instance;
	}
	
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
			str = str.replaceAll("'", "");
		}
		
		return str;
	}
	
	public Sentence analyze(String text) {
		System.out.println("sentence == " + text);	
		
		text = removeUnsupportedCharacters(text);
		
		Sentence sentence = new Sentence(text);
		
		TokenStream stream = koreanAnalyzer.tokenStream("k", new StringReader(text));

		TermAttribute termAttr = stream.getAttribute(TermAttribute.class); 		
        OffsetAttribute offSetAttr = stream.getAttribute(OffsetAttribute.class);
        int index = 0;
		try {
	        while (stream.incrementToken()) {
				try {					
					List<AnalysisOutput> list = morphAnalyzer.analyze(termAttr.term());
					
					for (AnalysisOutput o : list) {	
						Eojeol e = new Eojeol();
						e.makeObject(index, o);
						System.out.println(e.toString());
						
						sentence.add(e);
					}					
				} catch (MorphException e) {
					e.printStackTrace();
				}
				index++;
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return sentence;
	}
	
	public String extractTerms(String text) {
		System.out.println("sentence == " + text);	
		
		text = removeUnsupportedCharacters(text);
		
		StringBuffer sb = new StringBuffer();
		
		TokenStream stream = koreanAnalyzer.tokenStream("k", new StringReader(text));
		
		TermAttribute termAttr = stream.getAttribute(TermAttribute.class); 		
        OffsetAttribute offSetAttr = stream.getAttribute(OffsetAttribute.class);
        
 		try {
	        while (stream.incrementToken()) {
				try {					
					List<AnalysisOutput> list = morphAnalyzer.analyze(termAttr.term());
					
					for (AnalysisOutput o : list) {	
						String term = o.getStem();
						if (o.getPos() == 'V')
							term = term + "다";
						sb.append(term).append(" ");
					}					
				} catch (MorphException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return sb.toString();
	}
	
	public String extractCoreTerms(String text) {
		System.out.println("sentence == " + text);		
		
		text = removeUnsupportedCharacters(text);
		
		StringBuffer sb = new StringBuffer();
		
		TokenStream stream = koreanAnalyzer.tokenStream("k", new StringReader(text));
		
		TermAttribute termAttr = stream.getAttribute(TermAttribute.class); 		
        OffsetAttribute offSetAttr = stream.getAttribute(OffsetAttribute.class);
        
 		try {
	        while (stream.incrementToken()) {
				try {					
					List<AnalysisOutput> list = morphAnalyzer.analyze(termAttr.term());
					
					for (AnalysisOutput o : list) {
						if (o.getPos() == 'N' || o.getPos() == 'V') {
							String term = o.getStem();
							if (o.getPos() == 'V')
							term = term + "다";
							sb.append(term).append(" ");
						}
					}					
				} catch (MorphException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return sb.toString();
	}
	
	public static void main(String[] args) {		
		//String sentence = "이 물건은 배송이 빨라서 정말 좋지만, 품질이 별로 안 좋네요.";
		//String sentence = "철수가 음악에 재능이 없으면서도 노래를 아주 열심히 부르는 것을 영희가 안다.";
		String sentence = "디시인사이드 일본연애갤러리나 네이버 사쿠라학원에 가시면 많은 자료가 있으니 그곳에도 많이 들려주세요(ﾉ^∇^)ﾉ";
		
		KoreanMorphemeAnalyzer analyzer = new KoreanMorphemeAnalyzer();
		analyzer.analyze(sentence);	
		System.out.println(analyzer.extractTerms(sentence));	
	}
	
}
