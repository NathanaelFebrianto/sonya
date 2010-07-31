/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.view;

import java.applet.AppletContext;
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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.firebird.graph.bean.GraphClientHandler;
import org.firebird.graph.view.recommend.RecommendPanel;
import org.firebird.graph.view.tool.CollectToolPanel;
import org.firebird.graph.view.topic.TopicPanel;
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

	/** applet context */
	AppletContext context = null;
	
	/** graph client handler */
	GraphClientHandler handler;
	
	/** graph modeller */
	//GraphModeller modeller;
	SimpleGraphModeller modeller;

	/** the visual component and renderer for the graph */
	//GraphViewer viewer;
	//SatelliteGraphViewer satelliteViewer;
	SimpleGraphViewer viewer;
	SimpleSatelliteGraphViewer satelliteViewer;
	
	/** topic panel */
	TopicPanel panelRightTopic;
	
	/** recommend panel */
	RecommendPanel panelRightRecommend;

	/** split panel for content */
	JSplitPane spaneContent;
	/** left tab panel for tool */
	JTabbedPane tpaneTool;
	/** right split panel for graph */
	JSplitPane spaneRightGraph;
	/** right split panel for topics */
	JSplitPane spaneRightTopics;
	/** right main tab panel */
	JTabbedPane tpaneRight;
	/** right bottom tab panel */
	JTabbedPane tpaneRightBottom;
	
	/** vertices table */
	VertexTable tblVertices;
	/** edges table */
	EdgeTable tblEdges;
	
	/** button to refresh table*/
	JButton btnRefresh;

	/**
	 * Constructor.
	 * 
	 */
	public GraphPanel() {
		UIHandler.setResourceBundle("graph");
		UIHandler.setDefaultLookAndFeel();
		UIHandler.changeAllSwingComponentDefaultFont();
		
		this.handler = new GraphClientHandler();		

		setupGraphView();
		setupUI();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param context the applet context
	 */
	public GraphPanel(AppletContext context) {
		this.context = context;
		UIHandler.setResourceBundle("graph");
		UIHandler.setDefaultLookAndFeel();
		UIHandler.changeAllSwingComponentDefaultFont();
		
		this.handler = new GraphClientHandler();		

		setupGraphView();
		setupUI();
	}

	private void setupGraphView() {
		// create a graph
		//modeller = new GraphModeller(GraphModeller.DIRECTED_SPARSE_GRAPH);
		modeller = new SimpleGraphModeller(SimpleGraphModeller.DIRECTED_SPARSE_GRAPH);

		// create a graph viewer
		int width = 900;
		int height = 600;
		/*
		viewer = new GraphViewer(new FRLayout<Vertex, Edge>(modeller.getGraph()), 
				new Dimension(900, 600));
		satelliteViewer = new SatelliteGraphViewer(viewer, 
				new Dimension(width / 4, height / 4));
		*/
		viewer = new SimpleGraphViewer(new FRLayout<String, String>(modeller.getGraph()), 
				new Dimension(900, 600));
		satelliteViewer = new SimpleSatelliteGraphViewer(viewer, 
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
		CollectToolPanel tool = new CollectToolPanel(this);
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
		// graph panel
		spaneRightGraph = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		spaneRightGraph.setOneTouchExpandable(true);
		spaneRightGraph.setDividerLocation(490);
		spaneRightGraph.setTopComponent(setupRightTopGraphViewPanel());
		spaneRightGraph.setBottomComponent(setupRightBottomGraphTablePanel());
		
		// topics panel
		panelRightTopic = new TopicPanel(this);		
		
		// recommend panel
		panelRightRecommend = new RecommendPanel(this);	
		
		// right main tabbed panel
		tpaneRight = new JTabbedPane(SwingConstants.TOP);
		tpaneRight.addTab(
				UIHandler.getText("content.tab.graph"), 
				UIHandler.getImageIcon("/bug.gif"), 
				spaneRightGraph);
		
		tpaneRight.addTab(
				UIHandler.getText("content.tab.topics"), 
				UIHandler.getImageIcon("/bug.gif"), 
				panelRightTopic);
		
		tpaneRight.addTab(
				UIHandler.getText("content.tab.recommend"), 
				UIHandler.getImageIcon("/bug.gif"), 
				panelRightRecommend);
		
		return tpaneRight;
	}

	private JComponent setupRightTopGraphViewPanel() {
		GraphZoomScrollPane panelViewer = new GraphZoomScrollPane(viewer);		
		return panelViewer;
	}
	
	private JComponent setupRightBottomGraphTablePanel() {
		JPanel panel = new JPanel(new BorderLayout());
		
		JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		btnRefresh = new JButton(UIHandler.getText("button.refresh.table"));
		ActionListener refreshTableActionListener = (ActionListener)(GenericListener.create(
		        ActionListener.class,
				"actionPerformed",
				this,
				"refreshTableAction"));		
		btnRefresh.addActionListener(refreshTableActionListener);
		
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
		
		panelButtons.add(btnRefresh);
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
	/*
	public Graph<org.firebird.io.model.Vertex, org.firebird.io.model.Edge> getGraph() {
		return viewer.getGraph();
	}
	*/
	public Graph<String, String> getGraph() {
		return viewer.getGraph();
	}
	
	/**
	 * Gets the applet context.
	 * 
	 * @return AppletContext the applet context
	 */
	public AppletContext getAppletContext() {
		return context;
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
	 * Gets the satellite graph viewer.
	 * 
	 * @return GraphViewer the graph viewer
	 */
	/*
	public SatelliteGraphViewer getSatelliteGraphViewer() {
		return this.satelliteViewer;
	}
	*/
	public SimpleSatelliteGraphViewer getSatelliteGraphViewer() {
		return this.satelliteViewer;
	}
	
	/**
	 * Gets the graph viewer.
	 * 
	 * @return GraphViewer the graph viewer
	 */
	/*
	public GraphViewer getGraphViewer() {
		return this.viewer;
	}
	*/
	public SimpleGraphViewer getGraphViewer() {
		return this.viewer;
	}
	
	/**
	 * Gets the right tabbed panel.
	 * 
	 * @return JTabbedPane the right tabbed panel
	 */
	public JTabbedPane getRightTabbedPanel() {
		return this.tpaneRight;
	}
	
	/**
	 * Gets the topic panel in the right tabbed panel.
	 * 
	 * @return TopicPanel the topic panel
	 */
	public TopicPanel getRightTopicPanel() {
		return this.panelRightTopic;
	}
	
	/**
	 * Gets the recommend panel in the right tabbed panel.
	 * 
	 * @return RecommendPanel the recommend panel
	 */
	public RecommendPanel getRightRecommendPanel() {
		return this.panelRightRecommend;
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
		//viewer.setGraphLayout(new FRLayout<Vertex, Edge>(viewer.getGraph()));
		viewer.setGraphLayout(new FRLayout<String, String>(viewer.getGraph()));
		
		this.showGraphData(vertices, edges);
	}

	/**
	 * Shows the graph data with vertices and edges.
	 * 
	 * @param vertices the vertex list
	 * @param edges the edge list
	 */
	public void showGraphData(List<Vertex> vertices, List<Edge> edges) {
		tblVertices.setRowData(vertices);
		tpaneRightBottom.setTitleAt(0, UIHandler.getText("content.tab.vertex") + "(" + vertices.size() + ")");
		
		tblEdges.setRowData(edges);
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
	
	/**
	 * Sets if the refresh table button is enabled or not.
	 * 
	 * @param enabled true if enabled
	 */
	public void setRefreshTableButtonEnabled(boolean enabled) {
		btnRefresh.setEnabled(enabled);
	}
	
	//////////////////////////////////////////
	/*       Defines event actions.         */
	//////////////////////////////////////////
	
	public void refreshTableAction(ActionEvent e) {
		try {
			List<Vertex> vertices = handler.getVertices(1);
	       	List<Edge> edges = handler.getEdges(1, 1);
	    	
	       	this.showGraphData(vertices, edges);  
		} catch (Exception ex) {
			ex.printStackTrace();
		}	
	}
	
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
