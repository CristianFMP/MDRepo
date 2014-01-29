package md.smartitinerary.rest.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

import md.smartitinerary.rest.model.Itinerary;
import md.smartitinerary.util.Utilities;

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
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Itinerary> itineraries(
            MultivaluedMap<String, String> itineraryParams
            ) {
        double range = 500.0;
        double maxLength = 1000.0;
        int k = 10;
        Point userLocation = new Point(-73.979135, 40.759195);
        Set<String> macrocats = itineraryParams.keySet();
        List<String> categories = new ArrayList<>();
        for (String s : macrocats) {
        	categories.addAll(itineraryParams.get(s));
        }
        List<Itinerary> itineraries = Utilities.retrieveItineraries(categories, range, maxLength, userLocation, k);
        return itineraries;                         
    }
}
