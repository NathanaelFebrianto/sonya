package com.nhn.socialanalytics.opinion.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.nhn.socialanalytics.nlp.analysis.TextAnalyzer;
import com.nhn.socialanalytics.nlp.semantic.SemanticSentence;
import com.nhn.socialanalytics.nlp.sentiment.Polarity;
import com.nhn.socialanalytics.opinion.model.SourceDocument;

public class SourceDocumentGenerator {
	
	private TextAnalyzer textAnalyzer;
	
	public SourceDocumentGenerator() {
		
	}

	public void setTextAnalyzer(TextAnalyzer textAnalyzer) {
		this.textAnalyzer = textAnalyzer;
	}
	
	public SourceDocument generate(Locale locale, String objectId, List<String> texts) {
		SourceDocument doc = new SourceDocument();
		
		String text1 = "";
		String text2 = "";
		String standardLabels = "";
		String feature = "";
		String feature1 = "";
		String mainFeature = "";
		String clause = "";
		String subject = "";
		String predicate = "";
		String attribute = "";
		String modifier = "";
		String subjectPredicate = "";
		String subjectAttribute = "";
		String featureSubject = "";
		String featurePredicate = "";
		String featureSubjectPredicate = "";
		String featureSubjectAttribute = "";
		String competitor = "";
		String competitorSubject = "";
		String competitorPredicate = "";
		String competitorSubjectPredicate = "";
		String competitorSubjectAttribute = "";
		String competitorFeatureSubject = "";
		String competitorFeaturePredicate = "";
		String competitorFeatureSubjectPredicate = "";
		String competitorFeatureSubjectAttribute = "";
		
		Polarity polarity;
		
		
		List<SemanticSentence> semanticSentences = new ArrayList<SemanticSentence>();
		
		for (int i = 0; i < texts.size(); i++) {
			String text = texts.get(i);
			// text1
			text1 += textAnalyzer.extractTerms(locale, text) + " ";
			// text2
			text2 += textAnalyzer.extractCoreTerms(locale, text) + " ";
			
			// analyze semantics
			SemanticSentence semanticSentence = textAnalyzer.analyzeSemantics(locale, text);
			
			// classify feature
			semanticSentence = textAnalyzer.classifyFeature(objectId, locale, semanticSentence);
			standardLabels += semanticSentence.extractStandardLabel(" ", " ", true, false, false);
			feature += semanticSentence.extractFeaturesLabel(" ", false);
			feature1 += semanticSentence.extractFeaturesLabel(" ", true);
			
			// clause
			clause += semanticSentence.extractStandardLabel(",", "_", true, false, false);			
			// subject
			subject += semanticSentence.extractStandardSubjectLabel(" ", " ", false, false);		
			// predicate
			predicate += semanticSentence.extractStandardPredicateLabel(" ", " ", false, false);
			// attribute
			attribute += semanticSentence.extractStandardAttributesLabel(" ");
			// modifier
			modifier += semanticSentence.extractStandardModifiersLabel(" ");
			// subject_predicate
			subjectPredicate += semanticSentence.extractStandardSubjectPredicateLabel(" ", "_", false, false, false);
			// subject_attribute
			subjectAttribute += semanticSentence.extractStandardSubjectAttributeLabel(" ", "_", false, false);
			// feature_subject
			featureSubject += semanticSentence.extractStandardSubjectLabel(" ", "_", true, false);
			// feature_predicate
			featurePredicate += semanticSentence.extractStandardPredicateLabel(" ", "_", true, false);
			// feature_subject_predicate
			featureSubjectPredicate += semanticSentence.extractStandardSubjectPredicateLabel(" ", "_", true, false, false);
			// feature_subject_attribute
			featureSubjectAttribute += semanticSentence.extractStandardSubjectAttributeLabel(" ", "_", true, false);
			
			// competitor
			competitor += semanticSentence.extractCompetitorsLabel(" ");
			// competitor_subject
			competitorSubject += semanticSentence.extractStandardSubjectLabel(" ", "_", false, true);		
			// competitor_predicate
			competitorPredicate += semanticSentence.extractStandardPredicateLabel(" ", "_", false, true);
			// competitor_subject_predicate
			competitorSubjectPredicate += semanticSentence.extractStandardSubjectPredicateLabel(" ", "_", false, true, false);
			// competitor_subject_attribute
			competitorSubjectAttribute += semanticSentence.extractStandardSubjectAttributeLabel(" ", "_", false, true);
			// competitor_feature_subject
			competitorFeatureSubject += semanticSentence.extractStandardSubjectLabel(" ", "_", true, true);
			// competitor_feature_predicate
			competitorFeaturePredicate += semanticSentence.extractStandardPredicateLabel(" ", "_", true, true);
			// competitor_feature_subject_predicate
			competitorFeatureSubjectPredicate += semanticSentence.extractStandardSubjectPredicateLabel(" ", "_", true, true, false);
			// competitor_feature_subject_attribute
			competitorFeatureSubjectAttribute += semanticSentence.extractStandardSubjectAttributeLabel(" ", "_", true, true);

			
			if (i < texts.size() - 1) {
				standardLabels += " ";
				feature += " ";
				feature1 += " ";
				if (!clause.trim().equals(""))	clause += ",";
				subject += " ";
				predicate += " ";
				attribute += " ";
				modifier += " ";
				subjectPredicate += " ";
				subjectAttribute += " ";
				featureSubject += " ";
				featurePredicate += " ";
				featureSubjectPredicate += " ";
				featureSubjectAttribute += " ";
				competitor += " ";
				competitorSubject += " ";
				competitorPredicate += " ";
				competitorSubjectPredicate += " ";
				competitorSubjectAttribute += " ";
				competitorFeatureSubject += " ";
				competitorFeaturePredicate += " ";
				competitorFeatureSubjectPredicate += " ";
				competitorFeatureSubjectAttribute += " ";
			}
			semanticSentences.add(semanticSentence);
		}
		
		// main feature
		mainFeature = textAnalyzer.classifyMainFeature(objectId, locale, standardLabels);
		
		// analyze sentiment
		polarity  = textAnalyzer.analyzePolarity(locale, semanticSentences);
	
		// set document
		doc.setObjectId(objectId);
		doc.setText1(text1);
		doc.setText2(text2);
		doc.setFeature(feature);
		doc.setFeature1(feature1);
		doc.setMainFeature(mainFeature);
		doc.setClause(clause.trim());
		doc.setSubject(subject.trim());
		doc.setPredicate(predicate.trim());
		doc.setAttribute(attribute.trim());
		doc.setModifier(modifier.trim());
		doc.setSubjectPredicate(subjectPredicate.trim());
		doc.setSubjectAttribute(subjectAttribute.trim());
		doc.setFeatureSubject(featureSubject.trim());
		doc.setFeaturePredicate(featurePredicate.trim());
		doc.setFeatureSubjectPredicate(featureSubjectPredicate.trim());
		doc.setFeatureSubjectAttribute(featureSubjectAttribute.trim());
		doc.setCompetitor(competitor.trim());
		doc.setCompetitorSubject(competitorSubject.trim());
		doc.setCompetitorPredicate(competitorPredicate.trim());
		doc.setCompetitorSubjectPredicate(competitorSubjectPredicate.trim());
		doc.setCompetitorSubjectAttribute(competitorSubjectAttribute.trim());
		doc.setCompetitorFeatureSubject(competitorFeatureSubject.trim());
		doc.setCompetitorFeaturePredicate(competitorFeaturePredicate.trim());
		doc.setCompetitorFeatureSubjectPredicate(competitorFeatureSubjectPredicate.trim());
		doc.setCompetitorFeatureSubjectAttribute(competitorFeatureSubjectAttribute.trim());
		doc.setPolarity(polarity.getPolarity());
		doc.setPolarityStrength(polarity.getPolarityStrength());
		
		return doc;
	}
	
}
