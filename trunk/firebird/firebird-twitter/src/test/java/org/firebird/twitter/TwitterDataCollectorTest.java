package org.firebird.twitter;

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
        	collector.setLimitLevel(2);
        	//collector.setLimitPeople(5000);
        	collector.setLimitDegree(100);
        	collector.setCollectFollowingRelationship(true);
        	collector.setCollectFollowerRelationship(true);
        	
        	collector.collectSocialNetwork("louiezzang");
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
    	
    	
    	assertTrue(true);
    }
}
