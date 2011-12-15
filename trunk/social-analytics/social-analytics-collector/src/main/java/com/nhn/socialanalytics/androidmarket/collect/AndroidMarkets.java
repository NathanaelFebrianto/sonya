package com.nhn.socialanalytics.androidmarket.collect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class AndroidMarkets {

	public static final Map<String, Locale> ANDROID_MARKETS;	
	
	static {
		final Map<String, Locale> androidMarkets = new HashMap<String, Locale>();
		/*
		Locale[] locales = Locale.getAvailableLocales();
		
		for (int i = 0; i < locales.length; i++) {
			Locale locale = locales[i];
			
			androidMarkets.put(locale.getCountry(), locale);
		}
		*/
		
		androidMarkets.put("Korea", Locale.KOREA);
		androidMarkets.put("Japan", Locale.JAPAN);
		androidMarkets.put("Taiwan", Locale.TAIWAN);
		androidMarkets.put("Us", Locale.US);
		androidMarkets.put("China", Locale.CHINA);
		
		ANDROID_MARKETS = androidMarkets;
	}
	
	public static final Set<Locale> getAllAndroidMarkets() {
		Set<Locale> markets = new HashSet<Locale>();
		
		for (Map.Entry<String, Locale> entry : ANDROID_MARKETS.entrySet()) {
			markets.add(entry.getValue());
		}
		
		return markets;
	}
	
	public static final Locale getLocale(String country) {
		return ANDROID_MARKETS.get(country);
	}
	
	public static final String getCountry(Locale locale) {
		for (Map.Entry<String, Locale> entry : ANDROID_MARKETS.entrySet()) {
			if (locale.equals(entry.getValue())) {
				return (String) entry.getKey();
			}
		}
		return "";
	}
}
