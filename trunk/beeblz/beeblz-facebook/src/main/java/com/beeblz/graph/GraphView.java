/**
 * Copyright (c) 2010, beeblz.com
 * All rights reserved.
 */
package com.beeblz.graph;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.layout.Layout;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.controls.ControlAdapter;
import prefuse.controls.DragControl;
import prefuse.controls.FocusControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Graph;
import prefuse.data.Tuple;
import prefuse.data.event.TupleSetListener;
import prefuse.data.tuple.TupleSet;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.render.PolygonRenderer;
import prefuse.render.Renderer;
import prefuse.util.ColorLib;
import prefuse.util.GraphicsLib;
import prefuse.util.force.DragForce;
import prefuse.util.force.ForceSimulator;
import prefuse.util.force.NBodyForce;
import prefuse.util.force.SpringForce;
import prefuse.visual.AggregateItem;
import prefuse.visual.AggregateTable;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;

import com.beeblz.facebook.FacebookDataCollector;

/**
 * Graph view for social network.
 * 
 * @author YoungGue Bae
 */
public class GraphView extends Display {
	
    public static final String GRAPH = "graph";
    public static final String NODES = "graph.nodes";
    public static final String EDGES = "graph.edges";
    public static final String AGGR = "aggregates";
    
    public GraphView() {
        // initialize display and data
        super(new Visualization());
        Graph graph = initDataGroups();
        
        // set up the renderers        
        // draw the nodes as basic shapes
        //Renderer nodeR = new ShapeRenderer(20); 
        LabelRenderer nodeR = new LabelRenderer("label", "image");        
        nodeR.setRoundedCorner(10, 10);        
        nodeR.setImagePosition(Constants.TOP); 
        
        // draw aggregates as polygons with curved edges
        Renderer polyR = new PolygonRenderer(Constants.POLY_TYPE_CURVE);
        ((PolygonRenderer)polyR).setCurveSlack(0.15f);
        
        DefaultRendererFactory drf = new DefaultRendererFactory();
        drf.setDefaultRenderer(nodeR);
        drf.add("ingroup('aggregates')", polyR);
        m_vis.setRendererFactory(drf);      
        
        // fix selected focus nodes
        TupleSet focusGroup = m_vis.getGroup(Visualization.FOCUS_ITEMS); 
        focusGroup.addTupleSetListener(new TupleSetListener() {
            public void tupleSetChanged(TupleSet ts, Tuple[] add, Tuple[] rem) {
                for ( int i=0; i<rem.length; ++i )
                    ((VisualItem)rem[i]).setFixed(false);
                for ( int i=0; i<add.length; ++i ) {
                    ((VisualItem)add[i]).setFixed(false);
                    ((VisualItem)add[i]).setFixed(true);
                }
                if ( ts.getTupleCount() == 0 ) {
                    ts.addTuple(rem[0]);
                    ((VisualItem)rem[0]).setFixed(false);
                }
                m_vis.run("draw");
            }
        });
        
        // set up the visual operators
        // first set up all the color actions           
        ColorAction nNodeStroke = new ColorAction(NODES, VisualItem.STROKECOLOR, 0);        
        
        ColorAction nNodeFill = new ColorAction(NODES, VisualItem.FILLCOLOR, ColorLib.rgb(200,200,255));
        nNodeFill.add(VisualItem.FIXED, ColorLib.rgb(255,100,100));
        nNodeFill.add(VisualItem.HIGHLIGHT, ColorLib.rgb(255,200,125));
        
        ColorAction nNodeText = new ColorAction(NODES, VisualItem.TEXTCOLOR, ColorLib.rgb(0,0,0));
        
        ColorAction nEdgeFill = new ColorAction(EDGES, VisualItem.FILLCOLOR, ColorLib.gray(200)); 
        
        ColorAction nEdgeStroke = new ColorAction(EDGES, VisualItem.STROKECOLOR, ColorLib.gray(200));
        
        ColorAction aStroke = new ColorAction(AGGR, VisualItem.STROKECOLOR);
        aStroke.setDefaultColor(ColorLib.gray(200));
        aStroke.add("_hover", ColorLib.rgb(255,100,100));
        
        int[] palette = new int[] {
            ColorLib.rgba(255,200,200,150),
            ColorLib.rgba(200,255,200,150),
            ColorLib.rgba(200,200,255,150),
            ColorLib.rgba(216,134,134,150),
            ColorLib.rgba(135,137,211,150),
            ColorLib.rgba(134,206,189,150),
            ColorLib.rgba(206,176,134,150),
            ColorLib.rgba(194,204,134,150),
            ColorLib.rgba(145,214,134,150),
            ColorLib.rgba(133,178,209,150),
            ColorLib.rgba(103,148,255,150),
            ColorLib.rgba(60,220,220,150),
            ColorLib.rgba(30,250,100,150)
        };
              
        ColorAction aFill = new DataColorAction(AGGR, "id",
                Constants.NOMINAL, VisualItem.FILLCOLOR, palette);
        
        // bundle the color actions
        ActionList draw = new ActionList();
        draw.add(nNodeStroke);
        draw.add(nNodeFill);
        draw.add(nNodeText);
        draw.add(nEdgeFill);
        draw.add(nEdgeStroke);
        draw.add(aStroke);
        draw.add(aFill);        
        
        ForceSimulator forceSimulator = new ForceSimulator();
		forceSimulator.addForce(new NBodyForce(-1.0f, -1.0f, 0.899f));
		forceSimulator.addForce(new DragForce(0.009f));
		forceSimulator.addForce(new SpringForce(9.99E-6f, 200.0f));
        
        // now create the main layout routine
        ActionList animate = new ActionList(Activity.INFINITY);
        ForceDirectedLayout layout = new ForceDirectedLayout(GRAPH, forceSimulator, true); 
        animate.add(layout);
        animate.add(draw);        
        animate.add(new AggregateLayout(AGGR));
        animate.add(new RepaintAction());
        
        // finally, we register our ActionList with the Visualization.
        // we can later execute our Actions by invoking a method on our
        // Visualization, using the name we've chosen below.
        m_vis.putAction("draw", draw);             
        m_vis.putAction("layout", animate);
        m_vis.runAfter("draw", "layout");        
     
        // set up the display
        setSize(700, 700);
        pan(350, 350);
        setHighQuality(true);
        setForeground(Color.GRAY);
        setBackground(Color.WHITE);
        addControlListener(new AggregateDragControl());
        addControlListener(new ZoomControl());
        addControlListener(new PanControl());        
        addControlListener(new FocusControl(1));
        addControlListener(new DragControl());
        addControlListener(new WheelZoomControl());
        addControlListener(new ZoomToFitControl());
        addControlListener(new NeighborHighlightControl());
        
		addControlListener(new ControlAdapter() {

			public void itemEntered(VisualItem item, MouseEvent e) {
				//System.out.println("itemEntered....................");
				if (item.isInGroup(NODES)) {
					item.setFillColor(ColorLib.rgb(255,200,125));
					item.setStrokeColor(ColorLib.rgb(255,200,125));
					item.getVisualization().repaint();
				}
			}

			public void itemExited(VisualItem item, MouseEvent e) {
				//System.out.println("itemExited....................");
				if (item.isInGroup(NODES)) {
					if (item.isInGroup(Visualization.FOCUS_ITEMS)) {
						item.setFillColor(ColorLib.rgb(255,200,125));
						item.setStrokeColor(ColorLib.rgb(255,200,125));
					} else {

					}
					item.getVisualization().repaint();
				}
			}
			
			public void itemPressed(VisualItem item, MouseEvent e) {
				//System.out.println("itemPressed....................");
				
				if (e.getClickCount() == 2) {
					System.out.println("itemDoubleClicked....................");
					String id = item.getString("id");
					if (id != null) {
						System.out.println("ID[" + id + "] is selected.");
					}
				}
				
				if (item.isInGroup(NODES)) {
					//System.out.println("Node Selected....");
					item.setFillColor(ColorLib.rgb(255,200,125));
					item.setStrokeColor(ColorLib.rgb(255,200,125));
					item.getVisualization().repaint();
				}

				if (e.isPopupTrigger()) {
					System.out.println("---------isPopupTrigger....................");
					String id = item.getString("id");
					if (id != null) {
						System.out.println("ID[" + id + "] popup menu selected.");
					}
				}
			}
			
		});
        
        // set things running
        m_vis.run("layout");
    }
    
