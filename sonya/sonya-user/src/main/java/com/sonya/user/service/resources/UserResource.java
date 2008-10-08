package com.sonya.user.service.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.ProduceMime;

import com.sonya.user.model.User;
import com.sonya.user.service.UserManager;
import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.inject.Inject;
import com.sun.jersey.spi.resource.Singleton;

/**
 * The Java class will be hosted at the URI path "/user".
 *
 * @author YoungGue Bae
 */
@Path("/user")
@Singleton
@Autowire
public class UserResource {
	@Inject
	private UserManager userManager;	
    
    @GET 
    @ProduceMime("text/html")
    public String getClichedMessage() {
        // return some cliched textual content
    	List users = userManager.getUsers(null);
    	
    	StringBuffer html = new StringBuffer();
    	html.append("<table border='1'>");
    	for (int i = 0 ; i < users.size(); i++) {
    		User user = (User)users.get(i);
    		html.append("<tr>");
    		html.append("<td>")
    			.append(user.getFirstName() + " " + user.getLastName())
    			.append("</td>")
    			.append("<td>")
    			.append(user.getEmail())
    			.append("</td>")
    			.append("</tr>");
    	}
    	html.append("</table>");
    	
        return html.toString();
    }
}
