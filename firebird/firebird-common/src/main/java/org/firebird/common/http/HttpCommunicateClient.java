/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.common.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.zip.GZIPInputStream;

/**
 * Http communicate client for the client-side component.
 * 
 * @author Young-Gue Bae
 */
public class HttpCommunicateClient {
    private static HttpCommunicateClient instance;
    private HttpMessenger messenger;
    private URL url;

	/**
	 * Creates the http communicate client.
	 */
	private HttpCommunicateClient(URL url) {
		this.url = url;
		messenger = new HttpMessenger(url);
	}

	/**
	 * Inializes the http communicate client.
	 *
	 * @param url the http communicate servlet url
	 */
	public static void init(URL url) {
		if (instance == null) {
		    instance = new HttpCommunicateClient(url);
		}
	}

	/**
	 * Clients call this method to get a reference to the single instance.
	 */
	public static HttpCommunicateClient getInstance() throws Exception {
		if (instance == null)
			throw new Exception("HttpCommunicateClient is not initialized.");

		return instance;
	}
	
	/**
	 * Sends a HttpCommunicate object to server and recieves a result
	 *
	 * @param comm the http communicate model
	 * @return Object the result object from the server
	 * @exception HttpCommunicateException
	 */
	public Object execute(HttpCommunicate comm) throws HttpCommunicateException {
		InputStream in = null;
		ObjectInputStream result = null;

		try {
			in = messenger.sendPostMessage(comm);
			result = new ObjectInputStream(new GZIPInputStream(in));

			Object obj = result.readObject();
			result.close();
			if(obj instanceof Exception) {
			    Exception e = (Exception)obj;
			    e.printStackTrace();
			    throw new HttpCommunicateException(e.getMessage());
			}				
			return obj;
		} catch(Exception e) {
		    e.printStackTrace();
		    throw new HttpCommunicateException(e.getMessage());
		} finally {
			if (in != null) {
				try {
					in.close();
				}catch(IOException e) {
					e.printStackTrace();
					throw new HttpCommunicateException(e.getMessage());
				}
			}
		}
	}
}
