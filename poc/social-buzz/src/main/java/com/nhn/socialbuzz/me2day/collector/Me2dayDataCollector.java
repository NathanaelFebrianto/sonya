package com.nhn.socialbuzz.me2day.collector;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nhn.socialbuzz.me2day.model.Post;
import com.nhn.socialbuzz.me2day.service.CommentManager;
import com.nhn.socialbuzz.me2day.service.CommentManagerImpl;
import com.nhn.socialbuzz.me2day.service.MetooManager;
import com.nhn.socialbuzz.me2day.service.MetooManagerImpl;
import com.nhn.socialbuzz.me2day.service.PostManager;
import com.nhn.socialbuzz.me2day.service.PostManagerImpl;
import com.nhn.socialbuzz.me2day.service.TvProgramManager;
import com.nhn.socialbuzz.me2day.service.TvProgramManagerImpl;

public class Me2dayDataCollector {
	
	private TvProgramManager tvProgramManager;
	private PostManager postManager;
	private CommentManager commentManager;
	private MetooManager metooManager;
	
	public Me2dayDataCollector() {		
		tvProgramManager = new TvProgramManagerImpl();
		postManager = new PostManagerImpl();
		commentManager = new CommentManagerImpl();
		metooManager = new MetooManagerImpl();
	}

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
	
