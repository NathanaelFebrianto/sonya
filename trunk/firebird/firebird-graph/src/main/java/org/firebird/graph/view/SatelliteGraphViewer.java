/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.view;

import java.awt.Color;
import java.awt.Dimension;

import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.SatelliteVisualizationViewer;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

/**
 * A satellite visualization viewer.
 * 
 * @author Young-Gue Bae
 */
public class SatelliteGraphViewer extends SatelliteVisualizationViewer<org.firebird.io.model.Vertex, org.firebird.io.model.Edge> {

	private static final long serialVersionUID = -6282627685832660341L;

	/**
	 * Constructor.
	 * 
	 * @param viewer the master graph viewer
	 */
	public SatelliteGraphViewer(GraphViewer viewer) {
		super(viewer);
		
		createView();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param viewer the parent graph viewer
	 * @param dim the dimension
	 */
	public SatelliteGraphViewer(GraphViewer viewer, Dimension dim) {
		super(viewer, dim);
		
		createView();
	}
	
	private void createView() {
		getRenderContext().setEdgeDrawPaintTransformer(new PickableEdgePaintTransformer<org.firebird.io.model.Edge>(getPickedEdgeState(), Color.black, Color.cyan));
        getRenderContext().setVertexFillPaintTransformer(new PickableVertexPaintTransformer<org.firebird.io.model.Vertex>(getPickedVertexState(), Color.red, Color.yellow));		
	
        ScalingControl scaler = new CrossoverScalingControl();
        scaleToLayout(scaler);
        
        setVertexToolTipTransformer(new ToStringLabeller());
        getRenderContext().setVertexLabelTransformer(this.getMaster().getRenderContext().getVertexLabelTransformer());	
	}
}
