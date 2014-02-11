package md.smartitineraryclient.db;

import java.sql.Timestamp;
import java.text.MessageFormat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "SmartItinerary.db";
 
	private static final int SCHEMA_VERSION = 1;
 
	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE {0} ({1} INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "{2} TEXT NOT NULL,{3} TEXT NOT NULL,{4} TEXT NOT NULL,{5} TEXT);";
		db.execSQL(MessageFormat.format(sql, InterestTable.TABLE_NAME, 
				InterestTable._ID,
				InterestTable.CATEGORIA, 
				InterestTable.MACROCATEGORIA, 
				InterestTable.DATA_INSERIMENTO, 
				InterestTable.DATA_CANCELLAZIONE));
		
		sql = "CREATE TABLE {0} ({1} INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "{2} TEXT NOT NULL, {3} INTEGER NOT NULL, {4} DOUBLE NOT NULL, {5} INTEGER NOT NULL, "
				+ "{6} TEXT NOT NULL, {7} TEXT NOT NULL);";
		db.execSQL(MessageFormat.format(sql, ItineraryTable.TABLE_NAME,
				ItineraryTable._ID,
				ItineraryTable.ELENCO_POI,
				ItineraryTable.POPOLARITA,
				ItineraryTable.LUNGHEZZA,
				ItineraryTable.NUM_POI,
				ItineraryTable.POS_UTENTE,
				ItineraryTable.DATA_PREFERENZA));
		
		// TODO: poi cancellare questi linee
		insertInterest(db, "Accessories Store", "Shop");
		insertInterest(db, "Bar", "Nightlife");
		insertInterest(db, "Bed & Breakfast", "Travel");
		insertInterest(db, "Bookstore", "Shop");
		insertInterest(db, "Hotel", "Travel");
		insertInterest(db, "Music Store", "Shop");
		insertInterest(db, "Restaurant", "Food");
	}

	
	/*
	 * es. di inserimento: insertInterest(db, "categoria", "macrocategoria");
	 * (dove db e' il nome del database del tipo SQLiteDatabase)
	 */
	public long insertInterest(SQLiteDatabase db, String cat, String macrocat) {
		ContentValues v = new ContentValues();
		v.put(InterestTable.CATEGORIA, cat);
		v.put(InterestTable.MACROCATEGORIA, macrocat);
		java.util.Date date = new java.util.Date();
		Timestamp now = new Timestamp(date.getTime());
		v.put(InterestTable.DATA_INSERIMENTO, now.toString());
		long id = db.insert(InterestTable.TABLE_NAME, null, v);
		Log.d("Interest inserted", "with id: " + id);
		return id;
	}
	
	public Cursor getAllInterests(){
		return (getReadableDatabase().query(
			InterestTable.TABLE_NAME, 		// nome della tabella
			InterestTable.COLUMNS, 			// array dei nomi delle colonne da ritornare
			null, 							// filtro da applicare ai dati (where)
			null,							// argomenti su cui filtrare i dati (nel caso in cui nel filtro siano presenti parametri)
			null, 							// group by da eseguire
			null, 							// clausola having da usare
			InterestTable.CATEGORIA));		// ordinamento da applicare ai dati
	}
	
	public int deleteInterest(SQLiteDatabase db, String cat) {
	    ContentValues v = new ContentValues();
	    java.util.Date date = new java.util.Date();
		Timestamp now = new Timestamp(date.getTime());
	    v.put(InterestTable.DATA_CANCELLAZIONE, now.toString());
	    int idCanc = db.update(InterestTable.TABLE_NAME, v, InterestTable.CATEGORIA + "= '" + cat + "'", null);
	    Log.d("Interest deleted", "with id: " + idCanc);
	    return idCanc;
	}
	
	public int restoreInterest(SQLiteDatabase db, String cat) {
		ContentValues v = new ContentValues();
	    v.putNull(InterestTable.DATA_CANCELLAZIONE);
	    java.util.Date date = new java.util.Date();
		Timestamp now = new Timestamp(date.getTime());
	    v.put(InterestTable.DATA_INSERIMENTO, now.toString());
	    int idRes = db.update(InterestTable.TABLE_NAME, v, InterestTable.CATEGORIA + "= '" + cat + "'", null);
	    Log.d("Interest restored", "with id: " + idRes);
	    return idRes;
	}
	
	/*
	 * es. di inserimento: insertItinerary(db, "poi1,poi2,poi3", "4", "1200.35426", "3", "45.56236,9.14552");
	 * (dove db e' il nome del database del tipo SQLiteDatabase)
	 */
	public long insertItinerary(SQLiteDatabase db, String poi, int popolar, double lung, int numpoi, String posiz) {
		ContentValues v = new ContentValues();
		v.put(ItineraryTable.ELENCO_POI, poi);
		v.put(ItineraryTable.POPOLARITA, popolar);
		v.put(ItineraryTable.LUNGHEZZA, lung);
		v.put(ItineraryTable.NUM_POI, numpoi);
		v.put(ItineraryTable.POS_UTENTE, posiz);
		java.util.Date date = new java.util.Date();
		Timestamp now = new Timestamp(date.getTime());
		v.put(ItineraryTable.DATA_PREFERENZA, now.toString());
		long id = db.insert(ItineraryTable.TABLE_NAME, null, v);
		Log.d("Itinerary inserted", "with id: " + id);
		return id;
	}
	
	public Cursor getAllItineraries(){
		return (getReadableDatabase().query(
			ItineraryTable.TABLE_NAME, 		// nome della tabella
			ItineraryTable.COLUMNS, 		// array dei nomi delle colonne da ritornare
			null, 							// filtro da applicare ai dati (where)
			null,							// argomenti su cui filtrare i dati (nel caso in cui nel filtro siano presenti parametri)
			null, 							// group by da eseguire
			null, 							// clausola having da usare
			InterestTable._ID));			// ordinamento da applicare ai dati
	}
	
	public int deleteIitnerary(SQLiteDatabase db, long id) {
	    int idCanc = db.delete(ItineraryTable.TABLE_NAME, ItineraryTable._ID + "=" + id, null);
	    Log.d("Itinerary deleted with id:", id+"");
	    return idCanc;
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String query;
		query = "DROP TABLE IF EXISTS " + InterestTable.TABLE_NAME;
		db.execSQL(query);
		query = "DROP TABLE IF EXISTS " + ItineraryTable.TABLE_NAME;
		db.execSQL(query);
		onCreate(db);
	}
}
