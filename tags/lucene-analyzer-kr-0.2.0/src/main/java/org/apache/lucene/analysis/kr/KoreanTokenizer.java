package org.apache.lucene.analysis.kr;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizerImpl;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.util.Version;

/**
 * A grammar-based tokenizer constructed with JFlex.
 * <p>
 * As of Lucene version 3.1, this class implements the Word Break rules from the
 * Unicode Text Segmentation algorithm, as specified in <a
 * href="http://unicode.org/reports/tr29/">Unicode Standard Annex #29</a>.
 * <p/>
 * <p>
 * Many applications have specific tokenizer needs. If this tokenizer does not
 * suit your application, please consider copying this source code directory to
 * your project and maintaining your own grammar-based tokenizer.
 * 
 * <a name="version"/>
 * <p>
 * You must specify the required {@link Version} compatibility when creating
 * StandardTokenizer:
 * <ul>
 * <li>As of 3.1, StandardTokenizer implements Unicode text segmentation. If you
 * use a previous version number, you get the exact behavior of
 * {@link ClassicTokenizer} for backwards compatibility.
 * </ul>
 */

public final class KoreanTokenizer extends Tokenizer {
	/** A private instance of the JFlex-constructed scanner */
	private KoreanTokenizerImpl scanner;

	public static final int ALPHANUM = 0;
	/** @deprecated */
	@Deprecated
	public static final int APOSTROPHE = 1;
	/** @deprecated */
	@Deprecated
	public static final int ACRONYM = 2;
	/** @deprecated */
	@Deprecated
	public static final int COMPANY = 3;
	public static final int EMAIL = 4;
	/** @deprecated */
	@Deprecated
	public static final int HOST = 5;
	public static final int NUM = 6;
	/** @deprecated */
	@Deprecated
	public static final int CJ = 7;

	/**
	 * @deprecated this solves a bug where HOSTs that end with '.' are
	 *             identified as ACRONYMs.
	 */
	@Deprecated
	public static final int ACRONYM_DEP = 8;

	public static final int SOUTHEAST_ASIAN = 9;
	public static final int IDEOGRAPHIC = 10;
	public static final int HIRAGANA = 11;
	public static final int KATAKANA = 12;
	public static final int HANGUL = 13;

	/** String token types that correspond to token type int constants */
	public static final String[] TOKEN_TYPES = new String[]{"<ALPHANUM>",
			"<APOSTROPHE>", "<ACRONYM>", "<COMPANY>", "<EMAIL>", "<HOST>",
			"<NUM>", "<CJ>", "<ACRONYM_DEP>", "<SOUTHEAST_ASIAN>",
			"<IDEOGRAPHIC>", "<HIRAGANA>", "<KATAKANA>", "<HANGUL>"};

	private boolean replaceInvalidAcronym;

	private int maxTokenLength = KoreanAnalyzer.DEFAULT_MAX_TOKEN_LENGTH;

	/**
	 * Set the max allowed token length. Any token longer than this is skipped.
	 */
	public void setMaxTokenLength(int length) {
		this.maxTokenLength = length;
	}

	/** @see #setMaxTokenLength */
	public int getMaxTokenLength() {
		return maxTokenLength;
	}

	/**
	 * Creates a new instance of the
	 * {@link org.apache.lucene.analysis.standard.StandardTokenizer}. Attaches
	 * the <code>input</code> to the newly created JFlex scanner.
	 * 
	 * @param input
	 *            The input reader
	 * 
	 *            See http://issues.apache.org/jira/browse/LUCENE-1068
	 */
	public KoreanTokenizer(Version matchVersion, Reader input) {
		super();
		init(input, matchVersion);
	}

	/**
	 * Creates a new StandardTokenizer with a given {@link AttributeSource}.
	 */
	public KoreanTokenizer(Version matchVersion, AttributeSource source, Reader input) {
		super(source);
		init(input, matchVersion);
	}

	/**
	 * Creates a new StandardTokenizer with a given
	 * {@link org.apache.lucene.util.AttributeSource.AttributeFactory}
	 */
	public KoreanTokenizer(Version matchVersion, AttributeFactory factory, Reader input) {
		super(factory);
		init(input, matchVersion);
	}

	private final void init(Reader input, Version matchVersion) {
		this.scanner = new KoreanTokenizerImpl(input);
		this.input = input;
	}

	// this tokenizer generates three attributes:
	// term offset, positionIncrement and type
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
	private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
	private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.lucene.analysis.TokenStream#next()
	 */
	@Override
	public final boolean incrementToken() throws IOException {
		clearAttributes();
		int posIncr = 1;

		while (true) {
			int tokenType = scanner.getNextToken();

			if (tokenType == KoreanTokenizerImpl.YYEOF) {
				return false;
			}

			if (scanner.yylength() <= maxTokenLength) {
				posIncrAtt.setPositionIncrement(posIncr);
				scanner.getText(termAtt);
				final int start = scanner.yychar();
				offsetAtt.setOffset(correctOffset(start), correctOffset(start
						+ termAtt.length()));
				// This 'if' should be removed in the next release. For now, it
				// converts
				// invalid acronyms to HOST. When removed, only the 'else' part
				// should
				// remain.
				if (tokenType == KoreanTokenizer.ACRONYM_DEP) {
					if (replaceInvalidAcronym) {
						typeAtt.setType(KoreanTokenizer.TOKEN_TYPES[KoreanTokenizer.HOST]);
						termAtt.setLength(termAtt.length() - 1); // remove extra
																	// '.'
					} else {
						typeAtt.setType(KoreanTokenizer.TOKEN_TYPES[KoreanTokenizer.ACRONYM]);
					}
				} else {
					typeAtt.setType(KoreanTokenizer.TOKEN_TYPES[tokenType]);
				}
				return true;
			} else
				// When we skip a too-long term, we still increment the
				// position increment
				posIncr++;
		}
	}

	@Override
	public final void end() {
		// set final offset
		int finalOffset = correctOffset(scanner.yychar() + scanner.yylength());
		offsetAtt.setOffset(finalOffset, finalOffset);
	}

	@Override
	public void reset(Reader reader) throws IOException {
		super.reset(reader);
		scanner.yyreset(reader);
	}

	/**
	 * Prior to https://issues.apache.org/jira/browse/LUCENE-1068,
	 * StandardTokenizer mischaracterized as acronyms tokens like www.abc.com
	 * when they should have been labeled as hosts instead.
	 * 
	 * @return true if StandardTokenizer now returns these tokens as Hosts,
	 *         otherwise false
	 * 
	 * @deprecated Remove in 3.X and make true the only valid value
	 */
	@Deprecated
	public boolean isReplaceInvalidAcronym() {
		return replaceInvalidAcronym;
	}

	/**
	 * 
	 * @param replaceInvalidAcronym
	 *            Set to true to replace mischaracterized acronyms as HOST.
	 * @deprecated Remove in 3.X and make true the only valid value
	 * 
	 *             See https://issues.apache.org/jira/browse/LUCENE-1068
	 */
	@Deprecated
	public void setReplaceInvalidAcronym(boolean replaceInvalidAcronym) {
		this.replaceInvalidAcronym = replaceInvalidAcronym;
	}
}
