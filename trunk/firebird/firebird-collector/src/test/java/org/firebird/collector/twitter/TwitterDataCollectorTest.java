package org.firebird.collector.twitter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

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
        	collector.setDBStorageMode(true);
        	collector.setLevelLimit(2);
        	collector.setPeopleLimit(20);
        	collector.setDegreeLimit(5);
        	collector.setCollectFriendRelationship(true);
        	collector.setCollectFollowerRelationship(true);
        	collector.setCollectUserBlogEntry(false);
        	
        	//VertexManager vertexManager = new VertexManagerImpl();
        	//List<Vertex> vertices = vertexManager.getVertices(1);
        	
        	//collector.collectSocialNetwork("louiezzang");
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
    	
    	
    	assertTrue(true);
    }
}
