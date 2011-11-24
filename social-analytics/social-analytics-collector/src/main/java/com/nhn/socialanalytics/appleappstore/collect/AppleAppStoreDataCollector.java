package com.nhn.socialanalytics.appleappstore.collect;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

public class AppleAppStoreDataCollector {

	public AppleAppStoreDataCollector() {
		
	}
	
	public List<String> getReviews(String appStoreId, String appId) {
		List<String> reviews = new ArrayList<String>();
		
		int i = 0;
		while (true) {
			String review = this.getReviewsForPage(appStoreId, appId, i);
			if (review == null || review.length() == 0)
				break;
			
			reviews.add(review);
			i++;
		}
		
		return reviews;
	}
	
	private String getReviewsForPage(String appStoreId, String appId, int pageNo) {
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

            request.setFollowRedirects(true);
            request.setInstanceFollowRedirects(true);

            request.setRequestProperty("X-Apple-Store-Front", front);
            request.setRequestProperty("User-Agent", userAgent);
            
            request.setRequestMethod("POST");
            OutputStreamWriter post = new OutputStreamWriter(request.getOutputStream());
            //post.write(data);
            post.flush();             
           
            /*
            BufferedReader in = new BufferedReader(new InputStreamReader(isrequest.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content += inputLine;
            }
            in.close();            
            System.out.println("content == " + content);
            */            

            SAXBuilder builder = new SAXBuilder();
            Document doc = null;
            
            try {
            	
            	doc = builder.build(request.getInputStream());
            	String namespace = "http://www.apple.com/itms/";
            	System.out.println("document == " + doc);
                XPath path = XPath.newInstance("//" + namespace + ":View/ScrollView/VBoxView");
                List nodes = path.selectNodes(doc);
                
                if (nodes.size() == 0) {
                    System.out.println("xxx");
                } else {
                    Iterator it = nodes.iterator();
                    while (it.hasNext()) {
                    	System.out.println(it.next());
                    }
                }
 
            } catch (JDOMException e) {
            	e.printStackTrace();
            }
            
            post.close();            
                    
        } catch (IOException e){
            e.printStackTrace();
        }
		
		return "";
	}
	
	public static void main(String[] args) {
		AppleAppStoreDataCollector collector = new AppleAppStoreDataCollector();
		String appStoreId = AppStores.getAppStore("Korea");
		collector.getReviewsForPage(appStoreId, "443904275", 10);

	}

}
