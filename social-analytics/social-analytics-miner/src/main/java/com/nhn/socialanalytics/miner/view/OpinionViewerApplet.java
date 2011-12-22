package com.nhn.socialanalytics.miner.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.nhn.socialanalytics.miner.index.DetailDoc;
import com.nhn.socialanalytics.miner.opinion.FeatureResultSet;
import com.nhn.socialanalytics.miner.opinion.OpinionResultSet;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;

@SuppressWarnings("serial")
public class OpinionViewerApplet extends JApplet {
	
	JTabbedPane tpaneToolbar;
	JTabbedPane tpaneOpinion;
	JTabbedPane tpaneList;	
	
	OpinionGraphViewer graphViewer;
	OpinionToolbar toolbarOpinion;
	FeatureSummaryChart featureChart;
	JTextArea tareaDetailDocs;	
	DetailDocTable tableDetailDocs;

	public OpinionViewerApplet(Forest<TermNode, TermEdge> graph) {
		this.graphViewer = makeGraphView(graph);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOneTouchExpandable(true);
		splitPane.add(makeLeftPanel(graphViewer), JSplitPane.LEFT);
		splitPane.add(makeRightPanel(), JSplitPane.RIGHT);
		
		add(makeToolbarPanel(graphViewer), BorderLayout.NORTH);
		add(splitPane, BorderLayout.CENTER);	
	}
	
	public OpinionGraphViewer getOpinionGraphViewer() {
		return this.graphViewer;
	}
	
	public void showGraphView(OpinionResultSet resultSet, boolean translate) {
		OpinionGraphModeller modeller = new OpinionGraphModeller(resultSet, translate);
		Forest<TermNode, TermEdge> graph = modeller.getGraph();
		Layout<TermNode, TermEdge> treeLayout = new TreeLayout<TermNode, TermEdge>(graph);
		graphViewer.updateGraph(treeLayout);
	}
	
	public void showFeatureChart(FeatureResultSet resultSet, String site, boolean includeEtc) {
		featureChart.updateChart(resultSet, site, includeEtc);
	}
	
	private JComponent makeToolbarPanel(OpinionGraphViewer graphViewer) {
		tpaneToolbar = new JTabbedPane(SwingConstants.TOP);	
		
		tpaneToolbar.addTab(UIHandler.getText("tab.toolbar.graph"), makeGraphViewToolbar(graphViewer));
		tpaneToolbar.addTab(UIHandler.getText("tab.toolbar.feature"), new FeatureToolbar(this));
		
		ChangeListener tabChangeListener = (ChangeListener)(GenericListener.create(
				ChangeListener.class,
				"stateChanged",
				this,
				"toolbarTabChangeAction"));		
		tpaneToolbar.addChangeListener(tabChangeListener);
		
		return tpaneToolbar;
	}
	
	private JComponent makeGraphViewToolbar(OpinionGraphViewer graphViewer) {
		FormLayout layout = new FormLayout(
				"left:p, 4dlu, p",
				"p, 4dlu, p");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		toolbarOpinion = new OpinionToolbar(this);
		builder.add(toolbarOpinion, cc.xy(1, 1));
		builder.add(makeGraphViewControlPanel(graphViewer), cc.xy(3, 1, "left, top"));
		
		builder.getPanel().setBorder(new EmptyBorder(0,0,0,0));
		
		return builder.getPanel();
	}
	
	private JComponent makeGraphViewControlPanel(final OpinionGraphViewer viewer) {
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
		//scaleGrid.setBorder(BorderFactory.createTitledBorder("Zoom"));
		scaleGrid.setPreferredSize(new Dimension(56, 22));

		JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
		controls.setBorder(BorderFactory.createTitledBorder("Graph Option"));
		scaleGrid.add(plus);
		scaleGrid.add(minus);
		controls.add(radial);
		controls.add(scaleGrid);
		controls.add(modeBox);
		
		return controls;		
	}	
	
