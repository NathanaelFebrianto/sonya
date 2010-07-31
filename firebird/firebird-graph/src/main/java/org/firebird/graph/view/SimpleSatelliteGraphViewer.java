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
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

/**
 * A satellite visualization viewer.
 * 
 * @author Young-Gue Bae
 */
public class SimpleSatelliteGraphViewer extends SatelliteVisualizationViewer<String, String> {

	private static final long serialVersionUID = -6282627685832660341L;
	
	private SimpleGraphViewer master;
	
	/** vertex paints */
	Map<String, Paint> vertexPaints;
	/** edge paints */
	Map<String, Paint> edgePaints;
	
	/**
	 * Constructor.
	 * 
	 * @param master the master graph viewer
	 */
	public SimpleSatelliteGraphViewer(SimpleGraphViewer master) {
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
	public SimpleSatelliteGraphViewer(SimpleGraphViewer master, Dimension dim) {
		super(master, dim);
		this.master = master;		
		setupView();
	}
	
	private void setupView() {
		//getRenderContext().setEdgeDrawPaintTransformer(new PickableEdgePaintTransformer<String>(getPickedEdgeState(), Color.black, Color.cyan));
		//getRenderContext().setVertexFillPaintTransformer(new PickableVertexPaintTransformer<String>(getPickedVertexState(), Color.red, Color.yellow));		

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
		
		//getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<String,String>());
		
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
		Collection<String> vertices = master.getGraph().getVertices();
		for (String v : vertices) {
			vertexPaints.put(v, new Color(255, 255, 0));
		}
		
		Collection<String> edges = master.getGraph().getEdges();
		for (String e : edges) {
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
			vertexPaints.put(v, c);
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
			String edge = master.getEdge(v[0], v[1]);
			edgePaints.put(edge, color);
		}
		repaint();
	}
}
