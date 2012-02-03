package org.apache.lucene.analysis.kr;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.kr.morph.AnalysisOutput;
import org.apache.lucene.analysis.kr.morph.CompoundEntry;
import org.apache.lucene.analysis.kr.morph.MorphAnalyzer;
import org.apache.lucene.analysis.kr.morph.MorphException;
import org.apache.lucene.analysis.kr.morph.PatternConstants;
import org.apache.lucene.analysis.kr.morph.WordSpaceAnalyzer;
import org.apache.lucene.analysis.kr.utils.DictionaryUtil;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.Version;

public class KoreanFilter extends TokenFilter {
    	
	private final Version matchVersion;

	private LinkedList<Token> koreanQueue;
	
	private LinkedList<Token> cjQueue;
	
	private MorphAnalyzer morph;
	
	WordSpaceAnalyzer wsAnal;
	
	private boolean bigrammable = true;
	
	private boolean hasOrigin = true;
	
	private static final String APOSTROPHE_TYPE = KoreanTokenizerImpl.TOKEN_TYPES[KoreanTokenizerImpl.APOSTROPHE];
	private static final String ACRONYM_TYPE = KoreanTokenizerImpl.TOKEN_TYPES[KoreanTokenizerImpl.ACRONYM];
	 
	// this filters uses attribute type
	private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	
	public KoreanFilter(TokenStream input) {
		this(Version.LUCENE_33, input);
		koreanQueue =  new LinkedList();
		cjQueue =  new LinkedList();
		morph = new MorphAnalyzer();
		wsAnal = new WordSpaceAnalyzer();
	}

	public KoreanFilter(Version matchVersion, TokenStream input) {
		super(input);
		this.matchVersion = matchVersion;
	}
	
	/**
	 * 
	 * @param input	input token stream
	 * @param bigram	Whether the bigram index term return or not.
	 */
	public KoreanFilter(TokenStream input, boolean bigram) {
		this(input);	
		bigrammable = bigram;
	}
	
	public KoreanFilter(TokenStream input, boolean bigram, boolean has) {
		this(input, bigram);
		hasOrigin = has;
	}
	
	@Override
	public final boolean incrementToken() throws IOException {
		if (matchVersion.onOrAfter(Version.LUCENE_31))
			return input.incrementToken(); // TODO: add some niceties for the
											// new grammar
		else
			return incrementTokenClassic();
	}

	public final boolean incrementTokenClassic() throws IOException {
		if (!input.incrementToken()) {
			return false;
		}

		final char[] buffer = termAtt.buffer();
		final int bufferLength = termAtt.length();
		final String type = typeAtt.type();

		if (type == APOSTROPHE_TYPE
				&& // remove 's
				bufferLength >= 2
				&& buffer[bufferLength - 2] == '\''
				&& (buffer[bufferLength - 1] == 's' || buffer[bufferLength - 1] == 'S')) {
			// Strip last 2 characters off
			termAtt.setLength(bufferLength - 2);
		} else if (type == ACRONYM_TYPE) { // remove dots
			int upto = 0;
			for (int i = 0; i < bufferLength; i++) {
				char c = buffer[i];
				if (c != '.')
					buffer[upto++] = c;
			}
			termAtt.setLength(upto);
		}

		return true;
	}
	
	/**
	 * Modified by Louie.
	 * 
	 * @param result
	 * @return
	 * @throws IOException
	 */
	public Token next(Token result) throws IOException {
		
		if(koreanQueue.size() > 0)
			return (Token) koreanQueue.removeFirst();
		else if(cjQueue.size() > 0)
			return (Token) cjQueue.removeFirst();
		
	    int skippedPositions = 0;

	    /*
		try {
		    while((result = input.next(result)) != null) {

		    	if(result.type().equals(KoreanTokenizer.TOKEN_TYPES[KoreanTokenizer.KOROREAN])) {		    		
		    		result = analysisKorean(result, skippedPositions);
		    	} else if(result.type().equals(KoreanTokenizer.TOKEN_TYPES[KoreanTokenizer.CJ])) {
		    		result = analysisCJ(result, skippedPositions);
		    	} else {
		    		result = analysisETC(result);
		    	}

		    	if(result==null) {
					skippedPositions++;			    		
		    		continue;
		    	}
			
				return result;
			}
		} catch (MorphException e) {
			throw new IOException(e.getMessage());
		}
		*/
	    
	    // modified by louie.
	    TermAttribute termAttr = input.getAttribute(TermAttribute.class); 		
        OffsetAttribute offSetAttr = input.getAttribute(OffsetAttribute.class);

		try {
		    while(input.incrementToken()) {

		    	if(result.type().equals(KoreanTokenizer.TOKEN_TYPES[KoreanTokenizer.HANGUL])) {		    		
		    		result = analysisKorean(result, skippedPositions);
		    	} else if(result.type().equals(KoreanTokenizer.TOKEN_TYPES[KoreanTokenizer.CJ])) {
		    		result = analysisCJ(result, skippedPositions);
		    	} else {
		    		result = analysisETC(result);
		    	}

		    	if(result==null) {
					skippedPositions++;			    		
		    		continue;
		    	}
			
				return result;
			}
		} catch (MorphException e) {
			throw new IOException(e.getMessage());
		}
	    
		return null;
	}
	
