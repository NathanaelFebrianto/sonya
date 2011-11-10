package com.nhn.socialanalytics.miner.termvector;

public class DetailDoc {

	private String site;
	private String date;
	private String user;
	private String docId;
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
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
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
			.append(user).append(" | ")
			.append(docId).append(" | ")
			.append(text);
		
		return sb.toString();
	}
	
}
