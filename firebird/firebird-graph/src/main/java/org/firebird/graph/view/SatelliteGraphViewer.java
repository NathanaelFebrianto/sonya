/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.MapTransformer;
import org.apache.commons.collections15.map.LazyMap;

import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.SatelliteVisualizationViewer;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

/**
 * A satellite visualization viewer.
 * 
 * @author Young-Gue Bae
 */
public class SatelliteGraphViewer extends SatelliteVisualizationViewer<org.firebird.io.model.Vertex, org.firebird.io.model.Edge> {

	private static final long serialVersionUID = -6282627685832660341L;
	
	private GraphViewer master;
	
	/** vertex paints */
	Map<org.firebird.io.model.Vertex, Paint> vertexPaints;
	/** edge paints */
	Map<org.firebird.io.model.Edge, Paint> edgePaints;
	
	/**
	 * Constructor.
	 * 
	 * @param master the master graph viewer
	 */
	public SatelliteGraphViewer(GraphViewer master) {
		super(master);		
		this.master = master;		
		setupView();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param master the master graph viewer
	 * @param dim the dimension
	 */
	public SatelliteGraphViewer(GraphViewer master, Dimension dim) {
		super(master, dim);
		this.master = master;		
		setupView();
	}
	
	private void setupView() {
		//getRenderContext().setEdgeDrawPaintTransformer(new PickableEdgePaintTransformer<org.firebird.io.model.Edge>(getPickedEdgeState(), Color.black, Color.cyan));
		//getRenderContext().setVertexFillPaintTransformer(new PickableVertexPaintTransformer<org.firebird.io.model.Vertex>(getPickedVertexState(), Color.red, Color.yellow));		

		vertexPaints = LazyMap.<org.firebird.io.model.Vertex, Paint> decorate(
				new HashMap<org.firebird.io.model.Vertex, Paint>(),
				new Transformer<org.firebird.io.model.Vertex, Paint>() {
					public Paint transform(org.firebird.io.model.Vertex v) {
						return new Color(255, 255, 0); // yellow
					}
				});
		
		edgePaints = LazyMap.<org.firebird.io.model.Edge, Paint> decorate(
				new HashMap<org.firebird.io.model.Edge, Paint>(),
				new Transformer<org.firebird.io.model.Edge, Paint>() {
					public Paint transform(org.firebird.io.model.Edge e) {
						return new Color(90, 90, 90);
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
							return new Color(90, 90, 90);
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
		
        ScalingControl scaler = new CrossoverScalingControl();
        scaleToLayout(scaler);
        
        setVertexToolTipTransformer(new ToStringLabeller());
        getRenderContext().setVertexLabelTransformer(this.getMaster().getRenderContext().getVertexLabelTransformer());	
	}
	
	/**
	 * Initializes the color of vertices and edges.
	 * 
	 */
	public void initColor() {
		Collection<org.firebird.io.model.Vertex> vertices = master.getGraph().getVertices();
		for (org.firebird.io.model.Vertex v : vertices) {
			vertexPaints.put(v, new Color(255, 255, 0));
		}
		
		Collection<org.firebird.io.model.Edge> edges = master.getGraph().getEdges();
		for (org.firebird.io.model.Edge e : edges) {
			edgePaints.put(e, new Color(90, 90, 90));
		}
		repaint();
	}
	
	/**
	 * Recolors the vertices.
	 * 
	 * @param vertices the vertex id list
	 * @param c the color
	 */
	public void colorVertices(Set<String> vertices, Color c) {
		for (String v : vertices) {
			org.firebird.io.model.Vertex vertex = master.getVertex(v);
			vertexPaints.put(vertex, c);
		}
		repaint();
	}
	
	/**
	 * Recolors the edges.
	 * 
	 * @param vertexPair the pair of two vertices
	 * @param color the color
	 */
	public void colorEdges(Set<String[]> vertexPairs, Color color) {
		for (String[] v : vertexPairs) {
			org.firebird.io.model.Edge edge = master.getEdge(v[0], v[1]);
			edgePaints.put(edge, color);
		}
		repaint();
	}
}
