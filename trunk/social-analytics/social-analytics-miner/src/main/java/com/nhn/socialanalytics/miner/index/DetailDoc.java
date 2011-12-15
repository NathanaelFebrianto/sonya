package com.nhn.socialanalytics.miner.index;

public class DetailDoc {

	private String site = "";
	private String object = "";
	private String language = "";
	private String collectDate = "";
	private String docId = "";
	private String date = "";
	private String userId = "";
	private String userName = "";
	private String feature = "";
	private String mainFeature = "";
	private String clauseFeature = "";
	private String clauseMainFeature = "";
	private String subject = "";
	private String predicate = "";
	private String attribute = "";
	private String text = "";
	private double polarity = 0.0;
	private double polarityStrength = 0.0;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}
	
	public String getMainFeature() {
		return mainFeature;
	}

	public void setMainFeature(String mainFeature) {
		this.mainFeature = mainFeature;
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
		return subject.toLowerCase();
	}

	public void setSubject(String subject) {
		if (subject != null)
			subject = subject.toLowerCase();
		this.subject = subject;
	}

	public String getPredicate() {
		return predicate.toLowerCase();
	}

	public void setPredicate(String predicate) {
		if (predicate != null)
			predicate = predicate.toLowerCase();
		this.predicate = predicate;
	}

	public String getAttribute() {
		return attribute.toLowerCase();
	}

	public void setAttribute(String attribute) {
		if (attribute != null)
			attribute = attribute.toLowerCase();
		this.attribute = attribute;
	}

	public String getText() {
		return text.toLowerCase();
	}

	public void setText(String text) {
		if (text != null)
			text = text.toLowerCase();
		this.text = text;
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
		.append(FieldConstants.USER_ID).append(" | ")
		.append(FieldConstants.USER_NAME).append(" | ")
		.append(FieldConstants.FEATURE).append(" | ")
		.append(FieldConstants.MAIN_FEATURE).append(" | ")
		.append(FieldConstants.CLAUSE_FEATURE).append(" | ")
		.append(FieldConstants.CLAUSE_MAIN_FEATURE).append(" | ")
		.append(FieldConstants.SUBJECT).append(" | ")
		.append(FieldConstants.PREDICATE).append(" | ")
		.append(FieldConstants.ATTRIBUTE).append(" | ")
		.append(FieldConstants.TEXT).append(" | ")
		.append(FieldConstants.POLARITY).append(" | ")
		.append(FieldConstants.POLARITY_STRENGTH).append(" | ")
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
			.append(userId).append(" | ")
			.append(userName).append(" | ")
			.append(feature).append(" | ")
			.append(mainFeature).append(" | ")
			.append(clauseFeature).append(" | ")
			.append(clauseMainFeature).append(" | ")
			.append(subject).append(" | ")
			.append(predicate).append(" | ")
			.append(attribute).append(" | ")
			.append(text).append(" | ")
			.append(polarity).append(" | ")
			.append(polarityStrength).append(" | ")
			.append(clausePolarity).append(" | ")
			.append(clausePolarityStrength);

		return sb.toString();
	}

}
