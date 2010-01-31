package org.firebird.collector.twitter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.firebird.collector.CollectorConfig;

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
        	CollectorConfig config = new CollectorConfig();
        	config.setDBStorage(false);
        	config.setCollectFriend(true);
        	config.setCollectFollower(true);
        	config.setCollectUserBlogEntry(false);
        	config.setLevelLimit(2);
        	config.setDegreeLimit(2);
        	config.setPeopleLimit(10);
        	
        	//TwitterDataCollector collector = new TwitterDataCollector(config);
        	//collector.collect("louiezzang");
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
    	
    	
    	assertTrue(true);
    }
}
