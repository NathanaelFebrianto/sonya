/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.MapTransformer;
import org.apache.commons.collections15.map.LazyMap;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.renderers.DefaultEdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;

/**
 * A Social network visualization viewer.
 * 
 * @author Young-Gue Bae
 */
public class GraphViewer extends VisualizationViewer<org.firebird.io.model.Vertex, org.firebird.io.model.Edge> {
	private static final long serialVersionUID = 3521855270954095236L;

	/** vertex paints */
	Map<org.firebird.io.model.Vertex, Paint> vertexPaints;
	/** edge paints */
	Map<org.firebird.io.model.Edge, Paint> edgePaints;
	
	/**
	 * Constructor.
	 * 
	 * @param layout the Layout to apply, with its associated graph
	 */
	public GraphViewer(Layout<org.firebird.io.model.Vertex, org.firebird.io.model.Edge> layout) {
		super(layout);
		
		setupView();
	}

	/**
	 * Constructor.
	 * 
	 * @param layout the Layout to apply, with its associated graph
	 * @param dim the dimension
	 */
	public GraphViewer(Layout<org.firebird.io.model.Vertex, org.firebird.io.model.Edge> layout, Dimension dim) {
		super(layout, dim);
		
		setupView();
	}
	
	/**
	 * Gets the graph.
	 * 
	 * @return Graph<Vertex, Edge> the graph
	 */
	public Graph<org.firebird.io.model.Vertex, org.firebird.io.model.Edge> getGraph() {
		return getGraphLayout().getGraph();
	}

