package org.apache.lucene.analysis.kr;

import java.io.StringReader;

import junit.framework.TestCase;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

public class KoreanAnalyzerTest extends TestCase {

	/**
	 * t.getPositionIncrement() 는 같은 단어에서 추출되었는지, 다른 단어에서 추출되었는지를 알려준다.
	 * 즉 1이면 현재의 색인어는 새로운 단어에서 추출된 것이고
	 * 0 이면 이전 색인어와 같은 단어에서 추출된 것이다.
	 * 이 값은 검색 랭킹에 영향을 미친다.  즉 값이 작으면 검색랭킹이 올라가서 검색시 상위에 올라오게 된다.
	 * @throws Exception
	 */
	public void testKoreanAnalyzer() throws Exception {
		
		String source = "우리나라라면에서부터 일본라면이 파생되었잖니?";
		source = "너는 다시 내게 돌아 올거야";
		
		long start = System.currentTimeMillis();
		
		KoreanAnalyzer analyzer = new KoreanAnalyzer();
		
		TokenStream stream = analyzer.tokenStream("k", new StringReader(source));
		
		TermAttribute termAttr = stream.getAttribute(TermAttribute.class); 		
        OffsetAttribute offSetAttr = stream.getAttribute(OffsetAttribute.class);
		
		while(stream.incrementToken()) {
			System.out.println(offSetAttr.startOffset()+":"+offSetAttr.endOffset()+":"+termAttr.term());
		}
		
		System.out.println((System.currentTimeMillis()-start)+"ms");
	}
	
}
