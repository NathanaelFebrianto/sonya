package com.nhn.socialanalytics.nlp.kr.syntax;

import com.nhn.socialanalytics.nlp.kr.morpheme.MorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.kr.morpheme.Sentence;

public class SyntacticAnalyzer {
	
	private static SyntacticAnalyzer instance = null;
	MorphemeAnalyzer analyzer;
	Parser parser;
	
	public SyntacticAnalyzer() {
		analyzer = new MorphemeAnalyzer();
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
		//String source = "�� ������ ����� ���� ���� ������, ǰ���� ���� �� ���׿�.";
		String source = "ö���� ���ǿ� ����� �����鼭�� �뷡�� ���� ������ �θ��� ���� ���� �ȴ�.";
		
		SyntacticAnalyzer analyzer = new SyntacticAnalyzer();
		ParseTree tree = analyzer.parseTree(source);
	}
}
