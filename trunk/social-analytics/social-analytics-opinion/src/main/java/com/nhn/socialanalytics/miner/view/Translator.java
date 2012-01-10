package com.nhn.socialanalytics.miner.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Translator {

	public static String translate(String data) {
		StringBuffer sb = new StringBuffer();
		
		try {
			// send data
			URL url = new URL("http://xns38.exp.nhnsystem.com:25000/nsmt/");
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			
			//((HttpURLConnection) conn).setFollowRedirects(false);
			((HttpURLConnection) conn).setRequestMethod("POST");
			
			conn.setRequestProperty("Content-Type", "multipart/form-data");

			conn.connect();
			
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			out.write(data);
			out.flush();
			
			// get the response
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				sb.append(line).append(" ");
			}
			
			out.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sb.toString().trim();
	}
}
