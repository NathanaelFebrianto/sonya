package com.nhn.socialanalytics.nlp.lang.ja;

import java.util.ArrayList;
import java.util.List;

import com.nhn.socialanalytics.nlp.dictionary.SynonymEngine;
import com.nhn.socialanalytics.nlp.morpheme.Sentence;
import com.nhn.socialanalytics.nlp.semantic.SemanticAnalyzer;
import com.nhn.socialanalytics.nlp.semantic.SemanticClause;
import com.nhn.socialanalytics.nlp.semantic.SemanticSentence;

public class JapaneseSemanticAnalyzer implements SemanticAnalyzer {
	
	private static JapaneseSemanticAnalyzer instance = null;
	private JapaneseMorphemeAnalyzer morphAnalyzer;
	private SynonymEngine synonymEngine;
	
	public JapaneseSemanticAnalyzer() {
		morphAnalyzer = JapaneseMorphemeAnalyzer.getInstance();
		synonymEngine = SynonymEngine.getInstance("com/nhn/socialanalytics/nlp/lang/ja/dic/synonym_ja.dic");
	}
	
	public static JapaneseSemanticAnalyzer getInstance() {
		if (instance == null)
			instance = new JapaneseSemanticAnalyzer();
		return instance;
	}
	
	public SemanticSentence analyze(String text) {
		SemanticSentence semanticSentence = new SemanticSentence(1, text);
		
		Sentence sentence = morphAnalyzer.analyze(text);
		
		List<JapaneseToken> prevTokens = new ArrayList<JapaneseToken>();
		SemanticClause prevClause = new SemanticClause();
		for (int i = 0; i < sentence.size(); i++) {
			JapaneseToken token = (JapaneseToken) sentence.get(i);
			
			SemanticClause clause = null;
			String[] inPredicate = { "動詞-自立", "形容詞" };
			String[] outPredicate = { "動詞-非自立",  };
			if (token.containsTagOf(inPredicate)) { // && !token.containsTagOf(outPredicate)) {
				clause = new SemanticClause();
				clause.setPredicate(token.getTerm());
			}
			
			String[] inSubject = { "未知語", "名詞"  };
			String[] outSubject = { "名詞-非自立",  };
			if (token.containsTagOf(inSubject)) {// && !token.containsTagOf(outSubject)) {
				prevTokens.add(token);
			}
			
			String[] inJosa = { "助動詞", "助詞-終助詞" };
			String[] outJosa = {  };
			if (token.containsTagOf(inJosa)) {// && !token.containsTagOf(outJosa)) {
				if (prevTokens.size() > 0) {
					
					for (int j = prevTokens.size() - 1; j >= 0; j--) {
						JapaneseToken prevToken = (JapaneseToken) prevTokens.get(j);
						String prevTerm = prevToken.getTerm();
						
						if (j == 0 && prevToken.containsTagOf(inSubject)) {
							prevClause.setSubject(prevTerm);
							prevTokens.remove(j);
						}
						else if (j > 0 && prevToken.containsTagOf(inSubject)) {
							prevClause.addAttribute(prevTerm);
							prevTokens.remove(j);
						}
					}
				}			
			}
			else {
				if (prevTokens.size() > 0) {
					
					for (int j = prevTokens.size() - 1; j >= 0; j--) {
						JapaneseToken prevToken = (JapaneseToken) prevTokens.get(j);
						String prevTerm = prevToken.getTerm();						
						prevClause.addAttribute(prevTerm);
					}
				}							
			}
			
			if (clause != null) {
				semanticSentence.add(clause);					
				prevClause = clause;
			} else {
				prevClause = new SemanticClause();
			}			
		}	
		
		return semanticSentence;
	}
	
	private SemanticSentence exploreSemanticClause(SemanticSentence sentence, SemanticClause prevClause, 
			List<JapaneseToken> prevTokens, JapaneseToken token, boolean isLast) {		
		
		//char pos = token.getPos();
		//String partOfSpeech = token.getPartOfSpeech();
		//String conjugationalForm = token.getConjugationalForm();
		//String conjugationalType = token.getConjugationalType();		
		
		SemanticClause clause = null;
		String[] inPredicate = { "動詞-自立", "形容詞" };
		String[] outPredicate = { "動詞-非自立",  };
		if (token.containsTagOf(inPredicate) && !token.containsTagOf(outPredicate)) {
			clause = new SemanticClause();
			clause.setPredicate(token.getTerm());
		}
		
		String[] inSubject = { "未知語", "名詞"  };
		String[] outSubject = { "名詞-非自立",  };
		if (token.containsTagOf(inSubject) && !token.containsTagOf(outSubject)) {
			prevTokens.add(token);
		}
		
		String[] inJosa = { "助動詞", "助詞-終助詞" };
		String[] outJosa = {  };
		if (token.containsTagOf(inJosa) && !token.containsTagOf(outJosa)) {
			if (prevTokens.size() > 0) {
				
				for (int i = prevTokens.size() - 1; i <= 0; i--) {
					JapaneseToken prevToken = (JapaneseToken) prevTokens.get(i);
					String prevTerm = prevToken.getTerm();
					
					if (i == 0 && prevToken.containsTagOf(inSubject)) {
						prevClause.setSubject(prevTerm);
						prevTokens.remove(i);
					}
					else if (i > 0 && prevToken.containsTagOf(inSubject)) {
						prevClause.addAttribute(prevTerm);
						prevTokens.remove(i);
					}
				}
			}			
		}
		
		if (clause != null) {
			sentence.add(clause);	
		} else {
			clause = prevClause;
		}
		
		if (!isLast && token != null)
			exploreSemanticClause(sentence, clause, prevTokens, token, isLast);
		
		return sentence;
	}
	
	public static void main(String[] args) {		
		String text = "スゴイですね。こんなアプリを待ってました";
				
		JapaneseSemanticAnalyzer analyzer = new JapaneseSemanticAnalyzer();
		SemanticSentence ss = analyzer.analyze(text);
		ss.sort(true);
		
		System.out.println("-------------------------------------");
		for (SemanticClause clause : ss) {
			System.out.println(clause.toString());
			System.out.println("-------------------------------------");
		}
	}

}
