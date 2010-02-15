/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.firebird.graph.bean.GraphClientHandler;
import org.firebird.graph.view.tool.CollectorPanel;
import org.firebird.graph.view.tool.ScoringPanel;
import org.firebird.io.model.Edge;
import org.firebird.io.model.Vertex;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.util.Animator;

/**
 * A Social network graph toolbar.
 * 
 * @author Young-Gue Bae
 */
public class GraphToolBar extends JTabbedPane {

	private static final long serialVersionUID = 2421586141073868812L;

	/** the graph main panel */
	GraphPanel panelGraph;
	
	/** the visual component and renderer for the graph */
	GraphViewer viewer;

	/** graph */
	Graph<Vertex, Edge> graph;
	
	/** actions */
	AbstractAction zoominAction;
	AbstractAction zoomoutAction;
	AbstractAction showRealtimeGraphAction;
	AbstractAction showGraphAction;
	AbstractAction collectDataAction;
	AbstractAction scoringAction;
	
	/** toolbar group */
	HashMap<String, JPanel> toolbarMap = new HashMap<String, JPanel>();
	
	/** graph client handler */
	GraphClientHandler handler;
	
	/**
	 * Constructor.
	 * 
	 * @param viewer the graph visualization viewer
	 */
	public GraphToolBar(GraphPanel panelGraph) {
		UIHandler.setResourceBundle("graph");
		
		this.panelGraph = panelGraph;
		this.viewer = panelGraph.getGraphViewer();
		this.graph = panelGraph.getGraph();
		this.handler = new GraphClientHandler();
		
		this.
		
		initActions();
	    setActionMap();
	    createToolBar();
	}

	/**
	 * Gets the toolbar map.
	 * 
	 * @return HashMap<String, JPanel> the toolbar map
	 */
	public HashMap<String, JPanel> getToolBarMap() {
		return toolbarMap;
	}
	
	/**
	 * Creates a tool bar.
	 */
	private void createToolBar() 
	{
		JButton button = null;
		
		// toolbar group - control
		JPanel tgControl = new JPanel();	
		tgControl.setLayout(new FlowLayout(FlowLayout.LEFT));
		button = new JButton(this.getActionMap().get("zoomin"));
		//button.setText(UIHandler.getText("zoomin"));		
		button.setIcon(UIHandler.getImageIcon("/list-add.png"));
		tgControl.add(button);
		
		button = new JButton(this.getActionMap().get("zoomout"));
		//button.setText(UIHandler.getText("zoomout"));
		button.setIcon(UIHandler.getImageIcon("/list-remove.png"));
		tgControl.add(button);
		
		// mouse mode
		JComboBox jcbMouseMode = ((DefaultModalGraphMouse<Vertex, Edge>) viewer.getGraphMouse()).getModeComboBox();
		jcbMouseMode.setSelectedIndex(1);
		tgControl.add(jcbMouseMode);

		// create layout choose
		Object[] layouts = getLayoutComobos();
		final JComboBox jcbLayout = new JComboBox(layouts);
		jcbLayout.addActionListener(new LayoutChooser(jcbLayout, viewer));
		jcbLayout.setSelectedItem(FRLayout.class);
		tgControl.add(jcbLayout);		
	
		// toolbar group - graph
		JPanel tgGraph = new JPanel();
		tgGraph.setLayout(new FlowLayout(FlowLayout.LEFT));		
		
		button = new JButton(this.getActionMap().get("show.graph"));
		button.setText(UIHandler.getText("toolbar.show.db.graph"));
		button.setIcon(UIHandler.getImageIcon("/chart_pie.png"));
		tgGraph.add(button);
		
		button = new JButton(this.getActionMap().get("show.realtime.graph"));
		button.setText(UIHandler.getText("toolbar.show.realtime.graph"));
		button.setIcon(UIHandler.getImageIcon("/refresh.png"));
		tgGraph.add(button);
		
		// toolbar group - data
		JPanel tgData = new JPanel();
		tgData.setLayout(new FlowLayout(FlowLayout.LEFT));
		button = new JButton(this.getActionMap().get("collect.data"));
		button.setText(UIHandler.getText("toolbar.collect.data"));
		button.setIcon(UIHandler.getImageIcon("/add_to_folder.png"));
		tgData.add(button);
		
		// toolbar group - analysis
		JPanel tgAnalysis = new JPanel();
		tgAnalysis.setLayout(new FlowLayout(FlowLayout.LEFT));
		button = new JButton(this.getActionMap().get("scoring"));
		button.setText(UIHandler.getText("toolbar.analysis.scoring"));
		button.setIcon(UIHandler.getImageIcon("/chart.png"));
		tgAnalysis.add(button);
		
		button = new JButton();
		button.setText(UIHandler.getText("toolbar.analysis.clustering"));
		button.setIcon(UIHandler.getImageIcon("/users.png"));
		tgAnalysis.add(button);
		
		// create tab menu
		toolbarMap.put("Graph", tgGraph);
		this.addTab(
				UIHandler.getText("toolbar.tab.graph"), 
				tgGraph);
		
		toolbarMap.put("Data", tgData);
		this.addTab(
				UIHandler.getText("toolbar.tab.data"), 
				tgData);
		
		toolbarMap.put("Analysis", tgAnalysis);
		this.addTab(
				UIHandler.getText("toolbar.tab.analysis"), 
				tgAnalysis);
				
		toolbarMap.put("Control", tgControl);		
		this.addTab(
				UIHandler.getText("toolbar.tab.control"), 
				tgControl);
	}

