package org.firebird.twitter;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.firebird.io.dao.ibatis.VertexDaoiBatis;
import org.firebird.io.model.Vertex;
import org.firebird.io.service.VertexManager;
import org.firebird.io.service.impl.VertexManagerImpl;

/**
 * Unit test for simple App.
 */
public class TwitterDataCollectorTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TwitterDataCollectorTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(TwitterDataCollectorTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        try {
        	TwitterDataCollector collector = new TwitterDataCollector();
        	collector.setLimitLevel(2);
        	collector.setLimitPeople(1000);
        	collector.setLimitDegree(50);
        	collector.setCollectFollowingRelationship(true);
        	collector.setCollectFollowerRelationship(true);
        	collector.setCollectUserBlogEntry(false);
        	
        	//VertexManager vertexManager = new VertexManagerImpl(new VertexDaoiBatis());
        	//List<Vertex> vertices = vertexManager.getVertices(1);
        	
        	collector.collectSocialNetwork("louiezzang");
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
    	
    	
    	assertTrue(true);
    }
}
