/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import org.firebird.graph.view.tool.CollectorPanel;
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
	
	/** split panel */
	JSplitPane panelContent;
	/** left view panel */
	JTabbedPane panelLeft;	
	/** right view panel */
	JTabbedPane panelRight;
	
	/**
	 * Constructor.
	 * 
	 */
	public GraphPanel() {
		UIHandler.setResourceBundle("graph");
		UIHandler.setDefaultLookAndFeel();
		UIHandler.changeAllSwingComponentDefaultFont();		
		
		setupUI();
	}
	
	private void setupUI() {
		setLayout(new BorderLayout());
		
		// create a content panel
		panelContent = new JSplitPane();
		setupLeftPanel();
		setupRightPanel();
		panelContent.setOneTouchExpandable(true);		
		panelContent.setDividerLocation(panelLeft.getPreferredSize().width);
		panelContent.setLeftComponent(panelLeft);
		panelContent.setRightComponent(panelRight);
		
		// create a graph toolbar
		GraphToolBar toolbar = new GraphToolBar(this);
		
		setLayout(new BorderLayout());
		add(toolbar, BorderLayout.NORTH);
		add(panelContent, BorderLayout.CENTER);
	}
	
	private void setupLeftPanel() {
		// create a left default tool panel
		CollectorPanel tool = new CollectorPanel(this);
		tool.setDBStorage(false);
		
		panelLeft = new JTabbedPane();
		panelLeft.addTab(
				UIHandler.getText("toolbar.show.realtime.graph"), 
				UIHandler.getImageIcon("/info.png"),
				tool);
	}
	
	private void setupRightPanel() {
		panelRight = new JTabbedPane(SwingConstants.BOTTOM);
		
		// create a graph
		modeller = new GraphModeller(GraphModeller.DIRECTED_SPARSE_GRAPH);		
		// create a graph viewer
		viewer = new GraphViewer(new FRLayout<Vertex, Edge>(modeller.getGraph()));
		
		GraphZoomScrollPane panelViewer = new GraphZoomScrollPane(viewer);		
		panelRight.addTab(
				UIHandler.getText("right.tab.graph"), 
				UIHandler.getImageIcon("/chart_pie1.png"),
				panelViewer);
		panelRight.addTab(
				UIHandler.getText("right.tab.list"), 
				UIHandler.getImageIcon("/table.png"),
				new JTable());	
	}

	/**
	 * Sets the left tool panel.
	 * 
	 * @param title the tab title
	 * @param tool the tool panel
	 */
	public void setLeftToolPanel(String title, JComponent tool) {
		panelLeft.removeAll();
		panelLeft.addTab(title, UIHandler.getImageIcon("/info.png"), tool);

		panelContent.setDividerLocation(panelLeft.getPreferredSize().width);
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
	 * Gets the content panel.
	 * 
	 * @return JSplitPane the content panel
	 */
	public JSplitPane getContentPanel() {
		return this.panelContent;
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
	 * Shows the list.
	 * 
	 * @param vertices the vertex list
	 * @param edges the edge list
	 */
	public void showList(List<Vertex> vertices, List<Edge> edges) {
		
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
			frame.setTitle(UIHandler.getText("title"));
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			frame.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
