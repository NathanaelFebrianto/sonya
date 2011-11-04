package com.nhn.socialanalytics.nlp.kr.morpheme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings("serial")
public class Sentence extends ArrayList<Eojeol> {

	private String sentence;
	private HashSet<Integer> indexSet = new HashSet<Integer>();
	
	public Sentence(String sentence) {
		this.sentence = sentence;
	}

	public String getSentence() {
		return sentence;
	}
	
	public boolean add(Eojeol eojeol) {
		if (eojeol != null) {
			indexSet.add(eojeol.getIndex());
			return super.add(eojeol);
		}
		
		return false;
	}
	
	public int getUniqueSize() {
		return indexSet.size();
	}

	public List<Eojeol> getEojeols(int index) {
		List<Eojeol> list = new ArrayList<Eojeol>();
		
		for (int i = 0; i < size(); i++) {
			Eojeol eojeol = (Eojeol) get(i);
			if (eojeol.getIndex() == index)
				list.add(eojeol);				
		}		
		return list;
	}
	
	public void sort(boolean ascending) {
		Collections.sort(this, new EojeolComparator(ascending));
	}
	
	class EojeolComparator implements Comparator<Eojeol> {
		private boolean ascending = true;
		
		public EojeolComparator(boolean ascending) {
			this.ascending = ascending;				
		}
		
		public int compare(Eojeol e1, Eojeol e2) {		
			Integer s1 = e1.getIndex();
			Integer s2 = e2.getIndex();
			if (ascending)
				return s1.compareTo(s2);
			else
				return s2.compareTo(s1);
		}		
	}
}
