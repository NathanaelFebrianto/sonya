package com.nhn.socialanalytics.miner.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;
import org.apache.commons.collections15.functors.MapTransformer;
import org.apache.commons.collections15.map.LazyMap;

import com.nhn.socialanalytics.miner.index.DetailDoc;
import com.nhn.socialanalytics.miner.index.DocIndexSearcher;
import com.nhn.socialanalytics.miner.index.FieldConstants;
import com.nhn.socialanalytics.miner.index.TargetTerm;
import com.nhn.socialanalytics.nlp.kr.view.GenericListener;

import edu.uci.ics.jung.algorithms.layout.PolarPoint;
import edu.uci.ics.jung.algorithms.layout.RadialTreeLayout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.util.Animator;

@SuppressWarnings("serial")
public class OpinionTreeViewer extends JApplet {
	
	JTextArea tareaDetailDocs;
	
	/**
	 * the graph
	 */
	Forest<TermNode, TermEdge> graph;
	Map<TermNode, Paint> vertexPaints;

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
	VisualizationViewer<TermNode, TermEdge> vv;

	VisualizationServer.Paintable rings;

	String root;

	TreeLayout<TermNode, TermEdge> treeLayout;

	RadialTreeLayout<TermNode, TermEdge> radialLayout;

	public OpinionTreeViewer(Forest<TermNode, TermEdge> graph) {
		
		try {

			// create a simple graph for the demo
			this.graph = graph;
	
			treeLayout = new TreeLayout<TermNode, TermEdge>(graph);
			radialLayout = new RadialTreeLayout<TermNode, TermEdge>(graph);
			radialLayout.setSize(new Dimension(550, 550));
	
			vv = new VisualizationViewer<TermNode, TermEdge>(treeLayout,
					new Dimension(550, 550));
			vv.setBackground(Color.white);
			vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
			//vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
			
			vv.getRenderContext().setVertexLabelTransformer(
					new Transformer<TermNode, String>() {
						public String transform(TermNode v) {
							
							String label = v.getName();
							if (v.getTF() > 0)
								label += " (" + v.getTF() +  ")";
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
			
			//tell the renderer to use our own customized color rendering
			vv.getRenderContext().setVertexFillPaintTransformer(
							MapTransformer.<TermNode, Paint>getInstance(vertexPaints));
	
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
								tareaDetailDocs.setText("");
								List<DetailDoc> detailDocs = v.getDocs();
								
								int docCount = 1;
								for (DetailDoc doc : detailDocs) {
									if (docCount == 1) {
										tareaDetailDocs.append("============================================================\n");
										tareaDetailDocs.append(doc.toHeaderString() + "\n");
										tareaDetailDocs.append("============================================================\n");
									}
									tareaDetailDocs.append(doc.toString() + "\n\n");
									
									docCount++;
								}				
								
								return THICK;
							} else {
								return THIN;
							}
						}
					});
			
			this.changeVerbVerticesColor();
			
			// add a listener for ToolTips
			//vv.setVertexToolTipTransformer(new ToStringLabeller());
			vv.setVertexToolTipTransformer(new Transformer<TermNode, String>() {
				public String transform(final TermNode v) {
					String tooltip = v.getId();
	
					return tooltip;
				}
			});
			
			vv.getRenderContext().setArrowFillPaintTransformer(
					new ConstantTransformer(Color.lightGray));
			rings = new Rings();
	
			////////////////////////////////////////////////
			//add(this.makeTopPanel(), BorderLayout.NORTH);
			
			final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
			
			JSplitPane splitPane = new JSplitPane();
			splitPane.setOneTouchExpandable(true);
			splitPane.add(panel, JSplitPane.LEFT);
			splitPane.add(this.makeDetailDocsPanel(), JSplitPane.RIGHT);
			
			add(splitPane, BorderLayout.CENTER);
	
			final DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
	
			vv.setGraphMouse(graphMouse);
	
			JComboBox modeBox = graphMouse.getModeComboBox();
			modeBox.addItemListener(graphMouse.getModeListener());
			graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
	
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
	
			JToggleButton radial = new JToggleButton("Radial");
			radial.addItemListener(new ItemListener() {
	
				public void itemStateChanged(ItemEvent e) {
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
			});
	
			JPanel scaleGrid = new JPanel(new GridLayout(1, 0));
			scaleGrid.setBorder(BorderFactory.createTitledBorder("Zoom"));
	
			JPanel controls = new JPanel();
			scaleGrid.add(plus);
			scaleGrid.add(minus);
			controls.add(radial);
			controls.add(scaleGrid);
			controls.add(modeBox);
	
			add(controls, BorderLayout.SOUTH);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	private JPanel makeTopPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		
		JButton btnRun = new JButton("Run");
		ActionListener runActionListener = (ActionListener)(GenericListener.create(
		        ActionListener.class,
				"actionPerformed",
				this,
				"runAction"));		
		btnRun.addActionListener(runActionListener);
		panel.add(btnRun);
		
		return panel;
	}
	
	private JPanel makeDetailDocsPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		tareaDetailDocs = new JTextArea(34, 30);
		tareaDetailDocs.setLineWrap(true);

		panel.add(new JScrollPane(tareaDetailDocs), BorderLayout.CENTER);	
				
		return panel;
	}

	
	class Rings implements VisualizationServer.Paintable {

