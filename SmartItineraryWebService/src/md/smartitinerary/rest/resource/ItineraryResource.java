package md.smartitinerary.rest.resource;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
    
    // Use data from the client source to retrieve itineraries
    @GET
    @Path("/getItineraries/{kposition}/{klength}/{krange}/{kcategories}")
    // @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public String getItineraries(
    		@PathParam("kposition") String kposition,
    		@PathParam("klength") String klength,
    		@PathParam("krange") String krange,
    		@PathParam("kcategories") String kcategories) {
    	System.out.println("Parametri: pos. "+kposition+", lunghezza "+klength+", raggio "+krange);
        double range = Double.parseDouble(krange);
        double maxLength = Double.parseDouble(klength)*1000;
        int k = 5;
        String[] pos = kposition.split(",");
        double lat = Double.parseDouble(pos[0]);
        double lng = Double.parseDouble(pos[1]);
        String[] categoryArr = kcategories.split(",");  
        for (int i = 0; i < categoryArr.length; i++) {
        	categoryArr[i] = categoryArr[i].replace(".", " ");
        }
        List<String> categories = Arrays.asList(categoryArr);
        Point userLocation = new Point(lng, lat);
		List<Itinerary> itineraries = Utilities.retrieveItineraries(categories, range, maxLength, userLocation, k);
		for (Itinerary i : itineraries) {
			System.out.println(i);
		}
		try {		
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(itineraries);
			return json;
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			return "JSON mapper non ha funzionato!";
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return "JSON mapper non ha funzionato!";
		} catch (IOException e) {
			e.printStackTrace();
			return "Qualcosa non ha funzionato!";
		}                         
    }
}
