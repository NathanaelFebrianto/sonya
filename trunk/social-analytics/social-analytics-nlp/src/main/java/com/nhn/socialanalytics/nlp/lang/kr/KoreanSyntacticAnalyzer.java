package com.nhn.socialanalytics.nlp.lang.kr;

import com.nhn.socialanalytics.nlp.morpheme.Sentence;
import com.nhn.socialanalytics.nlp.syntax.ParseTree;
import com.nhn.socialanalytics.nlp.syntax.Parser;
import com.nhn.socialanalytics.nlp.syntax.SyntacticAnalyzer;

public class KoreanSyntacticAnalyzer implements SyntacticAnalyzer {
	
	private static KoreanSyntacticAnalyzer instance = null;
	KoreanMorphemeAnalyzer analyzer;
	Parser parser;
	
	public KoreanSyntacticAnalyzer() {
		analyzer = KoreanMorphemeAnalyzer.getInstance();
		parser = KoreanSyntacticParser.getInstance();
	}
	
	public static KoreanSyntacticAnalyzer getInstance() {
		if (instance == null)
			instance = new KoreanSyntacticAnalyzer();
		return instance;
	}
	
	public ParseTree analyze(String text) {
		
		Sentence sentence = analyzer.analyze(text);	
		return parser.parse(sentence);		
	}
	
	/*
	public Forest<ParseTreeNode, ParseTreeEdge> parseGraph(String source) {
		ParseTree tree = this.parseTree(source);
		
		GraphModeller graphModeller = new GraphModeller(GraphModeller.DIRECTED_SPARSE_GRAPH);
		Forest<ParseTreeNode, ParseTreeEdge> graph = graphModeller.createGraph(tree.getNodeList(), tree.getEdgeList());
		
		return graph;
	}
	*/
	
	public static void main(String[] args) {		
		//String source = "이 물건은 배송이 빨라서 정말 좋지만, 품질이 별로 안좋네요.";
		String source = "철수가 음악에 재능이 없으면서도 노래를 아주 열심히 부르는 것을 영희가 안다.";
		
		KoreanSyntacticAnalyzer analyzer = KoreanSyntacticAnalyzer.getInstance();
		ParseTree tree = analyzer.analyze(source);
	}
}
