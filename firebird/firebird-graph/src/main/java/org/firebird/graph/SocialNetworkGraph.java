/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import org.apache.commons.collections15.Transformer;
import org.firebird.twitter.TwitterDataCollector;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.renderers.DefaultEdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import edu.uci.ics.jung.visualization.util.Animator;

/**
 * A Social network graph.
 * 
 * @author Young-Gue Bae
 */
public class SocialNetworkGraph {

	/** graph */
	Graph<org.firebird.io.model.Vertex, org.firebird.io.model.Edge> graph;

	/** the visual component and renderer for the graph */
	VisualizationViewer<org.firebird.io.model.Vertex, org.firebird.io.model.Edge> vv;

	/** layout */
	Layout layout;

	/**
	 * Constructor.
	 * 
	 */
	public SocialNetworkGraph(List<org.firebird.io.model.Vertex> vertices, List<org.firebird.io.model.Edge> edges) {
		try {
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			changeAllSwingComponentDefaultFont();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		// create a simple graph
		graph = new DirectedSparseGraph<org.firebird.io.model.Vertex, org.firebird.io.model.Edge>();
		HashMap<String, org.firebird.io.model.Vertex> verticesMap = createVertices(vertices);
		createEdges(verticesMap, edges);

		vv = new VisualizationViewer<org.firebird.io.model.Vertex, org.firebird.io.model.Edge>(
				new FRLayout<org.firebird.io.model.Vertex, org.firebird.io.model.Edge>(graph));
		vv.getRenderContext().setVertexLabelTransformer(
				new Transformer<org.firebird.io.model.Vertex, String>() {

					public String transform(org.firebird.io.model.Vertex v) {
						return v.getUserName();
					}
				});
		
		vv.getRenderContext().setVertexLabelRenderer(new DefaultVertexLabelRenderer(Color.cyan));
		vv.getRenderContext().setEdgeLabelRenderer(new DefaultEdgeLabelRenderer(Color.cyan));
		
		vv.getRenderContext().setVertexIconTransformer(
				new Transformer<org.firebird.io.model.Vertex, Icon>() {

					/*
					 * Implements the Icon interface to draw an Icon with
					 * background color and a text label
					 */
					public Icon transform(final org.firebird.io.model.Vertex v) {
						return new Icon() {

							public int getIconHeight() {
								return 20;
							}

							public int getIconWidth() {
								return 20;
							}

							public void paintIcon(Component c, Graphics g, int x, int y) {
								if (vv.getPickedVertexState().isPicked(v)) {
									g.setColor(Color.yellow);
								} else {
									g.setColor(Color.red);
								}
								g.fillOval(x, y, 20, 20);
								if (vv.getPickedVertexState().isPicked(v)) {
									g.setColor(Color.black);
								} else {
									g.setColor(Color.white);
								}
								// g.drawString("" + v, x + 6, y + 15);
								g.drawString("", x + 6, y + 15);
							}
						};
					}
				});

		vv.getRenderContext().setVertexFillPaintTransformer(
						new PickableVertexPaintTransformer<org.firebird.io.model.Vertex>(
								vv.getPickedVertexState(), Color.white, Color.yellow));
		vv.getRenderContext().setEdgeDrawPaintTransformer(
				new PickableEdgePaintTransformer<org.firebird.io.model.Edge>(
						vv.getPickedEdgeState(), Color.black, Color.lightGray));

		vv.setBackground(Color.white);

		// add my listener for ToolTips
		//vv.setVertexToolTipTransformer(new ToStringLabeller<org.firebird.io.model.Vertex>());
		vv.setVertexToolTipTransformer(
				new Transformer<org.firebird.io.model.Vertex, String>() {
					public String transform(final org.firebird.io.model.Vertex v) {
						return v.getLastBlogEntryBody(); 
					}
				});

		// set vertex font
		vv.getRenderContext().setVertexFontTransformer(
				new Transformer<org.firebird.io.model.Vertex, Font>() {
					public Font transform(final org.firebird.io.model.Vertex v) {
						return new Font("³ª´®°íµñ", Font.PLAIN, 12);
					}
				});
		
		// create a frame to hold the graph
		final JFrame frame = new JFrame();
		Container content = frame.getContentPane();
		final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
		content.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final ModalGraphMouse gm = new DefaultModalGraphMouse<Integer, Number>();
		vv.setGraphMouse(gm);

		final ScalingControl scaler = new CrossoverScalingControl();

		JButton plus = new JButton("+");
		plus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scaler.scale(vv, 1.1f, vv.getCenter());
			}
		});
		JButton minus = new JButton("-");
		minus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scaler.scale(vv, 1 / 1.1f, vv.getCenter());
			}
		});

		// create layout choose
		Class[] combos = getCombos();
		final JComboBox jcb = new JComboBox(combos);
		// use a renderer to shorten the layout name presentation
		jcb.setRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				String valueString = value.toString();
				valueString = valueString.substring(valueString.lastIndexOf('.') + 1);
				return super.getListCellRendererComponent(list, valueString,
						index, isSelected, cellHasFocus);
			}
		});
		jcb.addActionListener(new LayoutChooser(jcb, vv));
		jcb.setSelectedItem(FRLayout.class);

		JPanel controls = new JPanel();
		controls.add(plus);
		controls.add(minus);
		controls.add(((DefaultModalGraphMouse<org.firebird.io.model.Vertex, org.firebird.io.model.Edge>) gm)
						.getModeComboBox());
		controls.add(jcb);
		content.add(controls, BorderLayout.SOUTH);

		frame.pack();
		frame.setVisible(true);
	}

	private class LayoutChooser implements ActionListener {
		private final JComboBox jcb;
		private final VisualizationViewer vv;

		private LayoutChooser(JComboBox jcb, VisualizationViewer vv) {
			super();
			this.jcb = jcb;
			this.vv = vv;
		}

		public void actionPerformed(ActionEvent arg0) {
			Object[] constructorArgs = { graph };

			Class<? extends Layout> layoutC = (Class<? extends Layout>) jcb.getSelectedItem();

			try {
				Constructor<? extends Layout> constructor = layoutC.getConstructor(new Class[] { Graph.class });
				Object o = constructor.newInstance(constructorArgs);
				Layout l = (Layout) o;
				l.setInitializer(vv.getGraphLayout());
				l.setSize(vv.getSize());
				layout = l;
				LayoutTransition lt = new LayoutTransition(vv, vv.getGraphLayout(), l);
				Animator animator = new Animator(lt);
				animator.start();
				vv.getRenderContext().getMultiLayerTransformer().setToIdentity();
				vv.repaint();

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

	/**
	 * Creates the vertices.
	 * 
	 */
	private HashMap<String, org.firebird.io.model.Vertex> createVertices(List<org.firebird.io.model.Vertex> vertices) {
		HashMap<String, org.firebird.io.model.Vertex> verticesMap = new HashMap<String, org.firebird.io.model.Vertex>();

		for (int i = 0; i < vertices.size(); i++) {
			org.firebird.io.model.Vertex vertex = (org.firebird.io.model.Vertex) vertices.get(i);
			graph.addVertex(vertex);
			verticesMap.put(vertex.getId(), vertex);
		}
		return verticesMap;
	}

	/**
	 * Creates edges for this graph
	 * 
	 */
	private void createEdges(HashMap<String, org.firebird.io.model.Vertex> vertices,
			List<org.firebird.io.model.Edge> edges) {

		for (int i = 0; i < edges.size(); i++) {
			org.firebird.io.model.Edge edge = (org.firebird.io.model.Edge) edges.get(i);
			graph.addEdge(edge, vertices.get(edge.getVertex1()), vertices.get(edge.getVertex2()), EdgeType.DIRECTED);
		}
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
	 * a driver for this demo
	 */
	public static void main(String[] args) {
		try {
			TwitterDataCollector collector = new TwitterDataCollector();
			collector.setDBStorageMode(false);
			collector.setLevelLimit(2);
			collector.setPeopleLimit(100);
			collector.setDegreeLimit(3);
			collector.setCollectFriendRelationship(true);
			collector.setCollectFollowerRelationship(true);
			collector.setCollectUserBlogEntry(false);

			collector.collectSocialNetwork("louiezzang");    	
			List<org.firebird.io.model.Vertex> vertices = collector.getVertices();
			List<org.firebird.io.model.Edge> edges = collector.getEdges();

			new SocialNetworkGraph(vertices, edges);		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
