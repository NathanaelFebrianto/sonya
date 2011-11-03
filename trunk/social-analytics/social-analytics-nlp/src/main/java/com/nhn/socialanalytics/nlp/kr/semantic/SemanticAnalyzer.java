package com.nhn.socialanalytics.nlp.kr.semantic;

import java.util.ArrayList;
import java.util.List;

import com.nhn.socialanalytics.nlp.kr.morpheme.Eojeol;
import com.nhn.socialanalytics.nlp.kr.syntax.ParseTree;
import com.nhn.socialanalytics.nlp.kr.syntax.ParseTreeEdge;
import com.nhn.socialanalytics.nlp.kr.syntax.ParseTreeNode;
import com.nhn.socialanalytics.nlp.kr.syntax.SyntacticAnalyzer;

public class SemanticAnalyzer {
	
	private SyntacticAnalyzer syntacticAnalyzer;
	
	public SemanticAnalyzer() {
		syntacticAnalyzer = SyntacticAnalyzer.getInstance();
	}
	
	public SemanticSentence createSemanticClause(String text) {
		SemanticSentence sentence = new SemanticSentence(1, text);
		
		ParseTree tree = syntacticAnalyzer.parseTree(text);		
		ParseTreeNode root = tree.getRoot();
		List<ParseTreeEdge> childEdges = root.getChildEdges();
		
		for (ParseTreeEdge edge : childEdges) {
			int toId = edge.getToId();			
			ParseTreeNode childNode = tree.findNode(toId);
			
			System.out.println("to id == " + toId);
			System.out.println("chile node == " + childNode.getEojeol().toString());
			
			Eojeol eojeol = childNode.getEojeol();
			char pos = eojeol.getPos();
			String josaTag = eojeol.getJosaTag();
			String eomiTag = eojeol.getEomiTag();
			String term = eojeol.getTerm();
			
			SemanticClause clause = new SemanticClause();
			
			List<SemanticClause> incompletedClauses = new ArrayList<SemanticClause>();
			if (pos == 'V') {
				clause.setPredicate(term+"다");	
			}
			else if (pos == 'N' && ("JX".equals(josaTag) || "JKS".equals(josaTag))) {
				clause.setSubject(term);				
			}
			
			sentence.add(clause);			
			exploreSemanticClause(tree, sentence, clause, childNode);
		}
		
		return sentence;
	}
	
	public SemanticSentence exploreSemanticClause(ParseTree tree, SemanticSentence sentence, 
			SemanticClause prevClause, ParseTreeNode node) {
		
		List<ParseTreeEdge> childEdges = node.getChildEdges();
		
		if (childEdges != null) {
			for (ParseTreeEdge edge : childEdges) {
				int toId = edge.getToId();			
				ParseTreeNode childNode = tree.findNode(toId);
				
				System.out.println("to id == " + toId);
				System.out.println("chile node == " + childNode.getEojeol().toString());
				
				Eojeol eojeol = childNode.getEojeol();
				char pos = eojeol.getPos();
				String josaTag = eojeol.getJosaTag();
				String eomiTag = eojeol.getEomiTag();
				String term = eojeol.getTerm();
				
				SemanticClause clause = null;
				
				if (pos == 'V') {
					clause = new SemanticClause();
					clause.setPredicate(term+"다");	
					prevClause.addChild(clause);
				}
				else if (pos == 'N' && ("JX".equals(josaTag) || "JKS".equals(josaTag))) {
					char[] tags = sentence.checkSemanticClause(prevClause.getSubject(), prevClause.getPredicate());
					if (tags[0] == '1' && tags[1] == '1') {
						//clause = prevClause.clone();
						//clause.setSubject(term);
						clause = prevClause;
						clause.addObject(term);
					} else {
						clause = prevClause;
						clause.setSubject(term);
					}	
				}
				else if (pos == 'N' && ("JKO".equals(josaTag) || "JKM".equals(josaTag))) {
					clause = prevClause;
					clause.addObject(term);
				}
				else if (pos == 'Z') {
					clause = prevClause;
					clause.addModifier(term);
				}
				else if (pos == 'N' && ("JKC".equals(josaTag) || "JKG".equals(josaTag))) {
					clause = prevClause;
					clause.addModifier(term);
				}
				else if (pos == 'N' && josaTag == null) {
					clause = prevClause;
					clause.addObject(term);					
				}
				
				if (clause != null) {
					sentence.add(clause);	
				} else {
					clause = prevClause;
				}
								
				exploreSemanticClause(tree, sentence, clause, childNode);
			}			
		}
		
		return sentence;
	}
	
	public static void main(String[] args) {		
		//String source = "이 물건은 배송이 빨라서 정말 좋지만, 품질이 별로  안좋네요.";
		//String source = "철수가 음악에 재능이 없으면서도 노래를 아주 열심히 부르는 것을 영희가 안다.";
		String source = "외장메모리로 옮길 수 있도록 업데이트 좀 해주세요. 어플 용량 2.5MB 어플이 내장메모리 설치라 안드로이드폰에선 좀 버겁네요. 라인 한 줄 넣는거 어렵지 않잖아요 개발자님. 빠른 업데이트 바랍니다. 참고로 EVO 4G+ 사용중입니다";
		//String source = "제발 기본 기능인 로그인 좀 잘 되게 해주세요";
				
		SemanticAnalyzer analyzer = new SemanticAnalyzer();
		SemanticSentence ss = analyzer.createSemanticClause(source);
		
		System.out.println("-------------------------------------");
		for (SemanticClause clause : ss) {
			System.out.println(clause.toString());
			System.out.println("-------------------------------------");
		}
	}

}
