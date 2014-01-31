package md.smartitineraryclient.database;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ItinerariesDataSource {

	// Database fields
	private SQLiteDatabase database;
	private SISQLiteHelper dbHelper;
	private String[] allColumns = { SISQLiteHelper.ITIN_COLUMN_ID,
		  SISQLiteHelper.ITIN_COLUMN_NICKNAME_UTENTE,
		  SISQLiteHelper.ITIN_COLUMN_ELENCO_POI,
		  SISQLiteHelper.ITIN_COLUMN_POPOLARITA,
		  SISQLiteHelper.ITIN_COLUMN_LUNGHEZZA,
		  SISQLiteHelper.ITIN_COLUMN_N_POI };

	public ItinerariesDataSource(Context context) {
		dbHelper = new SISQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Itinerary createItinerary(String nicknameUtente, String elencoPOI, int popolarità, double lunghezza, int numPOI) {
	    ContentValues values = new ContentValues();
	    values.put(SISQLiteHelper.ITIN_COLUMN_NICKNAME_UTENTE, nicknameUtente);
	    values.put(SISQLiteHelper.ITIN_COLUMN_ELENCO_POI, elencoPOI);
	    values.put(SISQLiteHelper.ITIN_COLUMN_POPOLARITA, popolarità);
	    values.put(SISQLiteHelper.ITIN_COLUMN_LUNGHEZZA, lunghezza);
	    values.put(SISQLiteHelper.ITIN_COLUMN_N_POI, numPOI);
	    long insertId = database.insert(SISQLiteHelper.ITIN_TABLE, null,
	        values);
	    Cursor cursor = database.query(SISQLiteHelper.ITIN_TABLE,
	        allColumns, SISQLiteHelper.ITIN_COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    Itinerary newItinerary = cursorToItinerary(cursor);
	    cursor.close();
	    return newItinerary;
	  }
  
	public void deleteItinerary(Itinerary itinerary) {
	    long id = itinerary.getId();
	    System.out.println("Itinerary deleted with id: " + id);
	    database.delete(SISQLiteHelper.ITIN_TABLE, SISQLiteHelper.ITIN_COLUMN_ID
	        + " = " + id, null);
	}
	
	public List<Itinerary> getAllItineraries() {
	    List<Itinerary> itineraries = new ArrayList<Itinerary>();

	    Cursor cursor = database.query(SISQLiteHelper.ITIN_TABLE,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	Itinerary itinerary = cursorToItinerary(cursor);
	    	itineraries.add(itinerary);
	    	cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return itineraries;
	}
  
	private Itinerary cursorToItinerary(Cursor cursor) {
	  Itinerary itinerary = new Itinerary();
	  itinerary.setId(cursor.getLong(0));
	  itinerary.setNicknameUtente(cursor.getString(1));
	  itinerary.setElencoPOI(cursor.getString(2));
	  itinerary.setPopolarità(cursor.getInt(3));
	  itinerary.setLunghezza(cursor.getDouble(4));
	  itinerary.setNumPOI(cursor.getInt(5));
	  return itinerary;
	}
}
