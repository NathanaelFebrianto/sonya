package com.nhn.socialanalytics.nlp.morpheme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings("serial")
public class Sentence extends ArrayList<Element> {

	private String sentence;
	private HashSet<Integer> indexSet = new HashSet<Integer>();
	
	public Sentence(String sentence) {
		this.sentence = sentence;
	}

	public String getSentence() {
		return sentence;
	}
	
	public boolean add(Element element) {
		if (element != null) {
			indexSet.add(element.getIndex());
			return super.add(element);
		}
		
		return false;
	}
	
	public int getUniqueSize() {
		return indexSet.size();
	}

	public List<Element> getEojeols(int index) {
		List<Element> list = new ArrayList<Element>();
		
		for (int i = 0; i < size(); i++) {
			Element element = (Element) get(i);
			if (element.getIndex() == index)
				list.add(element);				
		}		
		return list;
	}
	
	public void sort(boolean ascending) {
		Collections.sort(this, new ElementComparator(ascending));
	}
	
	class ElementComparator implements Comparator<Element> {
		private boolean ascending = true;
		
		public ElementComparator(boolean ascending) {
			this.ascending = ascending;				
		}
		
		public int compare(Element e1, Element e2) {		
			Integer s1 = e1.getIndex();
			Integer s2 = e2.getIndex();
			if (ascending)
				return s1.compareTo(s2);
			else
				return s2.compareTo(s1);
		}		
	}
}
