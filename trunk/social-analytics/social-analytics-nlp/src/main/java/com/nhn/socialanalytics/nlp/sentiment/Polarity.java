package com.nhn.socialanalytics.nlp.sentiment;

public class Polarity {
	double polarity;
	double polarityStrength;

	public Polarity() {
	}

	public Polarity(double polarity, double polarityStrength) {
		this.polarity = polarity;
		this.polarityStrength = polarityStrength;
	}

	public double getPolarity() {
		return this.polarity;
	}

	public void setPolarity(double polarity) {
		this.polarity = polarity;
	}

	public double getPolarityStrength() {
		return this.polarityStrength;
	}

	public void setPolarityStrength(double polarityStrength) {
		this.polarityStrength = polarityStrength;
	}
}
