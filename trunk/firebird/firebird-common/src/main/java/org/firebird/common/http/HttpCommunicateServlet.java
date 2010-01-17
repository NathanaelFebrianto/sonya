/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.common.http;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Http communicate servlet.
 * 
 * @author Young-Gue Bae
 */
public class HttpCommunicateServlet extends HttpServlet {

	private static final long serialVersionUID = -3289214676708380269L;
    private HttpCommunicateExecutor executor;	
    
    public void init() throws ServletException {
		try {
		    ServletContext ctx = getServletContext();
			HttpCommunicateExecutor.init(ctx);
			executor = HttpCommunicateExecutor.getInstance();
		} catch(Exception e) {
			throw new UnavailableException(e.getMessage());
		}
	}

	public void doGet(HttpServletRequest req,  HttpServletResponse res) throws IOException, ServletException {
	}

	public void doPost(HttpServletRequest req,  HttpServletResponse res) throws IOException, ServletException {
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		try {
			// read object from InputStream
			in = new ObjectInputStream(new GZIPInputStream(req.getInputStream()));
			HttpCommunicate comm = (HttpCommunicate)in.readObject();

			// make OutputStream
			out = new ObjectOutputStream(new GZIPOutputStream(res.getOutputStream()));

			// for security, checks if user already have a session
			HttpSession session = req.getSession();
			
			Object result = executor.execute(comm);
			out.writeObject(result);
			out.close();
		} catch(Exception e) {
			e.printStackTrace();
			out.writeObject(e);
			out.close();
		}
	}
}
