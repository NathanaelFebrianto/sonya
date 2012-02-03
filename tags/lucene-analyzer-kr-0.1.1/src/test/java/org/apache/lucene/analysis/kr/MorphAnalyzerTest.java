package org.apache.lucene.analysis.kr;

import java.io.StringReader;
import java.util.List;

import junit.framework.TestCase;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.kr.morph.AnalysisOutput;
import org.apache.lucene.analysis.kr.morph.MorphAnalyzer;
import org.apache.lucene.analysis.kr.morph.MorphAnalyzerManager;
import org.apache.lucene.analysis.kr.morph.WordEntry;
import org.apache.lucene.analysis.kr.utils.DictionaryUtil;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Version;

public class MorphAnalyzerTest extends TestCase {


	public void testMorphAnalyzer() throws Exception {
		
		String[] inputs = new String[] {
				"고물가시대의",
				"대외적",
				"합쳐져","뛰어오르고","급여생활자나","영세자영업자","영세농어민","서민계층들은","온몸으로","엄습하고",
				"드라마가",
				"과장광고","과소비",
				"날을","아울러","휴대전화기능처리부와","코발트의",
				"달라","포함하고",
				"사랑받아봄을","둔",				
				"발행일","출원인",	// 	어미로 끝나는 경우로 분석된다.	
				"노란",
				"만능청약통장","가시밭같다",
				"정책적",	"시리즈를","자리잡은","찜통이다","지난해",
				"데모입니다",
				"바이오및뇌공학",
				"급락조짐을",
				"4.19의거는",
				"고스트x를",
				"검색서비스를",
				"장애물이"
				};
		
		MorphAnalyzer analyzer = new MorphAnalyzer();
		long start = 0;
		for(String input:inputs) {
			List<AnalysisOutput> list = analyzer.analyze(input);
			for(AnalysisOutput o:list) {
				System.out.print(o.toString()+"->");
				for(int i=0;i<o.getCNounList().size();i++){
					System.out.print(o.getCNounList().get(i).getWord()+"/");
				}
				System.out.print(o.getPatn());
				System.out.println("<"+o.getScore()+">");
			}
			if(start==0) start = System.currentTimeMillis();
		}
		System.out.println((System.currentTimeMillis()-start)+"ms");
	}
		
	public void testCloneAnalysisOutput() throws Exception {
		AnalysisOutput output = new AnalysisOutput();
		
		output.setStem("aaaa");
		
		AnalysisOutput clone = output.clone();
		
		assertEquals("aaaa", clone.getStem());
		
		System.out.println(clone.getStem());
	}
	
	public void testKoreanAnalyzer() throws Exception {
		
//		String source = FileUtil.readFileToString(new File("input.txt"), "UTF-8");
		//source="금융당국이 조만간 발표키로 한 중장기 증시 안정대책에 어떤 내용이 담길 지 관심이 높아지고 있다.이창용 금융위 부위원장은 7일 국내외 금융회사 애널리스트와의 간담회가 끝난 뒤 기자회견에서 “조만간 주식시장 안정대책 몇 가지를 검토해 발표할 것”이라며 “해외증시가 급락했지만 국내 증시는 선방하고 있어 정부가 인센티브를 제공하면 효과가 있을 것”이라고 말했다.";
		//source="올해 크리스마스에는 눈이 내리지 않고 비교적 포근할 전망이다 ABC";
		//source = "new Integer(1)";
		//String source = "정성스러운게";
		String source = "이 물건은 배송이 빨라서 정말 좋지만,  품질이 별로 안 좋네요.";
		
		long start = System.currentTimeMillis();
		
		KoreanAnalyzer analyzer = new KoreanAnalyzer(Version.LUCENE_33);
		
		TokenStream stream = analyzer.tokenStream("k", new StringReader(source));
		TermAttribute termAttr = stream.getAttribute(TermAttribute.class); 		
        OffsetAttribute offSetAttr = stream.getAttribute(OffsetAttribute.class);

		while(stream.incrementToken()) {
			System.out.println(offSetAttr.startOffset()+":"+offSetAttr.endOffset()+":"+termAttr.term());
		}
		
		System.out.println((System.currentTimeMillis()-start)+"ms");
	}
	
