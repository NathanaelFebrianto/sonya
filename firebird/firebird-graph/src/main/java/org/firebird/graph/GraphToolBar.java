/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;

import org.firebird.io.dao.ibatis.EdgeDaoiBatis;
import org.firebird.io.dao.ibatis.VertexDaoiBatis;
import org.firebird.io.model.Edge;
import org.firebird.io.model.Vertex;
import org.firebird.io.service.EdgeManager;
import org.firebird.io.service.VertexManager;
import org.firebird.io.service.impl.EdgeManagerImpl;
import org.firebird.io.service.impl.VertexManagerImpl;
import org.firebird.twitter.TwitterDataCollector;

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
public class GraphToolBar {

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
	AbstractAction collectTwitterAction;
	
	/** toolbar group */
	HashMap<String, JPanel> toolbarMap = new HashMap<String, JPanel>();
	List<JPanel> toolbarList = new ArrayList();
	
	/**
	 * Constructor.
	 * 
	 * @param viewer the graph visualization viewer
	 */
	public GraphToolBar(GraphPanel panelGraph) {
		this.panelGraph = panelGraph;
		this.viewer = panelGraph.getGraphViewer();
		this.graph = panelGraph.getGraph();
		
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
	 * Gets the toolbar list.
	 * 
	 * @return List<JPanel> the toolbar list
	 */
	public List<JPanel> getToolBarList() {
		return toolbarList;
	}

	/**
	 * Creates a tool bar.
	 */
	private void createToolBar() 
	{
		JButton button = null;
		
		// group1		
		JPanel panel1 = new JPanel();	
		panel1.setToolTipText("Graph");
		panel1.setLayout(new FlowLayout(FlowLayout.LEFT));
		button = new JButton(panelGraph.getActionMap().get("zoomin"));
		button.setText("+");
		//button.setToolTipText(null);
		//button.setBorderPainted(true);
		//button.setContentAreaFilled(false);
		panel1.add(button);
		
		button = new JButton(panelGraph.getActionMap().get("zoomout"));
		button.setText("-");
		panel1.add(button);
		
		// mouse mode
		JComboBox jcbMouseMode = ((DefaultModalGraphMouse<Vertex, Edge>) viewer.getGraphMouse()).getModeComboBox();
		panel1.add(jcbMouseMode);

		// create layout choose
		Class[] combos = getCombos();
		final JComboBox jcbLayout = new JComboBox(combos);
		// use a renderer to shorten the layout name presentation
		jcbLayout.setRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				String valueString = value.toString();
				valueString = valueString.substring(valueString.lastIndexOf('.') + 1);
				return super.getListCellRendererComponent(list, valueString,
						index, isSelected, cellHasFocus);
			}
		});
		jcbLayout.addActionListener(new LayoutChooser(jcbLayout, viewer));
		jcbLayout.setSelectedItem(FRLayout.class);
		panel1.add(jcbLayout);
		
		toolbarMap.put("Graph", panel1);
		toolbarList.add(panel1);
		
		// group2
		JPanel panel2 = new JPanel();
		panel2.setToolTipText("Data");
		panel2.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		button = new JButton(panelGraph.getActionMap().get("collect.twitter"));
		button.setText("Collect Twitter to DB");
		panel2.add(button);
		
		button = new JButton(panelGraph.getActionMap().get("show.graph"));
		button.setText("Show Graph from DB");
		panel2.add(button);
		
		button = new JButton(panelGraph.getActionMap().get("show.realtime.graph"));
		button.setText("Show Realtime Graph");
		panel2.add(button);
		
		toolbarMap.put("Data", panel2);
		toolbarList.add(panel2);
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
					TwitterDataCollector collector = new TwitterDataCollector();
					collector.setDBStorageMode(false);
					collector.setLevelLimit(2);
					collector.setPeopleLimit(20);
					collector.setDegreeLimit(3);
					collector.setCollectFriendRelationship(true);
					collector.setCollectFollowerRelationship(false);
					collector.setCollectUserBlogEntry(false);

					collector.collectSocialNetwork("louiezzang");    	
					List<Vertex> vertices = collector.getVertices();
					List<Edge> edges = collector.getEdges();

					panelGraph.showGraph(vertices, edges);			
					//panelGraph.getGraphViewer().setGraphLayout(new FRLayout<Vertex, Edge>(panelGraph.getGraph()));
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		
		showGraphAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
		    	VertexManager vertexManager = new VertexManagerImpl(new VertexDaoiBatis());
		    	List<Vertex> vertices = vertexManager.getVertices(1);
		    	
		    	EdgeManager edgeManager = new EdgeManagerImpl(new EdgeDaoiBatis());
		    	List<Edge> edges = edgeManager.getEdges(1, 1);
		    	
		    	panelGraph.showGraph(vertices, edges);			
		    	//panelGraph.getGraphViewer().setGraphLayout(new FRLayout<Vertex, Edge>(panelGraph.getGraph()));    	
			}
		};
		
		collectTwitterAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				try {			
					TwitterDataCollector collector = new TwitterDataCollector();
					collector.setDBStorageMode(true);
					collector.setLevelLimit(3);
					collector.setPeopleLimit(1000);
					collector.setDegreeLimit(100);
					collector.setCollectFriendRelationship(true);
					collector.setCollectFollowerRelationship(true);
					collector.setCollectUserBlogEntry(false);

					collector.collectSocialNetwork("louiezzang");    	
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
		panelGraph.getActionMap().put("zoomin", zoominAction);
		panelGraph.getActionMap().put("zoomout", zoomoutAction);
		panelGraph.getActionMap().put("show.realtime.graph", showRealtimeGraphAction);
		panelGraph.getActionMap().put("show.graph", showGraphAction);
		panelGraph.getActionMap().put("collect.twitter", collectTwitterAction);
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

			Class<? extends Layout> layoutC = (Class<? extends Layout>) jcb.getSelectedItem();

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

	private Class<? extends Layout>[] getCombos() {
		List<Class<? extends Layout>> layouts = new ArrayList<Class<? extends Layout>>();
		layouts.add(KKLayout.class);
		layouts.add(FRLayout.class);
		layouts.add(CircleLayout.class);
		layouts.add(SpringLayout.class);
		layouts.add(SpringLayout2.class);
		layouts.add(ISOMLayout.class);
		return layouts.toArray(new Class[0]);
	}
}