	public List<Post> searchPosts(String programId, String query, String target, String beginTime, String endTime, int maxPage) {
		if (maxPage <= 0) {
			Hashtable result = searchPostsByPage(programId, query, target, beginTime, endTime, maxPage);
			if (result == null)
				return null; 
		
			int resultCount = new Integer((String)result.get("count")).intValue();
			List<Post> list = (List<Post>) result.get("list");
			return list;
		}
		
		List<Post> allList = new ArrayList<Post>();
		
		for (int page = 1; page <= maxPage; page++) {
			Hashtable result = searchPostsByPage(programId, query, target, beginTime, endTime, page);
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
	 * @param beginTime the "yyyy.mm.dd"
	 * @param endTime the "yyyy.mm.dd
	 * @param page the specified page
	 * @return Hashtable the result count and list
	 */
	public Hashtable<String, Object> searchPostsByPage(String programId, String query, String target, String beginTime, String endTime, int page) {
		StringBuffer url = new StringBuffer().append("http://me2day.net/search.xml?");
		
		Hashtable<String, Object> result = new Hashtable<String, Object>();
		ArrayList<Post> list = new ArrayList<Post>();
		
		if (query == null) 
			return null;
		
		System.out.println("============================================");
		System.out.println("page == " + page);
		
		try {
			url.append("query=").append(URLEncoder.encode(query, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		if (target != null)
			url.append("&target=").append(target)
			   .append("&search_at=all");
		if (beginTime != null && endTime != null)
			url.append("&begin_time=").append(beginTime)
			   .append("&end_time=").append(endTime)
			   .append("&time=custom&tz=Asia%2FSeoul");
		if (page > 0)
			url.append("&page=page_").append(page);
		
		System.out.println("url == " + url.toString());
		
		try {
			Document doc = getDocument(url.toString());

			Element root = doc.getDocumentElement();
			
			NodeList post_list = root.getElementsByTagName("post");
			
			int resultCount = post_list.getLength();
			
			System.out.println("# of posts == " + resultCount);
			
			for (int i = 0; i < post_list.getLength(); i++) {
				try {
					Post post = new Post();
					post.setProgramId(programId);
					
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
						//post.setPublishDate(new Date(pubDate));
						System.out.println(pubDate_node.getNodeName() + " == " + pubDate);
						
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
						
						// insert post into database
						postManager.addPost(post);
						
						// get comments
						if (commentCount > 0) {
							this.collectComments(postId);
						}
						
						// get metoos
						if (metooCount > 0) {
							this.collectMetoos(postId);
						}
						
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
		
		System.out.println("============================================");
		
		if (postId != null)
			url.append("post_id=").append(postId);
		
		System.out.println("url == " + url.toString());
		
		try {
			Document doc = getDocument(url.toString());
			
			Element root = doc.getDocumentElement();
			
			Node page_node = root.getElementsByTagName("page").item(0);
			Node postAuthorId_node = root.getElementsByTagName("postAuthorId").item(0);
			Node postAuthorNickname_node = root.getElementsByTagName("postAuthorNickname").item(0);
			Node totalCount_node = root.getElementsByTagName("totalCount").item(0);
			Node totalPage_node = root.getElementsByTagName("totalPage").item(0);
			Node itemsPerPage_node = root.getElementsByTagName("itemsPerPage").item(0);
			
			NodeList comment_list = root.getElementsByTagName("comment");
			
			int resultCount = comment_list.getLength();
			
			System.out.println(page_node.getNodeName() + " == " + page_node.getTextContent());
			System.out.println(postAuthorId_node.getNodeName() + " == " + postAuthorId_node.getTextContent());
			System.out.println(postAuthorNickname_node.getNodeName() + " == " + postAuthorNickname_node.getTextContent());
			System.out.println(totalCount_node.getNodeName() + " == " + totalCount_node.getTextContent());
			System.out.println(totalPage_node.getNodeName() + " == " + totalPage_node.getTextContent());
			System.out.println(itemsPerPage_node.getNodeName() + " == " + itemsPerPage_node.getTextContent());
			
			System.out.println("# of comments == " + resultCount);
			
			for (int i = 0; i < comment_list.getLength(); i++) {
				Node comment_node = comment_list.item(i);
				if (comment_node.getNodeType() == Node.ELEMENT_NODE) {
					Element comment_element = (Element) comment_node;

					Node commentId_node = comment_element.getElementsByTagName("commentId").item(0);
					System.out.println(commentId_node.getNodeName() + " == " + commentId_node.getTextContent());
					
					Node textBody_node = comment_element.getElementsByTagName("textBody").item(0);
					System.out.println(textBody_node.getNodeName() + " == " + textBody_node.getTextContent());
					
					Node pubDate_node = comment_element.getElementsByTagName("pubDate").item(0);
					System.out.println(pubDate_node.getNodeName() + " == " + pubDate_node.getTextContent());
					
					Node author_node = comment_element.getElementsByTagName("author").item(0);
					if (author_node.getNodeType() == Node.ELEMENT_NODE) {
						Element author_element = (Element) author_node;
						
						Node author_id_node = author_element.getElementsByTagName("id").item(0);
						System.out.println(author_id_node.getNodeName() + " == " + author_id_node.getTextContent());
						
						Node author_nickname_node = author_element.getElementsByTagName("nickname").item(0);
						System.out.println(author_nickname_node.getNodeName() + " == " + author_nickname_node.getTextContent());
						
						Node author_face_node = author_element.getElementsByTagName("face").item(0);
						System.out.println(author_face_node.getNodeName() + " == " + author_face_node.getTextContent());
					}
				}
			}
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
		
		System.out.println("============================================");
		
		if (postId != null)
			url.append("post_id=").append(postId);
		
		System.out.println("url == " + url.toString());
		
		try {
			Document doc = getDocument(url.toString());
			
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
			
			for (int i = 0; i < metoo_list.getLength(); i++) {
				Node metoo_node = metoo_list.item(i);
				if (metoo_node.getNodeType() == Node.ELEMENT_NODE) {
					Element metoo_element = (Element) metoo_node;

					Node pubDate_node = metoo_element.getElementsByTagName("pubDate").item(0);
					System.out.println(pubDate_node.getNodeName() + " == " + pubDate_node.getTextContent());
					
					Node author_node = metoo_element.getElementsByTagName("author").item(0);
					if (author_node.getNodeType() == Node.ELEMENT_NODE) {
						Element author_element = (Element) author_node;
						
						Node author_id_node = author_element.getElementsByTagName("id").item(0);
						System.out.println(author_id_node.getNodeName() + " == " + author_id_node.getTextContent());
						
						Node author_nickname_node = author_element.getElementsByTagName("nickname").item(0);
						System.out.println(author_nickname_node.getNodeName() + " == " + author_nickname_node.getTextContent());
						
						Node author_face_node = author_element.getElementsByTagName("face").item(0);
						System.out.println(author_face_node.getNodeName() + " == " + author_face_node.getTextContent());
					}
				}
			}
			return resultCount;			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static void main(String[] args) {
		try {
			Me2dayDataCollector collector = new Me2dayDataCollector();
	
			List<Post> texts = collector.searchPosts("1", "무한도전", "all", "2011.08.13", "2011.08.14", 50);			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
