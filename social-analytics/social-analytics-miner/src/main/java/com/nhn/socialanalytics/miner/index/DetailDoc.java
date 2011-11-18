package com.nhn.socialanalytics.miner.index;

public class DetailDoc {

	private String site;
	private String object;
	private String collectDate;
	private String docId;
	private String date;
	private String userId;
	private String userName;
	private String subject;
	private String predicate;
	private String attribute;
	private String text;

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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String toHeaderString() {
		StringBuffer sb = new StringBuffer()
		.append(FieldConstants.SITE).append(" | ")
		.append(FieldConstants.OBJECT).append(" | ")
		.append(FieldConstants.COLLECT_DATE).append(" | ")
		.append(FieldConstants.DOC_ID).append(" | ")
		.append(FieldConstants.DATE).append(" | ")			
		.append(FieldConstants.USER_ID).append(" | ")
		.append(FieldConstants.USER_NAME).append(" | ")
		.append(FieldConstants.SUBJECT).append(" | ")
		.append(FieldConstants.PREDICATE).append(" | ")
		.append(FieldConstants.ATTRIBUTE).append(" | ")
		.append(FieldConstants.TEXT);

	return sb.toString();		
	}

	public String toString() {
		StringBuffer sb = new StringBuffer()
			.append(site).append(" | ")
			.append(object).append(" | ")
			.append(collectDate).append(" | ")
			.append(docId).append(" | ")
			.append(date).append(" | ")			
			.append(userId).append(" | ")
			.append(userName).append(" | ")
			.append(subject).append(" | ")
			.append(predicate).append(" | ")
			.append(attribute).append(" | ")
			.append(text);

		return sb.toString();
	}

}
