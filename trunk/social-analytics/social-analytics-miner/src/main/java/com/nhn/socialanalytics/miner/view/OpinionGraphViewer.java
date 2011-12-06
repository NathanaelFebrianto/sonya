package com.nhn.socialanalytics.miner.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ItemEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;
import org.apache.commons.collections15.functors.MapTransformer;
import org.apache.commons.collections15.map.LazyMap;

import com.nhn.socialanalytics.miner.index.DetailDoc;
import com.nhn.socialanalytics.miner.index.FieldConstants;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.PolarPoint;
import edu.uci.ics.jung.algorithms.layout.RadialTreeLayout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.util.Animator;

public class OpinionGraphViewer {

	/**
	 * the graph
	 */
	Forest<TermNode, TermEdge> graph;
	Map<TermNode, Paint> vertexPaints;
	VisualizationViewer<TermNode, TermEdge> vv;

	Factory<DirectedGraph<TermNode, TermEdge>> graphFactory = new Factory<DirectedGraph<TermNode, TermEdge>>() {

		public DirectedGraph<TermNode, TermEdge> create() {
			return new DirectedSparseMultigraph<TermNode, TermEdge>();
		}
	};

	Factory<Tree<TermNode, TermEdge>> treeFactory = new Factory<Tree<TermNode, TermEdge>>() {

		public Tree<TermNode, TermEdge> create() {
			return new DelegateTree<TermNode, TermEdge>(graphFactory);
		}
	};

	/**
	 * the visual component and renderer for the graph
	 */
	VisualizationServer.Paintable rings;
	TreeLayout<TermNode, TermEdge> treeLayout;
	RadialTreeLayout<TermNode, TermEdge> radialLayout;
	final ScalingControl scaler = new CrossoverScalingControl();

	public OpinionGraphViewer(Layout<TermNode, TermEdge> layout, Dimension dimension) {

		UIHandler.setResourceBundle("label");
		
		// create a simple graph for the demo
		this.graph = (Forest<TermNode, TermEdge>) layout.getGraph();

		if (layout instanceof TreeLayout) {
			treeLayout = (TreeLayout<TermNode, TermEdge>) layout;
			radialLayout = new RadialTreeLayout<TermNode, TermEdge>(graph);
			radialLayout.setSize(dimension);
		} else if (layout instanceof RadialTreeLayout) {			
			radialLayout = (RadialTreeLayout<TermNode, TermEdge>) layout;
			treeLayout = new TreeLayout<TermNode, TermEdge>(graph);
		}
		
		vv = new VisualizationViewer<TermNode, TermEdge>(treeLayout, dimension);

		vv.setBackground(Color.white);
		vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
		// getRenderContext().setVertexLabelTransformer(new
		// ToStringLabeller());

		vv.getRenderContext().setVertexLabelTransformer(
				new Transformer<TermNode, String>() {
					public String transform(TermNode v) {

						String label = v.getName();
						if (v.getTF() > 0)
							label += " (" + v.getTF() + ")";
						return label;
					}
				});

		// vertex color
		vertexPaints = LazyMap.<TermNode, Paint> decorate(
				new HashMap<TermNode, Paint>(),
				new Transformer<TermNode, Paint>() {
					public Paint transform(TermNode v) {
						return Color.yellow; // yellow
					}
				});

		// tell the renderer to use our own customized color rendering
		vv.getRenderContext().setVertexFillPaintTransformer(
				MapTransformer.<TermNode, Paint> getInstance(vertexPaints));

		vv.getRenderContext().setVertexDrawPaintTransformer(
				new Transformer<TermNode, Paint>() {
					public Paint transform(TermNode v) {
						if (vv.getPickedVertexState().isPicked(v)) {
							return Color.blue;
						} else {
							return new Color(90, 90, 90);
						}
					}
				});

		vv.getRenderContext().setVertexStrokeTransformer(
				new Transformer<TermNode, Stroke>() {
					protected final Stroke THIN = new BasicStroke(1);
					protected final Stroke THICK = new BasicStroke(2);

					public Stroke transform(TermNode v) {
						if (vv.getPickedVertexState().isPicked(v)) {
							// tareaDetailDocs.setText("");
							List<DetailDoc> detailDocs = v.getDocs();

							int docCount = 1;
							for (DetailDoc doc : detailDocs) {
								if (docCount == 1) {
									// tareaDetailDocs.append("============================================================\n");
									// tareaDetailDocs.append(doc.toHeaderString()
									// + "\n");
									// tareaDetailDocs.append("============================================================\n");
								}
								// tareaDetailDocs.append(doc.toString() +
								// "\n\n");

								docCount++;
							}

							return THICK;
						} else {
							return THIN;
						}
					}
				});

		this.changeVerticesColor();

		// add a listener for ToolTips
		// setVertexToolTipTransformer(new ToStringLabeller());
		vv.setVertexToolTipTransformer(new Transformer<TermNode, String>() {
			public String transform(final TermNode v) {
				String tooltip = v.getId();

				return tooltip;
			}
		});

		vv.getRenderContext().setArrowFillPaintTransformer(new ConstantTransformer(Color.lightGray));
		rings = new Rings();
		
		final DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();		
		vv.setGraphMouse(graphMouse);		
	}
	
