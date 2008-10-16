package com.sonya.sample.service;

import com.sonya.sample.model.Person;
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.inject.Inject;
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

    @Inject
    private PersonManager personManager;

    @GET
    @Path("/somethings")
    @Produces("text/html")
    public String getSomethings() {
        List list = personManager.findByLastName("Bae");
        StringBuffer html = new StringBuffer();

        html.append("<table border='1'>");
        for (int i = 0; i < list.size(); i++) {
            Person person = (Person) list.get(i);
            html.append("<tr>");
            html.append("<td>")
                .append(person.getId())
                .append("</td>")
                .append("<td>")
                .append(person.getFirstName() + " " + person.getLastName())
                .append("</td>")
                .append("</tr>");
        }
        html.append("</table>");

        return html.toString();
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
