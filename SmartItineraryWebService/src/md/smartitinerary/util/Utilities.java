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
	public static List<Itinerary> retrieveItineraries(List<String> categories) {
		Connection connection = getConnection();
		ResultSet result;
		List<String> categoryList = categories;
		List<Itinerary> itineraryList = new ArrayList<>();
		// List<Category> catRel = retrieveCategories();
		try {
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			// First part of the query
			String query = "SELECT ST_AsText(geom) AS linestring, count(*) AS popularity, ST_Length(ST_Transform(geom, 26915)) AS length, ST_NPoints(geom) AS n_vertices" +
					"FROM (SELECT \"userID\", ST_MakeLine(location::geometry ORDER BY time) As geom, DATE(time) " +
						"FROM (SELECT Ckins.\"userID\", Pois.location, time " + 
							"FROM \"POIs\".\"Checkins4sqManhattan\" AS Ckins LEFT JOIN \"POIs\".\"POIsManhattan\" AS Pois ON Ckins.\"4sqExtended\" = Pois.\"4sqExtended\" " + 
							"WHERE ";
			boolean door = true;
			// Concatenating the categories for the POIs research
			for (String c : categoryList) {
				if (door) {
					query.concat("Pois.categoria LIKE '%" + c + "%'");
					door = false;
				} else
					query.concat(" OR Pois.categoria LIKE '%" + c + "%'");
			}
			// Second part of the query
			query.concat(") AS CatSel" + 
						"GROUP BY \"userID\", DATE(time) AS groups " +
					"GROUP BY geom " + 
					"HAVING ST_Length(geom) > 0 AND ST_NPoints(geom) > 1 " +
					"ORDER BY count(*) DESC " +
					"LIMIT 10;");
			result = statement.executeQuery(query);
			System.out.println("\nExecuted query: " + query);
			while (result.next()) {
				// Navigate query result
				LineString linestring = new LineString(result.getString("linestring"));
				Point[] points = linestring.getPoints();
				List<Poi> pois = new ArrayList<>();
				for (int i = 0; i < points.length; i++) {
					// TODO: add query to get id and name of POIs
					pois.add(new Poi(points[i], "id", "name"));
				}
				itineraryList.add(new Itinerary(linestring, pois, result.getInt("popularity"), result.getDouble("length")));
			}
			return itineraryList;
		} catch (SQLException e) {
			System.out.println("\nQuery error!");
			e.printStackTrace();
			return null;
		}
	}
}
