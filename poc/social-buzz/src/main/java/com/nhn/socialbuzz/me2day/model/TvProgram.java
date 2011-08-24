package com.nhn.socialbuzz.me2day.model;

import java.io.Serializable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Data model for a tv program.
 * 
 * @author Younggue Bae
 */
public class TvProgram implements Serializable {
	
	private String programId;
	private String title;
	private String category;
	private String channel;
	private String description;
	private String imageUrl;
	private String airCycle;
	private String airStartTime;
	private String airEndTime;
	private String emcee;
	private String actors;
	private String produceCompany;
	private String producers;
	private String homepage;
	private String watchRate;
	private String searchKeywords;
	
	public List<String> extractSearchKeywords() {
		
		Vector<String> keywords = new Vector<String>();
		
		String text = this.getSearchKeywords();
		if (text != null && !text.equals("")) {
			StringTokenizer st = new StringTokenizer(text, ",");
			 while (st.hasMoreTokens()) {
				 keywords.add(st.nextToken());
		     }
		}
		
		return keywords;
	}

	public String getProgramId() {
		return programId;
	}
	public String getTitle() {
		return title;
	}
	public String getCategory() {
		return category;
	}
	public String getChannel() {
		return channel;
	}
	public String getDescription() {
		return description;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public String getAirCycle() {
		return airCycle;
	}
	public String getAirStartTime() {
		return airStartTime;
	}
	public String getAirEndTime() {
		return airEndTime;
	}
	public String getEmcee() {
		return emcee;
	}
	public String getActors() {
		return actors;
	}
	public String getProduceCompany() {
		return produceCompany;
	}
	public String getProducers() {
		return producers;
	}
	public String getHomepage() {
		return homepage;
	}
	public String getWatchRate() {
		return watchRate;
	}
	public String getSearchKeywords() {
		return searchKeywords;
	}
	public void setProgramId(String programId) {
		this.programId = programId;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public void setAirCycle(String airCycle) {
		this.airCycle = airCycle;
	}
	public void setAirStartTime(String airStartTime) {
		this.airStartTime = airStartTime;
	}
	public void setAirEndTime(String airEndTime) {
		this.airEndTime = airEndTime;
	}
	public void setEmcee(String emcee) {
		this.emcee = emcee;
	}
	public void setActors(String actors) {
		this.actors = actors;
	}
	public void setProduceCompany(String produceCompany) {
		this.produceCompany = produceCompany;
	}
	public void setProducers(String producers) {
		this.producers = producers;
	}
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	public void setWatchRate(String watchRate) {
		this.watchRate = watchRate;
	}
	public void setSearchKeywords(String searchKeywords) {
		this.searchKeywords = searchKeywords;
	}


}
