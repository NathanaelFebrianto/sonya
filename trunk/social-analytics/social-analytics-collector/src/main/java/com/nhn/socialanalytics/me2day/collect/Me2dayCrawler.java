package com.nhn.socialanalytics.me2day.collect;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nhn.socialanalytics.common.util.DateUtil;
import com.nhn.socialanalytics.me2day.model.Comment;
import com.nhn.socialanalytics.me2day.model.Metoo;
import com.nhn.socialanalytics.me2day.model.Post;
import com.nhn.socialanalytics.me2day.parse.Me2dayParser;

public class Me2dayCrawler {
	
	public static final String TARGET_ALL = "all";
	public static final String TARGET_BODY = "body";
	public static final String TARGET_TAG = "tag";	
	
	public Me2dayCrawler() {}
	
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
	
	public List<Post> searchPosts(Map<String, Integer> queryMap, String target, Date since, Date until) {
		List<Post> result = new ArrayList<Post>();
		Set<String> idHashSet = new HashSet<String>();
		
		for (Map.Entry<String, Integer> entry : queryMap.entrySet()) {
			String query = entry.getKey();
			int maxPage = entry.getValue();			
			List<Post> posts = this.searchPosts(query, target, since, until, maxPage);
			
			if (posts != null) {
				for (int i = 0; i < posts.size(); i++) {
					Post post = (Post) posts.get(i);
					String postId = post.getPostId();				
					// check if it is duplicate
					if (!idHashSet.contains(postId)) {
						result.add(post);
					}				
					idHashSet.add(postId);
				}				
			}
		}
		
		return result;
	}
	
	/**
	 * Searches the posts.
	 * 
	 * @param query
	 * @param target
	 * @param publishStartDate
	 * @param publishEndDate
	 * @param maxPage
	 * @return
	 */
	public List<Post> searchPosts(String query, String target, Date since, Date until, int maxPage) {
    	
		String strSince = DateUtil.convertDateToString("yyyy.MM.dd", since);
		String strUntil = DateUtil.convertDateToString("yyyy.MM.dd", until);
     	System.out.println("query = " + query + " since: " + strSince +  " until: " + strUntil +" maxPage: " + maxPage);
		
		if (maxPage <= 0) {
			Hashtable<String, Object> result = searchPostsByPage(query, target, since, until, maxPage, false, false);
			if (result == null)
				return null; 
		
			//int resultCount = new Integer((String)result.get("COUNT")).intValue();
			List<Post> list = (List<Post>) result.get("POST");
			return list;
		}
		
		List<Post> allPosts = new ArrayList<Post>();
		
		for (int page = 1; page <= maxPage; page++) {
			Hashtable<String, Object> result = searchPostsByPage(query, target, since, until, page, false, false);
			if (result == null)
				return null; 
			
			int resultCount = (Integer) result.get("COUNT");
			List<Post> posts = (List<Post>) result.get("POST");
			allPosts.addAll(posts);
			
			if (resultCount < 19)
				break;			
		}
		
		return allPosts;
	}
	
	/**
	 * Searches the post list.
	 * 
	 * @param query the query string of "keyword1+keyword2"...
	 * @param target the "all" or "body" or "tag"
	 * @param since the publish start date(yyyy.mm.dd)
	 * @param until the publish end date(yyyy.mm.dd)
	 * @param page the specified page
	 * @param collectComment true if collect comments
	 * @param collectMetoo true if collect metoos
	 * @return Hashtable the result count and list
	 */
	public Hashtable<String, Object> searchPostsByPage(String query, String target, Date since, Date until, int page, 
			boolean collectComment, boolean collectMetoo) {
		StringBuffer url = new StringBuffer().append("http://me2day.net/search.xml?");
		
		Hashtable<String, Object> result = new Hashtable<String, Object>();
		List<Post> posts = new ArrayList<Post>();
		List<Comment> comments = new ArrayList<Comment>();
		List<Metoo> metoos = new ArrayList<Metoo>();
		
		if (query == null) 
			return null;
		
		System.out.println("searchPostsByPage.....................");
		
		System.out.println("page == " + page);
		
		try {
			url.append("query=").append(URLEncoder.encode(query, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		if (target != null)
			url.append("&target=").append(target).append("&search_at=all");
		
		if (since != null && until != null)
			url.append("&begin_time=").append(DateUtil.convertDateToString("yyyy.MM.dd", since))
			   .append("&end_time=").append(DateUtil.convertDateToString("yyyy.MM.dd", until))
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
			
						posts.add(post);
						
						// get comments
						if (collectComment && commentCount > 0) {				
							List<Comment> postComments = this.collectComments(postId);
							comments.addAll(postComments);
						}
						// get metoos
						if (collectMetoo && metooCount > 0) {							
							List<Metoo> postMetoos = this.collectMetoos(postId);
							metoos.addAll(postMetoos);
						}			
					} 
					
				} catch (Exception ee) {
					ee.printStackTrace();
				}

			} // end for
			
			result.put("COUNT", resultCount);
			result.put("POST", posts);
			
			if (collectComment)
				result.put("COMMENT", comments);
			if (collectMetoo)
				result.put("METOO", metoos);
			
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
	 * @return List<Comment> the comment list
	 */
	public List<Comment> collectComments(String postId) {
		List<Comment> comments = new ArrayList<Comment>();		
		StringBuffer url = new StringBuffer().append("http://me2day.net/api/get_comments.xml?");
		
		if (postId == null) 
			return comments;
		
		System.out.println("collectComments.....................");
		
		if (postId != null)
			url.append("post_id=").append(postId);
		
		System.out.println("url == " + url.toString());	
	
		try {
			Document doc = getDocument(url.toString());			
			Element root = doc.getDocumentElement();
			
			//Node page_node = root.getElementsByTagName("page").item(0);
			//Node postAuthorId_node = root.getElementsByTagName("postAuthorId").item(0);
			//Node postAuthorNickname_node = root.getElementsByTagName("postAuthorNickname").item(0);
			//Node totalCount_node = root.getElementsByTagName("totalCount").item(0);
			//Node totalPage_node = root.getElementsByTagName("totalPage").item(0);
			//Node itemsPerPage_node = root.getElementsByTagName("itemsPerPage").item(0);
			
			NodeList comment_list = root.getElementsByTagName("comment");
			
			int resultCount = comment_list.getLength();
			
			System.out.println("# of comments == " + resultCount);
			
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
					
					comments.add(comment);
				} catch (Exception ex) {
					ex.printStackTrace();
				}				
			} // end for
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return comments;
	}
	
	/**
	 * Collects the metoos.
	 * 
	 * @param postId
	 * @return List<Metoo> the metoo list
	 */
	public List<Metoo> collectMetoos(String postId) {
		List<Metoo> metoos = new ArrayList<Metoo>();
		StringBuffer url = new StringBuffer().append("http://me2day.net/api/get_metoos.xml?");
		
		if (postId == null) 
			return metoos;
		
		System.out.println("collectMetoos.....................");
		
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
					
					metoos.add(metoo);					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
			} // end for
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return metoos;
	}

	
	public static void main(String[] args) {
		Me2dayCrawler crawler = new Me2dayCrawler();
		
		Map<String, Integer> queryMap = new HashMap<String, Integer>();
		queryMap.put("한미FTA ISD", 5);
		queryMap.put("FTA ISD", 5);
		
		Date since = DateUtil.addDay(new Date(), -30);
		Date until = DateUtil.addDay(new Date(), +1);
		
		List<Post> posts = crawler.searchPosts(queryMap, Me2dayCrawler.TARGET_ALL, since, until);
	}
}