		Collection<Double> depths;

		public Rings() {
			depths = getDepths();
		}

		private Collection<Double> getDepths() {
			Set<Double> depths = new HashSet<Double>();
			Map<TermNode, PolarPoint> polarLocations = radialLayout
					.getPolarLocations();
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
	
	private void changeVerbVerticesColor() {
		Collection<TermNode> vertices = graph.getVertices();
		for (TermNode v : vertices) {
			if (v.getType() != null && v.getType().equals(FieldConstants.PREDICATE)) {
				//vertexPaints.put(v, Color.red);
			}
			if (v.getPolarity() > 0.0) {
				vertexPaints.put(v, Color.blue);
			}
			if (v.getPolarity() < 0.0) {
				vertexPaints.put(v, Color.red);
			}
			if (v.getName().equals(FieldConstants.SUBJECT) || 
					v.getName().equals(FieldConstants.PREDICATE) ||
					v.getName().equals(FieldConstants.ATTRIBUTE)) {
				vertexPaints.put(v, Color.gray);
			}
		}		
	}

	
	public void runAction(ActionEvent e) {

	}	

	public static void main(String[] args) {
		
		try {
			File[] indexDirs = new File[1];
			indexDirs[0] = new File("./bin/data/androidmarket/index/20111129");
			
			String object = "naverapp";
			
			Set<String> customStopwordSet = new HashSet<String>();
//			customStopwordSet.add("fta");
//			customStopwordSet.add("한미fta");
//			customStopwordSet.add("한미");
//			customStopwordSet.add("갤럭시노트");
//			customStopwordSet.add("갤럭시");

			DocIndexSearcher searcher = new DocIndexSearcher(indexDirs, "./conf/stopword.txt", customStopwordSet);
			System.out.println("stopwords == " + searcher.getStopwords());
			
			OpinionGraphModeller modeller = new OpinionGraphModeller();
			
			/////////////////////////////////
			/* target term ==> PREDICATE   */
			/////////////////////////////////

			Map<String, Integer> terms = searcher.getTerms(object, FieldConstants.PREDICATE, 3, true);					
			for (Map.Entry<String, Integer> entry : terms.entrySet()) {
				String term = entry.getKey();
			
				TargetTerm subjectTerm = searcher.search(object, FieldConstants.PREDICATE, FieldConstants.SUBJECT, term, 3);
				TargetTerm attributeTerm = searcher.search(object, FieldConstants.PREDICATE, FieldConstants.ATTRIBUTE, term, 3);
				Map<String, TargetTerm> termMap = new HashMap<String, TargetTerm>();
				termMap.put(FieldConstants.SUBJECT, subjectTerm);
				termMap.put(FieldConstants.ATTRIBUTE, attributeTerm);
				
				modeller.addTerms(FieldConstants.PREDICATE, termMap);			
			}
			
			/////////////////////////////////
			/* target term ==> SUBJECT   */
			/////////////////////////////////
			/*
			Map<String, Integer> terms = searcher.getTerms(object, FieldConstants.SUBJECT, 15, true);					
			for (Map.Entry<String, Integer> entry : terms.entrySet()) {
				String term = entry.getKey();
			
				TargetTerm subjectTerm = searcher.search(object, FieldConstants.SUBJECT, FieldConstants.PREDICATE, term, 15);
				TargetTerm attributeTerm = searcher.search(object, FieldConstants.SUBJECT, FieldConstants.ATTRIBUTE, term, 20);
				Map<String, TargetTerm> termMap = new HashMap<String, TargetTerm>();
				termMap.put(FieldConstants.PREDICATE, subjectTerm);
				termMap.put(FieldConstants.ATTRIBUTE, attributeTerm);
				
				modeller.addTerms(FieldConstants.SUBJECT, termMap);			
			}
			*/
				
			modeller.createGraph();
			Forest graph = modeller.getGraph();
			
			JFrame frame = new JFrame();
			Container content = frame.getContentPane();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			OpinionTreeViewer viewer = new OpinionTreeViewer(graph);

			content.add(viewer);
			frame.pack();
			frame.setVisible(true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
