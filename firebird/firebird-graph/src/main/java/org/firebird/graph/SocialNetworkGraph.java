/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.collections15.Transformer;
import org.firebird.io.dao.ibatis.EdgeDaoiBatis;
import org.firebird.io.dao.ibatis.VertexDaoiBatis;
import org.firebird.io.service.EdgeManager;
import org.firebird.io.service.VertexManager;
import org.firebird.io.service.impl.EdgeManagerImpl;
import org.firebird.io.service.impl.VertexManagerImpl;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.samples.DrawnIconVertexDemo;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.DefaultEdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;

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

	/**
	 * Constructor.
	 * 
	 */
	public SocialNetworkGraph() {

		// create a simple graph
		graph = new DirectedSparseGraph<org.firebird.io.model.Vertex, org.firebird.io.model.Edge>();
		HashMap<String, org.firebird.io.model.Vertex> v = createVertices(1);
		List<org.firebird.io.model.Edge> edges = createEdges(1, v);

		vv = new VisualizationViewer<org.firebird.io.model.Vertex, org.firebird.io.model.Edge>(
				new FRLayout<org.firebird.io.model.Vertex, org.firebird.io.model.Edge>(graph));
		vv.getRenderContext().setVertexLabelTransformer(
				new Transformer<org.firebird.io.model.Vertex, String>() {

					public String transform(org.firebird.io.model.Vertex v) {
						return v.getUserName();
					}
				});
		vv.getRenderContext().setVertexLabelRenderer(
				new DefaultVertexLabelRenderer(Color.cyan));
		vv.getRenderContext().setEdgeLabelRenderer(
				new DefaultEdgeLabelRenderer(Color.cyan));
		
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

							public void paintIcon(Component c, Graphics g,
									int x, int y) {
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
								//g.drawString("" + v, x + 6, y + 15);
								g.drawString("", x + 6, y + 15);
							}
						};
					}
				});

		vv.getRenderContext().setVertexFillPaintTransformer(
				new PickableVertexPaintTransformer<org.firebird.io.model.Vertex>(vv
						.getPickedVertexState(), Color.white, Color.yellow));
		vv.getRenderContext().setEdgeDrawPaintTransformer(
				new PickableEdgePaintTransformer<org.firebird.io.model.Edge>(vv
						.getPickedEdgeState(), Color.black, Color.lightGray));

		vv.setBackground(Color.white);

		// add my listener for ToolTips
		vv.setVertexToolTipTransformer(new ToStringLabeller<org.firebird.io.model.Vertex>());

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

		JPanel controls = new JPanel();
		controls.add(plus);
		controls.add(minus);
		controls.add(((DefaultModalGraphMouse<org.firebird.io.model.Vertex, org.firebird.io.model.Edge>) gm)
				.getModeComboBox());
		content.add(controls, BorderLayout.SOUTH);

		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Creates the vertices.
	 * 
	 * @param websiteId the website id
	 * @return HashMap<String, org.firebird.io.model.Vertex> the vertices map
	 */
	private HashMap<String, org.firebird.io.model.Vertex> createVertices(int websiteId) {
		HashMap<String, org.firebird.io.model.Vertex> verticesMap = new HashMap<String, org.firebird.io.model.Vertex>();
		VertexManager vertexManager = new VertexManagerImpl(new VertexDaoiBatis());		
		List<org.firebird.io.model.Vertex> vertices = vertexManager.getVertices(websiteId);

		for (int i = 0; i < vertices.size(); i++) {
			org.firebird.io.model.Vertex vertex = (org.firebird.io.model.Vertex)vertices.get(i);
			graph.addVertex(vertex);
			verticesMap.put(vertex.getId(), vertex);
		}
		return verticesMap;
	}

	/**
	 * Creates edges for this graph
	 * 
	 * @param websiteId the website id
	 * @return List<org.firebird.io.model.Edge> the edge list
	 */
	private List<org.firebird.io.model.Edge> createEdges(int websiteId, HashMap<String, org.firebird.io.model.Vertex> vertices) {

		EdgeManager edgeManager = new EdgeManagerImpl(new EdgeDaoiBatis());		
		List<org.firebird.io.model.Edge> edges = edgeManager.getEdges(websiteId);

		for (int i = 0; i < edges.size(); i++) {
			org.firebird.io.model.Edge edge = (org.firebird.io.model.Edge)edges.get(i);
			graph.addEdge(edge, vertices.get(edge.getVertex1()), vertices.get(edge.getVertex2()), EdgeType.DIRECTED);
		}
		return edges;
	}

	/**
	 * a driver for this demo
	 */
	public static void main(String[] args) {
		new SocialNetworkGraph();
	}
}
