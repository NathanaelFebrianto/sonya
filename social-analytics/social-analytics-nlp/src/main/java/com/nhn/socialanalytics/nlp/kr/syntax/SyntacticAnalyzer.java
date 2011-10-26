package com.nhn.socialanalytics.nlp.kr.syntax;

import com.nhn.socialanalytics.nlp.kr.morpheme.MorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.kr.morpheme.Sentence;

public class SyntacticAnalyzer {
	
	private static SyntacticAnalyzer instance = null;
	MorphemeAnalyzer analyzer;
	Parser parser;
	
	public SyntacticAnalyzer() {
		analyzer = MorphemeAnalyzer.getInstance();
		parser = Parser.getInstance();
	}
	
	public static SyntacticAnalyzer getInstance() {
		if (instance == null)
			instance = new SyntacticAnalyzer();
		return instance;
	}
	
	public ParseTree parseTree(String source) {
		
		Sentence sentence = analyzer.extractMorphemes(source);	
		return parser.parse(sentence);		
	}
	
	public static void main(String[] args) {		
		//String source = "이 물건은 배송이 빨라서 정말 좋지만, 품질이 별로 안 좋네요.";
		String source = "철수가 음악에 재능이 없으면서도 노래를 아주 열심히 부르는 것을 영희가 안다.";
		
		SyntacticAnalyzer analyzer = SyntacticAnalyzer.getInstance();
		ParseTree tree = analyzer.parseTree(source);
	}
}
