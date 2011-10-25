package com.nhn.socialanalytics.nlp.kr.syntax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.nhn.socialanalytics.nlp.kr.morpheme.Eojeol;
import com.nhn.socialanalytics.nlp.kr.morpheme.Sentence;

public class Parser {
	private List<ParseRule> ruleList = null;

	private static Parser parser = null;

	public static Parser getInstance() {
		if (parser == null)
			parser = new Parser();
		return parser;
	}

	public Parser() {
		initRules();
	}

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

	public ParseTree parse(Sentence sentence) {
		List<ParseTreeNode> nodeList = new ArrayList<ParseTreeNode>();
		
		for (int i = 0; i < sentence.size(); i++) {
			Eojeol eojeol = sentence.get(i);			
			nodeList.add(new ParseTreeNode(eojeol));
		}		
		
		for (int i = 0; i < nodeList.size() - 1; i++) {
			ParseTreeNode ptnPrev = (ParseTreeNode) nodeList.get(i);
		
			System.out.println("*******");
			System.out.println("prev == " + ptnPrev.getEojeol());
			System.out.println("prev index == " + ptnPrev.getEojeolIndex());

			for (int j = i + 1; j < nodeList.size(); j++) {
				ParseTreeNode ptnNext = (ParseTreeNode) nodeList.get(j);

				System.out.println("next == " + ptnNext.getEojeol());
				System.out.println("next index == " + ptnNext.getEojeolIndex());

				ParseTreeEdge arc = null;				
				
				if (ptnPrev.getEojeolIndex() == ptnNext.getEojeolIndex()) {
					System.out.println("######################");
					arc = null;
				} else {
					System.out.println("XXXXXXXXXXXXXXXXXXXXXX");
					arc = govern(ptnPrev, ptnNext, j - i);
					
					if (arc != null) {
						ptnNext.addChildEdge(arc);
						ptnPrev.setParentNode(ptnNext);
						break;
					}
				}
			}
		}

		ParseTree tree = new ParseTree();
		tree.setSentence(sentence.getSentence());
		for (Iterator<ParseTreeNode> itr = nodeList.iterator(); itr.hasNext();) {
			ParseTreeNode ptn = (ParseTreeNode) itr.next();
			if (ptn.getParentNode() != null)
				continue;
			tree.setRoot(ptn);
		}
		tree.setId();
		tree.setAllList();
		return tree;
	}
	
	/*
	public ParseTree parse1(Sentence sentence) {
		List<ParseTreeNode> nodeList = new ArrayList<ParseTreeNode>();
		
		for (int i = 0; i < sentence.size(); i++) {
			Eojeol eojeol = sentence.get(i);			
			nodeList.add(new ParseTreeNode(eojeol));
		}		
		
		for (int i = 0; i < nodeList.size() - 1; i++) {
			ParseTreeNode ptnPrev = (ParseTreeNode) nodeList.get(i);
		
			System.out.println("**** prev == " + ptnPrev.getEojeol());

			for (int j = i + 1; j < nodeList.size(); j++) {
				ParseTreeNode ptnNext = (ParseTreeNode) nodeList.get(j);
				System.out.println("**** next == " + ptnNext.getEojeol());

				ParseTreeEdge arc = govern(ptnPrev, ptnNext, j - i);
					
				if (arc != null) {
					ptnNext.addChildEdge(arc);
					ptnPrev.setParentNode(ptnNext);
					break;
				}
			}
		}

		ParseTree tree = new ParseTree();
		tree.setSentence(sentence.getSentence());
		for (Iterator<ParseTreeNode> itr = nodeList.iterator(); itr.hasNext();) {
			ParseTreeNode ptn = (ParseTreeNode) itr.next();
			if (ptn.getParentNode() != null)
				continue;
			tree.setRoot(ptn);
		}
		tree.setId();
		tree.setAllList();
		return tree;
	}
	*/
	
	public ParseTreeEdge govern(ParseTreeNode ptnPrev, ParseTreeNode ptnNext, int distance) {
		ParseTreeEdge arc = null;
		int i = 0;
		for (int size = this.ruleList.size(); i < size; i++) {
			
			ParseRule rule = (ParseRule) this.ruleList.get(i);
			arc = rule.govern(ptnPrev, ptnNext, distance);
			
			if (arc != null) {
				return arc;
			}
		}
		return null;
	}
}
