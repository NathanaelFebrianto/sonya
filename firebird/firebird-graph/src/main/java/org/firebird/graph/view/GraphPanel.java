/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
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

	/** split panel */
	JSplitPane panelContent;
	/** left tab tool panel */
	JTabbedPane tpanelTool;
	/** right view panel */
	JSplitPane panelRight;
	/** right bottom panel */
	JTabbedPane panelRightBottom;

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
		viewer = new GraphViewer(
				new FRLayout<Vertex, Edge>(modeller.getGraph()), new Dimension(
						900, 600));
		satelliteViewer = new SatelliteGraphViewer(viewer, new Dimension(
				width / 4, height / 4));
	}

	private void setupUI() {
		setLayout(new BorderLayout());

		// create a content panel
		panelContent = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JComponent panelLeft = setupLeftPanel();
		JComponent panelRight = setupRightPanel();

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

	private JComponent setupLeftPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		// create a left default tool panel
		CollectorPanel tool = new CollectorPanel(this);
		tool.setDBStorage(false);

		tpanelTool = new JTabbedPane();
		tpanelTool.addTab(UIHandler.getText("toolbar.show.realtime.graph"),
				UIHandler.getImageIcon("/info.png"), tool);

		JPanel panelSatelliteViewer = new JPanel();
		// panelSatelliteViewer.setBorder(LineBorder.createGrayLineBorder());
		//panelSatelliteViewer.setBorder(new EtchedBorder(EtchedBorder.RAISED));

		panelSatelliteViewer.add(satelliteViewer);

		panel.add(tpanelTool, BorderLayout.NORTH);
		panel.add(panelSatelliteViewer, BorderLayout.SOUTH);

		return panel;
	}

	private JComponent setupRightPanel() {
		panelRight = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

		GraphZoomScrollPane panelViewer = new GraphZoomScrollPane(viewer);

		panelRight.setOneTouchExpandable(true);
		panelRight.setDividerLocation(550);
		panelRight.setTopComponent(panelViewer);
		panelRight.setBottomComponent(setupRightBottomPanel());

		return panelRight;
	}

	private JComponent setupRightBottomPanel() {
		panelRightBottom = new JTabbedPane(SwingConstants.TOP);

		panelRightBottom.addTab(UIHandler.getText("right.tab.list"), UIHandler
				.getImageIcon("/table.png"), new JTable());

		return panelRightBottom;
	}

	/**
	 * Sets the left tool panel.
	 * 
	 * @param title
	 *            the tab title
	 * @param tool
	 *            the tool panel
	 */
	public void setLeftToolPanel(String title, JComponent tool) {
		tpanelTool.removeAll();
		tpanelTool.addTab(title, UIHandler.getImageIcon("/info.png"), tool);

		panelContent.setDividerLocation(tpanelTool.getPreferredSize().width);
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
	 * @param vertices
	 *            the vertex list
	 * @param edges
	 *            the edge list
	 */
	public void showGraph(List<Vertex> vertices, List<Edge> edges) {
		modeller.createGraph(vertices, edges);
	}

	/**
	 * Shows the list.
	 * 
	 * @param vertices
	 *            the vertex list
	 * @param edges
	 *            the edge list
	 */
	public void showList(List<Vertex> vertices, List<Edge> edges) {

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
			/*
			 * GraphPanel panel = new GraphPanel(); final JFrame frame = new
			 * JFrame(); Container content = frame.getContentPane();
			 * content.add(panel); frame.setTitle(UIHandler.getText("title"));
			 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			 * frame.pack(); frame.setVisible(true);
			 */

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