	public void testMorphAnalyzerManager() throws Exception {
		String input = "나는 학교에 갔습니다";
		input = "이 물건은 배송이 빨라서 괜츈다. 괜츈답니다 괜츈하였습니다 싫어하였습니다 사랑하다 좋아하다 사랑하였습니다 사랑했습니다  사랑받다 사랑받아보다 도와주다 괜찮았습니다 괜찮습니다 좋답니다 " +
				"괜찮다합니다 괜찬답니다. 괜찮답니다 훌륭하답니다 멋지답니다 들을만하다 간답니다 " +
				"걱정하다 걱정하였는데 ";

		MorphAnalyzerManager manager = new MorphAnalyzerManager();
		manager.analyze(input);
	}
	
	public void testAlphaNumeric() throws Exception {
		String str = "0123456789azAZ";
		for(int i=0;i<str.length();i++) {
			System.out.println(str.charAt(i)+":"+(str.charAt(i)-0));
		}
	}
	
	public void testGetWordEntry() throws Exception {
		String s = "밤하늘";
		WordEntry we = DictionaryUtil.getNoun(s);
		if (we == null) {
			System.out.println("Dictionary for '" + s + "' is null");
		} else {
			System.out.println(we.getWord());
		}
	}
	
	/**
	 * 세종사전에서 하다와 되다형 동사를 체언과 결합하기 위해 사용한 테스트케이스
	 * 
	 * @throws Exception
	 */
	/*
	public void testYongonAnalysis() throws Exception {
		
		String fname = "data/용언_상세.txt";
		
		List<String> list = FileUtils.readLines(new File(fname));
		Map<String, String> younons = new HashMap();
		
		MorphAnalyzer analyzer = new MorphAnalyzer();
		long start = 0;
		List youngOutputs = new ArrayList();
		for(String input:list) {
			
			if(!input.endsWith("하다")&&!input.endsWith("되다")) {
				youngOutputs.add(input);
				continue;			
			}
			String eogan = input.substring(0,input.length()-2);
			
			List<AnalysisOutput> outputs = analyzer.analyze(input);
			AnalysisOutput o = outputs.get(0);
			String result = o.toString()+"->";
			for(int i=0;i<o.getCNounList().size();i++){
				result += o.getCNounList().get(i).getWord()+"/";
			}
			result += "<"+o.getScore()+">";

			String tmp = younons.get(eogan);
			if(tmp==null) {
				younons.put(eogan, result);
			} else {
				younons.put(eogan, tmp+"| "+result);
			}
		}
		
		fname = "data/체언_상세.txt";		
		String cheonOutfile = "data/cheon.txt";
		String youngOutfile = "data/youngon.txt";
		
		List<String> cheons = FileUtils.readLines(new File(fname));	
		List<String> outputs = new ArrayList();
		System.out.println(younons.size());		
		for(String cheon : cheons) {
			String str = younons.remove(cheon);
			if(str!=null) {
				cheon += "=> "+str;
//				younons.remove(cheon);
			}
			outputs.add(cheon);
		}

		Iterator<String> iter = younons.keySet().iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			outputs.add(key+"=> " + younons.get(key));
		}
		
		Collections.sort(outputs);
		Collections.sort(youngOutputs);
		
		FileUtils.writeLines(new File(cheonOutfile), outputs);
		FileUtils.writeLines(new File(youngOutfile), youngOutputs);
		
		outputs.addAll(youngOutputs);
		Collections.sort(outputs);
		FileUtils.writeLines(new File("data/all.txt"), outputs);
	}
	*/
}