    /**
     * Sets the graph.
     * 
     * @param g the graph
     * @param label the label
     */
    public void setGraph(Graph g, String label) {
        // update labeling
        DefaultRendererFactory drf = (DefaultRendererFactory)m_vis.getRendererFactory();
        ((LabelRenderer)drf.getDefaultRenderer()).setTextField(label);
        
        // update graph
        m_vis.removeGroup(GRAPH);
        VisualGraph vg = m_vis.addGraph(GRAPH, g);
        m_vis.setValue(EDGES, null, VisualItem.INTERACTIVE, Boolean.FALSE);
        VisualItem f = (VisualItem)vg.getNode(0);
        m_vis.getGroup(Visualization.FOCUS_ITEMS).setTuple(f);
        f.setFixed(false);
    }
    
    private Graph initDataGroups() {
        // create graph
        FacebookDataCollector fdc = new FacebookDataCollector();
        GraphData graphData = fdc.getMyFriends();
        
    	Graph g = graphData.getGraph();
        
        // add visual data groups
        VisualGraph vg = m_vis.addGraph(GRAPH, g);
        m_vis.setValue(EDGES, null, VisualItem.INTERACTIVE, false);
        VisualItem f = (VisualItem)vg.getNode(0);
        m_vis.getGroup(Visualization.FOCUS_ITEMS).setTuple(f);
        f.setFixed(false);
        
        AggregateTable at = m_vis.addAggregates(AGGR);
        at.addColumn(VisualItem.POLYGON, float[].class);
        at.addColumn("id", int.class);
        
        // add nodes to aggregates
        // create an aggregate for each n-clique of nodes
        Set<Set<String>> clusterSet = graphData.clusterGraph(vg, 60);
        
        System.out.println("Cluster size == " + clusterSet.size());
        
		int i = 0;
		//Set the colors of each node so that each cluster's vertices have the same color
		for (Iterator<Set<String>> cIt = clusterSet.iterator(); cIt.hasNext();) {
			Set<String> nodes = cIt.next();
			AggregateItem aitem = (AggregateItem)at.addItem();
            aitem.setInt("id", i);
            for (Iterator<String> it = nodes.iterator(); it.hasNext();) {
            	String nodeId = (String)it.next();
                aitem.addItem(graphData.findNode(vg, nodeId));
            }
			i++;
		}
        return g;
    }
    
