package com.nhn.socialanalytics.opinion.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class SourceDocument implements Serializable {

	private static final String DELIMITER = "\t";
	
	private String site;
	private String objectId;
	private String language;
	private String collectDate;
	private String docId;
	private String createDate;
	private String authorId;
	private String authorName;
	private boolean isSpam;
	private String text;
	private String text1;
	private String text2;
	private String feature;
	private String feature1;
	private String mainFeature;
	private String clause;
	private String subject;
	private String predicate;
	private String attribute;
	private String modifier;
	private String subjectPredicate;
	private String subjectAttribute;
	private String featureSubject;
	private String featurePredicate;
	private String featureSubjectPredicate;
	private String featureSubjectAttribute;
	private String competitor;
	private String competitorSubject;
	private String competitorPredicate;
	private String competitorSubjectPredicate;
	private String competitorSubjectAttribute;
	private String competitorFeatureSubject;
	private String competitorFeaturePredicate;
	private String competitorFeatureSubjectPredicate;
	private String competitorFeatureSubjectAttribute;
	private double polarity;
	private double polarityStrength;
	private Map<String, Object> customFields = new HashMap<String, Object>();
	
	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCollectDate() {
		return collectDate;
	}

	public void setCollectDate(String collectDate) {
		this.collectDate = collectDate;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public boolean isSpam() {
		return isSpam;
	}

	public void setSpam(boolean isSpam) {
		this.isSpam = isSpam;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText1() {
		return text1;
	}

	public void setText1(String text1) {
		this.text1 = text1;
	}

	public String getText2() {
		return text2;
	}

	public void setText2(String text2) {
		this.text2 = text2;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getFeature1() {
		return feature1;
	}

	public void setFeature1(String feature1) {
		this.feature1 = feature1;
	}

	public String getMainFeature() {
		return mainFeature;
	}

	public void setMainFeature(String mainFeature) {
		this.mainFeature = mainFeature;
	}

	public String getClause() {
		return clause;
	}

	public void setClause(String clause) {
		this.clause = clause;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getSubjectPredicate() {
		return subjectPredicate;
	}

	public void setSubjectPredicate(String subjectPredicate) {
		this.subjectPredicate = subjectPredicate;
	}

	public String getSubjectAttribute() {
		return subjectAttribute;
	}

	public void setSubjectAttribute(String subjectAttribute) {
		this.subjectAttribute = subjectAttribute;
	}

	public String getFeatureSubject() {
		return featureSubject;
	}

	public void setFeatureSubject(String featureSubject) {
		this.featureSubject = featureSubject;
	}

	public String getFeaturePredicate() {
		return featurePredicate;
	}

	public void setFeaturePredicate(String featurePredicate) {
		this.featurePredicate = featurePredicate;
	}

	public String getFeatureSubjectPredicate() {
		return featureSubjectPredicate;
	}

	public void setFeatureSubjectPredicate(String featureSubjectPredicate) {
		this.featureSubjectPredicate = featureSubjectPredicate;
	}

	public String getFeatureSubjectAttribute() {
		return featureSubjectAttribute;
	}

	public void setFeatureSubjectAttribute(String featureSubjectAttribute) {
		this.featureSubjectAttribute = featureSubjectAttribute;
	}

	public String getCompetitor() {
		return competitor;
	}

	public void setCompetitor(String competitor) {
		this.competitor = competitor;
	}
	
	public String getCompetitorSubject() {
		return competitorSubject;
	}

	public void setCompetitorSubject(String competitorSubject) {
		this.competitorSubject = competitorSubject;
	}

	public String getCompetitorPredicate() {
		return competitorPredicate;
	}

	public void setCompetitorPredicate(String competitorPredicate) {
		this.competitorPredicate = competitorPredicate;
	}

	public String getCompetitorSubjectPredicate() {
		return competitorSubjectPredicate;
	}

	public void setCompetitorSubjectPredicate(String competitorSubjectPredicate) {
		this.competitorSubjectPredicate = competitorSubjectPredicate;
	}

	public String getCompetitorSubjectAttribute() {
		return competitorSubjectAttribute;
	}

	public void setCompetitorSubjectAttribute(String competitorSubjectAttribute) {
		this.competitorSubjectAttribute = competitorSubjectAttribute;
	}

	public String getCompetitorFeatureSubject() {
		return competitorFeatureSubject;
	}

	public void setCompetitorFeatureSubject(String competitorFeatureSubject) {
		this.competitorFeatureSubject = competitorFeatureSubject;
	}

	public String getCompetitorFeaturePredicate() {
		return competitorFeaturePredicate;
	}

	public void setCompetitorFeaturePredicate(String competitorFeaturePredicate) {
		this.competitorFeaturePredicate = competitorFeaturePredicate;
	}

	public String getCompetitorFeatureSubjectPredicate() {
		return competitorFeatureSubjectPredicate;
	}

	public void setCompetitorFeatureSubjectPredicate(
			String competitorFeatureSubjectPredicate) {
		this.competitorFeatureSubjectPredicate = competitorFeatureSubjectPredicate;
	}

	public String getCompetitorFeatureSubjectAttribute() {
		return competitorFeatureSubjectAttribute;
	}

	public void setCompetitorFeatureSubjectAttribute(
			String competitorFeatureSubjectAttribute) {
		this.competitorFeatureSubjectAttribute = competitorFeatureSubjectAttribute;
	}

	public double getPolarity() {
		return polarity;
	}

	public void setPolarity(double polarity) {
		this.polarity = polarity;
	}

	public double getPolarityStrength() {
		return polarityStrength;
	}

	public void setPolarityStrength(double polarityStrength) {
		this.polarityStrength = polarityStrength;
	}

	public Map<String, Object> getCustomFields() {
		return customFields;
	}

	public void setCustomFields(Map<String, Object> customFields) {
		this.customFields = customFields;
	}
	
	public void addCustomField(String key, Object value) {
		customFields.put(key, value);
	}

	public static String getHeaderString() {
		StringBuffer sb = new StringBuffer()
			.append("site").append(DELIMITER)
			.append("object_id").append(DELIMITER)
			.append("language").append(DELIMITER)
			.append("collect_date").append(DELIMITER)
			.append("doc_id").append(DELIMITER)
			.append("create_date").append(DELIMITER)
			.append("author_id").append(DELIMITER)
			.append("author_name").append(DELIMITER)
			.append("is_spam").append(DELIMITER)
			.append("text").append(DELIMITER)
			.append("text1").append(DELIMITER)
			.append("text2").append(DELIMITER)
			.append("feature").append(DELIMITER)
			.append("feature1").append(DELIMITER)
			.append("main_feature").append(DELIMITER)
			.append("clause").append(DELIMITER)
			.append("subject").append(DELIMITER)
			.append("predicate").append(DELIMITER)
			.append("attribute").append(DELIMITER)
			.append("modifier").append(DELIMITER)
			.append("subject_predicate").append(DELIMITER)
			.append("subject_attribute").append(DELIMITER)
			.append("feature_subject").append(DELIMITER)
			.append("feature_predicate").append(DELIMITER)
			.append("feature_subject_predicate").append(DELIMITER)
			.append("feature_subject_attribute").append(DELIMITER)
			.append("competitor").append(DELIMITER)
			.append("competitor_subject").append(DELIMITER)
			.append("competitor_predicate").append(DELIMITER)
			.append("competitor_subject_predicate").append(DELIMITER)
			.append("competitor_subject_attribute").append(DELIMITER)
			.append("competitor_feature_subject").append(DELIMITER)
			.append("competitor_feature_predicate").append(DELIMITER)
			.append("competitor_feature_subject_predicate").append(DELIMITER)
			.append("competitor_feature_subject_attribute").append(DELIMITER)
			.append("polarity").append(DELIMITER)
			.append("polarity_strength").append(DELIMITER)
			.append("custom_fields");
		
		return sb.toString();		
	}

	public String toString() {
		StringBuffer sb = new StringBuffer()
			.append(site).append(DELIMITER)
			.append(objectId).append(DELIMITER)
			.append(language).append(DELIMITER)
			.append(collectDate).append(DELIMITER)
			.append(docId).append(DELIMITER)
			.append(createDate).append(DELIMITER)
			.append(authorId).append(DELIMITER)
			.append(authorName).append(DELIMITER)
			.append(isSpam).append(DELIMITER)
			.append(text).append(DELIMITER)
			.append(text1).append(DELIMITER)
			.append(text2).append(DELIMITER)
			.append(feature).append(DELIMITER)
			.append(feature1).append(DELIMITER)
			.append(mainFeature).append(DELIMITER)
			.append(clause).append(DELIMITER)
			.append(subject).append(DELIMITER)
			.append(predicate).append(DELIMITER)
			.append(attribute).append(DELIMITER)
			.append(modifier).append(DELIMITER)
			.append(subjectPredicate).append(DELIMITER)
			.append(subjectAttribute).append(DELIMITER)
			.append(featureSubject).append(DELIMITER)
			.append(featurePredicate).append(DELIMITER)
			.append(featureSubjectPredicate).append(DELIMITER)
			.append(featureSubjectAttribute).append(DELIMITER)
			.append(competitor).append(DELIMITER)
			.append(competitorSubject).append(DELIMITER)
			.append(competitorPredicate).append(DELIMITER)
			.append(competitorSubjectPredicate).append(DELIMITER)
			.append(competitorSubjectAttribute).append(DELIMITER)
			.append(competitorFeatureSubject).append(DELIMITER)
			.append(competitorFeaturePredicate).append(DELIMITER)
			.append(competitorFeatureSubjectPredicate).append(DELIMITER)
			.append(competitorFeatureSubjectAttribute).append(DELIMITER)
			.append(polarity).append(DELIMITER)
			.append(polarityStrength).append(DELIMITER);
		
		int i = 0;
		for (Map.Entry<String, Object> entry : customFields.entrySet()) {
			String field  = "";
			String key = entry.getKey();
			Object value = entry.getValue();
			field = key + ":" + value;
			
			if (i < customFields.size() - 1)
				sb.append(field).append(",");
			else
				sb.append(field);
			
			i++;
		} 
		
		return sb.toString();
	}

}