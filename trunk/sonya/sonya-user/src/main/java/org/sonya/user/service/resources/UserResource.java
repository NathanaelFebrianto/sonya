package org.sonya.user.service.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.ProduceMime;

import org.sonya.user.model.User;
import org.sonya.user.service.UserManager;

/**
 * The Java class will be hosted at the URI path "/user".
 *
 * @author YoungGue Bae
 */
@Path("/user")
public class UserResource {
    private UserManager userManager;	
    
    public void setUserManager(UserManager userManager) {
    	this.userManager = userManager;
    }
    
	// The Java method will process HTTP GET requests
    @GET 
    // The Java method will produce content identified by the MIME Media
    // type "text/plain"
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