	public VisualizationViewer<TermNode, TermEdge> getVisualizationViewer() {
		return this.vv;
	}
	
	public void changeVerticesColor() {
		Collection<TermNode> vertices = graph.getVertices();
		for (TermNode v : vertices) {
			if (v.getType() != null && v.getType().equals(FieldConstants.PREDICATE)) {
				// vertexPaints.put(v, Color.red);
			}
			if (v.getPolarity() > 0.0) {
				vertexPaints.put(v, Color.blue);
			}
			if (v.getPolarity() < 0.0) {
				vertexPaints.put(v, Color.red);
			}
			if (v.getName().equals(FieldConstants.SUBJECT)
					|| v.getName().equals(FieldConstants.PREDICATE)
					|| v.getName().equals(FieldConstants.ATTRIBUTE)) {
				vertexPaints.put(v, Color.gray);
			}
		}
	}
	
	public void zoomin() {
		scaler.scale(vv, 1.1f, vv.getCenter());
	}
	
	public void zoomout() {
		scaler.scale(vv, 1 / 1.1f, vv.getCenter());
	}
	
	public void toggleLayout(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			
			LayoutTransition<TermNode, TermEdge> lt = new LayoutTransition<TermNode, TermEdge>(
					vv, treeLayout, radialLayout);
			Animator animator = new Animator(lt);
			animator.start();
			vv.getRenderContext().getMultiLayerTransformer()
					.setToIdentity();
			vv.addPreRenderPaintable(rings);
		} else {
			LayoutTransition<TermNode, TermEdge> lt = new LayoutTransition<TermNode, TermEdge>(
					vv, radialLayout, treeLayout);
			Animator animator = new Animator(lt);
			animator.start();
			vv.getRenderContext().getMultiLayerTransformer()
					.setToIdentity();
			vv.removePreRenderPaintable(rings);
		}
		vv.repaint();
	}

	class Rings implements VisualizationServer.Paintable {

		Collection<Double> depths;

		public Rings() {
			depths = getDepths();
		}

		private Collection<Double> getDepths() {
			Set<Double> depths = new HashSet<Double>();
			Map<TermNode, PolarPoint> polarLocations = radialLayout.getPolarLocations();
			for (TermNode v : graph.getVertices()) {
				PolarPoint pp = polarLocations.get(v);
				depths.add(pp.getRadius());
			}
			return depths;
		}

		public void paint(Graphics g) {
			g.setColor(Color.lightGray);

			Graphics2D g2d = (Graphics2D) g;
			Point2D center = radialLayout.getCenter();

			Ellipse2D ellipse = new Ellipse2D.Double();
			for (double d : depths) {
				ellipse.setFrameFromDiagonal(center.getX() - d, center.getY()
						- d, center.getX() + d, center.getY() + d);
				Shape shape = vv.getRenderContext().getMultiLayerTransformer()
						.getTransformer(Layer.LAYOUT).transform(ellipse);
				g2d.draw(shape);
			}
		}

		public boolean useTransform() {
			return true;
		}
	}

}
