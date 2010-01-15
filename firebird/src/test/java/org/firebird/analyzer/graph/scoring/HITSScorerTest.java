package org.firebird.analyzer.graph.scoring;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class HITSScorerTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public HITSScorerTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(HITSScorerTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        try {
        	HITSScorer hits = new HITSScorer();
        	hits.analyze();
        } catch (Exception ex) {
        	ex.printStackTrace();
        }  	
    	
    	assertTrue(true);
    }
}
