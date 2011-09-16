package com.nhn.socialbuzz.me2day.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
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
	private String nation;
	private String status;
	private String category;
	private String channel;
	private String description;
	private String imageUrl;
	private String airCycle;
	private String airCycleDesc;
	private String airStartTime;
	private String airEndTime;
	private String emcee;
	private String actors;
	private String produceCompany;
	private String producers;
	private String homepage;
	private String searchKeywords;
	private String twitterSearchKeywords;
	private Date registerDate;
	private Date updateDate;
	
	public class SearchQuery {
		private String keyword = null;
		private int maxResultPage = 0;
		
		public SearchQuery(String keyword, int maxResultPage) {
			this.keyword = keyword;
			this.maxResultPage = maxResultPage;
		}
		
		public String getKeyword() {
			return keyword;
		}
		
		public int getMaxResultPage() {
			return maxResultPage;
		}
	};
	
	public List<SearchQuery> extractMe2daySearchKeywords() {
		
		Vector<SearchQuery> keywords = new Vector<SearchQuery>();
		
		String text = this.getSearchKeywords();
		if (text != null && !text.equals("")) {
			StringTokenizer st = new StringTokenizer(text, ",");
			 while (st.hasMoreTokens()) {
				 String token = st.nextToken().trim();
				 StringTokenizer st1 = new StringTokenizer(token, ":");
				 String keyword = st1.nextToken().trim();
				 int maxResultPage = 50;	// default = 50
				 try {
					 maxResultPage = Integer.valueOf(st1.nextToken());
				 } catch (NoSuchElementException e) {
				 }
				 
				 SearchQuery query = new SearchQuery(keyword, maxResultPage);
				 keywords.add(query);
		     }
		}
		
		return keywords;
	}
	
	public List<SearchQuery> extractTwitterSearchKeywords() {
		
		Vector<SearchQuery> keywords = new Vector<SearchQuery>();
		
		String text = this.getTwitterSearchKeywords();
		if (text != null && !text.equals("")) {
			StringTokenizer st = new StringTokenizer(text, ",");
			 while (st.hasMoreTokens()) {
				 String token = st.nextToken().trim();
				 StringTokenizer st1 = new StringTokenizer(token, ":");
				 String keyword = st1.nextToken().trim();
				 int maxResultPage = 1;	// default = 1
				 try {
					 maxResultPage = Integer.valueOf(st1.nextToken());
				 } catch (NoSuchElementException e) {
				 }
				 
				 SearchQuery query = new SearchQuery(keyword, maxResultPage);
				 keywords.add(query);
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
	
	public String getNation() {
		return nation;
	}

	public String getStatus() {
		return status;
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
	
	public String getAirCycleDesc() {
		return airCycleDesc;
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

	public String getSearchKeywords() {
		return searchKeywords;
	}
	
	public String getTwitterSearchKeywords() {
		return twitterSearchKeywords;
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

	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setNation(String nation) {
		this.nation = nation;
	}

	public void setStatus(String status) {
		this.status = status;
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
	
	public void setAirCycleDesc(String airCycleDesc) {
		this.airCycleDesc = airCycleDesc;
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

	public void setSearchKeywords(String searchKeywords) {
		this.searchKeywords = searchKeywords;
	}
	
	public void setTwitterSearchKeywords(String twitterSearchKeywords) {
		this.twitterSearchKeywords = twitterSearchKeywords;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}
