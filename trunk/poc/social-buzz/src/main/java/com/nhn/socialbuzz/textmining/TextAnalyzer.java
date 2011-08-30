package com.nhn.socialbuzz.textmining;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.Character.UnicodeBlock;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
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
import com.nhn.socialbuzz.textmining.liwc.PersonalityRecognizer;
import com.nhn.socialbuzz.textmining.liwc.Utils;
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
	
	private String outputDir;
	
	private Set<Object> stopSet;
	
	private Extractor extractor;
	
	private MorphAnalyzer morphAnalyzer;
	
	private StopAnalyzer stopAnalyzer;
	
	private PersonalityRecognizer personalityRecognizer;
	

	/**
	 * Creates a text analyzer.
	 * 
	 * @param outputDir
	 */
	public TextAnalyzer(String outputDir) {
		this.outputDir = outputDir;
		
		extractor = new Extractor();
		
		stopSet = StopFilter.makeStopSet(Version.LUCENE_CURRENT, STOP_WORDS, true);
		stopSet.addAll(StopFilter.makeStopSet(Version.LUCENE_CURRENT, KOR_STOP_WORDS, true));
		
		morphAnalyzer = new MorphAnalyzer();		
		stopAnalyzer = new StopAnalyzer(Version.LUCENE_CURRENT, stopSet);
		System.out.println("stop words == " + stopAnalyzer.getStopwordSet());
		
		File liwcCatFile = new File("D:/workspace/social-buzz/output/_LIWC.txt");
		personalityRecognizer = new PersonalityRecognizer(liwcCatFile);
	}
	

	/**
	 * Analyzes the document of the specified TV program.
	 * 
	 * @param programId
	 */
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
	
    /**
     * Writes the analyzed text into a file.
     * 
     * @param programId
     * @param posts
     * @param comments
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
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
			
			//////////////////////////////////////
			// analyze LIWC features
			//////////////////////////////////////
			this.analyzeLIWCFeatures(strTerms);
			//////////////////////////////////////
			
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
			String publishDate = this.convertDate("yyyy-MM-dd", comment.getPublishDate());
			String textBody = comment.getTextBody();
			String type = "COMMENT";
			
			Vector<String> terms = this.extractTerms("COMMENT", textBody);
			String strTerms = this.convertTermsToString(terms);
			
			//////////////////////////////////////
			// analyze LIWC features
			//////////////////////////////////////
			this.analyzeLIWCFeatures(strTerms);
			//////////////////////////////////////
			
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

	/**
	 * Extract terms by lucene.
	 * 
	 * @param type
	 * @param text
	 * @return
	 */
	public Vector<String> extractTerms(String type, String text) {
		System.out.println("original text == " + text);
		
		///////////////////////////////////////////
		// normalize the text
		///////////////////////////////////////////
		text = this.normalizeText(type, text);
		///////////////////////////////////////////
		
		System.out.println("normalized text == " + text);
		
		Vector<String> terms = new Vector<String>();
		
		TokenStream stream = stopAnalyzer.tokenStream("k", new StringReader(text));

		TermAttribute termAttr = stream.getAttribute(TermAttribute.class); 		
        OffsetAttribute offSetAttr = stream.getAttribute(OffsetAttribute.class);
		
		try {

			while (stream.incrementToken()) {
				try {
					morphAnalyzer.setExactCompound(false);					
					List<AnalysisOutput> results = morphAnalyzer.analyze(termAttr.term());

					for (AnalysisOutput o : results) {

						String metaWord = o.toString();						
						String term = o.getStem();
						
						String noun = "";
						String verb = "";
						if (metaWord.indexOf("(N)") >= 0) {
							noun = term;
							if (!noun.trim().equals(""))
								terms.add(noun);
						}
						if (metaWord.indexOf("(V)") >= 0) {
							verb = term;
							if (!verb.trim().equals(""))
							terms.add(verb + "다");
						}
						
						System.out.println("token = " + termAttr.term() + " => " + o.getStem() + ", noun = " + noun + ", verb = " + verb);
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
	
		
	/**
	 * Normalizes the text before extracting terms by lucene.
	 * 
	 * @param type
	 * @param text
	 * @return
	 */
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
		
		text = text.replaceAll("\\?", "TAGQUESTION");
		text = text.replaceAll("\\^\\^", "TAGSMILE");
		
		text = this.removeUrls(text);
		text = this.replaceCharacters(text, "ㅋ", " TAGSMILE ");
		text = this.replaceCharacters(text, "ㅎ", " TAGSMILE ");
		text = this.replaceCharacters(text, "ㅜ", " TAGCRY ");
		text = this.replaceCharacters(text, "ㅠ", " TAGCRY ");
		text = this.replaceCharacters(text, "TAGSMILE", " TAGSMILE ");
		text = this.replaceCharacters(text, "♡", " TAGLOVE ");
		text = this.replaceCharacters(text, "♥", " TAGLOVE ");
		text = this.replaceCharacters(text, "TAGQUESTION", " TAGQUESTION ");
		text = this.replaceCharacters(text, "!", " TAGEXCLAMATION ");		
		
		return text;
	}
	
	/**
	 * Removes the urls from the source text.
	 * 
	 * @param text
	 * @return
	 */
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
	
	/**
	 * Replaces the old characters with the new characters.
	 * 
	 * @param text
	 * @param oldChar
	 * @param newChar
	 * @return
	 */
	private String replaceCharacters(String text, String oldChar, String newChar) {
		List<String> chars = extractor.extractSameCharacters(text, oldChar);
		
		for (String cha : chars) {
			text = text.replaceAll(cha, newChar);
		}
				
		return text;		
	}
	
	/**
	 * Convert terms into a delimited string.
	 * 
	 * @param terms
	 * @return
	 */
	private String convertTermsToString(Vector<String> terms) {
		StringBuffer sbTerms = new StringBuffer();
		for (String term: terms) {
			term = this.removeUnsupportedCharacters(term);
			if(!term.trim().equals(""))
				sbTerms.append(term).append(" ");
		}
		
		return sbTerms.toString().trim();
	}
		
	/**
	 * Removes unsupported characters such as Chinese, Japanese, etc.
	 * 
	 * @param str
	 * @return
	 */
	private String removeUnsupportedCharacters(String str) {
		//System.out.println(str);

		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			Character.UnicodeBlock unicodeBlock = Character.UnicodeBlock.of(ch);
			
			if ( !(Character.isDigit(ch)
					|| UnicodeBlock.HANGUL_SYLLABLES.equals(unicodeBlock)
					|| UnicodeBlock.HANGUL_COMPATIBILITY_JAMO.equals(unicodeBlock)
					|| UnicodeBlock.HANGUL_JAMO.equals(unicodeBlock)
					|| UnicodeBlock.BASIC_LATIN.equals(unicodeBlock)) 
				) {
				str = str.replace(ch, ' ');
			}
			
			str = str.replaceAll("ᆢ", "");
		}
		
		return str;
	}
	
	/**
	 * Convert data object into string with the specified format.
	 * 
	 * @param mask
	 * @param date
	 * @return
	 */
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
    
    /**
     * Analyzes the LIWC features for recognizing sentiment.
     * 
     * @param text
     * @return
     */
	private Map<String, Double> analyzeLIWCFeatures(String text) {
		try {
			System.out.println("@ source text to analyze LIWC == " + text);
			
			// get feature counts from the input text
			//Map<String,Double> counts = recognizer.getFeatureCounts(text, true);
			Map<String,Double> counts = personalityRecognizer.getJustFeatureCounts(text, true);
			
			System.out.println("Total features computed: " + counts.size());			
			System.out.println("Feature counts:");
			Utils.printMap(counts, System.out);
			System.out.println("\n");
			
			return counts;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public static void main(String[] args) {
		String outputDir = "D:/workspace/social-buzz/output/";
		TextAnalyzer analyzer = new TextAnalyzer(outputDir);
		
		String[] programs = new String[] {
//			"kbs1_greatking",
//			"kbs2_ojakkyo",
//			"mbc_thousand",
//			"sbs_besideme",
			"kbs2_princess",
//			"mbc_fallinlove",
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
				
//		String str = "공주의 남자今天OST：백지영 (Baek Ji Young) 1- 오늘도 사랑해 也愛你";
//		String str = "แอร๊กกกกกก แม่ลูกฉลองวันเกิด คิมฮีและคิมคิ กูจะร้องไห้ คิดถึงเมิงมากมายบอมมี่";
//		String str = "ทำไมไม่เอาหมวยไปด้วยอ่า";
//		String str = "긍정돼지 저게요 밑에 은근히 많이 깔려서 배부르답니다ㅋㅋ누군가를 좋아하게 되니 저절로 소식하고 싶다는ᆢ뭐래?;;탕수육 맛나게 드셔요~규일님^^";
//		str = analyzer.removeUnsupportedCharacters(str);
//		System.out.println(str);
		
	}

}
