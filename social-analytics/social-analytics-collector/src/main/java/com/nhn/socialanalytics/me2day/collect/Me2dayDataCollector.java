package com.nhn.socialanalytics.me2day.collect;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nhn.socialanalytics.common.JobLogger;
import com.nhn.socialanalytics.common.util.DateUtil;
import com.nhn.socialanalytics.me2day.model.Comment;
import com.nhn.socialanalytics.me2day.model.Metoo;
import com.nhn.socialanalytics.me2day.model.Post;
import com.nhn.socialanalytics.me2day.parse.Me2dayParser;

public class Me2dayDataCollector {

	private static JobLogger logger = JobLogger.getLogger(Me2dayDataCollector.class, "me2day-collect.log");
	
	public Me2dayDataCollector() {
	}
	
	/**
	 * Gets the document through url.
	 * @param strUrl
	 * @return
	 * @throws Exception
	 */
	private Document getDocument(String strUrl) throws Exception {
		URL url = new URL(strUrl);
		URLConnection connection = url.openConnection();

		InputStream is = connection.getInputStream();		

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(is);
		doc.getDocumentElement().normalize();
		
		/*
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
		String data = null;
		while((data = br.readLine()) != null){
			System.out.println(data);
		}
		*/

		return doc;
	}
	
	/**
	 * Searches the posts.
	 * 
	 * @param programId
	 * @param query
	 * @param target
	 * @param publishStartDate
	 * @param publishEndDate
	 * @param maxPage
	 * @return
	 */
	public List<Post> searchPosts(String objectId, String query, String target, String publishStartDate, String publishEndDate, int maxPage) {
    	
    	logger.info("------------------------------------------------");
    	logger.info("object id: " + objectId);    	
    	logger.info("query = " + query + " publishStartDate: " + publishStartDate +  " publishEndDate: " + publishEndDate +" maxPage: " + maxPage);
		
		if (maxPage <= 0) {
			Hashtable result = searchPostsByPage(objectId, query, target, publishStartDate, publishEndDate, maxPage);
			if (result == null)
				return null; 
		
			int resultCount = new Integer((String)result.get("count")).intValue();
			List<Post> list = (List<Post>) result.get("list");
			return list;
		}
		
		List<Post> allList = new ArrayList<Post>();
		
		for (int page = 1; page <= maxPage; page++) {
			Hashtable<String, Object> result = searchPostsByPage(objectId, query, target, publishStartDate, publishEndDate, page);
			if (result == null)
				return null; 
			
			int resultCount = (Integer) result.get("count");
			List<Post> list = (List<Post>) result.get("list");
			allList.addAll(list);
			
			if (resultCount < 19)
				break;			
		}
		
		return allList;
	}
	
