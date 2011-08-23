package com.nhn.textmining;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Vector;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.kr.KoreanTokenizer;
import org.apache.lucene.analysis.kr.morph.AnalysisOutput;
import org.apache.lucene.analysis.kr.morph.MorphAnalyzer;

public class TextAnalyzer {
	
	public TextAnalyzer() {
		
	}

	public Vector extractTerms(String text) {
		Vector<String> terms = new Vector<String>();
		
		MorphAnalyzer analyzer = new MorphAnalyzer();
		KoreanTokenizer tokenizer = new KoreanTokenizer(new StringReader(text));
		Token token = null;

		try {
			while ((token = tokenizer.next()) != null) {
				//if (!token.type().equals("<KOREAN>"))
				//	continue;

				try {
					analyzer.setExactCompound(false);
					
					List<AnalysisOutput> results = analyzer.analyze(token.toString());

					//System.out.println(token.toString());

					for (AnalysisOutput o : results) {
						
						String mWord = o.toString();						
						
						System.out.println(mWord);
						
						/*
						for (int i = 0; i < o.getCNounList().size(); i++) {
							System.out.println (o.getCNounList().get(i).getWord() + "/");
						}
						*/

						System.out.println("<" + o.getScore() + ">");
						
						if (mWord.indexOf("(N)") >= 0) {
							terms.add(o.getStem());
							System.out.println("noun == " + o.getStem());
						}
						if (mWord.indexOf("(V)") >= 0) {
							terms.add(o.getStem() + "다");
							System.out.println("verb == " + o.getStem() + "다");
						}
						
						System.out.println("\n");				
						
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
	
	public static void main(String[] args) {
		String text = "[연예] '무도' 음원 주간 다운로드 총 800만 건 : MBC TV '무한도전'의 '서해안 고속도로 가요제'가 발표한 음원들이 주간 종합 다운로드 차트 집계에서 총 800만 건을 넘겼고, 그중 지-드래곤.박명수의 '바람났어'가 1위에 올랐습니다. 노래는 안좋아 별로네 못하네";

		TextAnalyzer analyzer = new TextAnalyzer();
		analyzer.extractTerms(text);
	}
}
