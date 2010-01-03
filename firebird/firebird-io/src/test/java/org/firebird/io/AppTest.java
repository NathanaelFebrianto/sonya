package org.firebird.io;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.firebird.io.dao.ibatis.VertexDaoiBatis;
import org.firebird.io.model.Vertex;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
		
		//VertexDaoiBatis vertexDao = new VertexDaoiBatis();
		//vertexDao.insertVertex(new Vertex());
	}
}
