package com.sonya.sample.service.resources;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;

/**
 * The resource for the RESTful web service.
 *
 * @author YoungGue Bae
 */
@Path("/")
@Singleton
@Autowire
public class SampleResource {
    
    @GET
    @Path("/somethings")
    @Produces("text/html")
    public String getSomethings() {
            	
        return "Something List!";
    }

    @GET 
    @Path("/something")
    @Produces("text/html")
    public String getSomething() {
            	
        return "Something!";
    }
    
    @POST 
    @Produces("text/plain")
    public String addSomething() {
            	
        return "Added OK!";
    }
    
    @PUT 
    @Produces("text/plain")
    public String setSomething() {
            	
        return "Updated OK!";
    }
    
    @DELETE 
    @Produces("text/plain")
    public String deleteSomething() {
            	
        return "Deleted OK!";
    }
}
