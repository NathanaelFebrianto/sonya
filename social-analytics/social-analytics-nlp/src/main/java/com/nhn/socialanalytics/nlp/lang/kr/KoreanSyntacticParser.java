package com.nhn.socialanalytics.nlp.lang.kr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.nhn.socialanalytics.nlp.syntax.ParseRule;
import com.nhn.socialanalytics.nlp.syntax.Parser;

public class KoreanSyntacticParser extends Parser {

	private static Parser parser = null;

	public static Parser getInstance() {
		if (parser == null)
			parser = new KoreanSyntacticParser();
		return parser;
	}

	public KoreanSyntacticParser() {
		initRules();
	}

	@ Override
	protected void initRules() {
		this.ruleList = new ArrayList<ParseRule>();

		this.ruleList.add(new ParseRule(
				"부가",										//relation
				new String[]{ //"V", 
						"JKS", "JKO", "JKM", 
						"JX", "Z", "ECE", "ECD", "ECS" },	//dependentTags
				new String[]{ "V", "EFN" },					//governerTags
				30,											//distance
				1));										//priority

		this.ruleList.add(new ParseRule(
				"강조",										//relation
				new String[]{ "Z" },						//dependentTags
				new String[]{ "Z" },						//governerTags
				1,											//distance
				2));										//priority

		this.ruleList.add(new ParseRule(
				"수식",										//relation
				new String[]{ "N", "Z", "JKG", "ETD" },		//dependentTags
				new String[]{ "N" },						//governerTags
				1,											//distance
				3));										//priority
		
		this.ruleList.add(new ParseRule(
				"수식",										//relation
				new String[]{ "N" },						//dependentTags
				new String[]{ "V" },						//governerTags
				1,											//distance
				4));
		
		Collections.sort(this.ruleList, new Comparator<ParseRule>() {
			public int compare(ParseRule pr1, ParseRule pr2) {
				return pr1.priority - pr2.priority;
			}
		});
	}

}
