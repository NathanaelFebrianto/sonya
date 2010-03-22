package org.firebird.analyzer.text;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.firebird.io.model.UserBlogEntry;
import org.firebird.io.service.UserBlogEntryManager;
import org.firebird.io.service.impl.UserBlogEntryManagerImpl;

public class TextAnalysisTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TextAnalysisTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(TextAnalysisTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
    	assertTrue(true);
    	
    	try {
    		/*
    		DocSourceWriter docWriter = new DocSourceWriter("D:/firebird/text/");
    		
        	UserBlogEntryManager userBlogEntryManager = new UserBlogEntryManagerImpl();
        	List<String> users = userBlogEntryManager.getDistinctUsers();
        	
        	for (int i = 0; i < users.size(); i++) {
        		String userId = (String) users.get(i);
        		List<UserBlogEntry> userBlogEntries = userBlogEntryManager.getUserBlogEntries(userId);
        		docWriter.write(userId, userBlogEntries);
        	}
        	*/
    		
    		DocIndexWriter indexer = new DocIndexWriter("D:/firebird/text/", "D:/firebird/index/");
    		indexer.write();
    		
    	} catch (Exception ex) {
        	ex.printStackTrace();
        }    	
    }
}
