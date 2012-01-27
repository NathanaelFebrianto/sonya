package com.nhn.socialanalytics.nlp.lang.ko;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.nhn.socialanalytics.nlp.dictionary.SynonymFilter;
import com.nhn.socialanalytics.nlp.semantic.SemanticAnalyzer;
import com.nhn.socialanalytics.nlp.semantic.SemanticClause;
import com.nhn.socialanalytics.nlp.semantic.SemanticSentence;
import com.nhn.socialanalytics.nlp.syntax.ParseTree;
import com.nhn.socialanalytics.nlp.syntax.ParseTreeEdge;
import com.nhn.socialanalytics.nlp.syntax.ParseTreeNode;

public class KoreanSemanticAnalyzer implements SemanticAnalyzer {
	
	private static KoreanSemanticAnalyzer instance = null;
	private KoreanSyntacticAnalyzer syntacticAnalyzer;
	private SynonymFilter synonymFilter;
	
	public KoreanSemanticAnalyzer() {
		syntacticAnalyzer = KoreanSyntacticAnalyzer.getInstance();
		synonymFilter = SynonymFilter.getInstance("com/nhn/socialanalytics/nlp/lang/ko/dic/synonym_ko.dic");
	}
	
	public static KoreanSemanticAnalyzer getInstance() {
		if (instance == null)
			instance = new KoreanSemanticAnalyzer();
		return instance;
	}
	
