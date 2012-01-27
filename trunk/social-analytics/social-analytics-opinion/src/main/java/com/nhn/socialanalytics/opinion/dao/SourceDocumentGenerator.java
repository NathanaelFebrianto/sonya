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
		String featureCategory = "";
		String featureCategory1 = "";
		String mainFeatureCategory = "";
		String clause = "";
		String subject = "";
		String predicate = "";
		String attribute = "";
		String modifier = "";
		String subjectPredicate = "";
		String subjectAttribute = "";
		String featureCategorySubject = "";
		String featureCategoryPredicate = "";
		String featureCategorySubjectPredicate = "";
		String featureCategorySubjectAttribute = "";
		String competitor = "";
		String competitorSubject = "";
		String competitorPredicate = "";
		String competitorSubjectPredicate = "";
		String competitorSubjectAttribute = "";
		String competitorFeatureCategorySubject = "";
		String competitorFeatureCategoryPredicate = "";
		String competitorFeatureCategorySubjectPredicate = "";
		String competitorFeatureCategorySubjectAttribute = "";
		
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
			semanticSentence = textAnalyzer.classifyFeatureCategory(objectId, locale, semanticSentence);
			standardLabels += semanticSentence.extractStandardLabel(" ", " ", true, false, false);
			featureCategory += semanticSentence.extractFeaturesLabel(" ", false);
			featureCategory1 += semanticSentence.extractFeaturesLabel(" ", true);
			
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
			featureCategorySubject += semanticSentence.extractStandardSubjectLabel(" ", "_", true, false);
			// feature_predicate
			featureCategoryPredicate += semanticSentence.extractStandardPredicateLabel(" ", "_", true, false);
			// feature_subject_predicate
			featureCategorySubjectPredicate += semanticSentence.extractStandardSubjectPredicateLabel(" ", "_", true, false, false);
			// feature_subject_attribute
			featureCategorySubjectAttribute += semanticSentence.extractStandardSubjectAttributeLabel(" ", "_", true, false);
			
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
			competitorFeatureCategorySubject += semanticSentence.extractStandardSubjectLabel(" ", "_", true, true);
			// competitor_feature_predicate
			competitorFeatureCategoryPredicate += semanticSentence.extractStandardPredicateLabel(" ", "_", true, true);
			// competitor_feature_subject_predicate
			competitorFeatureCategorySubjectPredicate += semanticSentence.extractStandardSubjectPredicateLabel(" ", "_", true, true, false);
			// competitor_feature_subject_attribute
			competitorFeatureCategorySubjectAttribute += semanticSentence.extractStandardSubjectAttributeLabel(" ", "_", true, true);

			
			if (i < texts.size() - 1) {
				standardLabels += " ";
				featureCategory += " ";
				featureCategory1 += " ";
				if (!clause.trim().equals(""))	clause += ",";
				subject += " ";
				predicate += " ";
				attribute += " ";
				modifier += " ";
				subjectPredicate += " ";
				subjectAttribute += " ";
				featureCategorySubject += " ";
				featureCategoryPredicate += " ";
				featureCategorySubjectPredicate += " ";
				featureCategorySubjectAttribute += " ";
				competitor += " ";
				competitorSubject += " ";
				competitorPredicate += " ";
				competitorSubjectPredicate += " ";
				competitorSubjectAttribute += " ";
				competitorFeatureCategorySubject += " ";
				competitorFeatureCategoryPredicate += " ";
				competitorFeatureCategorySubjectPredicate += " ";
				competitorFeatureCategorySubjectAttribute += " ";
			}
			semanticSentences.add(semanticSentence);
		}
		
		// main feature
		mainFeatureCategory = textAnalyzer.classifyMainFeatureCategory(objectId, locale, standardLabels);
		
		// analyze sentiment
		polarity  = textAnalyzer.analyzePolarity(locale, semanticSentences);
	
		// set document
		doc.setObjectId(objectId);
		doc.setText1(text1);
		doc.setText2(text2);
		doc.setFeatureCategory(featureCategory);
		doc.setFeatureCategory1(featureCategory1);
		doc.setMainFeatureCategory(mainFeatureCategory);
		doc.setClause(clause.trim());
		doc.setSubject(subject.trim());
		doc.setPredicate(predicate.trim());
		doc.setAttribute(attribute.trim());
		doc.setModifier(modifier.trim());
		doc.setSubjectPredicate(subjectPredicate.trim());
		doc.setSubjectAttribute(subjectAttribute.trim());
		doc.setFeatureCategorySubject(featureCategorySubject.trim());
		doc.setFeatureCategoryPredicate(featureCategoryPredicate.trim());
		doc.setFeatureCategorySubjectPredicate(featureCategorySubjectPredicate.trim());
		doc.setFeatureCategorySubjectAttribute(featureCategorySubjectAttribute.trim());
		doc.setCompetitor(competitor.trim());
		doc.setCompetitorSubject(competitorSubject.trim());
		doc.setCompetitorPredicate(competitorPredicate.trim());
		doc.setCompetitorSubjectPredicate(competitorSubjectPredicate.trim());
		doc.setCompetitorSubjectAttribute(competitorSubjectAttribute.trim());
		doc.setCompetitorFeatureCategorySubject(competitorFeatureCategorySubject.trim());
		doc.setCompetitorFeatureCategoryPredicate(competitorFeatureCategoryPredicate.trim());
		doc.setCompetitorFeatureCategorySubjectPredicate(competitorFeatureCategorySubjectPredicate.trim());
		doc.setCompetitorFeatureCategorySubjectAttribute(competitorFeatureCategorySubjectAttribute.trim());
		doc.setPolarity(polarity.getPolarity());
		doc.setPolarityStrength(polarity.getPolarityStrength());
		
		return doc;
	}
	
}
