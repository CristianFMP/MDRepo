package md.smartitinerary.rest.resource;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import md.smartitinerary.rest.model.Category;
import md.smartitinerary.util.Utilities;

@Path("/category")
public class CategoryResource {
	// The @Context annotation allows us to have certain contextual objects
    // injected into this class.
    @Context
    UriInfo uriInfo;
    
    // Another "injected" object. This allows us to use the information that's
    // part of any incoming request.
    // We could, for example, get header information, or the requestor's address.
    @Context
    HttpServletRequest req;
    
    // Basic "is the service running" test
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String respondAsReady() {
        return "Demo service is ready! Request method is " + req.getMethod();
    }
    
    @GET
    @Path("getCategories")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Category> getCategories() {
    	List<Category> categories = Utilities.retrieveCategories();
		return categories;
    }
}
