package com.nhn.socialanalytics.nlp.kr.r;

import java.awt.Container;
import java.util.List;

import javax.swing.JFrame;

import com.nhn.socialanalytics.nlp.kr.morpheme.Eojeol;
import com.nhn.socialanalytics.nlp.kr.morpheme.MorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.kr.morpheme.Sentence;
import com.nhn.socialanalytics.nlp.kr.syntax.ParseTree;
import com.nhn.socialanalytics.nlp.kr.syntax.ParseTreeEdge;
import com.nhn.socialanalytics.nlp.kr.syntax.ParseTreeNode;
import com.nhn.socialanalytics.nlp.kr.syntax.SyntacticAnalyzer;
import com.nhn.socialanalytics.nlp.kr.view.GraphModeller;
import com.nhn.socialanalytics.nlp.kr.view.GraphTreeViewer;

import edu.uci.ics.jung.graph.Forest;


public class Rknlp {
	
	private String defaultText = "철수가 음악에 재능이 없으면서도 노래를 아주 열심히 부르는 것을 영희가 안다.";

	public Rknlp() {
		
	}
	
	public String[] morphemeList(String text) {		
		if (text == null || text.equals(""))
			text = defaultText;
		
		MorphemeAnalyzer analyzer = new MorphemeAnalyzer();
		Sentence sentence = analyzer.extractMorphemes(text);
		
		String[] eojeols = new String[sentence.size()];
		
		for (int i = 0; i < sentence.size(); i++) {
			Eojeol eojeol = sentence.get(i);
			eojeols[i] = eojeol.toString();
		}		
		
		return eojeols;
	}

	/*
	public String morphemes(String text) {
		if (text == null || text.equals(""))
			text = defaultText;
		
		MorphemeAnalyzer analyzer = new MorphemeAnalyzer();
		Sentence sentence = analyzer.extractMorphemes(text);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("sentence", sentence);
	
		String strJson = jsonObj.toString(1);
		return strJson;
	}
	
	public String parseTree(String text) {		
		if (text == null || text.equals(""))
			text = defaultText;
		
		SyntacticAnalyzer analyzer = SyntacticAnalyzer.getInstance();
		ParseTree tree = analyzer.parseTree(text);
		List<ParseTreeNode> nodes = tree.getNodeList();
		List<ParseTreeEdge> edges = tree.getEdgeList();
		
		JSONObject jsonObj = new JSONObject();
		
		for (int i = 0 ; i < nodes.size(); i++) {
			ParseTreeNode node = nodes.get(i);
			int id = node.getId();
			Eojeol eojeol = node.getEojeol();			
			
			if (eojeol == null) {
				jsonObj.accumulate("nodes", "{\"id\":" + id + "}");
			}
			else {
				eojeol.setId(id);
				jsonObj.accumulate("nodes", eojeol);
			}							
		}
		
		for (int j = 0 ; j < edges.size(); j++) {
			ParseTreeEdge edge = edges.get(j);
			jsonObj.accumulate("edges", "{\"from\":" + edge.getFromId() + ",\"to\":" + edge.getToId() + "}");
		}
	
		String strJson = jsonObj.toString(1);
		return strJson;
	}
	*/

	public String morphemes(String text) {
		if (text == null || text.equals(""))
			text = defaultText;
		
		MorphemeAnalyzer analyzer = new MorphemeAnalyzer();
		Sentence sentence = analyzer.extractMorphemes(text);
		
		StringBuffer json = new StringBuffer();
		json.append("{'sentence':[");
		
		for (int i = 0; i < sentence.size(); i++) {
			Eojeol eojeol = sentence.get(i);
			json.append(eojeol.toJSON());
			if (i < sentence.size()-1)
				json.append(",");	
		}	
		
		json.append("]");	
		json.append("}");	

		return json.toString();
	}
	
	public String parseTree(String text) {		
		if (text == null || text.equals(""))
			text = defaultText;
		
		SyntacticAnalyzer analyzer = SyntacticAnalyzer.getInstance();
		ParseTree tree = analyzer.parseTree(text);
		List<ParseTreeNode> nodes = tree.getNodeList();
		List<ParseTreeEdge> edges = tree.getEdgeList();
		
		StringBuffer json = new StringBuffer();
		json.append("{'nodes':[");
		
		for (int i = 0 ; i < nodes.size(); i++) {
			ParseTreeNode node = nodes.get(i);
			int id = node.getId();
			Eojeol eojeol = node.getEojeol();
			
			if (eojeol == null) {
				json.append("{'id':" + id + "}");
			}
			else {
				eojeol.setId(id);
				json.append(eojeol.toJSON());
			}
			
			if (i < nodes.size()-1)
				json.append(",");			
		}
		
		json.append("]");
		
		json.append(",'edges':[");
		for (int j = 0 ; j < edges.size(); j++) {
			ParseTreeEdge edge = edges.get(j);
			json.append("{'from':" + edge.getFromId() + ",'to':" + edge.getToId() + "}");
			
			if (j < nodes.size()-1)
				json.append(",");	
		}
		
		json.append("]");
		json.append("}");

		return json.toString();
	}
	
	public void showParseTree(String text) {
		
		try {

			if (text == null || text.equals(""))
				text = defaultText;
			
			SyntacticAnalyzer analyzer = SyntacticAnalyzer.getInstance();
			ParseTree tree = analyzer.parseTree(text);
			
			GraphModeller modeller = new GraphModeller(GraphModeller.DIRECTED_SPARSE_GRAPH);
			modeller.createGraph(tree.getNodeList(), tree.getEdgeList());			

			Forest graph = modeller.getGraph();
			
			JFrame frame = new JFrame();
			Container content = frame.getContentPane();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			GraphTreeViewer viewer = new GraphTreeViewer(graph);
			viewer.setSourceText(text);
			
			content.add(viewer);
			frame.pack();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Rknlp rknlp = new Rknlp();
		String text = "철수가 음악에 재능이 없으면서도 노래를 아주 열심히 부르는 것을 영희가 안다.";
		System.out.println(rknlp.morphemes(text));
		//System.out.println(rknlp.parseTree(text));
	}
	
}
