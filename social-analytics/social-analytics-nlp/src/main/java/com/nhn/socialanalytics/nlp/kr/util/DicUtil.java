package com.nhn.socialanalytics.nlp.kr.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.kr.utils.FileUtil;
import org.apache.lucene.analysis.kr.utils.StringUtil;

public class DicUtil {

	private static HashMap<String, String> josas;
	private static HashMap<String, String> eomis;

	public synchronized static void loadDictionary() throws Exception {

		josas = new HashMap<String, String>();
		eomis = new HashMap<String, String>();

		List<String> josaList = null;
		List<String> eomiList = null;

		try {
			josaList = FileUtil.readLines(
					"org/apache/lucene/analysis/kr/dic/josa_tags.dic", "UTF-8");
			eomiList = FileUtil.readLines(
					"org/apache/lucene/analysis/kr/dic/eomi_tags.dic", "UTF-8");

			for (int i = 0; i < josaList.size(); i++) {
				String strJosa = josaList.get(i);
				String[] pair = StringUtil.split(strJosa, ",");

				String josa = pair[0];
				String tag = pair[1];

				if (josa != null && tag != null) {
					josas.put(josa, tag);
					System.out.println(tag + ", " + josa);
				}
					
			}

			for (int i = 0; i < eomiList.size(); i++) {
				String strEomi = eomiList.get(i);
				String[] pair = StringUtil.split(strEomi, ",");

				String eomi = pair[0];
				String tag = pair[1];

				if (eomi != null && tag != null) {
					eomis.put(eomi, tag);
					System.out.println(tag + ", " + eomi);
				}
					
			}

		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	public static String getEomiTag(String eomi) {
		if (eomi != null) {
			for (Map.Entry<String, String> entry : eomis.entrySet()) {
				if (eomi.equals(entry.getKey())) {
					return (String) entry.getValue();
				}
			}				
		}
	
		return null;
	}
	
	public static String getJosaTag(String josa) {
		if (josa != null) {
			for (Map.Entry<String, String> entry : josas.entrySet()) {
				if (josa.equals(entry.getKey())) {
					return (String) entry.getValue();
				}
			}				
		}
	
		return null;
	}

	public static void main(String[] args) {
		try {
			DicUtil.loadDictionary();
			System.out.println(DicUtil.getEomiTag("À¸´Ï"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
