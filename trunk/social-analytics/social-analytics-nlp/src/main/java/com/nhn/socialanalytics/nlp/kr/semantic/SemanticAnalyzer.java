package com.nhn.socialanalytics.nlp.kr.semantic;

import java.util.List;

import com.nhn.socialanalytics.nlp.kr.syntax.ParseTree;
import com.nhn.socialanalytics.nlp.kr.syntax.ParseTreeEdge;
import com.nhn.socialanalytics.nlp.kr.syntax.ParseTreeNode;
import com.nhn.socialanalytics.nlp.kr.syntax.SyntacticAnalyzer;

public class SemanticAnalyzer {
	
	private SyntacticAnalyzer syntacticAnalyzer;
	
	public SemanticAnalyzer() {
		syntacticAnalyzer = SyntacticAnalyzer.getInstance();
	}
	
	public void createSemanticClause(String text) {
		ParseTree tree = syntacticAnalyzer.parseTree(text);
		
		ParseTreeNode root = tree.getRoot();
		List<ParseTreeEdge> childEdges = root.getChildEdges();
		
		SemanticSentence sentence = new SemanticSentence(1, text);
		
		for (ParseTreeEdge edge : childEdges) {
			int toId = edge.getToId();
			System.out.println("to id == " + toId);
			ParseTreeNode childNode = tree.findNode(toId);
			System.out.println("chile node == " + childNode.getEojeol().toString());
			
		}	
	}
	
	public static void main(String[] args) {		
		//String source = "이 물건은 배송이 빨라서 정말 좋지만, 품질이 별로 안 좋네요.";
		String source = "철수가 음악에 재능이 없으면서도 노래를 아주 열심히 부르는 것을 영희가 안다.";
		
		SemanticAnalyzer analyzer = new SemanticAnalyzer();
		analyzer.createSemanticClause(source);
	}

}
