package com.nhn.socialanalytics.appleappstore.collect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.nhn.socialanalytics.appleappstore.model.Review;
import com.nhn.socialanalytics.appleappstore.parse.AppStoreParser;


public class AppStoreCrawler {
	
	public AppStoreCrawler() {}
	
	public List<Review> getReviews(Set<String> appStores, String appId, int maxPage) {
		List<Review> result = new ArrayList<Review>();
		
		System.out.println("appStores = " + appStores + " appId: " + appId + " maxPage: " + maxPage);
     	
    	for (Iterator<String> it = appStores.iterator(); it.hasNext();) {
        	String appStoreId = it.next();
        	
        	List<Review> reviews = this.getReviews(appStoreId, appId, maxPage);
            
        	System.out.println("result size [appStoreId:" + appStoreId + "] = " + reviews.size());
            result.addAll(reviews);
    	}
		
		return result;
	}
	
	public List<Review> getReviews(String appStoreId, String appId, int maxPage) {
		List<Review> reviews = new ArrayList<Review>();
		// note: first page is started from 0
		for (int page = 0; page < maxPage; page++) {
			List<Review> reviewsForPage = this.getReviewsForPage(appStoreId, appId, page);
			if (reviewsForPage == null || reviewsForPage.size() == 0)
				break;
			
			reviews.addAll(reviewsForPage);
		}
		
		return reviews;
	}
	
	public List<Review> getReviews(String appStoreId, String appId) {
		List<Review> reviews = new ArrayList<Review>();
		
		int i = 0;
		while (true) {
			List<Review> reviewsForPage = this.getReviewsForPage(appStoreId, appId, i);
			if (reviewsForPage == null || reviewsForPage.size() == 0)
				break;
			
			reviews.addAll(reviewsForPage);
			i++;
		}
		
		return reviews;
	}
	
	public String getHTMLContent(String appStoreId, String appId, int pageNo) {
		String userAgent = "iTunes/9.2 (Macintosh; U; Mac OS X 10.6)";
		String front = appStoreId;
		String strUrl = "http://ax.phobos.apple.com.edgesuite.net/WebObjects/MZStore.woa/wa/viewContentsUserReviews?id=" + appId + 
				"&pageNumber=" + pageNo + "&sortOrdering=4&onlyLatestVersion=false&type=Purple+Software"; 
		String content = "";				
				
		try{
            URL url = new URL(strUrl);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            
            System.out.println("url == " + url);

            request.setUseCaches(false);
            request.setDoOutput(true);
            request.setDoInput(true);

            //request.setFollowRedirects(true);
            request.setInstanceFollowRedirects(true);

            request.setRequestProperty("X-Apple-Store-Front", front);
            request.setRequestProperty("User-Agent", userAgent);
            
            request.setRequestMethod("POST");
            OutputStreamWriter post = new OutputStreamWriter(request.getOutputStream());
            //post.write(data);
            post.flush();             
           
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content += inputLine;
            }
            System.out.println("content == " + content);  
            
            in.close();
            post.close(); 
        } catch (IOException e){
            e.printStackTrace();
        }
		
