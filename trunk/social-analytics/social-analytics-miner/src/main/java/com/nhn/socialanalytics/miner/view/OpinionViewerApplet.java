package com.nhn.socialanalytics.miner.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
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
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.collections15.Transformer;

import com.nhn.socialanalytics.miner.index.DetailDoc;
import com.nhn.socialanalytics.miner.index.DocIndexSearcher;
import com.nhn.socialanalytics.miner.index.FieldConstants;
import com.nhn.socialanalytics.miner.index.TargetTerm;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;

@SuppressWarnings("serial")
public class OpinionViewerApplet extends JApplet {
	
	private Forest<TermNode, TermEdge> graph;
	JTextArea tareaDetailDocs;	

	public OpinionViewerApplet(Forest<TermNode, TermEdge> graph) {
		this.graph = graph;
		OpinionGraphViewer viewer = makeGraphView(graph);
		VisualizationViewer<TermNode, TermEdge> vv = viewer.getVisualizationViewer();
		GraphZoomScrollPane panelGraph = new GraphZoomScrollPane(vv);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOneTouchExpandable(true);
		splitPane.add(panelGraph, JSplitPane.LEFT);
		splitPane.add(makeDetailDocsPanel(), JSplitPane.RIGHT);			
		add(splitPane, BorderLayout.CENTER);	
		add(makeGraphViewControlPanel(viewer), BorderLayout.SOUTH);
	}
	
	private OpinionGraphViewer makeGraphView(Forest<TermNode, TermEdge> graph) {
		Layout<TermNode, TermEdge> treeLayout = new TreeLayout<TermNode, TermEdge>(graph);
		final OpinionGraphViewer viewer = new OpinionGraphViewer(treeLayout, new Dimension(550, 550));
		final VisualizationViewer<TermNode, TermEdge> vv = viewer.getVisualizationViewer();
		
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
	
		return viewer;
	}
	
	private JPanel makeGraphViewControlPanel(final OpinionGraphViewer viewer) {
		final DefaultModalGraphMouse graphMouse = (DefaultModalGraphMouse) viewer.getVisualizationViewer().getGraphMouse();	
		JComboBox modeBox = graphMouse.getModeComboBox();
		modeBox.addItemListener(graphMouse.getModeListener());
		graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);

		JButton plus = new JButton("+");
		plus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewer.zoomin();
			}
		});
		JButton minus = new JButton("-");
		minus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewer.zoomout();
			}
		});

		JToggleButton radial = new JToggleButton("Radial");
		radial.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				viewer.toggleLayout(e);
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
		
		return controls;		
	}	
	
	private JPanel makeToolbarPanel() {
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
	
	public static void main(String[] args) {
		
		try {
			File[] indexDirs = new File[1];
			indexDirs[0] = new File("./bin/data/appstore/index/20111212");
			File liwcCatFile = new File("./bin/liwc/LIWC_ja.txt");
			
			String object = "naverline";
			
			Set<String> customStopwordSet = new HashSet<String>();
			customStopwordSet.add("좋은데");
			customStopwordSet.add("바이버보");
			customStopwordSet.add("그누구");
			customStopwordSet.add("이건");
			customStopwordSet.add("역시");
			customStopwordSet.add("아직");
			customStopwordSet.add("메세지는");
			customStopwordSet.add("한가지");
			customStopwordSet.add("人");

			File stopwordFile = new File("./conf/stopword_ja.txt");
			DocIndexSearcher searcher = new DocIndexSearcher(indexDirs, liwcCatFile, stopwordFile, customStopwordSet);
			System.out.println("stopwords == " + searcher.getStopwords());			
			
			OpinionGraphModeller modeller = new OpinionGraphModeller();
			
			/////////////////////////////////
			/* target term ==> PREDICATE   */
			/////////////////////////////////

//			Map<String, Integer> terms = searcher.getTerms(object, FieldConstants.PREDICATE, 10, true);					
//			for (Map.Entry<String, Integer> entry : terms.entrySet()) {
//				String term = entry.getKey();
//			
//				TargetTerm subjectTerm = searcher.search(object, FieldConstants.PREDICATE, FieldConstants.SUBJECT, term, 10);
//				TargetTerm attributeTerm = searcher.search(object, FieldConstants.PREDICATE, FieldConstants.ATTRIBUTE, term, 10);
//				Map<String, TargetTerm> termMap = new HashMap<String, TargetTerm>();
//				termMap.put(FieldConstants.SUBJECT, subjectTerm);
//				termMap.put(FieldConstants.ATTRIBUTE, attributeTerm);
//				
//				modeller.addTerms(FieldConstants.PREDICATE, termMap);			
//			}
			
			/////////////////////////////////
			/* target term ==> SUBJECT   */
			/////////////////////////////////

			Map<String, Integer> terms = searcher.getTerms(object, FieldConstants.SUBJECT, 10, true);					
			for (Map.Entry<String, Integer> entry : terms.entrySet()) {
				String term = entry.getKey();
			
				TargetTerm subjectTerm = searcher.search(object, FieldConstants.SUBJECT, FieldConstants.PREDICATE, term, 10);
				TargetTerm attributeTerm = searcher.search(object, FieldConstants.SUBJECT, FieldConstants.ATTRIBUTE, term, 40);
				Map<String, TargetTerm> termMap = new HashMap<String, TargetTerm>();
				termMap.put(FieldConstants.PREDICATE, subjectTerm);
				termMap.put(FieldConstants.ATTRIBUTE, attributeTerm);
				
				modeller.addTerms(FieldConstants.SUBJECT, termMap);			
			}
				
			modeller.createGraph();
			final Forest<TermNode, TermEdge> graph = modeller.getGraph();
			
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					try {
						UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceCremeLookAndFeel");
						//UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceGraphiteAquaLookAndFeel");
						//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
						//UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.Plastic3DLookAndFeel");
						//UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
						
						UIHandler.changeAllSwingComponentDefaultFont();

						JFrame frame = new JFrame();
						Container content = frame.getContentPane();
						frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						
						OpinionViewerApplet viewer = new OpinionViewerApplet(graph);

						content.add(viewer);
						frame.pack();
						frame.setTitle("Opinion View");
						frame.setVisible(true);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
