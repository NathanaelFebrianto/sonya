package com.nhn.socialbuzz.textmining;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.kr.KoreanTokenizer;
import org.apache.lucene.analysis.kr.morph.AnalysisOutput;
import org.apache.lucene.analysis.kr.morph.MorphAnalyzer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Version;

import com.nhn.socialbuzz.me2day.model.Comment;
import com.nhn.socialbuzz.me2day.model.Post;
import com.nhn.socialbuzz.me2day.service.CommentManager;
import com.nhn.socialbuzz.me2day.service.CommentManagerImpl;
import com.nhn.socialbuzz.me2day.service.PostManager;
import com.nhn.socialbuzz.me2day.service.PostManagerImpl;
import com.nhn.socialbuzz.textmining.regex.Extractor;

public class TextAnalyzer {
	
	public static final String[] STOP_WORDS = new String[]{
		"a", "an", "and", "are", "as", "at", "be", "but", "by",
		"for", "if", "in", "into", "is", "it",
		"no", "not", "of", "on", "or", "such",
		"that", "the", "their", "then", "there", "these",
		"they", "this", "to", "was", "will", "with"		  
	};
  
	public static final String[] KOR_STOP_WORDS = new String[]{
		"이","그","저","것","수","등","들"
	};	
	
	public static final String[] DOMAIN_STOP_WORDS = new String[]{		
	};
	
	private String outputDir;
	
	private Set<Object> stopSet;
	
	private Extractor extractor;
	
	private MorphAnalyzer morphAnalyzer;
	
	private StopAnalyzer stopAnalyzer;
	
	public TextAnalyzer(String outputDir) {
		this.outputDir = outputDir;
		
		extractor = new Extractor();
		
		stopSet = StopFilter.makeStopSet(Version.LUCENE_CURRENT, STOP_WORDS, true);
		stopSet.addAll(StopFilter.makeStopSet(Version.LUCENE_CURRENT, KOR_STOP_WORDS, true));
		stopSet.addAll(StopFilter.makeStopSet(Version.LUCENE_CURRENT, DOMAIN_STOP_WORDS, true));		
		
		morphAnalyzer = new MorphAnalyzer();		
		stopAnalyzer = new StopAnalyzer(Version.LUCENE_CURRENT, stopSet);
		System.out.println("stop words == " + stopAnalyzer.getStopwordSet());
	}
	
	private String removeUrls(String text) {
		List<String> urls = extractor.extractURLs(text);
		
		if (urls.size() > 0) {
			//System.out.println("text before removing urls == " + text);
			//System.out.println("urls == " + urls.toString());
		}		
		
		for (String url : urls) {
			text = text.replaceAll(url, " ");
			//System.out.println("text after removing urls == " + text);
		}
				
		return text;		
	}
	
	private String convertCharacters(String text, String oldChar, String newChar) {
		List<String> chars = extractor.extractSameCharacters(text, oldChar);
		
		if (chars.size() > 0) {
			//System.out.println("text before converting " + oldChar + " == " + text);
			//System.out.println("chars == " + chars.toString());
		}		
		
		for (String cha : chars) {
			text = text.replaceAll(cha, newChar);
			//System.out.println("text after converting " + cha + " == " + text);
		}
				
		return text;		
	}
	
	private String normalizeText(String type, String text) {		
		if (type.equals("TAG")) {
			text = text.replaceAll("me2photo", ""); 
			text = text.replaceAll("me2mobile", "");
			text = text.replaceAll("me2mms", "");
			text = text.replaceAll("me2sms", "");
			text = text.replaceAll("me2music", "");
			text = text.replaceAll("지식로그", "");
			text = text.replaceAll("네이버뉴스", "");
			
			if (text.indexOf("네이버블로그") >= 0) {
				return "";
			}				
			//text = text.replaceAll("네이버블로그", "");
		}
		
		if (type.equals("POST")) {
			text = text.replaceAll("지식로그", "");			
		}
		
		text = text.replaceAll("\\?", "QQQQQ");
		text = text.replaceAll("\\^\\^", "SSSSS");
		
		text = this.removeUrls(text);
		text = this.convertCharacters(text, "ㅋ", " #SMILE ");
		text = this.convertCharacters(text, "ㅎ", " #SMILE ");
		text = this.convertCharacters(text, "ㅜ", " #CRY ");
		text = this.convertCharacters(text, "ㅠ", " #CRY ");
		text = this.convertCharacters(text, "SSSSS", " #SMILE ");
		text = this.convertCharacters(text, "♡", " #LOVE ");
		text = this.convertCharacters(text, "♥", " #LOVE ");
		text = this.convertCharacters(text, "QQQQQ", " #QUESTION ");
		text = this.convertCharacters(text, "!", " #EXCLAMATION ");		
		
		return text;
	}

