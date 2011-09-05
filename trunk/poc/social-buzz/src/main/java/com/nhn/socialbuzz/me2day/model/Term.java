package com.nhn.socialbuzz.me2day.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Data model for a term.
 * 
 * @author Younggue Bae
 */
public class Term implements Serializable {

	private String programId;
	private String startDate;
	private String endDate;
	private String type;
	private String term;
	private String durationType;
	private String feature;
	private double tf;
	private double idf;
	private double score;
	private Date registerDate;
	private Date updateDate;
	
	public String getProgramId() {
		return programId;
	}
	public String getStartDate() {
		return startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public String getType() {
		return type;
	}
	public String getTerm() {
		return term;
	}
	public String getDurationType() {
		return durationType;
	}
	public String getFeature() {
		return feature;
	}
	public double getTf() {
		return tf;
	}
	public double getIdf() {
		return idf;
	}
	public double getScore() {
		return score;
	}
	public Date getRegisterDate() {
		return registerDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setProgramId(String programId) {
		this.programId = programId;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public void setDurationType(String durationType) {
		this.durationType = durationType;
	}
	public void setFeature(String feature) {
		this.feature = feature;
	}
	public void setTf(double tf) {
		this.tf = tf;
	}
	public void setIdf(double idf) {
		this.idf = idf;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
}
