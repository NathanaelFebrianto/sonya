package com.nhn.socialbuzz.me2day.analyzer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nhn.socialbuzz.common.CommonUtil;
import com.nhn.socialbuzz.common.Config;
import com.nhn.socialbuzz.me2day.model.Comment;
import com.nhn.socialbuzz.me2day.model.Post;
import com.nhn.socialbuzz.me2day.model.TvProgram;
import com.nhn.socialbuzz.me2day.service.CommentManager;
import com.nhn.socialbuzz.me2day.service.CommentManagerImpl;
import com.nhn.socialbuzz.me2day.service.PostManager;
import com.nhn.socialbuzz.me2day.service.PostManagerImpl;
import com.nhn.socialbuzz.me2day.service.TvProgramManager;
import com.nhn.socialbuzz.me2day.service.TvProgramManagerImpl;
import com.nhn.socialbuzz.textmining.analysis.TextAnalyzer;
import com.nhn.socialbuzz.textmining.liwc.LIWCDictionary.WordCount;
import com.nhn.socialbuzz.textmining.liwc.PersonalityRecognizer;
import com.nhn.socialbuzz.textmining.liwc.Utils;

public class Me2dayPostAnalyzer {
	
	private String outputDir;
	
	private TextAnalyzer textAnalyzer;
	private PersonalityRecognizer personalityRecognizer;
	
	private PostManager postManager;
	private CommentManager commentManager;
	

	/**
	 * Creates a Me2DAY post analyzer.
	 * 
	 * @param outputDir
	 */
	public Me2dayPostAnalyzer(String outputDir) {
		this.outputDir = outputDir;
		
		textAnalyzer = new TextAnalyzer();
		File liwcCatFile = new File(Config.getProperty("liwcCatFile_ko"));
		personalityRecognizer = new PersonalityRecognizer(liwcCatFile);
		
		postManager = new PostManagerImpl();
		commentManager = new CommentManagerImpl();
	}
	

