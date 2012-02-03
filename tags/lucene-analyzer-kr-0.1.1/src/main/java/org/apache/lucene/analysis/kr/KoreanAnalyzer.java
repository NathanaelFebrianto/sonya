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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WordlistLoader;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;

/**
 * Filters {@link StandardTokenizer} with {@link StandardFilter},
 * {@link LowerCaseFilter} and {@link StopFilter}, using a list of English stop
 * words.
 * 
 * @version $Id: KoreanAnalyzer.java,v 1.5 2010/05/25 01:41:04 smlee0818 Exp $
 */
public class KoreanAnalyzer extends StopwordAnalyzerBase {

	private Set stopSet;

	private boolean bigrammable = true;

	private boolean hasOrigin = true;

	/**
	 * An unmodifiable set containing some common English words that are usually
	 * not useful for searching.
	 */
	public static final Set<?> STOP_WORDS_SET = StopAnalyzer.ENGLISH_STOP_WORDS_SET;

	public static final String[] KOR_STOP_WORDS_SET = new String[]{"이", "그",
			"저", "것", "수", "등", "들"};

	public static final String DIC_ENCODING = "UTF-8";

	/** Default maximum allowed token length */
	public static final int DEFAULT_MAX_TOKEN_LENGTH = 255;

	private int maxTokenLength = DEFAULT_MAX_TOKEN_LENGTH;
	
	/**
	 * Specifies whether deprecated acronyms should be replaced with HOST type.
	 * See {@linkplain "https://issues.apache.org/jira/browse/LUCENE-1068"}
	 */
	private final boolean replaceInvalidAcronym;
	
	
	/**
	 * Builds an analyzer with the given stop words.
	 * 
	 * @param matchVersion
	 *            Lucene version to match See
	 *            {@link <a href="#version">above</a>}
	 * @param stopWords
	 *            stop words
	 */
	public KoreanAnalyzer(Version matchVersion, Set<?> stopWords) {
		super(matchVersion, stopWords);
		replaceInvalidAcronym = matchVersion.onOrAfter(Version.LUCENE_24);
	}

	/**
	 * Builds an analyzer with the default stop words ({@link #STOP_WORDS_SET}).
	 * 
	 * @param matchVersion
	 *            Lucene version to match See
	 *            {@link <a href="#version">above</a>}
	 */
	public KoreanAnalyzer(Version matchVersion) {
		this(matchVersion, STOP_WORDS_SET);
	}
	
	public KoreanAnalyzer(Version matchVersion, String[] stopWords) {
		this(matchVersion);
		stopSet = StopFilter.makeStopSet(Version.LUCENE_33, stopWords);
		stopSet.addAll(StopFilter.makeStopSet(Version.LUCENE_33, KOR_STOP_WORDS_SET));		
	}

	/**
	 * Builds an analyzer with the stop words from the given file.
	 * 
	 * @see WordlistLoader#getWordSet(File)
	 */
	public KoreanAnalyzer(Version matchVersion, File stopwords)	throws IOException {
		this(matchVersion, WordlistLoader.getWordSet(stopwords));
		InputStream is = new FileInputStream(stopwords);
		InputStreamReader reader = new InputStreamReader(is, DIC_ENCODING);
		stopSet = WordlistLoader.getWordSet(reader);
	}

	/**
	 * Builds an analyzer with the stop words from the given file.
	 * 
	 * @see WordlistLoader#getWordSet(File)
	 */
	public KoreanAnalyzer(Version matchVersion, File stopwords, String encoding) throws IOException {
		this(matchVersion, WordlistLoader.getWordSet(stopwords));
		InputStream is = new FileInputStream(stopwords);
		InputStreamReader reader = new InputStreamReader(is, encoding);
		stopSet = WordlistLoader.getWordSet(reader);
	}

	/**
	 * Builds an analyzer with the stop words from the given reader.
	 * 
	 * @see WordlistLoader#getWordSet(Reader)
	 */
	public KoreanAnalyzer(Version matchVersion, Reader stopwords) throws IOException {
		this(matchVersion, WordlistLoader.getWordSet(stopwords));
		stopSet = WordlistLoader.getWordSet(stopwords);
	}

	@Override
	public TokenStreamComponents createComponents(final String fieldName, final Reader reader) {
		final KoreanTokenizer src = new KoreanTokenizer(matchVersion, reader);
		src.setMaxTokenLength(maxTokenLength);
		src.setReplaceInvalidAcronym(replaceInvalidAcronym);
		TokenStream tok = new KoreanFilter(src, bigrammable, hasOrigin);
		tok = new LowerCaseFilter(matchVersion, tok);
		tok = new StopFilter(matchVersion, tok, stopwords);
		return new TokenStreamComponents(src, tok) {
			@Override
			protected boolean reset(final Reader reader) throws IOException {
				src.setMaxTokenLength(KoreanAnalyzer.this.maxTokenLength);
				return super.reset(reader);
			}
		};
	}

	/**
	 * Set maximum allowed token length. If a token is seen that exceeds this
	 * length then it is discarded. This setting only takes effect the next time
	 * tokenStream or reusableTokenStream is called.
	 */
	public void setMaxTokenLength(int length) {
		maxTokenLength = length;
	}

	/**
	 * @see #setMaxTokenLength
	 */
	public int getMaxTokenLength() {
		return maxTokenLength;
	}

	/**
	 * determine whether the bigram index term is returned or not if a input
	 * word is failed to analysis If true is set, the bigram index term is
	 * returned. If false is set, the bigram index term is not returned.
	 * 
	 * @param is
	 */
	public void setBigrammable(boolean is) {
		bigrammable = is;
	}

	/**
	 * determine whether the original term is returned or not if a input word is
	 * analyzed morphically.
	 * 
	 * @param has
	 */
	public void setHasOrigin(boolean has) {
		hasOrigin = has;
	}
}
