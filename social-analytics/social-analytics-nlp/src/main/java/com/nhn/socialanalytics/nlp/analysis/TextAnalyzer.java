package com.nhn.socialanalytics.nlp.analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nhn.socialanalytics.nlp.feature.FeatureClassifier;
import com.nhn.socialanalytics.nlp.morpheme.MorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.semantic.SemanticAnalyzer;
import com.nhn.socialanalytics.nlp.semantic.SemanticClause;
import com.nhn.socialanalytics.nlp.semantic.SemanticSentence;
import com.nhn.socialanalytics.nlp.sentiment.Polarity;
import com.nhn.socialanalytics.nlp.sentiment.SentimentAnalyzer;

public class TextAnalyzer {

	private Set<Locale> locales = new HashSet<Locale>();
	private Map<Locale, MorphemeAnalyzer> morphemeAnalyzers = new HashMap<Locale, MorphemeAnalyzer>();
	private Map<Locale, SemanticAnalyzer> semanticAnalyzers = new HashMap<Locale, SemanticAnalyzer>();
	private Map<Locale, SentimentAnalyzer> sentimentAnalyzers = new HashMap<Locale, SentimentAnalyzer>();
	private Map<String, Map<Locale, FeatureClassifier>> featureClassifiers = new HashMap<String, Map<Locale, FeatureClassifier>>();

	public TextAnalyzer() {
		
	}
	
	public String extractTerms(Locale locale, String text) {
		String source = text.replaceAll("\t", " ").replaceAll("\n", " ");
		source = convertEmoticonToTag(source);
		
		MorphemeAnalyzer morphemeAnalyzer = getMorphemeAnalyzer(locale);
		String result = morphemeAnalyzer.extractTerms(source);
		
		return result;
	}
	
	public String extractCoreTerms(Locale locale, String text) {
		String source = text.replaceAll("\t", " ").replaceAll("\n", " ");
		source = convertEmoticonToTag(source);
		
		MorphemeAnalyzer morphemeAnalyzer = getMorphemeAnalyzer(locale);
		String result = morphemeAnalyzer.extractCoreTerms(source);
		
		return result;
	}
	
	public SemanticSentence analyzeSemantics(Locale locale, String text) {
		if (locale == null || text == null)
			return null;
		
		text = text.replaceAll("\t", " ").replaceAll("\n", " ");
		String textEmotiTagged = convertEmoticonToTag(text);
		
		SemanticAnalyzer semanticAnalyzer = getSemanticAnalyzer(locale);
		SemanticSentence result = semanticAnalyzer.analyze(textEmotiTagged);
		
		return result;
	}
	
	public Polarity analyzePolarity(Locale locale, SemanticSentence sentence) {
		SentimentAnalyzer sentimentAnalyzer = getSentimentAnalyzer(locale);
		sentence = sentimentAnalyzer.analyzePolarity(sentence);
		
		return new Polarity(sentence.getPolarity(), sentence.getPolarityStrength());
	}
	
	public Polarity analyzePolarity(Locale locale, List<SemanticSentence> sentences) {
		SentimentAnalyzer sentimentAnalyzer = getSentimentAnalyzer(locale);
		Polarity polarity = sentimentAnalyzer.analyzePolarity(sentences);
		
		return polarity;
	}
	
	public SemanticSentence classifyFeature(String objectId, Locale locale, SemanticSentence sentence) {
		FeatureClassifier featureClassifier = getFeatureClassifier(objectId, locale);
		
		String standardLabels = sentence.extractStandardLabel(" ", " ", true, false, false);
		Map<String, Double> featureCounts = featureClassifier.getFeatureCounts(standardLabels, true);
		String mainFeature = featureClassifier.getMainFeature(featureCounts);
		sentence.setFeatures(featureCounts);
		sentence.setMainFeature(mainFeature);
		
		for (SemanticClause clause : sentence) {
			String clauseStandardLabels = clause.makeStandardLabel(" ", true, false, false);
			
			Map<String, Double> clauseFeatureCounts = featureClassifier.getFeatureCounts(clauseStandardLabels, true);
			String clauseMainFeature = featureClassifier.getMainFeature(clauseFeatureCounts);
			clause.setFeatures(clauseFeatureCounts);
			clause.setMainFeature(clauseMainFeature);
		}

		return sentence;
	}
	
