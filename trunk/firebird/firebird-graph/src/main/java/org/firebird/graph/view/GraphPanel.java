/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

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
	SatelliteGraphViewer satelliteViewer;

	/** split panel for content */
	JSplitPane spaneContent;
	/** left tab panel for tool */
	JTabbedPane tpaneTool;
	/** right split panel */
	JSplitPane spaneRight;
	/** right center tab panel */
	JTabbedPane tpaneRightCenter;
	/** right bottom tab panel */
	JTabbedPane tpaneRightBottom;
	
	/** vertices table */
	VertexTable tblVertices;
	/** edges table */
	EdgeTable tblEdges;

	/**
	 * Constructor.
	 * 
	 */
	public GraphPanel() {
		UIHandler.setResourceBundle("graph");
		UIHandler.setDefaultLookAndFeel();
		UIHandler.changeAllSwingComponentDefaultFont();

		setupGraphView();
		setupUI();
	}

	private void setupGraphView() {
		// create a graph
		modeller = new GraphModeller(GraphModeller.DIRECTED_SPARSE_GRAPH);

		// create a graph viewer
		int width = 900;
		int height = 600;
		viewer = new GraphViewer(new FRLayout<Vertex, Edge>(modeller.getGraph()), 
				new Dimension(900, 600));
		satelliteViewer = new SatelliteGraphViewer(viewer, 
				new Dimension(width / 4, height / 4));
	}

	private void setupUI() {
		setLayout(new BorderLayout());

		// create a content panel
		spaneContent = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JComponent panelLeft = setupLeftPanel();
		JComponent panelRight = setupRightPanel();

		spaneContent.setOneTouchExpandable(true);
		spaneContent.setDividerLocation(panelLeft.getPreferredSize().width);
		spaneContent.setLeftComponent(panelLeft);
		spaneContent.setRightComponent(panelRight);

		// create a graph toolbar
		GraphToolBar toolbar = new GraphToolBar(this);

		setLayout(new BorderLayout());
		add(toolbar, BorderLayout.NORTH);
		add(spaneContent, BorderLayout.CENTER);
	}

	private JComponent setupLeftPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		// create a left default tool panel
		CollectorPanel tool = new CollectorPanel(this);
		tool.setDBStorage(false);

		tpaneTool = new JTabbedPane();
		tpaneTool.addTab(UIHandler.getText("toolbar.show.realtime.graph"),
				UIHandler.getImageIcon("/info.png"), tool);

		JPanel panelSatelliteViewer = new JPanel();
		//panelSatelliteViewer.setBorder(LineBorder.createGrayLineBorder());
		//panelSatelliteViewer.setBorder(new EtchedBorder(EtchedBorder.RAISED));

		panelSatelliteViewer.add(satelliteViewer);

		panel.add(tpaneTool, BorderLayout.NORTH);
		panel.add(panelSatelliteViewer, BorderLayout.SOUTH);

		return panel;
	}

	private JComponent setupRightPanel() {
		spaneRight = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

		spaneRight.setOneTouchExpandable(true);
		spaneRight.setDividerLocation(490);
		spaneRight.setTopComponent(setupRightCenterPanel());
		spaneRight.setBottomComponent(setupRightBottomPanel());

		return spaneRight;
	}

	private JComponent setupRightCenterPanel() {
		tpaneRightCenter = new JTabbedPane(SwingConstants.TOP);

		GraphZoomScrollPane panelViewer = new GraphZoomScrollPane(viewer);		
		tpaneRightCenter.addTab(
				UIHandler.getText("content.tab.graph"), 
				UIHandler.getImageIcon("/bug.gif"), 
				panelViewer);
		
		return tpaneRightCenter;
	}
	
	private JComponent setupRightBottomPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		
		JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		JButton btnInitColor = new JButton(UIHandler.getText("button.init.color"));
		ActionListener initColorGraphActionListener = (ActionListener)(GenericListener.create(
		        ActionListener.class,
				"actionPerformed",
				this,
				"initColorGraphAction"));		
		btnInitColor.addActionListener(initColorGraphActionListener);
		
		JButton btnColor = new JButton(UIHandler.getText("button.paint.color"));
		ActionListener colorGraphActionListener = (ActionListener)(GenericListener.create(
		        ActionListener.class,
				"actionPerformed",
				this,
				"colorGraphAction"));		
		btnColor.addActionListener(colorGraphActionListener);
		
		panelButtons.add(btnInitColor);
		panelButtons.add(btnColor);
		
		tpaneRightBottom = new JTabbedPane(SwingConstants.BOTTOM);

		tblVertices = new VertexTable();
		
		tblEdges = new EdgeTable();
		
		tpaneRightBottom.addTab(
				UIHandler.getText("content.tab.vertex"), 
				UIHandler.getImageIcon("/vertex_s.gif"), 
				new JScrollPane(tblVertices));
		
		tpaneRightBottom.addTab(
				UIHandler.getText("content.tab.edge"), 
				UIHandler.getImageIcon("/edge_s.gif"), 
				new JScrollPane(tblEdges));
		
		panel.add(panelButtons, BorderLayout.NORTH);
		panel.add(tpaneRightBottom, BorderLayout.CENTER);

		return panel;
	}

	/**
	 * Sets the left tool panel.
	 * 
	 * @param title	the tab title
	 * @param tool the tool panel
	 */
	public void setLeftToolPanel(String title, JComponent tool) {
		tpaneTool.removeAll();
		tpaneTool.addTab(title, UIHandler.getImageIcon("/info.png"), tool);

		spaneContent.setDividerLocation(tpaneTool.getPreferredSize().width);
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
		return this.spaneContent;
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
		modeller.clearGraph();
		modeller.createGraph(vertices, edges);
		viewer.setGraphLayout(new FRLayout<Vertex, Edge>(viewer.getGraph()));
		
		this.showGraphData(vertices, edges);
	}

	/**
	 * Shows the graph data with vertices and edges.
	 * 
	 * @param vertices the vertex list
	 * @param edges the edge list
	 */
	public void showGraphData(List<Vertex> vertices, List<Edge> edges) {
		tblVertices.setVertices(vertices);
		tpaneRightBottom.setTitleAt(0, UIHandler.getText("content.tab.vertex") + "(" + vertices.size() + ")");
		
		tblEdges.setEdges(edges);
		tpaneRightBottom.setTitleAt(1, UIHandler.getText("content.tab.edge") + "(" + edges.size() + ")");
	}

	/**
	 * Runs this application.
	 */
	public static void main(String[] args) {
		try {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					try {						
						//UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceBusinessLookAndFeel");
						//UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceBusinessBlueSteelLookAndFeel");
						//UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel");
						//UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceRavenGraphiteLookAndFeel");
						UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceCremeLookAndFeel");
						//UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceCremeCoffeeLookAndFeel");
						//UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceSaharaLookAndFeel");
						//UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceModerateLookAndFeel");
						//UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceOfficeSilver2007LookAndFeel");
						//UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceOfficeBlue2007LookAndFeel");
						//UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceNebulaLookAndFeel");
						//UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceNebulaBrickWallLookAndFeel");
						//UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceAutumnLookAndFeel");
						//UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceMistSilverLookAndFeel");
						//UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceMistAquaLookAndFeel");
						//UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceDustLookAndFeel");
						//UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceDustCoffeeLookAndFeel");
						//UIManager.setLookAndFeel("org.jvnet.substance.api.skin.SubstanceGeminiLookAndFeel");

					} catch (Exception ex) {
						ex.printStackTrace();
					}
					GraphPanel panel = new GraphPanel();
					final JFrame frame = new JFrame();
					Container content = frame.getContentPane();
					content.add(panel);
					frame.setTitle(UIHandler.getText("title"));
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.pack();
					frame.setVisible(true);
				}
			});

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	//////////////////////////////////////////
	/*       Defines event actions.         */
	//////////////////////////////////////////
	
	public void initColorGraphAction(ActionEvent e) {
		viewer.initColor();
		satelliteViewer.initColor();
	}
	
	public void colorGraphAction(ActionEvent e) {
		if (tpaneRightBottom.getSelectedIndex() == 0) {
			int[] rows = tblVertices.getSelectedRows();
			Set<String> vertices = new HashSet<String>();
			for (int row : rows) {
				String vertexId = (String)tblVertices.getValueAt(row, 1);
				System.out.println("selected vertex id == " + vertexId);
				vertices.add(vertexId);
			}
			Color color = viewer.colorVertices(vertices);
			satelliteViewer.colorVertices(vertices, color);			
		}
		else if (tpaneRightBottom.getSelectedIndex() == 1) {
			int[] rows = tblEdges.getSelectedRows();
			Set<String[]> vertexPairs = new HashSet<String[]>();
			for (int row : rows) {
				String[] vertexPair = new String[2];
				vertexPair[0] = (String)tblEdges.getValueAt(row, 1);
				vertexPair[1] = (String)tblEdges.getValueAt(row, 2);
				System.out.println("selected vertex1 -> vertex2 == " + vertexPair[0] + " -> " + vertexPair[1]);
				vertexPairs.add(vertexPair);
			}
			Color color = viewer.colorEdges(vertexPairs);
			satelliteViewer.colorEdges(vertexPairs, color);			
		}

	}
	
}
