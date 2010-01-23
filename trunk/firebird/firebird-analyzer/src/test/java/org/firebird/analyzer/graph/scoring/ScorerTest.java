package org.firebird.analyzer.graph.scoring;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.firebird.graph.view.GraphModeller;
import org.firebird.io.model.Edge;
import org.firebird.io.model.Vertex;
import org.firebird.io.service.EdgeManager;
import org.firebird.io.service.VertexManager;
import org.firebird.io.service.impl.EdgeManagerImpl;
import org.firebird.io.service.impl.VertexManagerImpl;

import edu.uci.ics.jung.graph.Graph;

public class ScorerTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ScorerTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ScorerTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
    	assertTrue(true);
    	
    	try {
        	VertexManager vertexManager = new VertexManagerImpl();
        	EdgeManager edgeManager = new EdgeManagerImpl();
        	GraphModeller modeller = new GraphModeller();
        	
        	List<Vertex> vertices = vertexManager.getVertices(1);
        	List<Edge> edges = edgeManager.getEdges(1, 1);
        	
        	Graph<Vertex, Edge> graph = modeller.createGraph(vertices, edges);
    		
    		Scorer scorer = new Scorer(graph);
    		scorer.enableHITS(true);
    		scorer.enableBetweennessCentrality(true);
    		scorer.enableClosenessCentrality(true);
    		//scorer.enableEigenvectorCentrality(true);
    		
    		scorer.evaluate();
        } catch (Exception ex) {
        	ex.printStackTrace();
        }  	 
    }
}
