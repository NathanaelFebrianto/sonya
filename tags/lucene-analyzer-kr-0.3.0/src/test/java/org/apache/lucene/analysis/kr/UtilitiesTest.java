package org.apache.lucene.analysis.kr;

import org.apache.lucene.analysis.kr.utils.StringUtil;
import org.apache.lucene.analysis.kr.utils.VerbUtil;

import junit.framework.TestCase;

public class UtilitiesTest extends TestCase {

	public void testEndsWithVerbSuffix() throws Exception {
		String str = "말하";
		int i = VerbUtil.endsWithVerbSuffix(str);
		if(i==-1) return;
		assertEquals("하", str.substring(i));
		System.out.println(i+":"+str.substring(i));
	}
	
	public void testEndsWithXVerb() throws Exception {
		String str = "피어오르";
		int i = VerbUtil.endsWithXVerb(str);
		if(i==-1) return;
		assertEquals("오르", str.substring(i));
		System.out.println(i+":"+str.substring(i));
	}	
	
}
