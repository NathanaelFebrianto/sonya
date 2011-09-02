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
import java.io.Reader;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.Tokenizer;

public class KoreanTokenizer extends Tokenizer {

	/** A private instance of the JFlex-constructed scanner */
	private final KoreanTokenizerImpl scanner;

	public static final int ALPHANUM = 0;
	public static final int APOSTROPHE = 1;
	public static final int ACRONYM = 2;
	public static final int COMPANY = 3;
	public static final int EMAIL = 4;
	public static final int HOST = 5;
	public static final int NUM = 6;
	public static final int CJ = 7;
	public static final int KOROREAN = 8;

	/** String token types that correspond to token type int constants */
	public static final String[] TOKEN_TYPES = new String[] { "<ALPHANUM>",
			"<APOSTROPHE>", "<ACRONYM>", "<COMPANY>", "<EMAIL>", "<HOST>",
			"<NUM>", "<CJ>", "<KOREAN>" };

	private int maxTokenLength = KoreanAnalyzer.DEFAULT_MAX_TOKEN_LENGTH;

	public KoreanTokenizer(Reader input) {
		this.input = input;
		this.scanner = new KoreanTokenizerImpl(input);
	}

	/**
	 * Appended by Louie.
	 * 
	 * @return
	 * @throws IOException
	 */
	public boolean incrementToken() throws IOException {
		return incrementToken();
	}

	public Token next() throws IOException {
		return next(new Token());
	}

	public Token next(Token result) throws IOException {

		int posIncr = 1;
		if (result == null)
			result = new Token();

		while (true) {
			int tokenType = scanner.getNextToken();

			if (tokenType == KoreanTokenizerImpl.YYEOF) {
				return null;
			}

			if (scanner.yylength() <= maxTokenLength) {
				result.clear();
				result.setPositionIncrement(posIncr);
				scanner.getText(result);
				final int start = scanner.yychar();
				result.setStartOffset(start);
				result.setEndOffset(start + result.termLength());
				// This 'if' should be removed in the next release. For now, it converts
				// invalid acronyms to HOST. When removed, only the 'else' part should remain.
				if (tokenType == KoreanTokenizerImpl.ACRONYM_DEP) {
					result.setType(KoreanTokenizerImpl.TOKEN_TYPES[KoreanTokenizerImpl.ACRONYM]);
				} else {
					result.setType(KoreanTokenizerImpl.TOKEN_TYPES[tokenType]);
				}
				return result;
			} else
				// When we skip a too-long term, we still increment the position increment
				posIncr++;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.lucene.analysis.TokenStream#reset()
	 */
	public void reset() throws IOException {
		super.reset();
		scanner.yyreset(input);
	}

	public void reset(Reader reader) throws IOException {
		input = reader;
		reset();
	}
	  
}
