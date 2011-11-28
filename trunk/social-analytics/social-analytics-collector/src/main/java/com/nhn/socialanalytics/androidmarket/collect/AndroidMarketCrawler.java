package com.nhn.socialanalytics.androidmarket.collect;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.gc.android.market.api.MarketSession;
import com.gc.android.market.api.MarketSession.Callback;
import com.gc.android.market.api.model.Market.App;
import com.gc.android.market.api.model.Market.AppsRequest;
import com.gc.android.market.api.model.Market.AppsResponse;
import com.gc.android.market.api.model.Market.Comment;
import com.gc.android.market.api.model.Market.CommentsRequest;
import com.gc.android.market.api.model.Market.CommentsResponse;
import com.gc.android.market.api.model.Market.ResponseContext;

public class AndroidMarketCrawler {

	private MarketSession session;
	private List<Comment> commentList = new ArrayList<Comment>();
	
	public AndroidMarketCrawler(String loginAccount, String loginPasswd) {
		session = new MarketSession();
		session.login(loginAccount, loginPasswd);
		//session.getContext().setAndroidId("3774d56d682e549c");
	}
	
	public void searchAppsPerPage(Locale locale, String query, int startIndex) {
		AppsRequest appsRequest = AppsRequest.newBuilder()
				.setQuery(query)
				.setStartIndex(startIndex)
				.setEntriesCount(10)
				//.setOrderType(AppsRequest.OrderType.NEWEST)
				.setOrderType(AppsRequest.OrderType.POPULAR)
				.setWithExtendedInfo(true)
				.build();
		
		session.setLocale(locale);
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
	
	public List<Comment> getAppCommentsPerPage(Locale locale, String appId, int startIndex) {
		System.out.println("start index == " + startIndex);
		
		CommentsRequest commentsRequest = CommentsRequest.newBuilder()
				.setAppId(appId)
				.setStartIndex(startIndex)
				.setEntriesCount(10).build();
		
		session.setLocale(locale);
		session.append(commentsRequest, new Callback<CommentsResponse>() {			
			//@Override
			public void onResult(ResponseContext context, CommentsResponse response) {
				//System.out.println("Response : " + response);	
				List<Comment> comments = response.getCommentsList();
				
				commentList.addAll(comments);
			}
		});

		session.flush();
		
		return commentList;
	}
	
	public void searchApps(Locale locale, String appId, int maxPage) {
		int startIndex = 0;
		for (int page = 0; page < maxPage; page++) {
			startIndex = 10 * page;
			searchAppsPerPage(locale, appId, startIndex);			
		}		
	}
	
	public List<Comment> getAppComments(Locale locale, String appId, int maxPage, boolean clearList) {
		if (clearList)
			commentList.clear();
		
		int startIndex = 0;
		for (int page = 0; page < maxPage; page++) {
			startIndex = 10 * page;
			commentList = getAppCommentsPerPage(locale, appId, startIndex);			
		}
		
		return commentList;
	}
}