    public static void main(String[] argv) {
        JFrame frame = demo();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    public static JFrame demo() {
    	GraphView ad = new GraphView();
        JFrame frame = new JFrame("b e e b l z  |  g r a p h");
        frame.getContentPane().add(ad);
        frame.pack();
        return frame;
    }
    
} // end of class AggregateDemo


/**
 * Layout algorithm that computes a convex hull surrounding
 * aggregate items and saves it in the "_polygon" field.
 */
class AggregateLayout extends Layout {
    
    private int m_margin = 5; // convex hull pixel margin
    private double[] m_pts;   // buffer for computing convex hulls
    
    public AggregateLayout(String aggrGroup) {
        super(aggrGroup);
    }
    
    /**
     * @see edu.berkeley.guir.prefuse.action.Action#run(edu.berkeley.guir.prefuse.ItemRegistry, double)
     */
    public void run(double frac) {
        
        AggregateTable aggr = (AggregateTable)m_vis.getGroup(m_group);
        // do we have any  to process?
        int num = aggr.getTupleCount();
        if ( num == 0 ) return;
        
        // update buffers
        int maxsz = 0;
        for ( Iterator aggrs = aggr.tuples(); aggrs.hasNext();  )
            maxsz = Math.max(maxsz, 4*2*
                    ((AggregateItem)aggrs.next()).getAggregateSize());
        if ( m_pts == null || maxsz > m_pts.length ) {
            m_pts = new double[maxsz];
        }
        
        // compute and assign convex hull for each aggregate
        Iterator aggrs = m_vis.visibleItems(m_group);
        while ( aggrs.hasNext() ) {
            AggregateItem aitem = (AggregateItem)aggrs.next();

            int idx = 0;
            if ( aitem.getAggregateSize() == 0 ) continue;
            VisualItem item = null;
            Iterator iter = aitem.items();
            while ( iter.hasNext() ) {
                item = (VisualItem)iter.next();
                if ( item.isVisible() ) {
                    addPoint(m_pts, idx, item, m_margin);
                    idx += 2*4;
                }
            }
            // if no aggregates are visible, do nothing
            if ( idx == 0 ) continue;

            // compute convex hull
            double[] nhull = GraphicsLib.convexHull(m_pts, idx);
            
            // prepare viz attribute array
            float[]  fhull = (float[])aitem.get(VisualItem.POLYGON);
            if ( fhull == null || fhull.length < nhull.length )
                fhull = new float[nhull.length];
            else if ( fhull.length > nhull.length )
                fhull[nhull.length] = Float.NaN;
            
            // copy hull values
            for ( int j=0; j<nhull.length; j++ )
                fhull[j] = (float)nhull[j];
            aitem.set(VisualItem.POLYGON, fhull);
            aitem.setValidated(false); // force invalidation
        }
    }
    
