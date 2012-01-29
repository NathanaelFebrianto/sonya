package com.nhn.socialanalytics.nlp.analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nhn.socialanalytics.nlp.competitor.CompetitorExtractor;
import com.nhn.socialanalytics.nlp.feature.FeatureCategoryClassifier;
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
	private Map<String, Map<Locale, FeatureCategoryClassifier>> featureCategoryClassifiers = new HashMap<String, Map<Locale, FeatureCategoryClassifier>>();
	private Map<String, CompetitorExtractor> competitorExtractors = new HashMap<String, CompetitorExtractor>();

	public TextAnalyzer() { }
	
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
	
	public void putFeatureCategoryClassifier(String entityId, Locale locale, FeatureCategoryClassifier classifier) {
		locales.add(locale);
		Map<Locale, FeatureCategoryClassifier> classifiers = (Map<Locale, FeatureCategoryClassifier>) featureCategoryClassifiers.get(entityId);
		
		if (classifiers != null) {
			classifiers.put(locale, classifier);
		}
		else {
			Map<Locale, FeatureCategoryClassifier> newClassifiers = new HashMap<Locale, FeatureCategoryClassifier>();
			newClassifiers.put(locale, classifier);
			featureCategoryClassifiers.put(entityId, newClassifiers);			
		}		
	}
	
	public void putCompetitorExtractor(String entityId, CompetitorExtractor extractor) {
		competitorExtractors.put(entityId, extractor);
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
	
	private FeatureCategoryClassifier getFeatureCategoryClassifier(String entityId, Locale locale) {
		Map<Locale, FeatureCategoryClassifier> classifiers = (Map<Locale, FeatureCategoryClassifier>) featureCategoryClassifiers.get(entityId);
		FeatureCategoryClassifier classifier = (FeatureCategoryClassifier) classifiers.get(locale);
		
		return classifier;
	}
	
	private CompetitorExtractor getCompetitorExtractor(String entityId) {
		return (CompetitorExtractor) competitorExtractors.get(entityId);
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
	
	public SemanticSentence extractCompetitors(String entityId, SemanticSentence sentence) {
		CompetitorExtractor competitorExtractor = getCompetitorExtractor(entityId);
		
		competitorExtractor.loadDictionary(entityId);
		
		//String standardLabel = sentence.extractStandardLabel(" ", " ", true, false, false);
		String standardLabel = sentence.extractStandardSubjectLabel(" ", " ", false, false);
		Map<String, Boolean> competitors = competitorExtractor.getCompetitors(standardLabel);
		sentence.setCompetitors(competitors);
		
		for (SemanticClause clause : sentence) {
			//String clauseStandardLabel = clause.makeStandardLabel(" ", true, false, false);
			String clauseStandardLabel = clause.getStandardSubject();
			if (clauseStandardLabel != null) {
				Map<String, Boolean> clauseCompetitors = competitorExtractor.getCompetitors(clauseStandardLabel);
				clause.setCompetitors(clauseCompetitors);
			}
		}
		
		return sentence;
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
	
	public SemanticSentence classifyFeatureCategory(String entityId, Locale locale, SemanticSentence sentence) {
		FeatureCategoryClassifier featureCategoryClassifier = getFeatureCategoryClassifier(entityId, locale);
		
		String standardLabel = sentence.extractStandardLabel(" ", " ", true, false, false);
		Map<String, Double> featureCategoryCounts = featureCategoryClassifier.getFeatureCategoryCounts(standardLabel, true);
		String mainFeatureCategory = featureCategoryClassifier.getMainFeatureCategory(featureCategoryCounts);
		sentence.setFeatureCategories(featureCategoryCounts);
		sentence.setMainFeatureCategory(mainFeatureCategory);
		
		for (SemanticClause clause : sentence) {
			String clauseStandardLabel = clause.makeStandardLabel(" ", true, false, false);
			
			Map<String, Double> clauseFeatureCategoryCounts = featureCategoryClassifier.getFeatureCategoryCounts(clauseStandardLabel, true);
			String clauseMainFeatureCategory = featureCategoryClassifier.getMainFeatureCategory(clauseFeatureCategoryCounts);
			clause.setFeatureCategories(clauseFeatureCategoryCounts);
			clause.setMainFeatureCategory(clauseMainFeatureCategory);
		}

		return sentence;
	}
	
	public String classifyMainFeatureCategory(String entityId, Locale locale, String standardLabels) {
		FeatureCategoryClassifier featureCategoryClassifier = getFeatureCategoryClassifier(entityId, locale);
		
		Map<String, Double> featureCategoryCounts = featureCategoryClassifier.getFeatureCategoryCounts(standardLabels, true);
		String mainFeatureCategory = featureCategoryClassifier.getMainFeatureCategory(featureCategoryCounts);

		return mainFeatureCategory;
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
