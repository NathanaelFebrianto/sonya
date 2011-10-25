package com.nhn.socialanalytics.nlp.kr.morpheme;

import java.io.IOException;
import java.io.StringReader;
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

import com.nhn.socialanalytics.nlp.kr.util.DicUtil;

public class MorphemeAnalyzer {

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
	
	private MorphAnalyzer morphAnalyzer;
	
	//private StopAnalyzer stopAnalyzer;
	
	private KoreanAnalyzer koreanAnalyzer;
	
	public MorphemeAnalyzer() {
		stopSet = StopFilter.makeStopSet(Version.LUCENE_CURRENT, STOP_WORDS, true);
		stopSet.addAll(StopFilter.makeStopSet(Version.LUCENE_CURRENT, KOR_STOP_WORDS, true));
		
		morphAnalyzer = new MorphAnalyzer();		
		//stopAnalyzer = new StopAnalyzer(Version.LUCENE_CURRENT, stopSet);
		koreanAnalyzer = new KoreanAnalyzer(Version.LUCENE_CURRENT, stopSet);
		koreanAnalyzer.setBigrammable(false);
		koreanAnalyzer.setHasOrigin(false);
		System.out.println("stop words == " + koreanAnalyzer.getStopwordSet());			
		
		try {
			DicUtil.loadDictionary();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public Sentence extractMorphemes(String text) {
		System.out.println("sentence == " + text);		
		
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
	
	public static void main(String[] args) {		
		//String sentence = "이 물건은 배송이 빨라서 정말 좋지만, 품질이 별로 안 좋네요.";
		String sentence = "철수가 음악에 재능이 없으면서도 노래를 아주 열심히 부르는 것을 영희가 안다.";
		
		MorphemeAnalyzer analyzer = new MorphemeAnalyzer();
		analyzer.extractMorphemes(sentence);	
	}
	
}
