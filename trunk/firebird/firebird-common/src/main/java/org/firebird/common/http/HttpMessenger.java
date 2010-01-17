/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.common.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.zip.GZIPOutputStream;

/**
 * Http communicate messenger.
 * 
 * @author Young-Gue Bae
 */
public class HttpMessenger {
	private URL servlet = null;
	private Hashtable headers = null;    

	/**
	 * Constructs a new HttpMessagenger that can be used to communicate with the
	 * servlet at the specified URL.
	 *
	 * @param servlet the server resource (typically a servlet) with which to communicate
	 */
	public HttpMessenger(URL servlet) {
		this.servlet = servlet;
	}

	/**
	 * Performs a GET request to the servlet, with no query string.
	 *
	 * @return InputStream the InputStream to read the response
	 * @exception IOException if an I/O error occurs
	 */
	public InputStream sendGetMessage() throws IOException {
		return sendGetMessage(null);
	}

	/**
	 * Performs a GET request to the servlet, building
	 * a query string from the supplied properties list.
	 *
	 * @param args the properties list from which to build a query string
	 * @return InputStream 	the InputStream to read the response
	 * @exception IOException if an I/O error occurs
	 */
	public InputStream sendGetMessage(Properties args) throws IOException {
		String argString = "";	// default
		if (args != null)
			argString = "?" + toEncodedString(args);
	  	URL url = new URL(servlet.toExternalForm() + argString);

		// turn off caching
		URLConnection con = url.openConnection();
		con.setUseCaches(false);

		// send headers
		sendHeaders(con);
		return con.getInputStream();
	}

	/**
	 * Performs a POST request to the servlet, with no query string.
	 *
	 * @return InputStream InputStream to read the response
	 * @exception IOException if an I/O error occurs
	 */
	public InputStream sendPostMessage() throws IOException {
		return sendPostMessage(null);
	}

	/**
	 * Performs a POST request to the servlet, building
	 * post data from the supplied properties list.
	 *
	 * @param args the properties list from which to build the post data
	 * @return InputStream the InputStream to read the response
	 * @exception IOException if an I/O error occurs
	 */
	public InputStream sendPostMessage(Properties args) throws IOException {
		String argString = "";  // default
		if (args != null)
			argString = toEncodedString(args);  // notice no "?"

		URLConnection con = servlet.openConnection();

		// prepare for both input and output
		con.setDoInput(true);
		con.setDoOutput(true);

		// turn off caching
		con.setUseCaches(false);

		// work around a Netscape bug
		con.setRequestProperty("Content-Type",
		                       "application/x-www-form-urlencoded");

		// send headers
		sendHeaders(con);

		// write the arguments as post data
		PrintWriter out = new PrintWriter(con.getOutputStream());
		out.print(argString);
		out.flush();
		out.close();

		return con.getInputStream();
	}

	/**
	 * Performs a POST request to the servlet, uploading a serialized object.
	 * <p>
	 * The servlet can receive the object in its <tt>doPost()</tt> method
	 * like this:
	 * <pre>
	 *     ObjectInputStream objin =
	 *       new ObjectInputStream(new GZIPInputStream(req.getInputStream()));
	 *     Object obj = objin.readObject();
	 * </pre>
	 * The type of the uploaded object can be retrieved as the subtype of the
	 * content type (<tt>java-internal/<i>classname</i></tt>).
	 *
	 * @param obj the serializable object to upload
	 * @return InputStream the InputStream to read the response
	 * @exception IOException if an I/O error occurs
	 */
	public InputStream sendPostMessage(Serializable obj) throws IOException {
		URLConnection con = servlet.openConnection();

		// prepare for both input and output
		con.setDoInput(true);
		con.setDoOutput(true);

		// turn off caching
		con.setUseCaches(false);

		// set the content type to be java-internal/classname
		con.setRequestProperty("Content-Type",
		                       "java-internal/" + obj.getClass().getName());

		// send headers
		sendHeaders(con);

		// write the serialized object as post data
		ObjectOutputStream out = new ObjectOutputStream(new GZIPOutputStream(con.getOutputStream()));
		out.writeObject(obj);
		out.flush();
		out.close();

		return con.getInputStream();
	}

	/**
	 * Sets a request header with the given name and value.  The header
	 * persists across multiple requests.  The caller is responsible for
	 * ensuring there are no illegal characters in the name and value.
	 *
	 * @param String the header name
	 * @param String the header value
	 */
	public void setHeader(String name, String value) {
		if (headers == null)
			headers = new Hashtable();
		headers.put(name, value);
	}

	/**
	 * Send the contents of the headers hashtable to the server.
	 */
	private void sendHeaders(URLConnection con) {
		if (headers != null) {
			Enumeration e = headers.keys();
			while (e.hasMoreElements()) {
				String name = (String)e.nextElement();
			 	String value = (String)headers.get(name);
				con.setRequestProperty(name, value);
			}
		}
	}

	/**
	 * Sets a request cookie with the given name and value.  The cookie
	 * persists across multiple requests.  The caller is responsible for
	 * ensuring there are no illegal characters in the name and value.
	 *
	 * @param String the header name
	 * @param String the header value
	 */
	public void setCookie(String name, String value) {
		String existingCookies = (String)headers.get("Cookie");
		if (existingCookies == null)
			setHeader("Cookie", name + "=" + value);
		else
			setHeader("Cookie", existingCookies + "; " + name + "=" + value);
	}

	/**
	 * Converts a properties list to a URL-encoded query string.
	 */
	private String toEncodedString(Properties args) {
		StringBuffer buf = new StringBuffer();
		Enumeration names = args.propertyNames();
		while (names.hasMoreElements()) {
			String name = (String)names.nextElement();
			String value = args.getProperty(name);
			buf.append(URLEncoder.encode(name) + "=" + URLEncoder.encode(value));
			if (names.hasMoreElements())
				buf.append("&");
		}
		return buf.toString();
	}
}