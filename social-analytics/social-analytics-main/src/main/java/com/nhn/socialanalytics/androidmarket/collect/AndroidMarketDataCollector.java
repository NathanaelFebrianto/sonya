package com.nhn.socialanalytics.androidmarket.collect;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import com.gc.android.market.api.MarketSession;
import com.gc.android.market.api.MarketSession.Callback;
import com.gc.android.market.api.model.Market.App;
import com.gc.android.market.api.model.Market.AppsRequest;
import com.gc.android.market.api.model.Market.AppsResponse;
import com.gc.android.market.api.model.Market.Comment;
import com.gc.android.market.api.model.Market.CommentsRequest;
import com.gc.android.market.api.model.Market.CommentsResponse;
import com.gc.android.market.api.model.Market.ResponseContext;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.nhn.socialanalytics.common.Config;

public class AndroidMarketDataCollector {

	public AndroidMarketDataCollector() {
	}
	
	public void searchAppsPerPage(String query, int startIndex) {		
		
		MarketSession session = new MarketSession();
		session.login("louiezzang@gmail.com", "bae120809");
		//session.getContext().setAndroidId("3774d56d682e549c");
		//session.setLocale(Locale.KOREA);
		
		AppsRequest appsRequest = AppsRequest.newBuilder()
				.setQuery(query)
				.setStartIndex(startIndex)
				.setEntriesCount(10)
				//.setOrderType(AppsRequest.OrderType.NEWEST)
				.setOrderType(AppsRequest.OrderType.POPULAR)
				.setWithExtendedInfo(true)
				.build();
		
		session.append(appsRequest, new Callback<AppsResponse>() {
			//@Override
			public void onResult(ResponseContext context, AppsResponse response) {
				List<App> apps = response.getAppList();
				for (App app : apps) {
					System.out.println("---------------------------------");
					System.out.println("title == " + app.getTitle());
					System.out.println("creator == " + app.getCreator());
					System.out.println("rating == " + app.getRating());
					System.out.println("rating count == " + app.getRatingsCount());
					System.out.println("price == " + app.getPrice());
					System.out.println("id == " + app.getId());
					
					System.out.println("price currency == " + app.getPriceCurrency());
					System.out.println("price micros == " + app.getPriceMicros());
					System.out.println("serialized size == " + app.getSerializedSize());
					System.out.println("version == " + app.getVersion());
					System.out.println("download count == " + app.getExtendedInfo().getDownloadsCountText());
				}
			}
		});		
		session.flush();	
	}
	
	public void getAppCommentsPerPage(String appId, int startIndex) {
		System.out.println("\n\nstart index == " + startIndex);
		
		MarketSession session = new MarketSession();
		session.login("louiezzang@gmail.com", "bae120809");

		CommentsRequest commentsRequest = CommentsRequest.newBuilder()
				.setAppId(appId)
				.setStartIndex(startIndex)
				.setEntriesCount(10).build();
		
		session.append(commentsRequest, new Callback<CommentsResponse>() {			
			//@Override
			public void onResult(ResponseContext context, CommentsResponse response) {
				//System.out.println("Response : " + response);
				File outputDir = new File(Config.getProperty("ANDROIDMARKET_SOURCE_DATA_DIR"));
				File file = new File(outputDir.getPath() + File.separator + "android" + ".txt");
				
				try {
					BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getPath()), "UTF-8"));
					br.write("creation_time	author_id	author_name	rating	text");
					br.newLine();

					List<Comment> comments = response.getCommentsList();					
					for (Comment comment : comments) {	
						System.out.println("---------------------------------");
						System.out.println("author id == " + comment.getAuthorId());
						System.out.println("author name == " + comment.getAuthorName());
						System.out.println("rating == " + comment.getRating());
						System.out.println("text == " + comment.getText());
						System.out.println("creation time == " + comment.getCreationTime());
						
						/*
						Map<FieldDescriptor, Object> fields = comment.getAllFields();
						for (Map.Entry<FieldDescriptor, Object> entry : fields.entrySet()) {
							System.out.println("key == " + entry.getKey());
							System.out.println("value == " + entry.getValue());
						}
						*/
						br.write(
								comment.getCreationTime() + "\t" +
								comment.getAuthorId() + "\t" +
								comment.getAuthorName() + "\t" + 
								comment.getRating() + "\t" +
								comment.getText()
								);
						br.newLine();					
					}					
					br.close();
				} catch (Exception e) {
					e.printStackTrace();					
				}
			}
		});

		session.flush();		
	}	
	
	public void searchApps(String appId, int maxPage) {
		int startIndex = 0;
		for (int page = 0; page < maxPage; page++) {
			startIndex = 10 * page;
			searchAppsPerPage(appId, startIndex);			
		}		
	}
	
	public void getAppComments(String appId, int maxPage) {
		int startIndex = 0;
		for (int page = 0; page < maxPage; page++) {
			startIndex = 10 * page;
			getAppCommentsPerPage(appId, startIndex);			
		}		
	}
	
	

	public static void main(String[] args) {
		AndroidMarketDataCollector collector = new AndroidMarketDataCollector();	
		
		//String query = "네이버톡";
		//String query = "pname:com.nhn.android.navertalk"; //pname:com.nhn.android.navertalk
		//collector.searchApps(query, 1);
		
		//String appId = "com.nhn.android.navertalk";
		//String appId = "com.nhn.android.search";
		//String appId = "com.nhn.android.nbooks";
		String appId = "com.kakao.talk";
		collector.getAppComments(appId, 5);
	}

}
