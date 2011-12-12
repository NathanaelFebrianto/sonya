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
	
	private static final String[] PREDICATE_IN = { 
		"動詞-自立", 
		"形容詞-自立", 
		"形容詞-非自立",
		"名詞-形容動詞語幹",
		};
	private static final String[] PREDICATE_OUT = { 
		"動詞-非自立",
		"体言接続特殊",	// conjugational type
		};
	private static final String[] SUBJECT_IN = { 
		"未知語", 
		"名詞-一般", 
		"名詞-サ変接続", 
		"名詞-固有名詞-地域-一般", 
		"名詞-固有名詞-地域-国", 
		"名詞-ナイ形容詞語幹", 
		"名詞-代名詞-一般",
		"記号-アルファベット",
		//"名詞-数",
		};
	private static final String[] SUBJECT_OUT = { 
		"動詞-非自立", 
		};
	private static final String[] JOSA_IN = { 
		"助詞-格助詞-一般", 
		//"助詞-接続助詞", 
		};
	private static final String[] JOSA_OUT = { 
		"助詞-終助詞", 
		};
	private static final String[] JODONGSA_IN = { 
		"助動詞",
		};
	private static final String[] JODONGSA_OUT = {  };
	private static final String[] GUJUM = { 
		"記号-句点",
		"記号-読点",
		"助詞-終助詞",
		"助詞-接続助詞",
		};
	private static final String[] NEGATION_TYPE = {
		"不変化型",
		"特殊・ナイ",
	};
	
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
		
		List<JapaneseToken> waitingTokens = new ArrayList<JapaneseToken>();
		SemanticClause clause = null;
		JapaneseToken prevToken = null;
		for (int i = 0; i < sentence.size(); i++) {		
			JapaneseToken token = (JapaneseToken) sentence.get(i);
			
			if (token.containsTagOf(PREDICATE_IN) && !token.containsTagOf(PREDICATE_OUT)) {	
				waitingTokens.add(token);
			}			
			if (token.containsTagOf(SUBJECT_IN) && !token.containsTagOf(SUBJECT_OUT)) {
				waitingTokens.add(token);
			}
			
			if (token.containsTagOf(JOSA_IN) && !token.containsTagOf(JOSA_OUT)) {
				if (waitingTokens.size() == 0) {
					if (clause != null && token.containsTagOf(NEGATION_TYPE)) {
						System.out.println("#analyze(1-1) = ConjugationalType : " + token.getConjugationalType());		
						clause.setPredicate("NOT" + clause.getPredicate());	
						String standardTerm = synonymEngine.getStandardWord(clause.getPredicate());
						clause.setStandardPredicate("NOT" + standardTerm);
					}
				}
				else if (waitingTokens.size() > 0) {
					System.out.println("#analyze(1-2)");
					clause = this.updateSemanticClause(clause, waitingTokens, prevToken, token);
					
					if (clause != null)
						semanticSentence.add(clause);
				}
			}
			else if (token.containsTagOf(JODONGSA_IN) && !token.containsTagOf(JODONGSA_OUT)) {
				if (waitingTokens.size() == 0) {
					if (clause != null && token.containsTagOf(NEGATION_TYPE)) {
						System.out.println("#analyze(1-3) = ConjugationalType : " + token.getConjugationalType());		
						clause.setPredicate("NOT" + clause.getPredicate());	
						String standardTerm = synonymEngine.getStandardWord(clause.getPredicate());
						clause.setStandardPredicate("NOT" + standardTerm);
					}
				}
				else if (waitingTokens.size() > 0) {
					System.out.println("#analyze(1-4)");
					clause = this.updateSemanticClause(clause, waitingTokens, prevToken, token);
					
					if (clause != null)
						semanticSentence.add(clause);
				}
			}
			else if (token.containsTagOf(GUJUM)) {
				System.out.println("#analyze(1-5)");
				clause = this.updateSemanticClause(clause, waitingTokens, prevToken, token);
				
				if (clause != null)
					semanticSentence.add(clause);
				
				// initialize clause
				clause = null;
			}
			
			prevToken = token;
		}	
		
		if (waitingTokens.size() > 0) {	
			System.out.println("#analyze(1-6)");
			SemanticClause etcClause = createLeftoverSemanticClause(waitingTokens);
			semanticSentence.add(etcClause);
		}
		
		semanticSentence = this.applyPriorities(semanticSentence);
		
		return semanticSentence;
	}
	
	private SemanticClause updateSemanticClause(SemanticClause clause, List<JapaneseToken> waitingTokens, 
			JapaneseToken prevToken, JapaneseToken currentToken) {
		for (int j = waitingTokens.size() - 1; j >= 0; j--) {
			JapaneseToken waitingToken = (JapaneseToken) waitingTokens.get(j);
			String waitingTerm = waitingToken.getTerm();
			String standardTerm = synonymEngine.getStandardWord(waitingToken.getTerm());

			if (waitingToken.containsTagOf(PREDICATE_IN)) {
				System.out.println("#updateSemanticClause(1) = " + waitingTerm);							
				
				// create new clause
				clause = new SemanticClause();							
				
				if (currentToken.containsTagOf(NEGATION_TYPE)) {
					waitingTerm = "否定" + waitingTerm;
					standardTerm = "否定" + standardTerm;
				}
				clause.setPredicate(waitingTerm);
				clause.setStandardPredicate(standardTerm);
				
				waitingTokens.remove(j);								
			}
			else if (waitingToken.containsTagOf(SUBJECT_IN)) {
				if (clause != null && clause.getSubject() == null) {
					System.out.println("#updateSemanticClause(2-1) = " + waitingTerm);
					clause.setSubject(waitingTerm);
					clause.setStandardSubject(standardTerm);
					waitingTokens.remove(j);
				}
				else if (clause != null && clause.getSubject() != null) {
					System.out.println("#updateSemanticClause(2-2) = " + waitingTerm);
					clause.addAttribute(waitingTerm);
					clause.addStandardAttribute(standardTerm);
					waitingTokens.remove(j);
				}
				else if (clause == null) {
					if (currentToken.containsTagOf(JODONGSA_IN) && !currentToken.containsTagOf(JODONGSA_OUT)) {
						if (j == waitingTokens.size() - 1) {
							if (prevToken != null & waitingTerm.equals(prevToken.getTerm())) {
								System.out.println("#updateSemanticClause(2-3) = " + waitingTerm);
								
								// create new clause
								clause = new SemanticClause();	
								
								clause.setPredicate(waitingTerm);
								clause.setStandardPredicate(standardTerm);
								waitingTokens.remove(j);
							}
						}							
					}
				}
			}
		}
		
		return clause;		
	}
	
	private SemanticClause createLeftoverSemanticClause(List<JapaneseToken> waitingTokens) {
		SemanticClause clause = new SemanticClause();
		for (int j = waitingTokens.size() - 1; j >= 0; j--) {
			JapaneseToken waitingToken = (JapaneseToken) waitingTokens.get(j);
			String prevTerm = waitingToken.getTerm();
			String standardTerm = synonymEngine.getStandardWord(waitingToken.getTerm());
			
			if (waitingToken.containsTagOf(PREDICATE_IN)) {	
				System.out.println("#createLeftoverSemanticClause(1) = " + prevTerm);
				clause.setPredicate(prevTerm);
				clause.setStandardPredicate(standardTerm);
				waitingTokens.remove(j);								
			}
			else if (j == 0 && waitingToken.containsTagOf(SUBJECT_IN)) {
				System.out.println("#createLeftoverSemanticClause(2) = " + prevTerm);
				clause.setSubject(prevTerm);
				clause.setStandardSubject(standardTerm);				
				waitingTokens.remove(j);
			}
			else if (j > 0 && waitingToken.containsTagOf(SUBJECT_IN)) {
				System.out.println("#createLeftoverSemanticClause(3) = " + prevTerm);
				clause.addAttribute(prevTerm);
				clause.addStandardAttribute(standardTerm);
				waitingTokens.remove(j);								
			}
		}
		
		return clause;
	}
	
	private SemanticSentence applyPriorities(SemanticSentence sentence) {

		int priority = 1;
		for (int i = sentence.size() -1 ; i >= 0; i--) {
			SemanticClause clause = (SemanticClause) sentence.get(i);
			clause.setPriority(priority);
			priority++;
		}
		
		return sentence;
	}

	public static void main(String[] args) {		

		String[] texts = {
				"もう眠い",
				"スゴイですね。こんなアプリを待ってました",
				"送信ができません。",
				"アップデートしたら絵文字使えんくなったマジありえん",
				"横浜に住んている2人このママです。 韓国語でメールともしたいひと() 気軽に話かけてね。",
				"雑音がひどいし、時差みたいなものが生じてまともな通話が出来ない。 改善してくれ。",
				"このline2アプリ大好きです",
				
				"埼玉の高２♂です！！バドやってましゅ✨ 誰か友達かそれ以上(笑)☀になってください。！！ ID:ebisyuです！！",
				"北堀江に住んでる26のデザイナーやってまーす！年、地域気にしないんで仲良くしたってな！よろしくです！！ ID isku4",
				"さっさと削除しろよ次々と。何が仲良くしましょうねだ、バカかいい年こいて。 優良アプリなんだから、こんな事でケチが付いて欲しくないですね",
				"やばくないですか？ 会話にならんし、雑音やばい、  じぶんだけなんですかね？",
		};
		
		for (int i = 0; i < texts.length; i++) {
			JapaneseSemanticAnalyzer analyzer = new JapaneseSemanticAnalyzer();
			SemanticSentence semanticSentence = analyzer.analyze(texts[i]);
			semanticSentence.sort(true);
			
			System.out.println("-------------------------------------");
			for (SemanticClause clause : semanticSentence) {
				System.out.println(clause.toString());
				System.out.println("-------------------------------------");
			}			
		}

	}

}
