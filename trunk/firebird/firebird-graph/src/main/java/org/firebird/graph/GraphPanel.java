/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import org.firebird.io.model.Edge;
import org.firebird.io.model.Vertex;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;

/**
 * A Social network graph panel.
 * 
 * @author Young-Gue Bae
 */
public class GraphPanel extends JPanel {
	
	private static final long serialVersionUID = -5520707258678283156L;

	/** graph modeller */
	GraphModeller modeller;

	/** the visual component and renderer for the graph */
	GraphViewer viewer;

	/**
	 * Constructor.
	 * 
	 */
	public GraphPanel() {				
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			changeAllSwingComponentDefaultFont();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		// create a graph
		modeller = new GraphModeller();
		
		// create a graph viewer
		viewer = new GraphViewer(new FRLayout<Vertex, Edge>(modeller.getGraph()));		
		GraphZoomScrollPane panelViewer = new GraphZoomScrollPane(viewer);
		
		// create a graph toolbar
		GraphToolBar toolbar = new GraphToolBar(this);		
		
		setLayout(new BorderLayout());
		add(toolbar, BorderLayout.NORTH);
		add(panelViewer, BorderLayout.CENTER);
	}
	
	private void changeAllSwingComponentDefaultFont() {
		try {
			UIDefaults swingComponentDefaultTable = UIManager.getDefaults();
			
			Enumeration allDefaultKey = swingComponentDefaultTable.keys();
			while(allDefaultKey.hasMoreElements()) {
				String defaultKey = allDefaultKey.nextElement().toString();
				if(defaultKey.indexOf("font") != -1) {
					Font newDefaultFont = new Font("³ª´®°íµñ", Font.PLAIN, 12);
					UIManager.put(defaultKey, newDefaultFont);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	/**
	 * Gets the graph.
	 * 
	 * @return Graph<Vertex, Edge> the graph
	 */
	public Graph<org.firebird.io.model.Vertex, org.firebird.io.model.Edge> getGraph() {
		return viewer.getGraph();
	}
	
	/**
	 * Gets the graph viewer.
	 * 
	 * @return GraphViewer the graph viewer
	 */
	public GraphViewer getGraphViewer() {
		return this.viewer;
	}
	
	/**
	 * Shows a graph.
	 * 
	 * @param vertices the vertex list
	 * @param edges the edge list
	 */
	public void showGraph(List<Vertex> vertices, List<Edge> edges) {
		modeller.createGraph(vertices, edges);
	}

	/**
	 * Runs this application.
	 */
	public static void main(String[] args) {
		try {			
			GraphPanel panel = new GraphPanel();
			
			final JFrame frame = new JFrame();
			Container content = frame.getContentPane();
			content.add(panel);
			frame.setTitle("Social Network Analysis");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			frame.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
