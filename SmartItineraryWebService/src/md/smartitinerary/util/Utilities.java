package md.smartitinerary.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import md.smartitinerary.rest.model.Category;
import md.smartitinerary.rest.model.Itinerary;
import md.smartitinerary.rest.model.Poi;

import org.postgis.LineString;
import org.postgis.Point;

public class Utilities {
	/** Returns a connection to the database */
	public static Connection getConnection() {
		try { 
			Class.forName("org.postgresql.Driver"); 
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your PostgreSQL JDBC Driver? Include in your library path!");
			e.printStackTrace();
			return null; 
		}				
		Connection connection = null; 
		try { 
			connection = DriverManager.getConnection(					
					"jdbc:postgresql://localhost:5432/progetto_md", "postgres",	"password"); 
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return null; 
		}
		
		if (connection != null) {
			System.out.print("Connected to DB\n");	
		} else {
			System.out.println("Failed to make connection!");
		}

		return connection;
	}
	
	/** Returns a list of categories */
	// in questo modo ritrovo sia macrocategorie che categorie insieme. come faccio a comunicare al client solo macrocategorie?
	// devo implementare un altro metodo (cioe altro modello e risorsa)?
	
	public static List<Category> retrieveCategories() {
		Connection connection = getConnection();
		ResultSet result;
		
		List<Category> categoryList = new ArrayList<>();
		
		try {
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String query = "SELECT * " + 
					"FROM \"POIs\".\"MacroCategorie\"";
			System.out.println("\nExecuted query: " + query);
			result = statement.executeQuery(query);
			// Put the categories into a List
			while (result.next()) {
				String category = result.getString("nomeMC");
				String query_subcat = "SELECT categoria " +
						"FROM \"POIs\".\"RelCatMacroCat\" " +
						"WHERE macro_cat = '" + category + "'";
				Statement statement_subquery = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				ResultSet result_subquery = statement_subquery.executeQuery(query_subcat);
				System.out.println("\nExecuted query: " + query_subcat);
				List<String> subCategories = new ArrayList<>();
				while (result_subquery.next()) {
					subCategories.add(result_subquery.getString("categoria"));
				}
				categoryList.add(new Category(category, subCategories));
			}
			return categoryList;
		} catch (SQLException sqle) {
			System.out.println("\nQuery error!");
			sqle.printStackTrace();
			return null;
		}	
	}

	/** Returns a list of itineraries*/
	public static List<Itinerary> retrieveItineraries(List<String> categories, double range, double maxLength, Point userLocation, int k) {
		Connection connection = getConnection();
		ResultSet result;
		List<String> categoryList = categories;
		List<Itinerary> itineraryList = new ArrayList<>();
		// List<Category> catRel = retrieveCategories();
		try {
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			// First part of the query
			String query = "SELECT itinerari.\"pois\" AS poi_list, geom::geometry AS linestring, itinerari.\"st_length\" AS length, itinerari.\"count\" AS popularity " +
					"FROM (SELECT POIs, count(*), geom::geography, ST_Length(geom::geography, false), ST_NPoints(geom) " +
						"FROM (SELECT \"userID\", ST_MakeLine(location::geometry) AS geom, string_agg(\"4sqExtended\", ',') AS POIs, DATE(time) " + 
							"FROM (SELECT Ckins.\"userID\", Pois.location, Pois.\"4sqExtended\", time " + 
								"FROM \"POIs\".\"Checkins4sqManhattan\" AS Ckins, \"POIs\".\"POIsManhattan\" AS Pois, \"POIs\".\"RelPOIManCategorie\" AS Cats " +
								"WHERE (Ckins.\"4sqExtended\" = Pois.\"4sqExtended\" AND Pois.\"4sqExtended\" = poi) AND (";
			boolean door = true;
			// Concatenating the categories for the POIs research
			for (String c : categoryList) {
				if (door) {
					query = query.concat("Cats.categoria = '" + c + "'");
					door = false;
				} else
					query = query.concat(" OR Cats.categoria = '" + c + "'");
			}
			// Second part of the query
			query = query.concat(") ORDER BY time) AS CatSel " + 
						"GROUP BY \"userID\", DATE(time)) AS groups " +
					"GROUP BY POIs, geom " + 
					"HAVING ST_Length(geom::geography, false) > 0 AND ST_Length(geom::geography, false) < " + maxLength + " AND ST_NPoints(geom) > 1 " +
					"ORDER BY count(*) DESC " +
					"LIMIT 1000) AS itinerari, " +
					"(SELECT Pois.\"4sqExtended\", count(*) AS popolarita, ST_Distance(Geography(ST_SetSRID(ST_Point(" + userLocation.getX() + ", " + userLocation.getY() + "), 4326)), Pois.location) AS distance " +
					"FROM \"POIs\".\"Checkins4sqManhattan\" AS Ckins INNER JOIN \"POIs\".\"POIsManhattan\" AS Pois ON Ckins.\"4sqExtended\" = Pois.\"4sqExtended\" " +
					"WHERE ST_DWithin(Pois.location, Geography(ST_SetSRID(ST_Point(" + userLocation.getX() + ", " + userLocation.getY() + "), 4326)), " + range + ") " + 
					"GROUP BY Pois.\"4sqExtended\" " +
					"ORDER BY count(*) DESC) AS rangeQuery " +
				"WHERE itinerari.\"pois\" ILIKE CONCAT('%', rangeQuery.\"4sqExtended\", '%') " +
				"GROUP BY poi_list, geom, length, popularity " +
				"ORDER BY popularity DESC " +
				"LIMIT " + k);
			System.out.println("\nExecuted query: " + query);
			result = statement.executeQuery(query);
			System.out.println("Itinerari trovati:");
			while (result.next()) {
				// Navigate query result
				String[] poiList = result.getString("poi_list").split(",");
				String s = result.getString("linestring");
				LineString linestring = new LineString(s);
				Point[] points = linestring.getPoints();
				List<Poi> pois = new ArrayList<>();
				for (int i = 0; i < points.length; i++) {
					Statement statement_subquery = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
					String query_poi_details = "SELECT Pois.\"nome\", Pois.\"indirizzo\", Pois.\"categoria\", count(*) AS num_ckins " +
							"FROM \"POIs\".\"Checkins4sqManhattan\" AS Ckins INNER JOIN \"POIs\".\"POIsManhattan\" AS Pois ON Ckins.\"4sqExtended\" = Pois.\"4sqExtended\"" +
							"WHERE Pois.\"4sqExtended\"='" + poiList[i] + "' " +
							"ORDER BY Ckins.\"time\" DESC";
					ResultSet result_subquery = statement_subquery.executeQuery(query_poi_details);
					if (result_subquery.first()) {
						pois.add(new Poi(points[i], poiList[i], result_subquery.getString("name"), result_subquery.getString("indirizzo"), Integer.parseInt(result_subquery.getString("num_ckins"))));
					}
				}
				itineraryList.add(new Itinerary(linestring, pois, result.getInt("popularity"), result.getDouble("length")));
				System.out.println("");
			}
			return itineraryList;
		} catch (SQLException e) {
			System.out.println("\nQuery error!");
			e.printStackTrace();
			return null;
		}
	}
}
