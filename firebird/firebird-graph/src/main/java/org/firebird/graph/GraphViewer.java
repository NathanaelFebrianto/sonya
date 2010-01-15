/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.Icon;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.renderers.DefaultEdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;

/**
 * A Social network visualization viewer.
 * 
 * @author Young-Gue Bae
 */
public class GraphViewer extends VisualizationViewer<org.firebird.io.model.Vertex, org.firebird.io.model.Edge> {
	private static final long serialVersionUID = 3521855270954095236L;

	/**
	 * Constructor.
	 * 
	 * @param layout the Layout to apply, with its associated graph
	 */
	public GraphViewer(Layout<org.firebird.io.model.Vertex, org.firebird.io.model.Edge> layout) {
		super(layout, new Dimension(1000, 600));
		
		createView();
	}

	/**
	 * Gets the graph.
	 * 
	 * @return Graph<Vertex, Edge> the graph
	 */
	public Graph<org.firebird.io.model.Vertex, org.firebird.io.model.Edge> getGraph() {
		return getGraphLayout().getGraph();
	}

	private void createView() {
		getRenderContext().setVertexLabelTransformer(
				new Transformer<org.firebird.io.model.Vertex, String>() {

					public String transform(org.firebird.io.model.Vertex v) {
						return v.getUserName();
					}
				});

		getRenderContext().setVertexLabelRenderer(new DefaultVertexLabelRenderer(Color.cyan));
		getRenderContext().setEdgeLabelRenderer(new DefaultEdgeLabelRenderer(Color.cyan));

		getRenderContext().setVertexIconTransformer(
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
								if (getPickedVertexState().isPicked(v)) {
									g.setColor(Color.yellow);
								} else {
									g.setColor(Color.red);
								}
								g.fillOval(x, y, 20, 20);
								if (getPickedVertexState().isPicked(v)) {
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

		getRenderContext().setVertexFillPaintTransformer(
				new PickableVertexPaintTransformer<org.firebird.io.model.Vertex>(
						getPickedVertexState(), Color.white, Color.yellow));
		
		getRenderContext().setEdgeDrawPaintTransformer(
				new PickableEdgePaintTransformer<org.firebird.io.model.Edge>(
						getPickedEdgeState(), Color.black, Color.lightGray));

		setBackground(Color.white);

		// add my listener for tooltips
		// setVertexToolTipTransformer(new ToStringLabeller<org.firebird.io.model.Vertex>());		
		setVertexToolTipTransformer(
				new Transformer<org.firebird.io.model.Vertex, String>() {
					public String transform(final org.firebird.io.model.Vertex v) {
						return v.getId(); 
					}
				});
		 
		// set vertex font
		getRenderContext().setVertexFontTransformer(
				new Transformer<org.firebird.io.model.Vertex, Font>() {
					public Font transform(final org.firebird.io.model.Vertex v) {
						return new Font("³ª´®°íµñ", Font.PLAIN, 12);
					}
				});

		// create a frame to hold the graph
		final ModalGraphMouse gm = new DefaultModalGraphMouse<org.firebird.io.model.Vertex, org.firebird.io.model.Edge>();
		setGraphMouse(gm);		
	}

}
