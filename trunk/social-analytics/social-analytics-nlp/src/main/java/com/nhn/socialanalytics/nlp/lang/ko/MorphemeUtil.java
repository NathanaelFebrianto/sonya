package com.nhn.socialanalytics.nlp.lang.ko;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.kr.utils.StringUtil;

import com.nhn.socialanalytics.nlp.dictionary.DictionaryFactory;

public class MorphemeUtil {

	private static HashMap<String, String> josas;
	private static HashMap<String, String> eomis;

	public static void loadDictionary() throws Exception {

		josas = new HashMap<String, String>();
		eomis = new HashMap<String, String>();

		List<String> josaList = null;
		List<String> eomiList = null;

		try {
			josaList = DictionaryFactory.loadDictionary("com/nhn/socialanalytics/nlp/lang/ko/dic/josatag.dic"); 
			eomiList = DictionaryFactory.loadDictionary("com/nhn/socialanalytics/nlp/lang/ko/dic/eomitag.dic"); 

			System.out.println("loading josa tags...");
			for (int i = 0; i < josaList.size(); i++) {
				String strJosa = josaList.get(i);
				String[] pair = StringUtil.split(strJosa, ",");

				String josa = pair[0];
				String tag = pair[1];

				if (josa != null && tag != null) {
					josas.put(josa, tag);
					//System.out.println(tag + ", " + josa);
				}
					
			}

			System.out.println("loading eomi tags...");
			for (int i = 0; i < eomiList.size(); i++) {
				String strEomi = eomiList.get(i);
				String[] pair = StringUtil.split(strEomi, ",");

				String eomi = pair[0];
				String tag = pair[1];

				if (eomi != null && tag != null) {
					eomis.put(eomi, tag);
					//System.out.println(tag + ", " + eomi);
				}
					
			}
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
			MorphemeUtil.loadDictionary();
			System.out.println(MorphemeUtil.getEomiTag("은"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
