package com.nhn.socialanalytics.opinion.common;

public class DetailDoc {

	private String site = "";
	private String object = "";
	private String language = "";
	private String collectDate = "";
	private String docId = "";
	private String date = "";
	private String authorId = "";
	private String authorName = "";
	private String docFeature = "";
	private String docMainFeature = "";
	private String clauseFeature = "";
	private String clauseMainFeature = "";
	private String subject = "";
	private String predicate = "";
	private String attribute = "";
	private String modifier = "";
	private String text = "";
	private double docPolarity = 0.0;
	private double docPolarityStrength = 0.0;
	private double clausePolarity = 0.0;
	private double clausePolarityStrength = 0.0;

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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
	
	public String getDocFeature() {
		return docFeature;
	}

	public void setDocFeature(String docFeature) {
		this.docFeature = docFeature;
	}
	
	public String getDocMainFeature() {
		return docMainFeature;
	}

	public void setDocMainFeature(String docMainFeature) {
		this.docMainFeature = docMainFeature;
	}
	
	public String getClauseFeature() {
		return clauseFeature;
	}

	public void setClauseFeature(String clauseFeature) {
		this.clauseFeature = clauseFeature;
	}
	
	public String getClauseMainFeature() {
		return clauseMainFeature;
	}

	public void setClauseMainFeature(String clauseMainFeature) {
		this.clauseMainFeature = clauseMainFeature;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		if (subject != null)
			subject = subject.toLowerCase();
		this.subject = subject;
	}

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		if (predicate != null)
			predicate = predicate.toLowerCase();
		this.predicate = predicate;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		if (attribute != null)
			attribute = attribute.toLowerCase();
		this.attribute = attribute;
	}
	
	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		if (modifier != null)
			modifier = modifier.toLowerCase();
		this.modifier = modifier;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (text != null)
			text = text.toLowerCase();
		this.text = text;
	}
	
	public double getDocPolarity() {
		return docPolarity;
	}

	public void setDocPolarity(double docPolarity) {
		this.docPolarity = docPolarity;
	}
	
	public double getDocPolarityStrength() {
		return docPolarityStrength;
	}

	public void setDocPolarityStrength(double docPolarityStrength) {
		this.docPolarityStrength = docPolarityStrength;
	}
	
	public double getClausePolarity() {
		return clausePolarity;
	}

	public void setClausePolarity(double clausePolarity) {
		this.clausePolarity = clausePolarity;
	}
	
	public double getClausePolarityStrength() {
		return clausePolarityStrength;
	}

	public void setClausePolarityStrength(double clausePolarityStrength) {
		this.clausePolarityStrength = clausePolarityStrength;
	}
	
	public String toHeaderString() {
		StringBuffer sb = new StringBuffer()
		.append(FieldConstants.SITE).append(" | ")
		.append(FieldConstants.OBJECT).append(" | ")
		.append(FieldConstants.LANGUAGE).append(" | ")
		.append(FieldConstants.COLLECT_DATE).append(" | ")
		.append(FieldConstants.DOC_ID).append(" | ")
		.append(FieldConstants.DATE).append(" | ")			
		.append(FieldConstants.AUTHOR_ID).append(" | ")
		.append(FieldConstants.AUTHOR_NAME).append(" | ")
		.append(FieldConstants.DOC_FEATURE).append(" | ")
		.append(FieldConstants.DOC_MAIN_FEATURE).append(" | ")
		.append(FieldConstants.CLAUSE_FEATURE).append(" | ")
		.append(FieldConstants.CLAUSE_MAIN_FEATURE).append(" | ")
		.append(FieldConstants.SUBJECT).append(" | ")
		.append(FieldConstants.PREDICATE).append(" | ")
		.append(FieldConstants.ATTRIBUTE).append(" | ")
		.append(FieldConstants.MODIFIER).append(" | ")
		.append(FieldConstants.TEXT).append(" | ")
		.append(FieldConstants.DOC_POLARITY).append(" | ")
		.append(FieldConstants.DOC_POLARITY_STRENGTH).append(" | ")
		.append(FieldConstants.CLAUSE_POLARITY).append(" | ")
		.append(FieldConstants.CLAUSE_POLARITY_STRENGTH);

	return sb.toString();		
	}

	public String toString() {
		StringBuffer sb = new StringBuffer()
			.append(site).append(" | ")
			.append(object).append(" | ")
			.append(language).append(" | ")
			.append(collectDate).append(" | ")
			.append(docId).append(" | ")
			.append(date).append(" | ")			
			.append(authorId).append(" | ")
			.append(authorName).append(" | ")
			.append(docFeature).append(" | ")
			.append(docMainFeature).append(" | ")
			.append(clauseFeature).append(" | ")
			.append(clauseMainFeature).append(" | ")
			.append(subject).append(" | ")
			.append(predicate).append(" | ")
			.append(attribute).append(" | ")
			.append(modifier).append(" | ")
			.append(text).append(" | ")
			.append(docPolarity).append(" | ")
			.append(docPolarityStrength).append(" | ")
			.append(clausePolarity).append(" | ")
			.append(clausePolarityStrength);

		return sb.toString();
	}

}
