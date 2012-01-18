package com.nhn.socialanalytics.nlp.lang.ja;

import java.util.ArrayList;
import java.util.List;

import net.java.sen.SenFactory;
import net.java.sen.StringTagger;
import net.java.sen.dictionary.Token;

public class JapaneseMorphemeTest {
	
	public static void main(String[] args) throws Exception {
		//String text = "もう眠い";
		String text = "上記のように最新のソースではListを参照渡し風にしているようです。";
		
		StringTagger tagger = SenFactory.getStringTagger("");
		
		List<Token> tokens = new ArrayList<Token>();
		tokens = tagger.analyze(text, tokens);

		for (Token token : tokens) {
			System.out.println("=====");
			System.out.println("surface : " + token.getSurface());
			System.out.println("cost : " + token.getCost());
			System.out.println("length : " + token.getLength());
			System.out.println("start : " + token.getStart());
			System.out.println("additionalInformation : "
					+ token.getMorpheme().getAdditionalInformation());
			System.out.println("basicForm : "
					+ token.getMorpheme().getBasicForm());
			System.out.println("conjugationalForm : "
					+ token.getMorpheme().getConjugationalForm());
			System.out.println("conjugationalType : "
					+ token.getMorpheme().getConjugationalType());
			System.out.println("partOfSpeech : "
					+ token.getMorpheme().getPartOfSpeech());
			System.out.println("pronunciations : "
					+ token.getMorpheme().getPronunciations());
			System.out.println("readings : "
					+ token.getMorpheme().getReadings());
		}
	}
}
