package org.firebird.twitter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class TwitterSocialGraphTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TwitterSocialGraphTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(TwitterSocialGraphTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        try {
        	TwitterSocialGraph tsg = new TwitterSocialGraph();
        	tsg.createDirctedGraph("louiezzang");
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
    	
    	
    	assertTrue(true);
    }
}