	private void setupView() {
		setBackground(Color.white);
		
		getRenderContext().setVertexLabelTransformer(
				new Transformer<org.firebird.io.model.Vertex, String>() {
					public String transform(org.firebird.io.model.Vertex v) {
						return v.getUserName();
					}
				});
		
		getRenderContext().setVertexLabelRenderer(new DefaultVertexLabelRenderer(Color.blue));
		getRenderContext().setEdgeLabelRenderer(new DefaultEdgeLabelRenderer(Color.cyan));
		
		/*
		getRenderContext().setVertexIconTransformer(
				new Transformer<org.firebird.io.model.Vertex, Icon>() {
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
		*/

		vertexPaints = LazyMap.<org.firebird.io.model.Vertex, Paint> decorate(
				new HashMap<org.firebird.io.model.Vertex, Paint>(),
				new Transformer<org.firebird.io.model.Vertex, Paint>() {
					public Paint transform(org.firebird.io.model.Vertex v) {
						return Color.red;
					}
				});
		
		edgePaints = LazyMap.<org.firebird.io.model.Edge, Paint> decorate(
				new HashMap<org.firebird.io.model.Edge, Paint>(),
				new Transformer<org.firebird.io.model.Edge, Paint>() {
					public Paint transform(org.firebird.io.model.Edge e) {
						return Color.black;
					}
				});
		
		//tell the renderer to use our own customized color rendering
		getRenderContext().setVertexFillPaintTransformer(
				MapTransformer.<org.firebird.io.model.Vertex, Paint>getInstance(vertexPaints));
				
		getRenderContext().setVertexDrawPaintTransformer(
				new Transformer<org.firebird.io.model.Vertex, Paint>() {
					public Paint transform(org.firebird.io.model.Vertex v) {
						if (getPickedVertexState().isPicked(v)) {
							return Color.black;
						} else {
							return Color.white;
						}
					}
				});
		
		getRenderContext().setVertexStrokeTransformer(
				new Transformer<org.firebird.io.model.Vertex, Stroke>() {
					protected final Stroke THIN = new BasicStroke(1);
					protected final Stroke THICK = new BasicStroke(2);

					public Stroke transform(org.firebird.io.model.Vertex v) {
						if (getPickedVertexState().isPicked(v)) {
							return THICK;
						} else {
							return THIN;
						}
					}
				});
		
		/*
		getRenderContext().setEdgeDrawPaintTransformer(
				new PickableEdgePaintTransformer<org.firebird.io.model.Edge>(
						getPickedEdgeState(), Color.black, Color.red));		
		*/
		
		//tell the renderer to use our own customized color rendering
		getRenderContext().setEdgeDrawPaintTransformer(
						MapTransformer.<org.firebird.io.model.Edge, Paint> getInstance(edgePaints));
		
		getRenderContext().setEdgeStrokeTransformer(
				new Transformer<org.firebird.io.model.Edge, Stroke>() {
					protected final Stroke THIN = new BasicStroke(1);
					protected final Stroke THICK = new BasicStroke(2);

					public Stroke transform(org.firebird.io.model.Edge e) {
						/*
						 Paint c = edgePaints.get(e); 
						 if (c == Color.LIGHT_GRAY) 
						 	return THIN; 
						 else return THICK;
						 */
						if (getPickedEdgeState().isPicked(e)) {
							return THICK;
						} else {
							return THIN;
						}
					}
				});

		// add my listener for tooltips
		// setVertexToolTipTransformer(new
		// ToStringLabeller<org.firebird.io.model.Vertex>());
		setVertexToolTipTransformer(new Transformer<org.firebird.io.model.Vertex, String>() {
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

	/**
	 * Initializes the color of vertices and edges.
	 * 
	 */
	public void initColor() {
		Collection<org.firebird.io.model.Vertex> vertices = this.getGraph().getVertices();
		for (org.firebird.io.model.Vertex v : vertices) {
			vertexPaints.put(v, Color.red);
		}
		
		Collection<org.firebird.io.model.Edge> edges = this.getGraph().getEdges();
		for (org.firebird.io.model.Edge e : edges) {
			edgePaints.put(e, Color.black);
		}
		repaint();
	}
	
	/**
	 * Recolors the vertices with random color.
	 * 
	 * @param vertices the vertex id list
	 * @return Color
	 */
	public Color colorVertices(Set<String> vertices) {
		// create random color
		Random random = new Random();
		Color color = Color.getHSBColor( random.nextFloat(), 1.0F, 1.0F );
		/*
		Color color = new Color(
				random.nextInt(256), 
				random.nextInt(256),
				random.nextInt(256));
		*/
		colorVertices(vertices, color);
		
		return color;
	}
	
	/**
	 * Recolors the vertices.
	 * 
	 * @param vertices the vertex id list
	 * @param color the color
	 */
	public void colorVertices(Set<String> vertices, Color color) {
		for (String v : vertices) {
			org.firebird.io.model.Vertex vertex = this.getVertex(v);
			vertexPaints.put(vertex, color);
		}
		repaint();
	}
	
	/**
	 * Recolors the edges.
	 * 
	 * @param vertexPair the pair of two vertices
	 * @param color the color
	 */
	public void colorEdges(Set<String[]> vertexPair, Color color) {
		for (String[] v : vertexPair) {
			org.firebird.io.model.Edge edge = this.getEdge(v[0], v[1]);
			edgePaints.put(edge, color);
		}
		repaint();
	}

	/**
	 * Gets the vertex.
	 * 
	 * @param id the vertex id
	 * @return Vertex the vertex
	 */
	public org.firebird.io.model.Vertex getVertex(String id) {
		Collection<org.firebird.io.model.Vertex> vertices = this.getGraph().getVertices();
		for (org.firebird.io.model.Vertex v : vertices) {
			if (v.getId().equals(id))
				return v;
		}		
		return null;		
	}
	
	/**
	 * Gets the edge.
	 * 
	 * @param id1 the vertex id1
	 * @param id2 the vertex id2
	 * @return Edge the edge
	 */
	public org.firebird.io.model.Edge getEdge(String vertex1, String vertex2) {
		Collection<org.firebird.io.model.Edge> edges = this.getGraph().getEdges();
		for (org.firebird.io.model.Edge e : edges) {
			if (e.getVertex1().equals(vertex1) && e.getVertex2().equals(vertex2))
				return e;
		}		
		return null;		
	}
	
	/**
	 * Colors.
	 */
	public final Color[] similarColors = {
		new Color(216, 134, 134),
		new Color(135, 137, 211),
		new Color(134, 206, 189),
		new Color(206, 176, 134),
		new Color(194, 204, 134),
		new Color(145, 214, 134),
		new Color(133, 178, 209),
		new Color(103, 148, 255),
		new Color(60, 220, 220),
		new Color(30, 250, 100)
	};

}
