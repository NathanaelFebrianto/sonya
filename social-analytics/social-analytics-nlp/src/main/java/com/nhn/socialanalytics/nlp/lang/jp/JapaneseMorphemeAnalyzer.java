package com.nhn.socialanalytics.nlp.lang.jp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.java.sen.SenFactory;
import net.java.sen.StringTagger;
import net.java.sen.dictionary.Token;

import com.nhn.socialanalytics.nlp.morpheme.MorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.morpheme.Sentence;

public class JapaneseMorphemeAnalyzer implements MorphemeAnalyzer {
	
	private static JapaneseMorphemeAnalyzer instance = null;
	private StringTagger tagger;
	
	public JapaneseMorphemeAnalyzer() {
		tagger = SenFactory.getStringTagger("");
	}
	
	public static JapaneseMorphemeAnalyzer getInstance() {
		if (instance == null)
			instance = new JapaneseMorphemeAnalyzer();
		return instance;
	}

	public Sentence analyze(String text) {
		System.out.println("sentence == " + text);	
		
		Sentence sentence = new Sentence(text);
		
		try {
		List<Token> tokens = new ArrayList<Token>();
		tokens = tagger.analyze(text, tokens);

		int index = 0;
		for (Token token : tokens) {
			JapaneseToken jtoken = new JapaneseToken();			
			jtoken.makeObject(index, token);
			
			System.out.println(jtoken.toString());
			
			sentence.add(jtoken);			
			index++;
		}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sentence;
	}
	
	public String extractTerms(String text) {
		return null;
	}
	
	public String extractCoreTerms(String text) {
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		//String text = "もう眠い";
		String text = "上記のように最新のソースでは Listを 参照渡し風にしているようです。";
		
		JapaneseMorphemeAnalyzer analyzer = JapaneseMorphemeAnalyzer.getInstance();
		analyzer.analyze(text);
	}

}