	public SemanticSentence analyze(String text) {
		SemanticSentence sentence = new SemanticSentence(1, text);
		
		ParseTree tree = syntacticAnalyzer.analyze(text);		
		ParseTreeNode root = tree.getRoot();
		List<ParseTreeEdge> childEdges = root.getChildEdges();
		
		
		if (childEdges != null) {	
			for (ParseTreeEdge edge : childEdges) {				
				int toId = edge.getToId();			
				ParseTreeNode childNode = tree.findNode(toId);
				
				System.out.println("to id == " + toId);
				System.out.println("chile node == " + childNode.getToken().toString());
				
				Eojeol eojeol = (Eojeol) childNode.getToken();
				char pos = eojeol.getPos();
				String josaTag = eojeol.getJosaTag();
				String eomiTag = eojeol.getEomiTag();
				String term = eojeol.getTerm();				
				String standardTerm = synonymFilter.getStandardWord(term);
				
				SemanticClause clause = new SemanticClause();
				
				int priority = 1;
				if (pos == 'V'|| 
						// "pos == 'N'..." condition is added by Younggue 2012-01-11
						(pos == 'N' && ("EFN".equals(eomiTag) || "EFQ".equals(eomiTag) || 
								"EFO".equals(eomiTag) || "EFA".equals(eomiTag) || "EFI".equals(eomiTag)))) {
					if (pos == 'V') {
						term = term+"다";
						standardTerm = standardTerm+"다";
					}
					
					clause.setPredicate(term);
					clause.setStandardPredicate(standardTerm);
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
				System.out.println("chile node == " + childNode.getToken().toString());
				
				Eojeol eojeol = (Eojeol) childNode.getToken();
				char pos = eojeol.getPos();
				String josaTag = eojeol.getJosaTag();
				String eomiTag = eojeol.getEomiTag();
				String term = eojeol.getTerm();
				String standardTerm = synonymFilter.getStandardWord(term);
				
				SemanticClause clause = null;
				
				if (pos == 'V' || 
						// "pos == 'N'..." condition is added by Younggue 2012-01-11
						(pos == 'N' && ("EFN".equals(eomiTag) || "EFQ".equals(eomiTag) || 
								"EFO".equals(eomiTag) || "EFA".equals(eomiTag) || "EFI".equals(eomiTag)))) {
					clause = new SemanticClause();
					
					if (pos == 'V') {
						term = term+"다";
						standardTerm = standardTerm+"다";
					}
					clause.setPredicate(term);
					clause.setStandardPredicate(standardTerm);
					clause.setPriority(priority);
					prevClause.addChild(clause);	
					
					prevVerbDepth = currentDepth;
				}
				else if (pos == 'N' && ("JX".equals(josaTag) || "JKS".equals(josaTag))) {
					char[] tags = sentence.checkSemanticClause(prevClause.getSubject(), prevClause.getPredicate());
					if (tags[0] == '1' && tags[1] == '1') {							
						if (prevDepth == prevVerbDepth) {
							clause = prevClause.clone();
							
							// clear previous attributes
							clause.setAttributes(new HashSet<String>());
							clause.setStandardAttributes(new HashSet<String>());
							
							clause.setSubject(term);
							clause.setStandardSubject(standardTerm);

							/**
							 * Added by Younggue, 2012-01-26
							 * "카카오톡은(JX or JKS) 품질이(JX or JKS) 별로 안좋다(V)"인 경우 
							 * -> "품질-안좋다", "카카오톡-안좋다"로 분해 되는데, "품질-안좋다"에 속성으로 "카카오톡"을 추가하고
							 * "카카오톡-안좋다"에 속성으로 "품질"이 추가되도록 함.
							 */
							
							if (!prevClause.getSubject().equals(term)) {
								prevClause.addAttribute(term);
								prevClause.addStandardAttribute(standardTerm);
							}
							if (!clause.getSubject().equals(prevClause.getSubject())) {
								clause.addAttribute(prevClause.getSubject());
								clause.addStandardAttribute(prevClause.getStandardSubject());
							}
							
							/** end */
							
							if (prevClause.getParentClause() != null)
								prevClause.getParentClause().addChild(clause);
						}	
						else {
							clause = prevClause;
							clause.addAttribute(term);
							clause.addStandardAttribute(standardTerm);
						}
					} 
					else {
						clause = prevClause;
						clause.setSubject(term);
						clause.setStandardSubject(standardTerm);
					}
					
					/**
					 * Added by Younggue, 2012-01-27
					 * 부모 clause에 서술어만 있고 주제어가 없는 경우, 즉 자식동사(V)-부모동사(V)가 연결되는 문장의 경우,
					 * 자식동사에 연결된 주제어에 의존적인 동사일 가능성이 크므로 자식 clause의 주제어를 부모동사의 주제어로 넣어주고
					 * 즉 자식동사에 연결된 보조사(JX), 주격조사(JKS)를 가진 가장 거리가 먼 명사를 부모동사의 주제어로 넣어 준다.
					 * ex. "네이버라인은(JX) 속도가(JKS) 빨라서(V) 정말 좋다(V)" -> 속도-빠르다, 네이버라인-좋다
					 */
					/*
					SemanticClause parentClause = clause.getParentClause();
					if (parentClause != null) {
						char[] parentTags = sentence.checkSemanticClause(parentClause.getSubject(), parentClause.getPredicate());
						if (parentTags[0] == '0' && parentTags[1] == '1' && isLeafSubjectNode(childNode)) {	
							parentClause.setSubject(term);
							parentClause.setStandardSubject(standardTerm);
							parentClause.getAttributes().remove(term);
							parentClause.getStandardAttributes().remove(standardTerm);
						}
					}
					*/
					/** end */
					
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
	
	private boolean isLeafSubjectNode(ParseTreeNode node) {
		Eojeol eojeol = (Eojeol) node.getToken();
		String josaTag = eojeol.getJosaTag();
		if ("JX".equals(josaTag) || "JKS".equals(josaTag)) {
			if (node.getChildEdges() != null) {
				for (Iterator<ParseTreeEdge> iterChildEdge = node.getChildEdges().iterator(); iterChildEdge.hasNext();) {
					ParseTreeEdge edge = (ParseTreeEdge) iterChildEdge.next();
					ParseTreeNode child = edge.getChildNode();
					Eojeol eojeolChild = (Eojeol) child.getToken();
					String josaTagChild = eojeolChild.getJosaTag();
					if ("JX".equals(josaTagChild) || "JKS".equals(josaTagChild)) {
						return false;
					}
					else {
						return isLeafSubjectNode(child);
					}
				}					
			}
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {		
		String source = "이 피씨는 배송이 빨라서 좋다.";
		//String source = "철수가 음악에 재능이 없으면서도 노래를 아주 열심히 부르는 것을 영희가 안다.";
		//String source = "외장메모리로 옮길 수 있도록 업데이트 좀 해주세요. 어플 용량 2.5MB 어플이 내장메모리 설치라 안드로이드폰에선 좀 버겁네요. 라인 한 줄 넣는거 어렵지 않잖아요 개발자님. 빠른 업데이트 바랍니다. 참고로 EVO 4G+ 사용중입니다";
		//String source = "제발 기본 기능인 로그인 좀 잘 되게 해주세요";
		//String source = "카톡은 푸시가 안되지만 네이버톡은 되네";
				
		KoreanSemanticAnalyzer analyzer = new KoreanSemanticAnalyzer();
		SemanticSentence ss = analyzer.analyze(source);
		ss.sort(true);
		
		System.out.println("-------------------------------------");
		for (SemanticClause clause : ss) {
			System.out.println(clause.toString());
			System.out.println("-------------------------------------");
		}
	}

}
