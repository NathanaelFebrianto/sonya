package com.nhn.socialanalytics.me2day.collect;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.nhn.socialanalytics.common.Config;
import com.nhn.socialanalytics.common.JobLogger;
import com.nhn.socialanalytics.common.util.DateUtil;
import com.nhn.socialanalytics.common.util.StringUtil;
import com.nhn.socialanalytics.me2day.model.Comment;
import com.nhn.socialanalytics.me2day.model.Post;
import com.nhn.socialanalytics.me2day.parse.Me2dayParser;
import com.nhn.socialanalytics.nlp.kr.morpheme.MorphemeAnalyzer;

public class Me2dayDataWriter {

	private static JobLogger logger = JobLogger.getLogger(Me2dayDataCollector.class, "me2day-collect.log");
	private File outputDir;
	
	public Me2dayDataWriter() {
		outputDir = new File(Config.getProperty("ME2DAY_SOURCE_DATA_DIR"));
		if (!outputDir.exists()) {
			outputDir.mkdir();
			logger.info(outputDir + " is created.");
		}
	}
	
    /**
     * Writes the analyzed text into the file.
     * 
     * @param objectId
     * @param posts
     * @param comments
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
	public void writeOutput(String objectId, List<Post> posts, List<Comment> comments) 
			throws UnsupportedEncodingException, IOException {	
		
		MorphemeAnalyzer morph = MorphemeAnalyzer.getInstance();
				
		File file = new File(outputDir.getPath() + File.separator + "me2day_" + objectId + ".txt");		
		
		BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getPath()), "UTF-8"));
				
		// write header
		br.write("object_id	author_id	author_nickname	type	post_id	comment_id	publish_date	text	text1	text2");
		br.newLine();
		
		// post
		for (Post post : posts) {
			String postId = post.getPostId();
			String authorId = post.getAuthorId();
			String authorNickname = post.getAuthorNickname();
			String publishDate = DateUtil.convertDateToString("yyyyMMddHHmmss", post.getPublishDate());
			//String textBody = post.getTextBody();
			String body = post.getBody();
			String textTag = post.getTagText();
			String type = "POST";			
			
			System.out.println("post == " + body);
			System.out.println("tag == " + textTag);
			
			body = Me2dayParser.extractContent(body, "POST");			
			textTag = Me2dayParser.extractContent("TAG", textTag);
			
			String body0 = Me2dayParser.stripHTML(body);
			body0 = Me2dayParser.convertEmoticonToTag(body0);
			String body1 = morph.extractTerms(body0);
			String body2 = morph.extractCoreTerms(body0);
			String textTagl = morph.extractTerms(textTag);			
			
			br.write(
					objectId + "\t" +
					authorId + "\t" + 
					authorNickname + "\t" + 
					type + "\t" + 
					postId + "\t" + 
					"" + "\t" +
					publishDate + "\t" + 
					body + "\t" +
					body1 + "\t" +
					body2
					);
			br.newLine();
		}
		
		br.close();
	}
}