	@Deprecated
	public Vector<String> extractTerms(String text) {
		this.normalizeText("", text);
		
		Vector<String> terms = new Vector<String>();
		
		KoreanTokenizer tokenizer = new KoreanTokenizer(new StringReader(text));

		Token token = null;
		
		try {
			while ((token = tokenizer.next()) != null) {
				/*
				if (!token.type().equals("<KOREAN>"))
					continue;
				*/
				try {
					morphAnalyzer.setExactCompound(false);
					
					List<AnalysisOutput> results = morphAnalyzer.analyze(token.toString());

					System.out.println(token.toString());

					for (AnalysisOutput o : results) {
						
						String mWord = o.toString();						
						
						//System.out.println(mWord);
						
						/*
						for (int i = 0; i < o.getCNounList().size(); i++) {
							System.out.println (o.getCNounList().get(i).getWord() + "/");
						}
						*/

						//System.out.println("<" + o.getScore() + ">");
						
						if (mWord.indexOf("(N)") >= 0) {
							terms.add(o.getStem());
							//System.out.println("noun == " + o.getStem());
						}
						if (mWord.indexOf("(V)") >= 0) {
							terms.add(o.getStem());
							//System.out.println("verb == " + o.getStem());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		return terms;		
	}
	
	public Vector<String> extractTerms(String type, String text) {
		System.out.println("original text == " + text);
		
		text = this.normalizeText(type, text);
		
		System.out.println("normalized text == " + text);
		
		Vector<String> terms = new Vector<String>();
		
		TokenStream stream = stopAnalyzer.tokenStream("k", new StringReader(text));

		TermAttribute termAttr = stream.getAttribute(TermAttribute.class); 		
        OffsetAttribute offSetAttr = stream.getAttribute(OffsetAttribute.class);
		
		try {

			while (stream.incrementToken()) {
				try {
					//System.out.println(termAttr.term());
					
					morphAnalyzer.setExactCompound(false);					
					List<AnalysisOutput> results = morphAnalyzer.analyze(termAttr.term());

					for (AnalysisOutput o : results) {
						
						String mWord = o.toString();						

						if (mWord.indexOf("(N)") >= 0) {
							terms.add(o.getStem());
							System.out.println("noun == " + o.getStem());
						}
						if (mWord.indexOf("(V)") >= 0) {
							terms.add(o.getStem());
							System.out.println("verb == " + o.getStem());
						}
					}										

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		return terms;		
	}
	
	
	private String convertTermsToString(Vector<String> terms) {
		StringBuffer sbTerms = new StringBuffer();
		for (String term: terms) {
			sbTerms.append(term).append(" ");
		}
		
		return sbTerms.toString();
	}
	
    private String convertDate(String mask, Date date) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (date == null) {
        } else {
            df = new SimpleDateFormat(mask);
            returnValue = df.format(date);
        }

        return returnValue;
    }
	
	private void writeFile(String programId, List<Post> posts, List<Comment> comments) 
			throws UnsupportedEncodingException, IOException {
		File dir = new File(outputDir);
		if (!dir.exists())
			dir.mkdir();
		
		File out = new File(outputDir + File.separator + "src_" + programId + ".txt");
		
		//PrintWriter writer = new PrintWriter(new FileWriter(out));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out.getPath()), "UTF-8"));
		
		// UTF-8의 BOM인 "EF BB BF"를 UTF-16BE 로 변환하면 "65279"라는 값이 됨
		//writer.write(65279);
		
		// write header
		//writer.println("program_id	author_id	type	post_id	comment_id	publish_date	terms");
		writer.write("program_id	author_id	type	post_id	comment_id	publish_date	terms");
		writer.newLine();
		
		for (Post post : posts) {
			String postId = post.getPostId();
			String authorId = post.getAuthorId();
			String publishDate = this.convertDate("yyyy-MM-dd", post.getPublishDate());
			String textBody = post.getTextBody();
			String textTag = post.getTagText();
			String type = "POST";
			
			Vector<String> terms = this.extractTerms("POST", textBody);
			terms.addAll(this.extractTerms("TAG", textTag));			
			String strTerms = this.convertTermsToString(terms);
			
			writer.write(
					programId + "\t" +
					authorId + "\t" + 
					type + "\t" + 
					postId + "\t" + 
					"" + "\t" +
					publishDate + "\t" + 
					strTerms);
			writer.newLine();
			
		}
		
		for (Comment comment : comments) {
			String postId = comment.getPostId();
			String commentId = comment.getCommentId();
			String authorId = comment.getAuthorId();
			String publishDate = this.convertDate("yyyy-mm-dd", comment.getPublishDate());
			String textBody = comment.getTextBody();
			String type = "COMMENT";
			
			Vector<String> terms = this.extractTerms("COMMENT", textBody);
			String strTerms = this.convertTermsToString(terms);
			
			writer.write(
					programId + "\t" +
					authorId + "\t" + 
					type + "\t" + 
					postId + "\t" + 
					commentId + "\t" +
					publishDate + "\t" + 
					strTerms);
			writer.newLine();
		}
		
		writer.close();		
	}
	
	public void analyze(String programId) {
		try {
			PostManager postManager = new PostManagerImpl();
			CommentManager commentManager = new CommentManagerImpl();
						
			Post paramPost = new Post();
			paramPost.setProgramId(programId);
			List<Post> posts = postManager.getPosts(paramPost);
			
			Comment paramComment = new Comment();
			paramComment.setProgramId(programId);
			List<Comment> comments = commentManager.getComments(paramComment);
			
			// write file
			this.writeFile(programId, posts, comments);			
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private void checkFileEncoding(String filepath) {
		try {
			FileInputStream fis = new FileInputStream(filepath);
			    	
			byte[] BOM = new byte[4];
			fis.read(BOM, 0, 4);
			    	
			if( (BOM[0] & 0xFF) == 0xEF && (BOM[1] & 0xFF) == 0xBB && (BOM[2] & 0xFF) == 0xBF )
				System.out.println("UTF-8");
			else if( (BOM[0] & 0xFF) == 0xFE && (BOM[1] & 0xFF) == 0xFF )
				System.out.println("UTF-16BE");
			else if( (BOM[0] & 0xFF) == 0xFF && (BOM[1] & 0xFF) == 0xFE )
				System.out.println("UTF-16LE");
			else if( (BOM[0] & 0xFF) == 0x00 && (BOM[1] & 0xFF) == 0x00 && 
			         (BOM[0] & 0xFF) == 0xFE && (BOM[1] & 0xFF) == 0xFF )
				System.out.println("UTF-32BE");
			else if( (BOM[0] & 0xFF) == 0xFF && (BOM[1] & 0xFF) == 0xFE && 
			         (BOM[0] & 0xFF) == 0x00 && (BOM[1] & 0xFF) == 0x00 )
				System.out.println("UTF-32LE");
			else
				System.out.println("EUC-KR");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		//String text = "[연예] '무도' 음원 주간 다운로드 총 800만 건 : MBC TV '무한도전'의 '서해안 고속도로 가요제'가 발표한 음원들이 주간 종합 다운로드 차트 집계에서 총 800만 건을 넘겼고, 그중 지-드래곤.박명수의 '바람났어'가 1위에 올랐습니다. 노래는 안좋아 별로네 못하네";
		
		String outputDir = "D:/dev/workspace/social-buzz/output/";
		TextAnalyzer analyzer = new TextAnalyzer(outputDir);
		
		//String programId = "kbs1_greatking";
		//String programId = "kbs2_ojakkyo";
		//String programId = "mbc_thousand";
		//String programId = "sbs_besideme";
		//String programId = "kbs2_princess";
		//String programId = "mbc_fallinlove";
		//String programId = "sbs_boss";
		//String programId = "kbs2_spy";
		//String programId = "mbc_gyebaek";
		//String programId = "sbs_baekdongsoo";
		//String programId = "mbc_wedding";
		//String programId = "mbc_challenge";
		//String programId = "sbs_starking";
		//String programId = "kbs2_happysunday_1bak2il";
		//String programId = "kbs2_happysunday_men";
		//String programId = "mbc_sundaynight_nagasoo";
		//String programId = "mbc_sundaynight_house";
		//String programId = "sbs_newsunday";
		
		String[] programs = new String[] {
//			"kbs1_greatking",
//			"kbs2_ojakkyo",
//			"mbc_thousand",
//			"sbs_besideme",
//			"kbs2_princess",
			"mbc_fallinlove",
//			"sbs_boss",
//			"kbs2_spy",
//			"mbc_gyebaek",
//			"sbs_baekdongsoo",
//			"mbc_wedding",
//			"mbc_challenge",
//			"sbs_starking",
//			"kbs2_happysunday_1bak2il",
//			"kbs2_happysunday_men",
//			"mbc_sundaynight_nagasoo",
//			"mbc_sundaynight_house",
//			"sbs_newsunday"
		};
			
		for (int i = 0; i <programs.length; i++) {
			analyzer.analyze(programs[i]);
		}	
			
		
		///////////////////////////////////////////////////
		// just for testing
		///////////////////////////////////////////////////
		
		//analyzer.checkFileEncoding(outputDir + "kbs2_princess.txt");
		
		/*
		try {
			String charSet[] = { "utf-8", "euc-kr", "8859_1" };
			String fileName = "테스트";
			for (int i = 0; i < charSet.length; i++) {
				for (int j = 0; j < charSet.length; j++) {
					System.out.println(charSet[i]
							+ " to "
							+ charSet[j]
							+ " = "
							+ new String(fileName.getBytes(charSet[i]),
									charSet[j]));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		
		/*
		PostManager postManager = new PostManagerImpl();
		Post param = new Post();
		param.setProgramId("kbs1_greatking");
		List<Post> posts = postManager.getPosts(param);		
		
		for (Post post : posts) {
			String text = post.getTextBody();
			Vector<String> terms = analyzer.extractTerms(text);
			System.out.println(terms.toString());
		}
		*/
	}
}
