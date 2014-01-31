package md.smartitinerary.rest.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import md.smartitinerary.rest.model.Itinerary;
import md.smartitinerary.util.Utilities;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.postgis.Point;

@Path("/itinerary")
public class ItineraryResource {
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
    
    /*
    @GET
    @Path("getItineraries/{categories}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Itinerary> getItineraries(
    		@PathParam("categories") List<String> categories) {
    	List<Itinerary> itineraries = Utilities.retrieveItineraries(categories);
		return itineraries;
    }
    */
    
    // Use data from the client source to retrieve itineraries
    @GET
    @Path("post")
    // @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public String getItineraries() {
        double range = 500.0;
        double maxLength = 1000.0;
        int k = 5;
        Point userLocation = new Point(-73.979135, 40.759195);
        List<String> categories = new ArrayList<>();
        categories.add("Home (private)");
        categories.add("Coworking Space");
        categories.add("Office");
        List<Itinerary> itineraries = Utilities.retrieveItineraries(categories, range, maxLength, userLocation, k);
        for (Itinerary i : itineraries) {
			System.out.println(i);
		}
        ObjectMapper mapper = new ObjectMapper();
        String json;
		try {
			json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(itineraries);
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
