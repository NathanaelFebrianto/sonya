package org.apache.lucene.analysis.kr;

import java.util.Iterator;

import org.apache.lucene.analysis.kr.morph.AnalysisOutput;
import org.apache.lucene.analysis.kr.morph.MorphAnalyzer;
import org.apache.lucene.analysis.kr.tagging.Tagger;

import junit.framework.TestCase;

public class TaggerTest extends TestCase {

	public void testTagging() throws Exception {
		
		Iterator<String[]> iter = Tagger.getGR("할");
		while(iter.hasNext()) {
			String[] strs = iter.next();
			System.out.println(strs[0]+"?"+strs[1]+"?"+strs[2]+"?"+strs[3]+"?"+strs[4]+"?"+strs[5]);
		}
		
	}
	
	public void testTag() throws Exception {

		String str0 = "증가함에";
		String str1 = "따라서";
		String str2 = "적다";
		
		MorphAnalyzer morphAnal = new MorphAnalyzer();
		
		Tagger tagger = new Tagger();
		tagger.tagging(str0, morphAnal.analyze(str0));
		AnalysisOutput o = tagger.tagging(str1,str2, morphAnal.analyze(str1),morphAnal.analyze(str2));
		
		System.out.println(">>"+o);
	}
}
