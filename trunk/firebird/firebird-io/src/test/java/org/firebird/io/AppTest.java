package org.firebird.io;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.firebird.io.model.Edge;
import org.firebird.io.service.EdgeManager;
import org.firebird.io.service.impl.EdgeManagerImpl;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	assertTrue( true );
    	
    	/*
    	EdgeManager edgeManager = new EdgeManagerImpl();
    	List<Edge> edges = edgeManager.getEdges(1, 1);
    	
    	System.out.println("edge size == " + edges.size());
		
    	for (int i = 0; i < edges.size(); i++) {
    		Edge edge = (Edge)edges.get(i);
    		System.out.println("edge == " + edge.getVertex1() + "->" + edge.getVertex2());
    	}
    	*/

    	/*
    	SqlSessionFactory sqlSessionFactory = GenericSqlSessionFactory.getSqlSessionFactory();
    	SqlSession session = sqlSessionFactory.openSession();

    	try {
    		VertexMapper mapper = session.getMapper(VertexMapper.class);
    		List<Vertex> vertices = mapper.selectVertices(1);
    	} finally {
    		session.close();
    	}
    	*/       
    }
}
