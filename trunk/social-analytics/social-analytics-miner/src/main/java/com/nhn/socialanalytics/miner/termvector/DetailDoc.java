package com.nhn.socialanalytics.miner.termvector;

public class DetailDoc {

	private String site;
	private String date;
	private String userId;
	private String userName;
	private String docId;
	private String subject;
	private String predicate;
	private String objects;
	private String text;

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
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

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
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

	public String getObjects() {
		return objects;
	}

	public void setObjects(String objects) {
		this.objects = objects;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer()
			.append(site).append(" | ")
			.append(date).append(" | ")
			.append(userId).append(" | ")
			.append(userName).append(" | ")
			.append(docId).append(" | ")
			.append(subject).append(" | ")
			.append(predicate).append(" | ")
			.append(objects).append(" | ")
			.append(text);

		return sb.toString();
	}

}