	/**
	 * Analyzes the document of the specified TV program.
	 * 
	 * @param programId
	 * @param publishStartDate
	 * @param publishEndDate
	 */
	public void analyze(String programId, String publishStartDate, String publishEndDate) {
		try {
			Date startDate = CommonUtil.convertStringToDate("yyyyMMdd", publishStartDate);
			Date endDate = CommonUtil.convertStringToDate("yyyyMMdd", publishEndDate);			
			
			PostManager postManager = new PostManagerImpl();
			CommentManager commentManager = new CommentManagerImpl();
						
			Post paramPost = new Post();
			paramPost.setProgramId(programId);
			paramPost.setPublishStartDate(startDate);
			paramPost.setPublishEndDate(endDate);
			List<Post> posts = postManager.getPosts(paramPost);
			
			Comment paramComment = new Comment();
			paramComment.setProgramId(programId);
			paramComment.setPublishStartDate(startDate);
			paramComment.setPublishEndDate(endDate);
			List<Comment> comments = commentManager.getComments(paramComment);
			
			// write output into file and database
			String path = publishStartDate + "-" + publishEndDate;
			this.writeOutput(path, programId, posts, comments);			
			
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
	private void writeOutput(String path, String programId, List<Post> posts, List<Comment> comments) 
			throws UnsupportedEncodingException, IOException {
		File dir = new File(outputDir + File.separator + path);
		if (!dir.exists())
			dir.mkdir();
		
		File outTerms = new File(dir.getPath() + File.separator + "src_" + programId + ".txt");
		File outSentiment = new File(dir.getPath() + File.separator + "liwc_" + programId + ".txt");
		
		
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
			String publishDate = CommonUtil.convertDateToString("yyyy-MM-dd", post.getPublishDate());
			//String textBody = post.getTextBody();
			String body = post.getBody();
			String textTag = post.getTagText();
			String type = "POST";			
			
			System.out.println("post == " + body);
			System.out.println("tag == " + textTag);
			
			body = this.removeReservedWords("POST", body);
			body = this.removeMe2dayUrls(body);
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
			
			this.updateLIWCFeaturesToDatabase(post, liwcMaps);
		}
		
		// comment
		for (Comment comment : comments) {
			String postId = comment.getPostId();
			String commentId = comment.getCommentId();
			String authorId = comment.getAuthorId();
			String authorNickname = comment.getAuthorNickname();
			String publishDate = CommonUtil.convertDateToString("yyyy-MM-dd", comment.getPublishDate());
			//String textBody = comment.getTextBody();
			String body = comment.getBody();
			String type = "COMMENT";
			
			System.out.println("comment == " + body);
			
			body = this.removeReservedWords("COMMENT", body);
			body = this.removeMe2dayUrls(body);
			
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
			
			this.updateLIWCFeaturesToDatabase(comment, liwcMaps);
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
			text = text.replaceAll("me2movie", "");
			text = text.replaceAll("me2book", "");
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
	 * Removes the me2day urls.
	 * 
	 * @param usernames
	 * @param text
	 * @return
	 */
	private String removeMe2dayUrls(String text) {
		Pattern pattern = Pattern.compile("<\\s*a href='http://me2day.net[^>]*>(.*?)<\\s*/\\s*a>", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(text);
		text = matcher.replaceAll("");
		text = text.replaceAll("\\[", "").replaceAll("\\]", "");
		
		return text;
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
	
	private void updateLIWCFeaturesToDatabase(Post post, Map<String, WordCount> liwcMaps) {
		Post postNew = new Post();
		postNew.setPostId(post.getPostId());
		postNew.setProgramId(post.getProgramId());		
		
		for (Object key : liwcMaps.keySet()) {
			WordCount wordCount = (WordCount) liwcMaps.get(key);
			double count = wordCount.getCount();
			
			if (key.equals("NEGATION"))
				postNew.setLiwcNegation(count);
			else if (key.equals("QUANTIFIER"))
				postNew.setLiwcQuantifier(count);
			else if (key.equals("SWEAR"))
				postNew.setLiwcSwear(count);
			else if (key.equals("QUESTIONMARK"))
				postNew.setLiwcQmark(count);
			else if (key.equals("EXCLAMMARK"))
				postNew.setLiwcExclam(count);
			else if (key.equals("SMILEMARK"))
				postNew.setLiwcSmile(count);
			else if (key.equals("CRYMARK"))
				postNew.setLiwcCry(count);
			else if (key.equals("LOVEMARK"))
				postNew.setLiwcLove(count);
			else if (key.equals("POSITIVE"))
				postNew.setLiwcPositive(count);
			else if (key.equals("NEGATIVE"))
				postNew.setLiwcNegative(count);
			else if (key.equals("ANGER"))
				postNew.setLiwcAnger(count);
			else if (key.equals("ANXIETY"))
				postNew.setLiwcAnxiety(count);
			else if (key.equals("SADNESS"))
				postNew.setLiwcSadness(count);
		}
		
		postManager.setPost(postNew);
	}

	private void updateLIWCFeaturesToDatabase(Comment comment, Map<String, WordCount> liwcMaps) {
		Comment commentNew = new Comment();
		commentNew.setPostId(comment.getPostId());
		commentNew.setCommentId(comment.getCommentId());
		commentNew.setProgramId(comment.getProgramId());		
		
		for (Object key : liwcMaps.keySet()) {
			WordCount wordCount = (WordCount) liwcMaps.get(key);
			double count = wordCount.getCount();
			
			if (key.equals("NEGATION"))
				commentNew.setLiwcNegation(count);
			else if (key.equals("QUANTIFIER"))
				commentNew.setLiwcQuantifier(count);
			else if (key.equals("SWEAR"))
				commentNew.setLiwcSwear(count);
			else if (key.equals("QUESTIONMARK"))
				commentNew.setLiwcQmark(count);
			else if (key.equals("EXCLAMMARK"))
				commentNew.setLiwcExclam(count);
			else if (key.equals("SMILEMARK"))
				commentNew.setLiwcSmile(count);
			else if (key.equals("CRYMARK"))
				commentNew.setLiwcCry(count);
			else if (key.equals("LOVEMARK"))
				commentNew.setLiwcLove(count);
			else if (key.equals("POSITIVE"))
				commentNew.setLiwcPositive(count);
			else if (key.equals("NEGATIVE"))
				commentNew.setLiwcNegative(count);
			else if (key.equals("ANGER"))
				commentNew.setLiwcAnger(count);
			else if (key.equals("ANXIETY"))
				commentNew.setLiwcAnxiety(count);
			else if (key.equals("SADNESS"))
				commentNew.setLiwcSadness(count);
		}
		
		commentManager.setComment(commentNew);
	}
	
	public static void main(String[] args) {
		String outputDir = Config.getProperty("dataDir");
		Me2dayPostAnalyzer analyzer = new Me2dayPostAnalyzer(outputDir);
		
		/*
		String[] programIds = new String[] {
			"kbs1_greatking",
			"kbs_homewomen",
			"kbs2_princess",
			"kbs2_spy",
			"kbs2_ojakkyo",
			"mbc_gyebaek",
			"mbc_fallinlove",
			"mbc_urpretty",
			"mbc_thousand",
			"sbs_besideme",
			"sbs_dangsin",
			"sbs_baekdongsoo",
			"sbs_boss",
			"sbs_scent",
			"kbs2_gagcon",
			"kbs2_happysunday_1bak2il",
			"kbs2_happysunday_men",
			"sbs_happytogether",
			"mbc_challenge",
			"mbc_three",
			"mbc_wedding",
			"mbc_sundaynight_house",
			"mbc_sundaynight_nagasoo",	
			"sbs_strongheart",
			"sbs_starking",
			"sbs_newsunday",
		};
		*/
		
		try {
			String publishStartDate = "20110905";
			String publishEndDate = "20110911";
			
			TvProgramManager programManager = new TvProgramManagerImpl();
			TvProgram param = new TvProgram();
			param.setStatus("open");
			param.setNation("KO");
			List<TvProgram> programs = programManager.getPrograms(param);
			
			for (int i = 0; i <programs.size(); i++) {
				TvProgram program = programs.get(i);
				analyzer.analyze(program.getProgramId(), publishStartDate, publishEndDate);
			}			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
