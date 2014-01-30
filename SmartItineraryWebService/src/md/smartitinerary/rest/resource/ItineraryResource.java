package md.smartitinerary.rest.resource;

import java.sql.SQLException;
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

import org.postgis.LineString;
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
    @Path("/post")
    // @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Itinerary> itineraries() {
        double range = 500.0;
        double maxLength = 1000.0;
        int k = 10;
        Point userLocation = new Point(-73.979135, 40.759195);
        try {
			LineString line = new LineString("0102000020E610000002000000C828CFBC1C7E52C031854178B8604440725CCBE11A7E52C023B679ACBB604440");
			System.out.println(line.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(userLocation.toString());
        List<String> categories = new ArrayList<>();
        categories.add("Home (private)");
        categories.add("Coworking Space");
        categories.add("Office");
        List<Itinerary> itineraries = Utilities.retrieveItineraries(categories, range, maxLength, userLocation, k);
        return itineraries;                         
    }
}