	public String classifyMainFeature(String objectId, Locale locale, String standardLabels) {
		FeatureClassifier featureClassifier = getFeatureClassifier(objectId, locale);
		
		Map<String, Double> featureCounts = featureClassifier.getFeatureCounts(standardLabels, true);
		String mainFeature = featureClassifier.getMainFeature(featureCounts);

		return mainFeature;
	}
	
	public void putMorphemeAnalyzer(Locale locale, MorphemeAnalyzer analyzer) {
		locales.add(locale);
		morphemeAnalyzers.put(locale, analyzer);
	}
	
	public void putSemanticAnalyzer(Locale locale, SemanticAnalyzer analyzer) {
		locales.add(locale);
		semanticAnalyzers.put(locale, analyzer);
	}
	
	public void putSentimentAnalyzer(Locale locale, SentimentAnalyzer analyzer) {
		locales.add(locale);
		sentimentAnalyzers.put(locale, analyzer);
	}
	
	public void putFeatureClassifier(String objectId, Locale locale, FeatureClassifier classifier) {
		locales.add(locale);
		Map<Locale, FeatureClassifier> classifiers = (Map<Locale, FeatureClassifier>) featureClassifiers.get(objectId);
		
		if (classifiers != null) {
			classifiers.put(locale, classifier);
		}
		else {
			Map<Locale, FeatureClassifier> newClassifiers = new HashMap<Locale, FeatureClassifier>();
			newClassifiers.put(locale, classifier);
			featureClassifiers.put(objectId, newClassifiers);			
		}		
	}
	
	private MorphemeAnalyzer getMorphemeAnalyzer(Locale locale) {
		return (MorphemeAnalyzer) morphemeAnalyzers.get(locale);
	}
	
	private SemanticAnalyzer getSemanticAnalyzer(Locale locale) {
		return (SemanticAnalyzer) semanticAnalyzers.get(locale);
	}
	
	private SentimentAnalyzer getSentimentAnalyzer(Locale locale) {
		return (SentimentAnalyzer) sentimentAnalyzers.get(locale);
	}
	
	private FeatureClassifier getFeatureClassifier(String objectId, Locale locale) {
		Map<Locale, FeatureClassifier> classifiers = (Map<Locale, FeatureClassifier>) featureClassifiers.get(objectId);
		FeatureClassifier classifier = (FeatureClassifier) classifiers.get(locale);
		
		return classifier;
	}
	
	private String convertEmoticonToTag(String text) {		
		text = replaceStrings(text, "(\\?+)", " TAGQUESTION ");
		text = replaceStrings(text, "(\\^\\^+)", " TAGSMILE ");
		text = replaceStrings(text, "(ㅋ+)", " TAGSMILE ");
		text = replaceStrings(text, "(ㅎ+)", " TAGSMILE ");
		text = replaceStrings(text, "(ㅜ+)", " TAGCRY ");
		text = replaceStrings(text, "(ㅠ+)", " TAGCRY ");
		text = replaceStrings(text, "(ㅡㅡ)", " TAGCRY ");
		text = replaceStrings(text, "(♡+)", " TAGLOVE ");
		text = replaceStrings(text, "(♥+)", " TAGLOVE ");
		text = replaceStrings(text, "(!+)", " TAGEXCLAMATION ");		
		
		return text;
	}
	
	private String replaceStrings(String text, String regex, String newStr) {
		if (text == null) {
			return null;
		}
	
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

		Matcher matcher = pattern.matcher(text);
		return matcher.replaceAll(newStr);
	}
	
	public static void main(String[] args) {
		
	}

}
