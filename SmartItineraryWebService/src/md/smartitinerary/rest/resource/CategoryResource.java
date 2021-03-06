package md.smartitinerary.rest.resource;

import java.io.IOException;
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

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

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
    public String getCategories() {
    	List<Category> categories = Utilities.retrieveCategories();
    	ObjectMapper mapper = new ObjectMapper();
    	String json;
		try {
			json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(categories);
			return json;
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			return "Qualcosa non ha funzionato!";
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return "Qualcosa non ha funzionato!";
		} catch (IOException e) {
			e.printStackTrace();
			return "Qualcosa non ha funzionato!";
		}
    }
}
