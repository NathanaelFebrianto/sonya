package com.nhn.socialanalytics.nlp.kr.r;

import java.awt.Container;

import javax.swing.JFrame;

import com.nhn.socialanalytics.nlp.kr.morpheme.Eojeol;
import com.nhn.socialanalytics.nlp.kr.morpheme.MorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.kr.morpheme.Sentence;
import com.nhn.socialanalytics.nlp.kr.syntax.ParseTree;
import com.nhn.socialanalytics.nlp.kr.syntax.SyntacticAnalyzer;
import com.nhn.socialanalytics.nlp.kr.view.GraphModeller;
import com.nhn.socialanalytics.nlp.kr.view.GraphTreeViewer;

import edu.uci.ics.jung.graph.Forest;


public class Rknlp {

	public Rknlp() {
		
	}
	
	public String[] analyzeMorpheme(String text) {		
		
		MorphemeAnalyzer analyzer = new MorphemeAnalyzer();
		Sentence sentence = analyzer.extractMorphemes(text);
		
		String[] eojeols = new String[sentence.size()];
		
		for (int i = 0; i < sentence.size(); i++) {
			Eojeol eojeol = sentence.get(i);
			eojeols[i] = eojeol.toString();
		}		
		
		return eojeols;
	}
	
	public void showParseTree(String text) {
		
		try {

			if (text == null || text.equals(""))
				text = "철수가 음악에 재능이 없으면서도 노래를 아주 열심히 부르는 것을 영희가 안다.";
			
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
	
}
