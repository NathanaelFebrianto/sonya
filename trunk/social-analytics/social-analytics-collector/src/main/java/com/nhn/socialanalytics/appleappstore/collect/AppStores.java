package com.nhn.socialanalytics.appleappstore.collect;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.CharArraySet;

public class AppStores {

	public static final Map<String, String> APP_STORES;
	
	static {
		final Map<String, String> appStores = new HashMap<String, String>();
		appStores.put("Argentina", "143505");
		appStores.put("Australia", "143460");
		appStores.put("Belgium", "143446");
		appStores.put("Brazil", "143503");
		appStores.put("Canada", "143455");
		appStores.put("Chile", "143483");
		appStores.put("China", "143465");
		appStores.put("Colombia", "143501");
		appStores.put("Costa Rica", "143495");
		appStores.put("Croatia", "143494");
		appStores.put("Czech Republic", "143489");
		appStores.put("Denmark", "143458");
		appStores.put("Deutschland", "143443");
		appStores.put("Dominican Republic", "143508");
		appStores.put("Ecuador", "143509");
		appStores.put("Egypt", "143516");
		appStores.put("El Salvador", "143506");
		appStores.put("Espana", "143454");
		appStores.put("Estonia", "143518");
		appStores.put("Finland", "143447");
		appStores.put("France", "143442");
		appStores.put("Greece", "143448");
		appStores.put("Guatemala", "143504");
		appStores.put("Honduras", "143510");
		appStores.put("Hong Kong", "143463");
		appStores.put("Hungary", "143482");
		appStores.put("India", "143467");
		appStores.put("Indonesia", "143476");
		appStores.put("Ireland", "143449");
		appStores.put("Israel", "143491");
		appStores.put("Italia", "143450");
		appStores.put("Jamaica", "143511");
		appStores.put("Japan", "143462");
		appStores.put("Kazakhstan", "143517");
		appStores.put("Korea", "143466");
		appStores.put("Kuwait", "143493");
		appStores.put("Latvia", "143519");
		appStores.put("Lebanon", "143497");
		appStores.put("Lithuania", "143520");
		appStores.put("Luxembourg", "143451");
		appStores.put("Macau", "143515");
		appStores.put("Malaysia", "143473");
		appStores.put("Malta", "143521");
		appStores.put("Mexico", "143468");
		appStores.put("Moldova", "143523");
		appStores.put("Nederland", "143452");
		appStores.put("New Zealand", "143461");
		appStores.put("Nicaragua", "143512");
		appStores.put("Norway", "143457");
		appStores.put("Osterreich", "143445");
		appStores.put("Pakistan", "143477");
		appStores.put("Panama", "143485");
		appStores.put("Paraguay", "143513");
		appStores.put("Peru", "143507");
		appStores.put("Phillipines", "143474");
		appStores.put("Poland", "143478");
		appStores.put("Portugal", "143453");
		appStores.put("Qatar", "143498");
		appStores.put("Romania", "143487");
		appStores.put("Russia", "143469");
		appStores.put("Saudi Arabia", "143479");
		appStores.put("Schweiz/Suisse", "143459");
		appStores.put("Singapore", "143464");
		appStores.put("Slovakia", "143496");
		appStores.put("Slovenia", "143499");
		appStores.put("South Africa", "143472");
		appStores.put("Sri Lanka", "143486");
		appStores.put("Sweden", "143456");
		appStores.put("Taiwan", "143470");
		appStores.put("Thailand", "143475");
		appStores.put("Turkey", "143480");
		appStores.put("United Arab Emirates", "143481");
		appStores.put("United Kingdom", "143444");
		appStores.put("United States", "143441");
		appStores.put("Uruguay", "143514");
		appStores.put("Venezuela", "143502");
		appStores.put("Vietnam", "143471");
		
		APP_STORES = appStores;
	}
	
	public static final String getAppStore(String country) {
		return APP_STORES.get(country);
	}
}
