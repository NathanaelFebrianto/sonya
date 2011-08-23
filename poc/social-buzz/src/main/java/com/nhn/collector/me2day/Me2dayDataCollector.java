package com.nhn.collector.me2day;

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

import com.nhn.textmining.DocIndexWriter;
import com.nhn.textmining.TextAnalyzer;

public class Me2dayDataCollector {
	
	public Me2dayDataCollector() {
		
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
	
	public List<String> searchPosts(String query, String target, String beginTime, String endTime, int maxPage) {
		if (maxPage <= 0) {
			Hashtable result = searchPostsByPage(query, target, beginTime, endTime, maxPage);
			if (result == null)
				return null; 
		
			int resultCount = new Integer((String)result.get("count")).intValue();
			List<String> list = (List<String>) result.get("list");
			return list;
		}
		
		List<String> allList = new ArrayList<String>();
		
		for (int page = 1; page <= maxPage; page++) {
			Hashtable result = searchPostsByPage(query, target, beginTime, endTime, page);
			if (result == null)
				return null; 
			
			int resultCount = (Integer) result.get("count");
			List<String> list = (List<String>) result.get("list");
			allList.addAll(list);
			
			if (resultCount < 19)
				break;			
		}
		
		return allList;
	}
	
	/**
	 * Searches the post list.
	 * 
	 * @param query the query string of "keyword1+keyword2"...
	 * @param target the "all" or "body" or "tag"
	 * @param beginTime the "yyyy.mm.dd"
	 * @param endTime the "yyyy.mm.dd
	 * @param page the specified page
	 * @return Hashtable the result count and list
	 */
	public Hashtable<String, Object> searchPostsByPage(String query, String target, String beginTime, String endTime, int page) {
		StringBuffer url = new StringBuffer().append("http://me2day.net/search.xml?");
		
		Hashtable<String, Object> result = new Hashtable<String, Object>();
		ArrayList<String> list = new ArrayList<String>();
		
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
				Node post_node = post_list.item(i);
				if (post_node.getNodeType() == Node.ELEMENT_NODE) {
					Element post_element = (Element) post_node;

					Node post_id_node = post_element.getElementsByTagName("post_id").item(0);
					System.out.println(post_id_node.getNodeName() + " == " + post_id_node.getTextContent());
					
					Node textBody_node = post_element.getElementsByTagName("textBody").item(0);
					System.out.println(textBody_node.getNodeName() + " == " + textBody_node.getTextContent());
					
					// just for test
					TextAnalyzer analyzer = new TextAnalyzer();
					analyzer.extractTerms(textBody_node.getTextContent());
					///////////////////////
					
					Node tagText_node = post_element.getElementsByTagName("tagText").item(0);
					System.out.println(tagText_node.getNodeName() + " == " + tagText_node.getTextContent());
					
					Node pubDate_node = post_element.getElementsByTagName("pubDate").item(0);
					System.out.println(pubDate_node.getNodeName() + " == " + pubDate_node.getTextContent());
					
					Node commentsCount_node = post_element.getElementsByTagName("commentsCount").item(0);
					System.out.println(commentsCount_node.getNodeName() + " == " + commentsCount_node.getTextContent());
					
					Node metooCount_node = post_element.getElementsByTagName("metooCount").item(0);
					System.out.println(metooCount_node.getNodeName() + " == " + metooCount_node.getTextContent());
					
					Node author_node = post_element.getElementsByTagName("author").item(0);
					if (author_node.getNodeType() == Node.ELEMENT_NODE) {
						Element author_element = (Element) author_node;
						
						Node author_id_node = author_element.getElementsByTagName("id").item(0);
						System.out.println(author_id_node.getNodeName() + " == " + author_id_node.getTextContent());
						
						Node author_nickname_node = author_element.getElementsByTagName("nickname").item(0);
						System.out.println(author_nickname_node.getNodeName() + " == " + author_nickname_node.getTextContent());
						
						Node author_face_node = author_element.getElementsByTagName("face").item(0);
						System.out.println(author_face_node.getNodeName() + " == " + author_face_node.getTextContent());
					}
					
					// get comments
					int commentCount = Integer.parseInt(commentsCount_node.getTextContent());
					if (commentCount > 0) {
						this.getComments(post_id_node.getTextContent());
					}
					
					// get metoos
					int metooCount = Integer.parseInt(metooCount_node.getTextContent());
					if (metooCount > 0) {
						this.getMetoos(post_id_node.getTextContent());
					}
					
					list.add(textBody_node.getTextContent());
				}
			}
			result.put("count", resultCount);
			result.put("list", list);
			
			return result;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Gets the comments.
	 * 
	 * @param postId
	 * @return int the result count
	 */
	public int getComments(String postId) {
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
	 * Gets the metoos.
	 * 
	 * @param postId
	 * @return int the result count
	 */
	public int getMetoos(String postId) {
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
	
			List<String> texts = collector.searchPosts("무한도전", "all", "2011.08.13", "2011.08.14", 1);
			
			DocIndexWriter indexWriter = new DocIndexWriter();
			indexWriter.write(texts, "D:/test/");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
