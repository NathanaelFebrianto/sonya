package com.nhn.socialbuzz.me2day.analyzer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.nhn.socialbuzz.me2day.model.Comment;
import com.nhn.socialbuzz.me2day.model.Post;
import com.nhn.socialbuzz.me2day.service.CommentManager;
import com.nhn.socialbuzz.me2day.service.CommentManagerImpl;
import com.nhn.socialbuzz.me2day.service.PostManager;
import com.nhn.socialbuzz.me2day.service.PostManagerImpl;
import com.nhn.socialbuzz.textmining.analysis.TextAnalyzer;
import com.nhn.socialbuzz.textmining.liwc.LIWCDictionary.WordCount;
import com.nhn.socialbuzz.textmining.liwc.PersonalityRecognizer;
import com.nhn.socialbuzz.textmining.liwc.Utils;

public class Me2dayPostAnalyzer {
	
	private String outputDir;
	
	private TextAnalyzer textAnalyzer;
	private PersonalityRecognizer personalityRecognizer;
	

	/**
	 * Creates a Me2DAY post analyzer.
	 * 
	 * @param outputDir
	 */
	public Me2dayPostAnalyzer(String outputDir) {
		this.outputDir = outputDir;
		
		textAnalyzer = new TextAnalyzer();
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
			
			// write output into file and database
			this.writeOutput(programId, posts, comments);			
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
    /**
     * Writes the analyzed text into a file and database.
     * 
     * @param programId
     * @param posts
     * @param comments
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
	private void writeOutput(String programId, List<Post> posts, List<Comment> comments) 
			throws UnsupportedEncodingException, IOException {
		File dir = new File(outputDir);
		if (!dir.exists())
			dir.mkdir();
		
		File outTerms = new File(outputDir + File.separator + "terms_" + programId + ".txt");
		File outSentiment = new File(outputDir + File.separator + "sentiment_" + programId + ".txt");
		
		
		BufferedWriter writerTerms = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outTerms.getPath()), "UTF-8"));
		BufferedWriter writerSentiment = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outSentiment.getPath()), "UTF-8"));
				
		// UTF-8의 BOM인 "EF BB BF"를 UTF-16BE 로 변환하면 "65279"라는 값이 됨
		//writer.write(65279);
		
		// write header
		writerTerms.write("program_id	author_id	type	post_id	comment_id	publish_date	terms");
		writerTerms.newLine();
		
		writerSentiment.write("program_id	author_id	type	post_id	comment_id	publish_date	feature	count	word");
		writerSentiment.newLine();
		
		// post
		for (Post post : posts) {
			String postId = post.getPostId();
			String authorId = post.getAuthorId();
			String authorNickname = post.getAuthorNickname();
			String publishDate = this.convertDate("yyyy-MM-dd", post.getPublishDate());
			//String textBody = post.getTextBody();
			String body = post.getBody();
			String textTag = post.getTagText();
			String type = "POST";			
			
			body = this.removeReservedWords("POST", body);
			textTag = this.removeReservedWords("TAG", textTag);

			Vector<String> terms = textAnalyzer.extractTerms(body);
			terms.addAll(textAnalyzer.extractTerms(textTag));			
			String strTerms = textAnalyzer.convertTermsToStringWithoutUnsupportedChars(terms);	
			
			writerTerms.write(
					programId + "\t" +
					authorId + "\t" + 
					type + "\t" + 
					postId + "\t" + 
					"" + "\t" +
					publishDate + "\t" + 
					strTerms);
			writerTerms.newLine();
			
			//////////////////////////////////////
			// analyze LIWC features
			//////////////////////////////////////
			Map<String, WordCount> liwcMaps = this.analyzeLIWCFeatures(strTerms);
			//////////////////////////////////////
			
			for (Object key : liwcMaps.keySet()) {
				WordCount wordCount = (WordCount) liwcMaps.get(key);
				
				if (wordCount.getCount() > 0 && wordCount.getWords().size() > 0) {
					writerSentiment.write(
							programId + "\t" +
							authorId + "\t" + 
							type + "\t" + 
							postId + "\t" + 
							"" + "\t" +
							publishDate + "\t" + 
							key + "\t" + 
							wordCount.getCount() + "\t" + 
							wordCount.getWordsText());
					writerSentiment.newLine();					
				}
			}			
		}
		
		// comment
		for (Comment comment : comments) {
			String postId = comment.getPostId();
			String commentId = comment.getCommentId();
			String authorId = comment.getAuthorId();
			String authorNickname = comment.getAuthorNickname();
			String publishDate = this.convertDate("yyyy-MM-dd", comment.getPublishDate());
			//String textBody = comment.getTextBody();
			String body = comment.getBody();
			String type = "COMMENT";
			
			body = this.removeReservedWords("COMMENT", body);
			Vector<String> terms = textAnalyzer.extractTerms(body);
			String strTerms = textAnalyzer.convertTermsToStringWithoutUnsupportedChars(terms);	
			
			writerTerms.write(
					programId + "\t" +
					authorId + "\t" + 
					type + "\t" + 
					postId + "\t" + 
					commentId + "\t" +
					publishDate + "\t" + 
					strTerms);
			writerTerms.newLine();
			
			//////////////////////////////////////
			// analyze LIWC features
			//////////////////////////////////////
			Map<String, WordCount> liwcMaps = this.analyzeLIWCFeatures(strTerms);
			//////////////////////////////////////
			
			for (Object key : liwcMaps.keySet()) {
				WordCount wordCount = (WordCount) liwcMaps.get(key);
				
				if (wordCount.getCount() > 0 && wordCount.getWords().size() > 0) {
					writerSentiment.write(
							programId + "\t" +
							authorId + "\t" + 
							type + "\t" + 
							postId + "\t" + 
							"" + "\t" +
							publishDate + "\t" + 
							key + "\t" + 
							wordCount.getCount() + "\t" + 
							wordCount.getWordsText());
					writerSentiment.newLine();					
				}
			}
		}
		
		writerTerms.close();
		writerSentiment.close();
	}

	/**
	 * Removes the reserved words.
	 * 
	 * @param type
	 * @param text
	 * @return
	 */
	private String removeReservedWords(String type, String text) {		
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
		}
		
		if (type.equals("POST")) {
			text = text.replaceAll("지식로그", "");			
		}
		
		return text;
	}
	
	/**
	 * Removes the author nicknames.
	 * 
	 * @param usernames
	 * @param text
	 * @return
	 */
	private String removeUserNicknames(String text) {

		return text;
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
	private Map<String, WordCount> analyzeLIWCFeatures(String text) {
		try {
			System.out.println("@ source text to analyze LIWC == " + text);
			
			// get feature counts from the input text
			Map<String, WordCount> counts = personalityRecognizer.getWordFeatureCounts(text, true);
			
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
		Me2dayPostAnalyzer analyzer = new Me2dayPostAnalyzer(outputDir);
		
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
	}

}