	/**
	 * 한글을 분석한다.
	 * @param token
	 * @param skipinc
	 * @return
	 * @throws MorphException
	 */
	private Token analysisKorean(Token token, int skipinc) throws MorphException {
		
		//String input = token.termText();
		String input = token.term();	// modified by louie.
		
		List<AnalysisOutput> outputs = morph.analyze(input);
		if(outputs.size()==0) return null;
		
		HashMap<String,Integer> map = new HashMap();
		if(hasOrigin) map.put(input, new Integer(1));
		
		if(outputs.get(0).getScore()==AnalysisOutput.SCORE_CORRECT) {
			extractKeyword(outputs, map);
		} else {
			try{
				List<AnalysisOutput> list = wsAnal.analyze(input);
				
				List<AnalysisOutput> results = new ArrayList();			
				if(list.size()>1) {
					for(AnalysisOutput o : list) {
						if(hasOrigin) map.put(o.getSource(), new Integer(1));				
						results.addAll(morph.analyze(o.getSource()));
					}				
				} else {
					results.addAll(list);
				}
	
				extractKeyword(results, map);
			}catch(Exception e) {
				extractKeyword(outputs, map);
			}
		}
		
		Iterator<String> iter = map.keySet().iterator();
		int i=0;
		while(iter.hasNext()) {
			
			String text = iter.next();
			
			if(text.length()<=1) continue;
			
			int index = input.indexOf(text);
			Token t = new Token(text,
					token.startOffset()+(index!=-1?index:0),
					index!=-1?token.startOffset()+index+text.length():token.endOffset(),
					KoreanTokenizer.TOKEN_TYPES[KoreanTokenizer.HANGUL]);
			if(i==0) t.setPositionIncrement(token.getPositionIncrement()+skipinc);
			else t.setPositionIncrement(0);
			koreanQueue.add(t);
			i++;
		}
		
		if(koreanQueue.size()==0) return null;

		return koreanQueue.removeFirst();
	}
	
	private void extractKeyword(List<AnalysisOutput> outputs, HashMap<String,Integer> map) throws MorphException {
		
		for(AnalysisOutput output : outputs) {			

			if(output.getPos()!=PatternConstants.POS_VERB) {
				map.put(output.getStem(), new Integer(1));	
//			}else {
//				map.put(output.getStem()+"다", new Integer(1));	
			}				
			
			if(output.getScore()>=AnalysisOutput.SCORE_COMPOUNDS) {
				List<CompoundEntry> cnouns = output.getCNounList();
				for(int jj=0;jj<cnouns.size();jj++) {
					CompoundEntry cnoun = cnouns.get(jj);
					if(cnoun.getWord().length()>1) map.put(cnoun.getWord(),  new Integer(0));
					if(jj==0 && cnoun.getWord().length()==1)
						map.put(cnoun.getWord()+cnouns.get(jj+1).getWord(),  new Integer(0));
					else if(jj>1 && cnoun.getWord().length()==1)
						map.put(cnouns.get(jj).getWord()+cnoun.getWord(),  new Integer(0));
				}
			} else if(bigrammable){
				addBiagramToMap(output.getStem(),map);
			}
		}

	}
	
	private void addBiagramToMap(String input, HashMap map) {
		int offset = 0;
		int strlen = input.length();
		while(offset<strlen-1) {
			if(isAlphaNumChar(input.charAt(offset))) {
				String text = findAlphaNumeric(input.substring(offset));
				map.put(text,  new Integer(0));
				offset += text.length();
			} else {
				String text = input.substring(offset,
						offset+2>strlen?strlen:offset+2);
				map.put(text,  new Integer(0));
				offset++;
			}				
		}
	}
	
	private String findAlphaNumeric(String text) {
		int pos = 0;
		for(int i=0;i<text.length();i++) {
			if(!isAlphaNumChar(text.charAt(i))) break;
			pos++;
		}				
		return text.substring(0,pos);
	}
	
	private Token analysisCJ(Token token, int skipinc) throws MorphException {
		
		//String input = token.termText();
		String input = token.term();	// modified by louie.
		
		Token t = new Token(input,0,token.endOffset(),KoreanTokenizer.TOKEN_TYPES[KoreanTokenizer.CJ]);
		t.setPositionIncrement(token.getPositionIncrement()+skipinc);
		cjQueue.add(t);
		
		String kor = DictionaryUtil.getCJWord(input);	
		if(kor!=null) {			
			Token t1 = new Token(kor,0,token.endOffset(),KoreanTokenizer.TOKEN_TYPES[KoreanTokenizer.CJ]);
			t1.setPositionIncrement(0);
			cjQueue.add(t1);			
		}
			
		return cjQueue.removeFirst();
	}
	
	private Token analysisETC(Token t) throws MorphException {
	    char[] buffer = t.termBuffer();
	    final int bufferLength = t.termLength();
	    final String type = t.type();

	    if (type == APOSTROPHE_TYPE &&		  // remove 's
	    	bufferLength >= 2 &&
	        buffer[bufferLength-2] == '\'' &&
	        (buffer[bufferLength-1] == 's' || buffer[bufferLength-1] == 'S')) {
	      // Strip last 2 characters off
	      t.setTermLength(bufferLength - 2);
	    } else if (type == ACRONYM_TYPE) {		  // remove dots
	      int upto = 0;
	      for(int i=0;i<bufferLength;i++) {
	        char c = buffer[i];
	        if (c != '.')
	          buffer[upto++] = c;
	      }
	      t.setTermLength(upto);
	    }

	    return t;
	}
	
	private boolean isAlphaNumChar(int c) {
		if((c>=48&&c<=57)||(c>=65&&c<=122)) return true;		
		return false;
	}
	
	public void setHasOrigin(boolean has) {
		hasOrigin = has;
	}
}
