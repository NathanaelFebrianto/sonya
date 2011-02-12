/**
 * Copyright (c) 2010, beeblz.com
 * All rights reserved.
 */
package com.beeblz.webapp.web;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet for facebook social graph.
 * 
 * @author YoungGue Bae
 */
public class FacebookSocialGraphServlet extends HttpServlet {
	
	private static final Logger log = LoggerFactory.getLogger(FacebookSocialGraphServlet.class.getName());
	
	public void init(FilterConfig fc) throws ServletException {
		log.debug("FacebookSocialGraphServlet init...........");		
	}

	@Override
	public void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		response.sendRedirect("http://www.beeblz.com/facebook/main.jsp");
	}
	
	@Override
	public void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	public void destroy() {
	}
}