	private JComponent makeLeftPanel(OpinionGraphViewer graphViewer) {
		tpaneOpinion = new JTabbedPane(SwingConstants.TOP);
		
		VisualizationViewer<TermNode, TermEdge> vv = graphViewer.getVisualizationViewer();
		GraphZoomScrollPane panelGraph = new GraphZoomScrollPane(vv);
		
		tpaneOpinion.addTab(UIHandler.getText("tab.graph.view"), panelGraph);		
		//tpaneOpinion.addTab(UIHandler.getText("tab.table.view"), new JPanel());
		tpaneOpinion.addTab(UIHandler.getText("tab.summary.view"), this.makeFeatureSummaryChart());
		
		ChangeListener tabChangeListener = (ChangeListener)(GenericListener.create(
				ChangeListener.class,
				"stateChanged",
				this,
				"opinionTabChangeAction"));		
		tpaneOpinion.addChangeListener(tabChangeListener);
		
		return tpaneOpinion;
	}
	
	private JComponent makeRightPanel() {
		tpaneList = new JTabbedPane(SwingConstants.TOP);			
		tpaneList.addTab(UIHandler.getText("tab.doc.list"), makeDetailDocListPanel());		
		
		return tpaneList;
	}
	
	private OpinionGraphViewer makeGraphView(Forest<TermNode, TermEdge> graph) {
		Layout<TermNode, TermEdge> treeLayout = new TreeLayout<TermNode, TermEdge>(graph);
		final OpinionGraphViewer viewer = new OpinionGraphViewer(treeLayout, new Dimension(600, 600));
		
		ItemListener itemPickListener = (ItemListener)(GenericListener.create(
				ItemListener.class,
				"itemStateChanged",
				this,
				"itemPickAction"));
		viewer.getVisualizationViewer().getPickedVertexState().addItemListener(itemPickListener);
	
		return viewer;
	}
	
	private JComponent makeFeatureSummaryChart() {
		this.featureChart = new FeatureSummaryChart();
		JScrollPane scrollPane = new JScrollPane(featureChart);
		
		return scrollPane;
	}
	
	private JComponent makeDetailDocListPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		tareaDetailDocs = new JTextArea(34, 30);
		tareaDetailDocs.setLineWrap(true);
		
		tableDetailDocs = new DetailDocTable();

		//panel.add(new JScrollPane(tareaDetailDocs), BorderLayout.CENTER);	
		panel.add(new JScrollPane(tableDetailDocs), BorderLayout.CENTER);	
				
		return panel;
	}
	
	//////////////////////////////////////////
	/*       Defines event actions.         */
	//////////////////////////////////////////
	
	public void toolbarTabChangeAction(ChangeEvent e) {
		JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
        int index = sourceTabbedPane.getSelectedIndex();
        
        if (index == 0) {
        	tpaneOpinion.setSelectedIndex(0);
        }
        else if (index == 1) {
        	tpaneOpinion.setSelectedIndex(1);
        }
	}
	
	public void opinionTabChangeAction(ChangeEvent e) {
		JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
        int index = sourceTabbedPane.getSelectedIndex();
        
        if (index == 0) {
        	tpaneToolbar.setSelectedIndex(0);
        }
        else if (index == 1) {
        	tpaneToolbar.setSelectedIndex(1);
        }
	}
	
	public void itemPickAction(ItemEvent e) {
		Object item = e.getItem();
		
		if (e.getStateChange() == ItemEvent.SELECTED) {
			if (item instanceof TermNode) {
				TermNode node = (TermNode) item;
				
				tareaDetailDocs.setText("");
				List<DetailDoc> detailDocs = node.getDocs();
				
				if (detailDocs.size() > 0) {
					tableDetailDocs.removeAllRow();
					// display max 50 rows
					if (detailDocs.size() > 50)
						detailDocs = detailDocs.subList(0, 50);
					
					tableDetailDocs.setRowData(detailDocs, toolbarOpinion.isTranslate());
					
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
				}
			}
		}
	}
	
	public static void main(String[] args) {
		
		try {
			OpinionGraphModeller modeller = new OpinionGraphModeller();
			final Forest<TermNode, TermEdge> graph = modeller.getEmptyGraph();
			
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					try {
						UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceCremeLookAndFeel");
						//UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceGraphiteAquaLookAndFeel");
						//UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
							
						//Font font = new Font("tahoma", Font.PLAIN, 11);
						Font font = new Font("MS Gothic", Font.PLAIN, 12);
						//Font font = new Font("MS Mincho", Font.PLAIN, 12);
						UIHandler.changeAllSwingComponentDefaultFont(font);

						JFrame frame = new JFrame();
						Container content = frame.getContentPane();
						frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						
						OpinionViewerApplet viewer = new OpinionViewerApplet(graph);

						content.add(viewer);
						frame.pack();
						frame.setTitle("Opinion Finder");
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
