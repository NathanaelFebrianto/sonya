package com.nhn.socialanalytics.nlp.kr.view;

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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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

import com.nhn.socialanalytics.nlp.kr.semantic.SemanticAnalyzer;
import com.nhn.socialanalytics.nlp.kr.semantic.SemanticClause;
import com.nhn.socialanalytics.nlp.kr.semantic.SemanticSentence;
import com.nhn.socialanalytics.nlp.kr.sentiment.SentimentAnalyzer;
import com.nhn.socialanalytics.nlp.kr.syntax.ParseTree;
import com.nhn.socialanalytics.nlp.kr.syntax.ParseTreeEdge;
import com.nhn.socialanalytics.nlp.kr.syntax.ParseTreeNode;
import com.nhn.socialanalytics.nlp.kr.syntax.SyntacticAnalyzer;

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
public class GraphTreeViewer extends JApplet {
	
	JTextArea tareaSentence;	
	JTextArea tareaSemanticOutput;
	
	/**
	 * the graph
	 */
	Forest<ParseTreeNode, ParseTreeEdge> graph;
	Map<ParseTreeNode, Paint> vertexPaints;

	Factory<DirectedGraph<ParseTreeNode, ParseTreeEdge>> graphFactory = new Factory<DirectedGraph<ParseTreeNode, ParseTreeEdge>>() {

		public DirectedGraph<ParseTreeNode, ParseTreeEdge> create() {
			return new DirectedSparseMultigraph<ParseTreeNode, ParseTreeEdge>();
		}
	};

	Factory<Tree<ParseTreeNode, ParseTreeEdge>> treeFactory = new Factory<Tree<ParseTreeNode, ParseTreeEdge>>() {

		public Tree<ParseTreeNode, ParseTreeEdge> create() {
			return new DelegateTree<ParseTreeNode, ParseTreeEdge>(graphFactory);
		}
	};

	/**
	 * the visual component and renderer for the graph
	 */
	VisualizationViewer<ParseTreeNode, ParseTreeEdge> vv;

	VisualizationServer.Paintable rings;

	String root;

	TreeLayout<ParseTreeNode, ParseTreeEdge> treeLayout;

	RadialTreeLayout<ParseTreeNode, ParseTreeEdge> radialLayout;

	public GraphTreeViewer(Forest<ParseTreeNode, ParseTreeEdge> graph) {

		// create a simple graph for the demo
		this.graph = graph;

		treeLayout = new TreeLayout<ParseTreeNode, ParseTreeEdge>(graph);
		radialLayout = new RadialTreeLayout<ParseTreeNode, ParseTreeEdge>(graph);
		radialLayout.setSize(new Dimension(550, 550));

		vv = new VisualizationViewer<ParseTreeNode, ParseTreeEdge>(treeLayout,
				new Dimension(550, 550));
		vv.setBackground(Color.white);
		vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
		//vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		
		vv.getRenderContext().setVertexLabelTransformer(
				new Transformer<ParseTreeNode, String>() {
					public String transform(ParseTreeNode v) {
						
						String label = "[" + String.valueOf(v.getId()) + "]";
						if (v.getEojeol() != null) {
							label += v.getEojeol().getSource();
							String josa = v.getEojeol().getJosaTag();
							String eomi = v.getEojeol().getEomiTag();
							
							if (josa != null)
								label += ("[" + josa + "]");
							if (eomi != null)
								label += ("[" + eomi + "]");
						}
						//String label = 
						//System.out.println("label == " + label);
						return label;
					}
				});
		
		// vertex color
		vertexPaints = LazyMap.<ParseTreeNode, Paint> decorate(
				new HashMap<ParseTreeNode, Paint>(),
				new Transformer<ParseTreeNode, Paint>() {
					public Paint transform(ParseTreeNode v) {
						return Color.yellow; // yellow
					}
				});
		
		//tell the renderer to use our own customized color rendering
		vv.getRenderContext().setVertexFillPaintTransformer(
						MapTransformer.<ParseTreeNode, Paint>getInstance(vertexPaints));

		vv.getRenderContext().setVertexDrawPaintTransformer(
				new Transformer<ParseTreeNode, Paint>() {
					public Paint transform(ParseTreeNode v) {
						if (vv.getPickedVertexState().isPicked(v)) {
							return Color.blue;
						} else {
							return new Color(90, 90, 90);
						}
					}
				});
		
		vv.getRenderContext().setVertexStrokeTransformer(
				new Transformer<ParseTreeNode, Stroke>() {
					protected final Stroke THIN = new BasicStroke(1);
					protected final Stroke THICK = new BasicStroke(2);

					public Stroke transform(ParseTreeNode v) {
						if (vv.getPickedVertexState().isPicked(v)) {
							return THICK;
						} else {
							return THIN;
						}
					}
				});
		
		this.changeVerbVerticesColor();
		
		// add a listener for ToolTips
		//vv.setVertexToolTipTransformer(new ToStringLabeller());
		vv.setVertexToolTipTransformer(new Transformer<ParseTreeNode, String>() {
			public String transform(final ParseTreeNode v) {
				String tooltip = "";
				if (v.getEojeol() != null)	
					tooltip = v.getEojeol().toString();
				else
					tooltip = String.valueOf(v.getId());
				return tooltip;
			}
		});
		
		vv.getRenderContext().setArrowFillPaintTransformer(
				new ConstantTransformer(Color.lightGray));
		rings = new Rings();

		
		// add sentence input panel
		add(this.makeInputSentencePanel(), BorderLayout.NORTH);
		
		final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOneTouchExpandable(true);
		splitPane.add(panel, JSplitPane.LEFT);
		splitPane.add(this.makeSemanticOutputPanel(), JSplitPane.RIGHT);
		
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

					LayoutTransition<ParseTreeNode, ParseTreeEdge> lt = new LayoutTransition<ParseTreeNode, ParseTreeEdge>(
							vv, treeLayout, radialLayout);
					Animator animator = new Animator(lt);
					animator.start();
					vv.getRenderContext().getMultiLayerTransformer()
							.setToIdentity();
					vv.addPreRenderPaintable(rings);
				} else {
					LayoutTransition<ParseTreeNode, ParseTreeEdge> lt = new LayoutTransition<ParseTreeNode, ParseTreeEdge>(
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
		
		//this.setPreferredSize(new Dimension(580, 700));
	}
	
	private JPanel makeInputSentencePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		
		tareaSentence = new JTextArea(2, 70);
		if (tareaSentence.getText() == null || tareaSentence.getText().equals(""))
			tareaSentence.setText("철수가 음악에 재능이 없으면서도 노래를 아주 열심히 부르는 것을 영희가 안다.");
		tareaSentence.setLineWrap(true);
		panel.add(new JScrollPane(tareaSentence));
				
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
	
	private JPanel makeSemanticOutputPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		tareaSemanticOutput = new JTextArea(34, 30);
		tareaSemanticOutput.setLineWrap(true);

		panel.add(new JScrollPane(tareaSemanticOutput), BorderLayout.CENTER);	
				
		return panel;
	}

	
	class Rings implements VisualizationServer.Paintable {

