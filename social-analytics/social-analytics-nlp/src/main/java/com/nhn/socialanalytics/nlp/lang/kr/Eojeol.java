package com.nhn.socialanalytics.nlp.lang.kr;

import org.apache.lucene.analysis.kr.morph.AnalysisOutput;
import org.apache.lucene.analysis.kr.morph.MorphException;
import org.apache.lucene.analysis.kr.morph.WordEntry;
import org.apache.lucene.analysis.kr.utils.DictionaryUtil;

import com.nhn.socialanalytics.nlp.morpheme.Token;


public class Eojeol extends Token {
	
	private String josa;
	private String eomi;
	private String josaTag;
	private String eomiTag;
	private char[] posCode;
	private int pattern;
	private String constituent;
	private int score;
	
	public String getJosa() {
		return josa;
	}
	public void setJosa(String josa) {
		this.josa = josa;
	}
	public String getEomi() {
		return eomi;
	}
	public void setEomi(String eomi) {
		this.eomi = eomi;
	}
	public String getJosaTag() {
		return josaTag;
	}
	public void setJosaTag(String josaTag) {
		this.josaTag = josaTag;
	}
	public String getEomiTag() {
		return eomiTag;
	}
	public void setEomiTag(String eomiTag) {
		this.eomiTag = eomiTag;
	}
	public char[] getPosCode() {
		return posCode;
	}
	public void setPosCode(char[] posCode) {
		this.posCode = posCode;
	}
	public int getPattern() {
		return pattern;
	}
	public void setPattern(int pattern) {
		this.pattern = pattern;
	}
	public String getConstituent() {
		return constituent;
	}
	public void setConstituent(String constituent) {
		this.constituent = constituent;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	public void makeObject(int index, AnalysisOutput o) throws MorphException {
		this.setIndex(index);
		this.setSource(o.toString());
		this.setTerm(o.getStem());
		this.setJosa(o.getJosa());
		this.setEomi(o.getEomi());
		this.setPattern(o.getPatn());
		this.setPos(o.getPos());
		this.setScore(o.getScore());
		
		WordEntry entry = DictionaryUtil.getWord(o.getStem());
		if (entry != null) {
			this.setPosCode(entry.getFeatures());
		}
		
		this.setEomiTag(MorphemeUtil.getEomiTag(o.getEomi()));
		this.setJosaTag(MorphemeUtil.getJosaTag(o.getJosa()));
	
		/*
		for (int i = 0; i < o.getCNounList().size(); i++) {
			String word = o.getCNounList().get(i).getWord();
			System.out.print(word + "/");
		}
		*/
	}
	
	@ Override
	public boolean containsTagOf(String[] tags) {
		if (containsEomiTagOf(tags))
			return true;
		else if (containsJosaTagOf(tags))
			return true;
		else if (containsPosTagOf(tags))
			return true;
		
		return false;
	}
	
	 public boolean containsEomiTagOf(String[] tags) {
		 for (int i = 0; i < tags.length; i++) {
			if (tags[i].equals(this.getEomiTag())) {
				return true;
			}
		 }		 
		 return false;		 
	 }
	 
	 public boolean containsJosaTagOf(String[] tags) {
		 for (int i = 0; i < tags.length; i++) {
			if (tags[i].equals(this.getJosaTag())) {
				return true;
			}
		 }		 
		 return false;		 
	 }

	
	public String toString() {
		StringBuffer sb = new StringBuffer()
			.append("[").append(index).append("]")
			.append(" eojeol=").append(source)
			.append("|term=").append(term)
			.append("|josa=").append((josa != null) ? josa : " ")
			.append("|josaTag=").append((josaTag != null) ? josaTag : " ")
			.append("|eomi=").append((eomi != null) ? eomi : " ")
			.append("|eomiTag=").append((eomiTag != null) ? eomiTag : " ")
			.append("|pos=").append(pos)
			.append("|posCode=").append((posCode != null) ? String.valueOf(posCode) : " ")
			.append("|pattern=").append(pattern)
			.append("|constituent=").append(constituent)
			.append("|score=").append(score);
		
		return sb.toString();		
	}	
	
	public String toJSON() {
		StringBuffer sb = new StringBuffer()
			.append("{")
			.append("'id':").append(id).append(",")
			.append("'index':").append(index).append(",")
			.append("'eojeol':").append(source).append(",")
			.append("'term':").append(term).append(",")
			.append("'josa':").append((josa != null) ? josa : " ").append(",")
			.append("'josaTag':").append((josaTag != null) ? josaTag : " ").append(",")
			.append("'eomi':").append((eomi != null) ? eomi : " ").append(",")
			.append("'eomiTag':").append((eomiTag != null) ? eomiTag : " ").append(",")
			.append("'pos':").append(pos).append(",")
			.append("'posCode':").append((posCode != null) ? String.valueOf(posCode) : " ").append(",")
			.append("'pattern':").append(pattern).append(",")
			.append("'score':").append(score)
			.append("}");
		
		return sb.toString();
	}
}