		return null;
	}
	/**
	 * Gets the reviews for page. Number of reviews per page is 25.
	 * 
	 * @param appStoreId
	 * @param appId
	 * @param pageNo
	 * @return
	 */
	public List<Review> getReviewsForPage(String appStoreId, String appId, int pageNo) {
		String userAgent = "iTunes/9.2 (Macintosh; U; Mac OS X 10.6)";
		String front = appStoreId;
		String strUrl = "http://ax.phobos.apple.com.edgesuite.net/WebObjects/MZStore.woa/wa/viewContentsUserReviews?id=" + appId + 
				"&pageNumber=" + pageNo + "&sortOrdering=4&onlyLatestVersion=false&type=Purple+Software"; 
		
		String country = AppStores.getCountry(appStoreId);
		
		List<Review> reviews = new ArrayList<Review>();
				
		try{
            URL url = new URL(strUrl);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            
            System.out.println("url == " + url);

            request.setUseCaches(false);
            request.setDoOutput(true);
            request.setDoInput(true);

            //request.setFollowRedirects(true);
            request.setInstanceFollowRedirects(true);

            request.setRequestProperty("X-Apple-Store-Front", front);
            request.setRequestProperty("User-Agent", userAgent);
            
            request.setRequestMethod("POST");
            OutputStreamWriter post = new OutputStreamWriter(request.getOutputStream());
            post.flush();      

        	DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        	domFactory.setNamespaceAware(true); // never forget this!
            
            try {
            	DocumentBuilder builder = domFactory.newDocumentBuilder();
            	Document doc = builder.parse(request.getInputStream());
             	
            	XPathFactory factory = XPathFactory.newInstance();
                XPath xpath = factory.newXPath();
                xpath.setNamespaceContext(new AppStoreNamespaceContext());
                XPathExpression expr = xpath.compile("//pre:View/pre:ScrollView/pre:VBoxView/pre:View/pre:MatrixView/pre:VBoxView/pre:VBoxView/pre:VBoxView");
                Object result = expr.evaluate(doc, XPathConstants.NODESET);                

                NodeList nodes = (NodeList) result;

                for (int i = 0; i < nodes.getLength(); i++) {
                	Node node = (Node) nodes.item(i);
   
                	// review id
                	XPathExpression exprReviewId = xpath.compile("./pre:HBoxView/pre:HBoxView/pre:LoadFrameURL");
                	Object objReviewId = exprReviewId.evaluate(node, XPathConstants.NODE);
                   	Element nodeReviewId = (Element) objReviewId;
                	String reviewId = AppStoreParser.extractReviewId(nodeReviewId.getAttribute("url"));
                 	System.out.println(i + " review id == " + reviewId);      

                	// author id
                   	XPathExpression exprAuthorId = xpath.compile("./pre:HBoxView/pre:TextView/pre:SetFontStyle/pre:GotoURL");
                	Object objAuthorId = exprAuthorId.evaluate(node, XPathConstants.NODE);
                	Element nodeAuthorId = (Element) objAuthorId;
                	String authorId = "";
                	if (nodeAuthorId != null) {
                		authorId = AppStoreParser.extractAuthorId(nodeAuthorId.getAttribute("url"));
                	}                 	
                	System.out.println(i + " author id == " + authorId);      
                 	
                	// author name
                   	XPathExpression exprAuthorName = xpath.compile("./pre:HBoxView/pre:TextView/pre:SetFontStyle/pre:GotoURL/pre:b/text()");
                	Object objAuthorName = exprAuthorName.evaluate(node, XPathConstants.NODE);
                	Node nodeAuthorName = (Node) objAuthorName;
                	String authorName = "";                	
                	if (nodeAuthorName != null) {
	                	authorName = nodeAuthorName.getNodeValue();
	                	authorName = authorName.trim();
	                	authorName = authorName.replaceAll("\n", " ");
	                	authorName = authorName.replaceAll("\t", " ");
                	}
                	System.out.println(i + " author name == " + authorName); 
                 	
                	// topic
                   	XPathExpression exprTopic = xpath.compile("./pre:HBoxView/pre:TextView/pre:SetFontStyle/pre:b/text()");
                	Object objTopic = exprTopic.evaluate(node, XPathConstants.NODE);
                	Node nodeTopic = (Node) objTopic;
                	String topic = nodeTopic.getNodeValue();
                 	System.out.println(i + " topic == " + topic);
                 	
                	// review text
                	XPathExpression exprReviewText = xpath.compile("./pre:TextView/pre:SetFontStyle/text()");
                	Object objReviewText = exprReviewText.evaluate(node, XPathConstants.NODE);
                	Node nodeReviewText = (Node) objReviewText;
                	String reviewText = nodeReviewText.getNodeValue();
                	reviewText = reviewText.trim();
                	reviewText = reviewText.replaceAll("\n", " ");
                	reviewText = reviewText.replaceAll("\t", " ");                	
                	System.out.println(i + " reivew text == " + reviewText); 
                	
                	// version and create time
                   	XPathExpression exprVersion = xpath.compile("./pre:HBoxView/pre:TextView/pre:SetFontStyle/text()");
                	Object objVersions = exprVersion.evaluate(node, XPathConstants.NODESET);
                	NodeList nodeVersions = (NodeList) objVersions;
                	Node nodeVersion = (Node) nodeVersions.item(1);
                	String version = "";                	
                	if (nodeVersion != null) {
                		version = AppStoreParser.extractVersion(nodeVersion.getNodeValue());
                	}                		
                 	System.out.println(i + " version == " + version);   
                	
                 	String createDate = "";
                 	if (nodeVersion != null) {
                 		createDate = AppStoreParser.extractDate(nodeVersion.getNodeValue());
                 	}
                	System.out.println(i + " create date == " + createDate);
                	
                	// rating
                   	XPathExpression exprRating = xpath.compile("./pre:HBoxView/pre:HBoxView/pre:HBoxView");
                	Object objRating = exprRating.evaluate(node, XPathConstants.NODE);
                	Element nodeRating = (Element) objRating;
                	int rating = Integer.valueOf(AppStoreParser.extractNumber(nodeRating.getAttribute("alt")));
                 	System.out.println(i + " rating == " + rating);
                 	
                 	Review review = new Review();
                 	review.setAppStoreId(appStoreId);
                 	review.setCountry(country);
                 	review.setReviewId(reviewId);                 	
                 	review.setAuthorId(authorId);
                 	review.setAuthorName(authorName);
                 	review.setTopic(topic);
                 	review.setText(reviewText);
                 	review.setVersion(version);
                 	review.setCreateDate(createDate);
                 	review.setRating(rating);
                 	
                 	reviews.add(review);
                }
            	
            } catch (ParserConfigurationException e) {
            	e.printStackTrace();
            } catch (SAXException e) {
            	e.printStackTrace();
            } catch (XPathExpressionException e) {
            	e.printStackTrace();
            }

            post.close();                    
        } catch (IOException e){
            e.printStackTrace();
        }
		
		return reviews;
	}
	
	public static void main(String[] args) {
		AppStoreCrawler crawler = new AppStoreCrawler();
		String appStoreId = AppStores.getAppStore("Korea");
		
		//System.out.println("html == " + crawler.getHTMLContent(appStoreId, "443904275", 0));
		
		//crawler.getReviewsForPage(appStoreId, "443904275", 0);

		//crawler.getReviews(appStoreId, "443904275", 3);		
		
		Set<String> appStores = new HashSet<String>();
		appStores.add(AppStores.getAppStore("Korea"));
		crawler.getReviews(appStores, "443904275", 3);
	}

}
