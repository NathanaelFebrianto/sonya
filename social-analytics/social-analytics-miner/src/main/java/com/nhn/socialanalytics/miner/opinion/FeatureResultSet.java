package com.nhn.socialanalytics.miner.opinion;

import java.util.ArrayList;
import java.util.List;

import com.nhn.socialanalytics.miner.opinion.FeatureResultSet.FeatureSummary;

@SuppressWarnings("serial")
public class FeatureResultSet extends ArrayList<FeatureSummary> {
	
	private List<FeatureSummary> featureSummaries = new ArrayList<FeatureSummary>();
	
	public FeatureResultSet() {
		
	}
	
	public List<FeatureSummary> getFeatureSummaries() {
		return featureSummaries;
	}

	public void setFeatureSummaries(List<FeatureSummary> featureSummaries) {
		this.featureSummaries = featureSummaries;
	}
	
	public void addFeatureSummary(FeatureSummary featureSummary) {
		featureSummaries.add(featureSummary);
	}
	
	public FeatureSummary newFeatureSummary() {
		return new FeatureSummary();
	}
	
	
	public class FeatureSummary {
		private String feature;
		private double docNums = 0.0;
		private double positiveNums = 0.0;
		private double negativeNums = 0.0;
		private double polarity = 0.0;
		
		public FeatureSummary() {
			
		}
		
		public String getFeature() {
			return feature;
		}
		
		public void setFeature(String feature) {
			this.feature = feature;
		}
		
		public double getDocNums() {
			return docNums;
		}
		
		public void setDocNums(double docNums) {
			this.docNums = docNums;
		}
		
		public void addDocNum() {
			docNums = docNums + 1.0;
		}
		
		public double getPositiveNums() {
			return positiveNums;
		}
		
		public void setPositiveNums(double positiveNums) {
			this.positiveNums = positiveNums;
		}
		
		public void addPositiveNum() {
			positiveNums = positiveNums + 1.0;
		}
		
		public double getNegativeNums() {
			return negativeNums;
		}
		
		public void setNegativeNums(double negativeNums) {
			this.negativeNums = negativeNums;
		}
		
		public void addNegativeNum() {
			negativeNums = negativeNums + 1.0;
		}
		
		public double getPolarity() {
			return polarity;
		}
		
		public void setPolarity(double polarity) {
			this.polarity = polarity;
		}	
		
		public void addPolarity(double p) {
			polarity = polarity + p;
		}
		
		public String getHeaderText() {
			StringBuffer sb = new StringBuffer();
			
			sb.append("=============================================================================\n")
				.append("FEATURE").append("\t")
				.append("DOC_NUMS").append("\t")
				.append("POSITIVE_NUMS").append("\t")
				.append("NEGATIVE_NUMS").append("\t")
				.append("POLARITY");
				//.append("------------------------------------------------------------------------------\n");
			
			return sb.toString();
		}
		
		public String toString() {
			StringBuffer sb = new StringBuffer();
			
			sb.append(feature).append("\t")
				.append(docNums).append("\t")
				.append(positiveNums).append("\t")
				.append(negativeNums).append("\t")
				.append(polarity);
			
			return sb.toString();			
		}
	}
}
