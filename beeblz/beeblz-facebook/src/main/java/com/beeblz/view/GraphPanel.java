/**
 * Copyright (c) 2010, beeblz.com
 * All rights reserved.
 */
package com.beeblz.view;

import java.applet.AppletContext;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import com.beeblz.facebook.FacebookDataCollector;
import com.beeblz.graph.GraphData;
import com.beeblz.graph.GraphView;

/**
 * A Social network graph main panel.
 * 
 * @author YoungGue Bae
 */
public class GraphPanel extends JPanel implements PropertyChangeListener {
	
	private static final long serialVersionUID = -5520707258678283156L;

	// applet context
	AppletContext context = null;
	
	// progress bar
	JProgressBar progressBar;
	
	// graph tool bar
	GraphToolbar toolbar;
	
	// graph view
	GraphView graphView;
	
	// split panel for content
	JSplitPane spaneContent;
	
	// right split panel
	JSplitPane spaneTable;
	
	// tab panel for graph
	JTabbedPane tpaneGraph;
	
	// tab panel for top table
	JTabbedPane tpaneTopTable;
	
	// tab panel for bottom table
	JTabbedPane tpaneBottomTable;
	
	// cluster combo box
	JComboBox comboCluster;
	
	/**
	 * Constructor.
	 * 
	 */
	public GraphPanel() {
		this(null);

		setupUI();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param context the applet context
	 */
	public GraphPanel(AppletContext context) {
		this.context = context;
		UIHandler.setResourceBundle("graph");
		UIHandler.changeAllSwingComponentDefaultFont();

		setupUI();
	}
	
	/**
	 * Initializes graph view.
	 */
	public void init() {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		Task task = new Task();
		task.addPropertyChangeListener(this);
        task.execute();
	}
	
    /**
     * Initializes graph view with graph data.
     * 
     * @param graphData the graph data
     */
    public void initGraph(GraphData graphData) {
    	graphView.initGraph(graphData);
    	
    	// update the cluster size label in toolbar.
    	toolbar.updateClusterSizeLabel(graphView.getClusterSize());
    	
		// update the maximum value of clustering slider in toolbar.
		int maxEdges = graphView.getGraph().getEdges().getTupleCount();
		System.out.println("total edge count == " + maxEdges);
		toolbar.updateClusteringSliderMaximum(maxEdges/4);
		
    	// update cluster combo box.
		this.updateClusterComboBox();    
    }
	
	/**
	 * Gets the graph view.
	 * 
	 * @return GraphView the graph view
	 */
	public GraphView getGraphView() {
		return graphView;
	}
	
	/**
	 * Updates the cluster combo box items.
	 */
	public void updateClusterComboBox() {
		comboCluster.removeAllItems();
		int clusterSize = graphView.getClusterSize();
		comboCluster.addItem("All");
		for (int i = 0; i < clusterSize; i++) {
			comboCluster.addItem(new Integer(i+1));
		}
	}
	
    /**
     * Invoked when task's progress property changes.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
        } 
    }
	
	private void setupUI() {
		setLayout(new BorderLayout());

		// create a content panel
		spaneContent = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JComponent panelLeft = createLeftPanel();
		JComponent panelRight = createRightPanel();

		spaneContent.setOneTouchExpandable(true);
		spaneContent.setDividerLocation(panelLeft.getPreferredSize().width);
		spaneContent.setLeftComponent(panelLeft);
		spaneContent.setRightComponent(panelRight);

		setLayout(new BorderLayout());
		add(createTopPanel(), BorderLayout.NORTH);
		add(spaneContent, BorderLayout.CENTER);
	}
	
	private JComponent createTopPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		// create a graph toolbar
		toolbar = new GraphToolbar(this);
		
		// create a progress bar
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		
		panel.add(toolbar);
		panel.add(progressBar);
		
		return panel;
	}

	private JComponent createLeftPanel() {
		graphView = new GraphView();
		
		tpaneGraph = new JTabbedPane(SwingConstants.TOP);
		
		tpaneGraph.addTab(UIHandler.getText("tab.social.graph"), 
				new JScrollPane(graphView, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));		
		tpaneGraph.addTab(UIHandler.getText("tab.closeness.graph"), new JPanel());

		return tpaneGraph;
	}

	private JComponent createRightPanel() {
		spaneTable = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		spaneTable.setOneTouchExpandable(true);
		spaneTable.setDividerLocation(400);
		spaneTable.setTopComponent(createTopMyFriendsPanel());
		spaneTable.setBottomComponent(createBottomFriendsOfFriendPanel());
		
		return spaneTable;
	}
	
	private JComponent createTopMyFriendsPanel() {
		tpaneTopTable = new JTabbedPane(SwingConstants.TOP);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		// panel for combo box
		JPanel panelCombo = new JPanel();
		panelCombo.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelCombo.add(new JLabel(UIHandler.getText("label.cluster")));
		panelCombo.add(new JLabel(" "));

		comboCluster = new JComboBox();
		this.updateClusterComboBox();
		panelCombo.add(comboCluster);		
		
		// panel for table
		JScrollPane spaneTable = new JScrollPane(new JTable());				
		
		panel.add(panelCombo, BorderLayout.NORTH);
		panel.add(spaneTable, BorderLayout.CENTER);
						
		tpaneTopTable.addTab(UIHandler.getText("tab.my.friends"), panel);
		
		return tpaneTopTable;
	}
	
	private JComponent createBottomFriendsOfFriendPanel() {
		tpaneTopTable = new JTabbedPane(SwingConstants.TOP);
		
		tpaneTopTable.addTab(UIHandler.getText("tab.friends.of.friend"), new JTable());		
		tpaneTopTable.addTab(UIHandler.getText("tab.friend.may.know"), new JTable());
		
		return tpaneTopTable;
	}
	
    class Task extends SwingWorker<Void, Void> {
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            Random random = new Random();
            int progress = 0;
            //Initialize progress property.
            setProgress(0);
            
            //Collect facebook data.
            FacebookDataCollector fdc = new FacebookDataCollector();
	        GraphData graphData = fdc.getMyFriends(false);
			initGraph(graphData);
            
            while (progress < 100) {
                //Sleep for up to one second.
                try {
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException ignore) {}
                //Make random progress.
                progress += random.nextInt(10);
                setProgress(Math.min(progress, 100));
            }
            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
        	setCursor(null); //turn off the wait cursor
        	progressBar.setValue(0);
        	progressBar.setString("done!");      	
        	
    		//graphView.getVisualization().cancel("layout");
        }
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("g r a p h v i e w");
        GraphPanel panelGraph = new GraphPanel();        			
        frame.setContentPane(panelGraph);
        frame.pack();
        frame.setVisible(true);
        
        panelGraph.init();       
    }

}
