package com.sonya.webapp.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sonya.user.model.User;
import com.sonya.user.service.UserManager;

/**
 * This class is used to filter all requests to the <code>Action</code>
 * servlet and detect if a user is authenticated.
 * 
 * @author  YoungGue Bae
 */
public class ActionFilter implements Filter {
    private final transient Log log = LogFactory.getLog(ActionFilter.class);
    private FilterConfig config = null;

    public void init(FilterConfig config) throws ServletException {
        this.config = config;
    }

    public void destroy() {
        config = null;
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
    	throws IOException, ServletException {  
    	
    	// cast to the types I want to use
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession(true);
        
        String username = request.getRemoteUser();
        
        User user = (User) session.getAttribute("loginUser");        
  
        if (username != null && user == null) { 
            ApplicationContext ctx =
                WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
            
            UserManager userManager = (UserManager) ctx.getBean("userManager");
            user = userManager.getUserByUsername(username);  
            
            if (user != null) {
            	session.setAttribute("loginUser", user);
            	log.debug("loginUser == " + user.getUsername());            	
            }            
        }
        
        chain.doFilter(req, resp);       	
    }
}
