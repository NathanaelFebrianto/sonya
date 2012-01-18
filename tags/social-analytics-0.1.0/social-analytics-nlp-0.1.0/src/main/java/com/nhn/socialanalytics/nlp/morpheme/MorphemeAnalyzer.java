package com.nhn.socialanalytics.nlp.morpheme;

public interface MorphemeAnalyzer {

	public Sentence analyze(String text);
	
	public String extractTerms(String text);
	
	public String extractCoreTerms(String text);
}