	/**
	 * Searches the post list.
	 * 
	 * @param programId the tv program id
	 * @param query the query string of "keyword1+keyword2"...
	 * @param target the "all" or "body" or "tag"
	 * @param publishStartDate the "yyyy.mm.dd"
	 * @param publishEndDate the "yyyy.mm.dd
	 * @param page the specified page
	 * @return Hashtable the result count and list
	 */
	public Hashtable<String, Object> searchPostsByPage(String objectId, String query, String target, String publishStartDate, String publishEndDate, int page) {
		StringBuffer url = new StringBuffer().append("http://me2day.net/search.xml?");
		
		Hashtable<String, Object> result = new Hashtable<String, Object>();
		ArrayList<Post> list = new ArrayList<Post>();
		
		if (query == null) 
			return null;
		
		logger.info("searchPostsByPage.....................");
		
		logger.info("page == " + page);
		
		try {
			url.append("query=").append(URLEncoder.encode(query, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		if (target != null)
			url.append("&target=").append(target)
			   .append("&search_at=all");
		if (publishStartDate != null && publishEndDate != null)
			url.append("&begin_time=").append(publishStartDate)
			   .append("&end_time=").append(publishEndDate)
			   .append("&time=custom&tz=Asia%2FSeoul");
		if (page > 0)
			url.append("&page=page_").append(page);
		
		logger.info("url == " + url.toString());
		
		try {
			Document doc = getDocument(url.toString());
			Element root = doc.getDocumentElement();			
			NodeList post_list = root.getElementsByTagName("post");
			
			int resultCount = post_list.getLength();			
			logger.info("# of posts == " + resultCount);
			
			for (int i = 0; i < post_list.getLength(); i++) {
				try {
					Post post = new Post();
					post.setObjectId(objectId);
					
					Node post_node = post_list.item(i);
					if (post_node.getNodeType() == Node.ELEMENT_NODE) {
						Element post_element = (Element) post_node;
	
						Node post_id_node = post_element.getElementsByTagName("post_id").item(0);
						String postId = post_id_node.getTextContent();
						post.setPostId(postId);
						System.out.println(post_id_node.getNodeName() + " == " + postId);
						
						Node permalink_node = post_element.getElementsByTagName("permalink").item(0);
						String permalink = permalink_node.getTextContent();
						post.setPermalink(permalink);
						System.out.println(permalink_node.getNodeName() + " == " + permalink);
						
						Node body_node = post_element.getElementsByTagName("body").item(0);
						String body = body_node.getTextContent();
						post.setBody(body);
						System.out.println(body_node.getNodeName() + " == " + body);
						
						Node textBody_node = post_element.getElementsByTagName("textBody").item(0);
						String textBody = textBody_node.getTextContent();
						post.setTextBody(textBody);
						System.out.println(textBody_node.getNodeName() + " == " + textBody);
						
						Node icon_node = post_element.getElementsByTagName("icon").item(0);
						String icon = icon_node.getTextContent();
						post.setIcon(icon);
						System.out.println(icon_node.getNodeName() + " == " + icon);
						
						Node tagText_node = post_element.getElementsByTagName("tagText").item(0);
						String tagText = tagText_node.getTextContent();
						post.setTagText(tagText);
						System.out.println(tagText_node.getNodeName() + " == " + tagText);
						
						Node me2dayPage_node = post_element.getElementsByTagName("me2dayPage").item(0);
						String me2dayPage = me2dayPage_node.getTextContent();
						post.setMe2dayPage(me2dayPage);
						System.out.println(me2dayPage_node.getNodeName() + " == " + me2dayPage);
						
						Node pubDate_node = post_element.getElementsByTagName("pubDate").item(0);
						String pubDate = pubDate_node.getTextContent();
						post.setPublishDate(Me2dayParser.convertDate(pubDate));
						System.out.println(pubDate_node.getNodeName() + " == " + pubDate + " -> " + post.getPublishDate());
						
						Node commentsCount_node = post_element.getElementsByTagName("commentsCount").item(0);
						int commentCount = Integer.parseInt(commentsCount_node.getTextContent());
						post.setCommentCount(commentCount);
						System.out.println(commentsCount_node.getNodeName() + " == " + commentCount);
						
						Node metooCount_node = post_element.getElementsByTagName("metooCount").item(0);
						int metooCount = Integer.parseInt(metooCount_node.getTextContent());
						post.setMetooCount(metooCount);
						System.out.println(metooCount_node.getNodeName() + " == " + metooCount);
						
						Node iconUrl_node = post_element.getElementsByTagName("iconUrl").item(0);
						String iconUrl = iconUrl_node.getTextContent();
						post.setIconUrl(iconUrl);
						System.out.println(iconUrl_node.getNodeName() + " == " + iconUrl);
						
						Node callbackUrl_node = post_element.getElementsByTagName("callbackUrl").item(0);
						String callbackUrl = callbackUrl_node.getTextContent();
						post.setCallbackUrl(callbackUrl);
						System.out.println(callbackUrl_node.getNodeName() + " == " + callbackUrl);
						
						
						Node author_node = post_element.getElementsByTagName("author").item(0);
						if (author_node.getNodeType() == Node.ELEMENT_NODE) {
							Element author_element = (Element) author_node;
							
							Node author_id_node = author_element.getElementsByTagName("id").item(0);
							String authorId = author_id_node.getTextContent();
							post.setAuthorId(authorId);						
							System.out.println(author_id_node.getNodeName() + " == " + authorId);
							
							Node author_nickname_node = author_element.getElementsByTagName("nickname").item(0);
							String authorNickname = author_nickname_node.getTextContent();
							post.setAuthorNickname(authorNickname);
							System.out.println(author_nickname_node.getNodeName() + " == " + authorNickname);
							
							Node author_face_node = author_element.getElementsByTagName("face").item(0);
							String authorProfileImage = author_face_node.getTextContent();
							post.setAuthorProfileImage(authorProfileImage);
							System.out.println(author_face_node.getNodeName() + " == " + authorProfileImage);
							
							Node author_me2dayHome_node = author_element.getElementsByTagName("me2dayHome").item(0);
							String authorMe2dayHome = author_me2dayHome_node.getTextContent();
							post.setAuthorMe2dayHome(authorMe2dayHome);
							System.out.println(author_me2dayHome_node.getNodeName() + " == " + authorMe2dayHome);
						}
						
						///////////////////////////////////////
						// write post to file
						//////////////////////////////////////

						
						// get comments
//						if (commentCount > 0) {							
//							this.collectComments(postId);
//						}
						
						// get metoos
//						if (metooCount > 0) {							
//							this.collectMetoos(postId);
//						}
						
						list.add(post);
					} 
					
				} catch (Exception ee) {
					ee.printStackTrace();
				}

			} // end for
			
			result.put("count", resultCount);
			result.put("list", list);
			
			return result;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Collects the comments.
	 * 
	 * @param postId
	 * @return int the result count
	 */
	public int collectComments(String postId) {
		StringBuffer url = new StringBuffer().append("http://me2day.net/api/get_comments.xml?");
		
		if (postId == null) 
			return 0;
		
		logger.info("collectComments.....................");
		
		if (postId != null)
			url.append("post_id=").append(postId);
		
		logger.info("url == " + url.toString());	
	
		try {
			Document doc = getDocument(url.toString());

			///////////////////////////////////////////
			// delete comments 
			///////////////////////////////////////////
			// code here.....
			
			Element root = doc.getDocumentElement();
			
			Node page_node = root.getElementsByTagName("page").item(0);
			Node postAuthorId_node = root.getElementsByTagName("postAuthorId").item(0);
			Node postAuthorNickname_node = root.getElementsByTagName("postAuthorNickname").item(0);
			Node totalCount_node = root.getElementsByTagName("totalCount").item(0);
			Node totalPage_node = root.getElementsByTagName("totalPage").item(0);
			Node itemsPerPage_node = root.getElementsByTagName("itemsPerPage").item(0);
			
			NodeList comment_list = root.getElementsByTagName("comment");
			
			int resultCount = comment_list.getLength();
			
			logger.info("# of comments == " + resultCount);
			
			for (int i = 0; i < comment_list.getLength(); i++) {
				try {
					Comment comment = new Comment();
					comment.setPostId(postId);
					
					Node comment_node = comment_list.item(i);
					if (comment_node.getNodeType() == Node.ELEMENT_NODE) {
						Element comment_element = (Element) comment_node;
	
						Node commentId_node = comment_element.getElementsByTagName("commentId").item(0);
						String commentId = commentId_node.getTextContent();
						comment.setCommentId(commentId);
						System.out.println(commentId_node.getNodeName() + " == " + commentId);
						
						Node body_node = comment_element.getElementsByTagName("body").item(0);
						String body = body_node.getTextContent();
						comment.setBody(body);
						System.out.println(body_node.getNodeName() + " == " + body);
						
						Node textBody_node = comment_element.getElementsByTagName("textBody").item(0);
						String textBody = textBody_node.getTextContent();
						comment.setTextBody(textBody);
						System.out.println(textBody_node.getNodeName() + " == " + textBody);
						
						Node pubDate_node = comment_element.getElementsByTagName("pubDate").item(0);
						String pubDate = pubDate_node.getTextContent();
						comment.setPublishDate(Me2dayParser.convertDate(pubDate));
						System.out.println(pubDate_node.getNodeName() + " == " + pubDate + " -> " + comment.getPublishDate());
						
						Node author_node = comment_element.getElementsByTagName("author").item(0);
						if (author_node.getNodeType() == Node.ELEMENT_NODE) {
							Element author_element = (Element) author_node;
							
							Node author_id_node = author_element.getElementsByTagName("id").item(0);
							String authorId = author_id_node.getTextContent();
							comment.setAuthorId(authorId);
							System.out.println(author_id_node.getNodeName() + " == " + authorId);
							
							Node author_nickname_node = author_element.getElementsByTagName("nickname").item(0);
							String authorNickname = author_nickname_node.getTextContent();
							comment.setAuthorNickname(authorNickname);
							System.out.println(author_nickname_node.getNodeName() + " == " + authorNickname);
							
							Node author_face_node = author_element.getElementsByTagName("face").item(0);
							String authorProfileImage = author_face_node.getTextContent();
							comment.setAuthorProfileImage(authorProfileImage);
							System.out.println(author_face_node.getNodeName() + " == " + authorProfileImage);
							
							Node me2dayHome_node = author_element.getElementsByTagName("me2dayHome").item(0);
							String authorMe2dayHome = me2dayHome_node.getTextContent();
							comment.setAuthorMe2dayHome(authorMe2dayHome);
							System.out.println(me2dayHome_node.getNodeName() + " == " + authorMe2dayHome);
						}
					}
					
					///////////////////////////////////////
					// insert comment into database
					//////////////////////////////////////
					// code here.....
					
				} catch (Exception ee) {
					ee.printStackTrace();
				}
				
			} // end for
			
			return resultCount;	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Collects the metoos.
	 * 
	 * @param postId
	 * @return int the result count
	 */
	public int collectMetoos(String postId) {
		StringBuffer url = new StringBuffer().append("http://me2day.net/api/get_metoos.xml?");
		
		if (postId == null) 
			return 0;
		
		logger.info("collectMetoos.....................");
		
		if (postId != null)
			url.append("post_id=").append(postId);
		
		logger.info("url == " + url.toString());	
		
		try {
			Document doc = getDocument(url.toString());
			
			///////////////////////////////////////////
			// delete metoos 
			///////////////////////////////////////////
			// code here...
			
			Element root = doc.getDocumentElement();
			
			Node page_node = root.getElementsByTagName("page").item(0);
			Node totalCount_node = root.getElementsByTagName("totalCount").item(0);
			Node totalPage_node = root.getElementsByTagName("totalPage").item(0);
			Node itemsPerPage_node = root.getElementsByTagName("itemsPerPage").item(0);
			
			NodeList metoo_list = root.getElementsByTagName("metoo");
			
			int resultCount = metoo_list.getLength();
			
			System.out.println(page_node.getNodeName() + " == " + page_node.getTextContent());
			System.out.println(totalCount_node.getNodeName() + " == " + totalCount_node.getTextContent());
			System.out.println(totalPage_node.getNodeName() + " == " + totalPage_node.getTextContent());
			System.out.println(itemsPerPage_node.getNodeName() + " == " + itemsPerPage_node.getTextContent());
			
			System.out.println("# of metoos == " + resultCount);
			logger.info("# of metoos == " + resultCount);
			
			for (int i = 0; i < metoo_list.getLength(); i++) {
				try {
					Metoo metoo = new Metoo();
					metoo.setPostId(postId);				
					
					Node metoo_node = metoo_list.item(i);
					if (metoo_node.getNodeType() == Node.ELEMENT_NODE) {
						Element metoo_element = (Element) metoo_node;
	
						Node pubDate_node = metoo_element.getElementsByTagName("pubDate").item(0);
						String pubDate = pubDate_node.getTextContent();
						metoo.setPublishDate(Me2dayParser.convertDate(pubDate));
						System.out.println(pubDate_node.getNodeName() + " == " + pubDate + " -> " + metoo.getPublishDate());
						
						Node author_node = metoo_element.getElementsByTagName("author").item(0);
						if (author_node.getNodeType() == Node.ELEMENT_NODE) {
							Element author_element = (Element) author_node;
							
							Node author_id_node = author_element.getElementsByTagName("id").item(0);
							String authorId = author_id_node.getTextContent();
							metoo.setAuthorId(authorId);
							System.out.println(author_id_node.getNodeName() + " == " + authorId);					
							
							Node author_nickname_node = author_element.getElementsByTagName("nickname").item(0);
							String authorNickname = author_nickname_node.getTextContent();
							metoo.setAuthorNickname(authorNickname);
							System.out.println(author_nickname_node.getNodeName() + " == " + authorNickname);
							
							Node author_face_node = author_element.getElementsByTagName("face").item(0);
							String authorProfileImage = author_face_node.getTextContent();
							metoo.setAuthorProfileImage(authorProfileImage);
							System.out.println(author_face_node.getNodeName() + " == " + authorProfileImage);
							
							Node me2dayHome_node = author_element.getElementsByTagName("me2dayHome").item(0);
							String authorMe2dayHome = me2dayHome_node.getTextContent();
							metoo.setAuthorMe2dayHome(authorMe2dayHome);
							System.out.println(me2dayHome_node.getNodeName() + " == " + authorMe2dayHome);
						}
					}
										
					///////////////////////////////////////
					// insert metoo into database
					//////////////////////////////////////
					// code here...
					
				} catch (Exception ee) {
					ee.printStackTrace();
				}
				
			} // end for
			
			return resultCount;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	
	public static void main(String[] args) {
		Me2dayDataCollector collector = new Me2dayDataCollector();
		Me2dayDataWriter writer = new Me2dayDataWriter();
		
		//String objectId = "navertalk";
		//String query = "네이버톡";
		
		//String objectId = "kakaotalk";
		//String query = "카카오톡";
		
		String objectId = "naverapp";
		String query = "네이버앱";
		
		//String publishStartDate = DateUtil.convertDateToString("yyyy.MM.dd", DateUtil.addDay(new Date(), -1));
		String publishEndDate = DateUtil.convertDateToString("yyyy.MM.dd", DateUtil.addDay(new Date(), +1));
		
		List<Post> posts = collector.searchPosts(objectId, query, "body", "2011.10.01", publishEndDate, 20);
		
		try {
			writer.writeOutput(objectId, posts, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
