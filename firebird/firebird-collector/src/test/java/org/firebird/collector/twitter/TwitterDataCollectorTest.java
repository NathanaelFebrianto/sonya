/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.collector.twitter;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.firebird.collector.CollectorConfig;
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
        	// test to collect twitter friends and followers
        	/*
        	CollectorConfig config = new CollectorConfig();
        	config.setDBStorage(false);
        	config.setCollectFriend(true);
        	config.setCollectFollower(true);
        	config.setCollectUserBlogEntry(false);
        	config.setLevelLimit(2);
        	config.setDegreeLimit(2);
        	config.setPeopleLimit(10);        	
        	TwitterDataCollector collector = new TwitterDataCollector(config);
        	collector.collect("louiezzang");
        	*/
        	
        	// test to collect blog entries
        	/*
        	CollectorConfig config = new CollectorConfig();
        	config.setDBStorage(true);
        	config.setCollectUserBlogEntry(true);
        	
        	TwitterDataCollector collector = new TwitterDataCollector(config);
        	VertexManager vertexManager = new VertexManagerImpl();
        	Vertex cond = new Vertex();
        	cond.setWebsiteId(1);
        	cond.setAuthority(0.01);        	
        	List<String> screenNames = vertexManager.getVertexIdsByScoringCondition(cond); 
           	collector.collectBlogEntries(screenNames);     
           	*/
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
    	
    	
    	assertTrue(true);
    }
}
