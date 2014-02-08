package md.smartitinerary.rest.resource;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import md.smartitinerary.rest.model.Comment;
import md.smartitinerary.util.Utilities;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

@Path("/comment")
public class CommentResource {
	private final static String ID = "Param";
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
    
    @POST
    @Path("getComments")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public String getComments(MultivaluedMap<String, String> param) {
    	System.out.println("id poi: "+ param.getFirst(ID));
    	List<Comment> comments = Utilities.retrieveComments(param.getFirst(ID));
    	ObjectMapper mapper = new ObjectMapper();
    	String json;
		try {
			json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(comments);
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