		Collection<Double> depths;

		public Rings() {
			depths = getDepths();
		}

		private Collection<Double> getDepths() {
			Set<Double> depths = new HashSet<Double>();
			Map<ParseTreeNode, PolarPoint> polarLocations = radialLayout
					.getPolarLocations();
			for (ParseTreeNode v : graph.getVertices()) {
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
		Collection<ParseTreeNode> vertices = graph.getVertices();
		for (ParseTreeNode v : vertices) {
			if (v.getEojeol() != null && 
			   ('V' == v.getEojeol().getPos() || 
			    "EFN".equals(v.getEojeol().getEomiTag()))) {
				vertexPaints.put(v, Color.red);
			}
		}		
	}

	
	public void runAction(ActionEvent e) {
		try {			
			if (tareaSentence.getText() != null) {
				String source = tareaSentence.getText().trim();
				
				SyntacticAnalyzer syntaticAnalyzer = SyntacticAnalyzer.getInstance();
				ParseTree tree = syntaticAnalyzer.parseTree(source);
				
				GraphModeller modeller = new GraphModeller(GraphModeller.DIRECTED_SPARSE_GRAPH);
				modeller.createGraph(tree.getNodeList(), tree.getEdgeList());					
	
				//vv.removeAll();
				graph = modeller.getGraph();
				treeLayout.setGraph(graph);
				radialLayout.setGraph(graph);
				this.changeVerbVerticesColor();
				vv.repaint();
				
				SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
				SemanticSentence semanticSentence = semanticAnalyzer.createSemanticSentence(source);
				
				SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer("./liwc/LIWC_ko.txt");
				
				semanticSentence.sort(true);
				tareaSemanticOutput.append("\n\n=================================\n");
				tareaSemanticOutput.append("       Semantic Analysis\n");
				tareaSemanticOutput.append("=================================\n");
				tareaSemanticOutput.append(source + "\n");				
				tareaSemanticOutput.append("-------------------------------------\n");
				for (SemanticClause clause : semanticSentence) {
					clause = sentimentAnalyzer.analyzePolarity(clause);
					tareaSemanticOutput.append(clause.toString() + "\n");
					tareaSemanticOutput.append("-------------------------------------\n");
				}
				
				sentimentAnalyzer.analyzePolarity(semanticSentence);
				
				tareaSemanticOutput.append("\n=================================\n");
				tareaSemanticOutput.append("       Sentiment Analysis\n");
				tareaSemanticOutput.append("=================================\n");
				tareaSemanticOutput.append("polarity = " + semanticSentence.getPolarity());
				if (semanticSentence.getPolarity() == 1.0)
					tareaSemanticOutput.append(" (positive)\n");
				else if (semanticSentence.getPolarity() == -1.0)
					tareaSemanticOutput.append(" (negative)\n");
				else
					tareaSemanticOutput.append(" (neutral)\n");
				
				tareaSemanticOutput.append("strength = " + semanticSentence.getPolarityStrength() + "\n");
				tareaSemanticOutput.append("-------------------------------------\n");				
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}

	public void setSourceText(String text) {
		tareaSentence.setText(text);
	}

	public static void main(String[] args) {
		
		try {
			
			String source = "이 물건은 배송이 빨라서 정말 좋지만, 품질이 별로 안좋네요.";
			//String source = "철수가 음악에 재능이 없으면서도 노래를 아주 열심히 부르는 것을 영희가 안다.";
			
			SyntacticAnalyzer analyzer = SyntacticAnalyzer.getInstance();
			ParseTree tree = analyzer.parseTree(source);
			
			GraphModeller modeller = new GraphModeller(GraphModeller.DIRECTED_SPARSE_GRAPH);
			modeller.createGraph(tree.getNodeList(), tree.getEdgeList());			
			
			Forest graph = modeller.getGraph();
			
			JFrame frame = new JFrame();
			Container content = frame.getContentPane();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			GraphTreeViewer viewer = new GraphTreeViewer(graph);
			viewer.setSourceText(source);

			content.add(viewer);
			frame.pack();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