	/**
	 * Initializes actions.
	 */
	private void initActions() {
		
		// scale
		final ScalingControl scaler = new CrossoverScalingControl();
		
		zoominAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				scaler.scale(viewer, 1.1f, viewer.getCenter());
			}
		};
		
		zoomoutAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				scaler.scale(viewer, 1 / 1.1f, viewer.getCenter());
			}
		};
		
		showRealtimeGraphAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				try {												
					CollectorPanel tool = new CollectorPanel(panelGraph);
					tool.setDBStorage(false);
					panelGraph.setLeftToolPanel(e.getActionCommand(), tool);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		
		showGraphAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				try {
			    	List<Vertex> vertices = handler.getVertices(1);
			       	List<Edge> edges = handler.getEdges(1, 1);
			    	
			    	panelGraph.showGraph(vertices, edges);  
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		
		collectDataAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				try {												
					CollectorPanel tool = new CollectorPanel(panelGraph);
					tool.setDBStorage(true);
					panelGraph.setLeftToolPanel(e.getActionCommand(), tool);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		
		scoringAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				try {												
					ScoringPanel tool = new ScoringPanel(panelGraph);
					panelGraph.setLeftToolPanel(e.getActionCommand(), tool);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
	}

	/**
	 * Sets the action map.
	 */
	private void setActionMap() {
		this.getActionMap().put("zoomin", zoominAction);
		this.getActionMap().put("zoomout", zoomoutAction);
		this.getActionMap().put("show.realtime.graph", showRealtimeGraphAction);
		this.getActionMap().put("show.graph", showGraphAction);
		this.getActionMap().put("collect.data", collectDataAction);
		this.getActionMap().put("scoring", scoringAction);
	}

	/**
	 * LayoutChooser.
	 */
	private class LayoutChooser implements ActionListener {
		private final JComboBox jcb;
		private final GraphViewer viewer;

		private LayoutChooser(JComboBox jcb, GraphViewer viewer) {
			super();
			this.jcb = jcb;
			this.viewer = viewer;
		}

		public void actionPerformed(ActionEvent arg0) {
			Object[] constructorArgs = { graph };

			//Class<? extends Layout> layoutC = (Class<? extends Layout>) jcb.getSelectedItem();
			Object selectedItem = jcb.getSelectedItem();
			
			Class<? extends Layout> layoutC = FRLayout.class;;
			if (selectedItem.equals("KKLayout")) 
				layoutC = KKLayout.class;
			else if (selectedItem.equals("FRLayout")) 
				layoutC = FRLayout.class;
			else if (selectedItem.equals("CircleLayout")) 
				layoutC = CircleLayout.class;
			else if (selectedItem.equals("SpringLayout")) 
				layoutC = SpringLayout.class;
			else if (selectedItem.equals("SpringLayout2")) 
				layoutC = SpringLayout2.class;
			else if (selectedItem.equals("ISOMLayout")) 
				layoutC = ISOMLayout.class;
			
			try {
				Constructor<? extends Layout> constructor = layoutC.getConstructor(new Class[] { Graph.class });
				Object o = constructor.newInstance(constructorArgs);
				Layout l = (Layout) o;
				l.setInitializer(viewer.getGraphLayout());
				l.setSize(viewer.getSize());				
				LayoutTransition lt = new LayoutTransition(viewer, viewer.getGraphLayout(), l);
				Animator animator = new Animator(lt);
				animator.start();
				viewer.getRenderContext().getMultiLayerTransformer().setToIdentity();
				viewer.repaint();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private Object[] getLayoutComobos() {
		List<Object> layouts = new ArrayList<Object>();
		layouts.add("KKLayout");
		layouts.add("FRLayout");
		layouts.add("CircleLayout");
		layouts.add("SpringLayout");
		layouts.add("SpringLayout2");
		layouts.add("ISOMLayout");
		return layouts.toArray();
	}
}