    private static void addPoint(double[] pts, int idx, 
                                 VisualItem item, int growth)
    {
        Rectangle2D b = item.getBounds();
        double minX = (b.getMinX())-growth, minY = (b.getMinY())-growth;
        double maxX = (b.getMaxX())+growth, maxY = (b.getMaxY())+growth;
        pts[idx]   = minX; pts[idx+1] = minY;
        pts[idx+2] = minX; pts[idx+3] = maxY;
        pts[idx+4] = maxX; pts[idx+5] = minY;
        pts[idx+6] = maxX; pts[idx+7] = maxY;
    }
    
} // end of class AggregateLayout


/**
 * Interactive drag control that is "aggregate-aware"
 */
class AggregateDragControl extends ControlAdapter {

    private VisualItem activeItem;
    protected Point2D down = new Point2D.Double();
    protected Point2D temp = new Point2D.Double();
    protected boolean dragged;
    
    /**
     * Creates a new drag control that issues repaint requests as an item
     * is dragged.
     */
    public AggregateDragControl() {
    }
        
    /**
     * @see prefuse.controls.Control#itemEntered(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemEntered(VisualItem item, MouseEvent e) {
        Display d = (Display)e.getSource();
        d.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        activeItem = item;
        if ( !(item instanceof AggregateItem) )
            setFixed(item, true);
    }
    
    /**
     * @see prefuse.controls.Control#itemExited(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemExited(VisualItem item, MouseEvent e) {
        if ( activeItem == item ) {
            activeItem = null;
            setFixed(item, false);
        }
        Display d = (Display)e.getSource();
        d.setCursor(Cursor.getDefaultCursor());
    }
    
    /**
     * @see prefuse.controls.Control#itemPressed(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemPressed(VisualItem item, MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;
        dragged = false;
        Display d = (Display)e.getComponent();
        d.getAbsoluteCoordinate(e.getPoint(), down);
        if ( item instanceof AggregateItem )
            setFixed(item, true);
    }
    
    /**
     * @see prefuse.controls.Control#itemReleased(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemReleased(VisualItem item, MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;
        if ( dragged ) {
            activeItem = null;
            setFixed(item, false);
            dragged = false;
        }            
    }
    
    /**
     * @see prefuse.controls.Control#itemDragged(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemDragged(VisualItem item, MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;
        dragged = true;
        Display d = (Display)e.getComponent();
        d.getAbsoluteCoordinate(e.getPoint(), temp);
        double dx = temp.getX()-down.getX();
        double dy = temp.getY()-down.getY();
        
        move(item, dx, dy);
        
        down.setLocation(temp);
    }

    protected static void setFixed(VisualItem item, boolean fixed) {
        if ( item instanceof AggregateItem ) {
            Iterator items = ((AggregateItem)item).items();
            while ( items.hasNext() ) {
                setFixed((VisualItem)items.next(), fixed);
            }
        } else {
            item.setFixed(fixed);
        }
    }
    
    protected static void move(VisualItem item, double dx, double dy) {
        if ( item instanceof AggregateItem ) {
            Iterator items = ((AggregateItem)item).items();
            while ( items.hasNext() ) {
                move((VisualItem)items.next(), dx, dy);
            }
        } else {
            double x = item.getX();
            double y = item.getY();
            item.setStartX(x);  item.setStartY(y);
            item.setX(x+dx);    item.setY(y+dy);
            item.setEndX(x+dx); item.setEndY(y+dy);
        }
    }

}
