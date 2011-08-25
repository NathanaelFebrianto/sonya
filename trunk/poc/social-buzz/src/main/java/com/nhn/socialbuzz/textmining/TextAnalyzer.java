package com.nhn.socialbuzz.textmining;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.kr.KoreanTokenizer;
import org.apache.lucene.analysis.kr.morph.AnalysisOutput;
import org.apache.lucene.analysis.kr.morph.MorphAnalyzer;

import com.nhn.socialbuzz.me2day.model.Comment;
import com.nhn.socialbuzz.me2day.model.Post;
import com.nhn.socialbuzz.me2day.service.CommentManager;
import com.nhn.socialbuzz.me2day.service.CommentManagerImpl;
import com.nhn.socialbuzz.me2day.service.PostManager;
import com.nhn.socialbuzz.me2day.service.PostManagerImpl;

public class TextAnalyzer {
	
	public TextAnalyzer() {
		
	}

	public Vector<String> extractTerms(String text) {
		Vector<String> terms = new Vector<String>();
		
		MorphAnalyzer analyzer = new MorphAnalyzer();
		KoreanTokenizer tokenizer = new KoreanTokenizer(new StringReader(text));
		Token token = null;

		try {
			while ((token = tokenizer.next()) != null) {
				/*
				if (!token.type().equals("<KOREAN>"))
					continue;
				*/
				try {
					analyzer.setExactCompound(false);
					
					List<AnalysisOutput> results = analyzer.analyze(token.toString());

					//System.out.println(token.toString());

					for (AnalysisOutput o : results) {
						
						String mWord = o.toString();						
						
						System.out.println(mWord);
						
						/*
						for (int i = 0; i < o.getCNounList().size(); i++) {
							System.out.println (o.getCNounList().get(i).getWord() + "/");
						}
						*/

						System.out.println("<" + o.getScore() + ">");
						
						if (mWord.indexOf("(N)") >= 0) {
							terms.add(o.getStem());
							System.out.println("noun == " + o.getStem());
						}
						if (mWord.indexOf("(V)") >= 0) {
							terms.add(o.getStem() + "다");
							System.out.println("verb == " + o.getStem() + "다");
						}
						
						System.out.println("\n");				
						
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
	

	
	private String convertTerms(Vector<String> terms) {
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
	
	private void writeFile(String programId, List<Post> posts, List<Comment> comments) throws IOException {
		File out = new File("D:/workspace/social-buzz/output/" + programId + ".txt");
		PrintWriter writer = new PrintWriter(new FileWriter(out));
		
		// write header
		writer.println("program_id, author_id, type, post_id, comment_id, publish_date, terms");
		
		for (Post post : posts) {
			String postId = post.getPostId();
			String authorId = post.getAuthorId();
			String publishDate = this.convertDate("yyyy-mm-dd", post.getPublishDate());
			String textBody = post.getTextBody();
			String textTag = post.getTagText();
			String type = "POST";
			
			Vector<String> terms = this.extractTerms(textBody);
			terms.addAll(this.extractTerms(textTag));			
			String strTerms = this.convertTerms(terms);
			
			writer.println(
					programId + "," +
					authorId + "," + 
					type + "," + 
					postId + "," + 
					"" + "," +
					publishDate + "," + 
					strTerms);
			
		}
		
		for (Comment comment : comments) {
			String postId = comment.getPostId();
			String commentId = comment.getCommentId();
			String authorId = comment.getAuthorId();
			String publishDate = this.convertDate("yyyy-mm-dd", comment.getPublishDate());
			String textBody = comment.getTextBody();
			String type = "COMMENT";
			
			Vector<String> terms = this.extractTerms(textBody);
			String strTerms = this.convertTerms(terms);
			
			writer.println(
					programId + "," +
					authorId + "," + 
					type + "," + 
					postId + "," + 
					commentId + "," +
					publishDate + "," + 
					strTerms);
		}
		
		writer.close();		
	}
	
	public void analyze() {
		try {
			//String programId = "kbs1_greatking";
			//String programId = "kbs2_ojakkyo";
			//String programId = "mbc_thousand";
			//String programId = "sbs_besideme";
			//String programId = "kbs2_princess";
			//String programId = "mbs_fallinlove";
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
			String programId = "sbs_newsunday";
									
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
	
	public static void main(String[] args) {
		//String text = "[연예] '무도' 음원 주간 다운로드 총 800만 건 : MBC TV '무한도전'의 '서해안 고속도로 가요제'가 발표한 음원들이 주간 종합 다운로드 차트 집계에서 총 800만 건을 넘겼고, 그중 지-드래곤.박명수의 '바람났어'가 1위에 올랐습니다. 노래는 안좋아 별로네 못하네";
		
		TextAnalyzer analyzer = new TextAnalyzer();
		
		analyzer.analyze();
		
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
