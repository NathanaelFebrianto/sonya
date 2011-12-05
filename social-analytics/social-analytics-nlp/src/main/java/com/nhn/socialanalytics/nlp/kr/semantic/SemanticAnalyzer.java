package com.nhn.socialanalytics.nlp.kr.semantic;

import java.util.List;

import com.nhn.socialanalytics.nlp.kr.dictionary.SynonymEngine;
import com.nhn.socialanalytics.nlp.kr.morpheme.Eojeol;
import com.nhn.socialanalytics.nlp.kr.syntax.ParseTree;
import com.nhn.socialanalytics.nlp.kr.syntax.ParseTreeEdge;
import com.nhn.socialanalytics.nlp.kr.syntax.ParseTreeNode;
import com.nhn.socialanalytics.nlp.kr.syntax.SyntacticAnalyzer;

public class SemanticAnalyzer {
	
	private static SemanticAnalyzer instance = null;
	private SyntacticAnalyzer syntacticAnalyzer;
	private SynonymEngine synonymEngine;
	
	public SemanticAnalyzer() {
		syntacticAnalyzer = SyntacticAnalyzer.getInstance();
		synonymEngine = SynonymEngine.getInstance();
	}
	
	public static SemanticAnalyzer getInstance() {
		if (instance == null)
			instance = new SemanticAnalyzer();
		return instance;
	}
	
	public SemanticSentence createSemanticSentence(String text) {
		SemanticSentence sentence = new SemanticSentence(1, text);
		
		ParseTree tree = syntacticAnalyzer.parseTree(text);		
		ParseTreeNode root = tree.getRoot();
		List<ParseTreeEdge> childEdges = root.getChildEdges();
		
		
		if (childEdges != null) {	
			for (ParseTreeEdge edge : childEdges) {				
				int toId = edge.getToId();			
				ParseTreeNode childNode = tree.findNode(toId);
				
				System.out.println("to id == " + toId);
				System.out.println("chile node == " + childNode.getEojeol().toString());
				
				Eojeol eojeol = childNode.getEojeol();
				char pos = eojeol.getPos();
				String josaTag = eojeol.getJosaTag();
				//String eomiTag = eojeol.getEomiTag();
				String term = eojeol.getTerm();				
				String standardTerm = synonymEngine.getStandardWord(term);
				
				SemanticClause clause = new SemanticClause();
				
				int priority = 1;
				if (pos == 'V') {
					clause.setPredicate(term+"다");
					clause.setStandardPredicate(standardTerm+"다");
					clause.setPriority(priority);
				}
				else if (pos == 'N' && ("JX".equals(josaTag) || "JKS".equals(josaTag))) {
					clause.setSubject(term);
					clause.setStandardSubject(standardTerm);
				}
				
				sentence.add(clause);	
				priority++;
				exploreSemanticClause(tree, sentence, clause, childNode, priority, 1, 1);
			}
		}
		
		return sentence;
	}
	
	private SemanticSentence exploreSemanticClause(ParseTree tree, SemanticSentence sentence, 
			SemanticClause prevClause, ParseTreeNode node, int priority, int prevVerbDepth, int prevDepth) {		

		List<ParseTreeEdge> childEdges = node.getChildEdges();
		
		if (childEdges != null) {
			for (ParseTreeEdge edge : childEdges) {
				int toId = edge.getToId();			
				ParseTreeNode childNode = tree.findNode(toId);	
				int currentDepth  = prevDepth + 1;

				System.out.println("to node id == " + toId);
				System.out.println("edge priority == " + edge.getPriority());
				System.out.println("edge distance == " + edge.getDistance());
				System.out.println("chile node == " + childNode.getEojeol().toString());
				
				Eojeol eojeol = childNode.getEojeol();
				char pos = eojeol.getPos();
				String josaTag = eojeol.getJosaTag();
				//String eomiTag = eojeol.getEomiTag();
				String term = eojeol.getTerm();
				String standardTerm = synonymEngine.getStandardWord(term);
				
				SemanticClause clause = null;
				
				if (pos == 'V') {
					clause = new SemanticClause();
					clause.setPredicate(term+"다");
					clause.setStandardPredicate(standardTerm+"다");
					clause.setPriority(priority);
					prevClause.addChild(clause);	
					
					prevVerbDepth = currentDepth;
				}
				else if (pos == 'N' && ("JX".equals(josaTag) || "JKS".equals(josaTag))) {
					char[] tags = sentence.checkSemanticClause(prevClause.getSubject(), prevClause.getPredicate());
					if (tags[0] == '1' && tags[1] == '1') {							
						if (prevDepth == prevVerbDepth) {
							clause = prevClause.clone();
							clause.setSubject(term);
							clause.setStandardSubject(standardTerm);
							prevClause.getParentClause().addChild(clause);
						}						
						else {
							clause = prevClause;
							clause.addAttribute(term);
							clause.addStandardAttribute(standardTerm);
						}
					} else {
						clause = prevClause;
						clause.setSubject(term);
						clause.setStandardSubject(standardTerm);
					}
				}
				else if (pos == 'N' && ("JKO".equals(josaTag) || "JKM".equals(josaTag))) {
					clause = prevClause;
					clause.addAttribute(term);
					clause.addStandardAttribute(standardTerm);
				}
				else if (pos == 'Z') {
					clause = prevClause;
					clause.addModifier(term);
					clause.addStandardModifier(standardTerm);
				}
				else if (pos == 'N' && ("JKC".equals(josaTag) || "JKG".equals(josaTag))) {
					clause = prevClause;
					clause.addModifier(term);
					clause.addStandardModifier(standardTerm);
				}
				else if (pos == 'N' && josaTag == null) {
					clause = prevClause;
					clause.addAttribute(term);
					clause.addStandardAttribute(standardTerm);
				}
				
				if (clause != null) {
					sentence.add(clause);	
				} else {
					clause = prevClause;
				}
				
				priority++;
				exploreSemanticClause(tree, sentence, clause, childNode, priority, prevVerbDepth, currentDepth);
			}			
		}
		
		return sentence;
	}
	
	public static void main(String[] args) {		
		String source = "이 피씨는 배송이 빨라서 좋다.";
		//String source = "철수가 음악에 재능이 없으면서도 노래를 아주 열심히 부르는 것을 영희가 안다.";
		//String source = "외장메모리로 옮길 수 있도록 업데이트 좀 해주세요. 어플 용량 2.5MB 어플이 내장메모리 설치라 안드로이드폰에선 좀 버겁네요. 라인 한 줄 넣는거 어렵지 않잖아요 개발자님. 빠른 업데이트 바랍니다. 참고로 EVO 4G+ 사용중입니다";
		//String source = "제발 기본 기능인 로그인 좀 잘 되게 해주세요";
		//String source = "카톡은 푸시가 안되지만 네이버톡은 되네";
				
		SemanticAnalyzer analyzer = new SemanticAnalyzer();
		SemanticSentence ss = analyzer.createSemanticSentence(source);
		ss.sort(true);
		
		System.out.println("-------------------------------------");
		for (SemanticClause clause : ss) {
			System.out.println(clause.toString());
			System.out.println("-------------------------------------");
		}
	}

}
