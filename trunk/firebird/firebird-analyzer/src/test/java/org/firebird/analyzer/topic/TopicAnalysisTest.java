package org.firebird.analyzer.topic;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TopicAnalysisTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TopicAnalysisTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(TopicAnalysisTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
    	assertTrue(true);
    	
    	try {    		
    		// document source file writer
    		/*
    		DocSourceWriter sourceWriter = new DocSourceWriter("D:/firebird/text/");
    		
        	UserBlogEntryManager userBlogEntryManager = new UserBlogEntryManagerImpl();
        	List<String> users = userBlogEntryManager.getDistinctUsers(1);
        	
        	for (int i = 0; i < users.size(); i++) {
        		String userId = (String) users.get(i);
        		List<UserBlogEntry> userBlogEntries = userBlogEntryManager.getUserBlogEntries(1, userId);
        		sourceWriter.write(userId, userBlogEntries);
        	}
        	
    		// index writer
        	DocIndexWriter indexWriter = new DocIndexWriter();
    		indexWriter.write("D:/firebird/text/", "D:/firebird/index/");
        	
        	// vector writer    		
    		DocVectorWriter vectorWriter = new DocVectorWriter();
    		vectorWriter.write("D:/firebird/index/",
    					 "D:/firebird/vectors",
    					 "body",
    					 null,
    					 "D:/firebird/dict.txt",
    					 null,
    					 null,
    					 null,
    					 Long.MAX_VALUE,
    					 null,
    					 5,
    					 70);
    		
    		// LDA analysis
    		LDAAnalyzer ldaAnalyzer = new LDAAnalyzer();
    		ldaAnalyzer.analyze("D:/firebird/vectors", 
    					"D:/firebird/lda/", 
    					true, 
    					10, 
    					10000, 
    					-1, 
    					40, 
    					2);
 
    		// Print LDA topics
    		LDATopics ldaTopics = new LDATopics();
    		ldaTopics.writeEachTopics("D:/firebird/lda/state-40", 
    					"D:/firebird/dict.txt", 
    					"D:/firebird/topics/", 
    					50, 
    					null);
    		*/
    		
    	
     	} catch (Exception ex) {
        	ex.printStackTrace();
        }    	
    }
}
