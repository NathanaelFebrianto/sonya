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
import edu.uci.ics.jung.visualization.renderers.DefaultEdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;

/**
 * A Social network visualization viewer.
 * 
 * @author Young-Gue Bae
 */
public class SimpleGraphViewer extends VisualizationViewer<String, String> {
	private static final long serialVersionUID = 3521855270954095236L;

	/** vertex paints */
	Map<String, Paint> vertexPaints;
	/** edge paints */
	Map<String, Paint> edgePaints;
	
	/**
	 * Constructor.
	 * 
	 * @param layout the Layout to apply, with its associated graph
	 */
	public SimpleGraphViewer(Layout<String, String> layout) {
		super(layout);
		
		setupView();
	}

	/**
	 * Constructor.
	 * 
	 * @param layout the Layout to apply, with its associated graph
	 * @param dim the dimension
	 */
	public SimpleGraphViewer(Layout<String, String> layout, Dimension dim) {
		super(layout, dim);
		
		setupView();
	}
	
	/**
	 * Gets the graph.
	 * 
	 * @return Graph<Vertex, Edge> the graph
	 */
	public Graph<String, String> getGraph() {
		return getGraphLayout().getGraph();
	}

	private void setupView() {
		setBackground(Color.white);		
		/*
		getRenderContext().setVertexLabelTransformer(
				new Transformer<String, String>() {
					public String transform(String v) {
						return v;
					}
				});
		*/
		
		getRenderContext().setVertexLabelRenderer(new DefaultVertexLabelRenderer(new Color(0, 112, 192)));
		getRenderContext().setEdgeLabelRenderer(new DefaultEdgeLabelRenderer(new Color(0, 112, 192)));
		
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

		vertexPaints = LazyMap.<String, Paint> decorate(
				new HashMap<String, Paint>(),
				new Transformer<String, Paint>() {
					public Paint transform(String v) {
						return new Color(255, 255, 0); // yellow
					}
				});
		
		edgePaints = LazyMap.<String, Paint> decorate(
				new HashMap<String, Paint>(),
				new Transformer<String, Paint>() {
					public Paint transform(String e) {
						return new Color(90, 90, 90);
					}
				});
		
		//tell the renderer to use our own customized color rendering
		getRenderContext().setVertexFillPaintTransformer(
				MapTransformer.<String, Paint>getInstance(vertexPaints));
				
		getRenderContext().setVertexDrawPaintTransformer(
				new Transformer<String, Paint>() {
					public Paint transform(String v) {
						if (getPickedVertexState().isPicked(v)) {
							return Color.black;
						} else {
							return new Color(90, 90, 90);
						}
					}
				});
		
		getRenderContext().setVertexStrokeTransformer(
				new Transformer<String, Stroke>() {
					protected final Stroke THIN = new BasicStroke(1);
					protected final Stroke THICK = new BasicStroke(2);

					public Stroke transform(String v) {
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
						MapTransformer.<String, Paint> getInstance(edgePaints));
		
		getRenderContext().setEdgeStrokeTransformer(
				new Transformer<String, Stroke>() {
					protected final Stroke THIN = new BasicStroke(1);
					protected final Stroke THICK = new BasicStroke(2);

					public Stroke transform(String e) {
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
		setVertexToolTipTransformer(new Transformer<String, String>() {
			public String transform(final String v) {
				return v;
			}
		});

		// set vertex font
		getRenderContext().setVertexFontTransformer(
				new Transformer<String, Font>() {
					public Font transform(final String v) {
						return new Font("³ª´®°íµñ", Font.PLAIN, 12);
					}
				});

		// create a frame to hold the graph
		final ModalGraphMouse gm = new DefaultModalGraphMouse<String, String>();
		setGraphMouse(gm);		
	}

	/**
	 * Initializes the color of vertices and edges.
	 * 
	 */
	public void initColor() {
		Collection<String> vertices = this.getGraph().getVertices();
		for (String v : vertices) {
			vertexPaints.put(v, new Color(255, 255, 0));
		}
		
		Collection<String> edges = this.getGraph().getEdges();
		for (String e : edges) {
			edgePaints.put(e, new Color(90, 90, 90));
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
			vertexPaints.put(v, color);
		}
		repaint();
	}

	/**
	 * Recolors the edges with random color.
	 * 
	 * @param vertexPair the pair of two vertices
	 * @return Color
	 */
	public Color colorEdges(Set<String[]> vertexPairs) {
		// create random color
		Random random = new Random();
		Color color = Color.getHSBColor( random.nextFloat(), 1.0F, 1.0F );

		colorEdges(vertexPairs, color);
		
		return color;
	}
	
	/**
	 * Recolors the edges.
	 * 
	 * @param vertexPair the pair of two vertices
	 * @param color the color
	 */
	public void colorEdges(Set<String[]> vertexPairs, Color color) {
		for (String[] v : vertexPairs) {
			String edge = this.getEdge(v[0], v[1]);
			edgePaints.put(edge, color);
		}
		repaint();
	}

	/**
	 * Gets the edge.
	 * 
	 * @param id1 the vertex id1
	 * @param id2 the vertex id2
	 * @return Edge the edge
	 */
	public String getEdge(String vertex1, String vertex2) {
		return vertex1 + "^" + vertex2;		
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
