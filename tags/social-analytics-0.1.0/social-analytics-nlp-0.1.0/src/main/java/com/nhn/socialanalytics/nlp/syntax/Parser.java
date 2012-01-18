package com.nhn.socialanalytics.nlp.syntax;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.nhn.socialanalytics.nlp.morpheme.Sentence;
import com.nhn.socialanalytics.nlp.morpheme.Token;

public abstract class Parser {
	protected List<ParseRule> ruleList = null;

	public Parser() {
		initRules();
	}

	abstract protected void initRules();

	public ParseTree parse(Sentence sentence) {
		List<ParseTreeNode> nodeList = new ArrayList<ParseTreeNode>();
		
		for (int i = 0; i < sentence.size(); i++) {
			Token token = sentence.get(i);			
			nodeList.add(new ParseTreeNode(token));
		}		
		
		for (int i = 0; i < nodeList.size() - 1; i++) {
			ParseTreeNode ptnPrev = (ParseTreeNode) nodeList.get(i);
		
			System.out.println("*******");
			System.out.println("prev == " + ptnPrev.getToken());
			System.out.println("prev index == " + ptnPrev.getTokenIndex());

			for (int j = i + 1; j < nodeList.size(); j++) {
				ParseTreeNode ptnNext = (ParseTreeNode) nodeList.get(j);

				System.out.println("next == " + ptnNext.getToken());
				System.out.println("next index == " + ptnNext.getTokenIndex());

				ParseTreeEdge arc = null;				
				
				if (ptnPrev.getTokenIndex() == ptnNext.getTokenIndex()) {
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
		for (Iterator<ParseTreeNode> it = nodeList.iterator(); it.hasNext();) {
			ParseTreeNode ptn = (ParseTreeNode) it.next();
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
