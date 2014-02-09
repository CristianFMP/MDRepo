package md.smartitineraryclient.db;

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
				+ "{2} TEXT NOT NULL,{3} TEXT NOT NULL,{4} DATETIME NOT NULL,{5} DATETIME);";
		db.execSQL(MessageFormat.format(sql, InterestTable.TABLE_NAME, 
				InterestTable._ID,
				InterestTable.CATEGORIA, 
				InterestTable.MACROCATEGORIA, 
				InterestTable.DATA_INSERIMENTO, 
				InterestTable.DATA_CANCELLAZIONE));
		
		sql = "CREATE TABLE {0} ({1} INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "{2} TEXT NOT NULL, {3} INTEGER NOT NULL, {4} DOUBLE NOT NULL, {5} INTEGER NOT NULL, "
				+ "{6} TEXT NOT NULL, {7} DATETIME NOT NULL);";
		db.execSQL(MessageFormat.format(sql, ItineraryTable.TABLE_NAME,
				ItineraryTable._ID,
				ItineraryTable.ELENCO_POI,
				ItineraryTable.POPOLARITA,
				ItineraryTable.LUNGHEZZA,
				ItineraryTable.NUM_POI,
				ItineraryTable.POS_UTENTE,
				ItineraryTable.DATA_PREFERENZA));
		
		insertInterest(db, "categoria", "macrocategoria", "adesso", null); // TODO: poi tolgo queste linee
		insertInterest(db, "bar", "food", "23-01-2014,23:34:12", null);
		insertInterest(db, "provaCat", "provaMacro", "una data...", "ieri");
		insertItinerary(db, "poi1,poi2,poi3", 4, 1200.35426, 3, "45.56236,9.14552", "l'altroieri");
	}

	
	/*
	 * es. di inserimento: insertInterest(db, "categoria", "macrocategoria", "adesso", null);
	 * (dove db è il nome del database del tipo SQLiteDatabase)
	 */
	public void insertInterest(SQLiteDatabase db, String cat, String macrocat, String dataIns, String dataCanc) {
		ContentValues v = new ContentValues();
		v.put(InterestTable.CATEGORIA, cat);
		v.put(InterestTable.MACROCATEGORIA, macrocat);
		v.put(InterestTable.DATA_INSERIMENTO, dataIns);
		v.put(InterestTable.DATA_CANCELLAZIONE, dataCanc);
		db.insert(InterestTable.TABLE_NAME, null, v);
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
	
	public void deleteInterest(SQLiteDatabase db, String cat) {
	    ContentValues v = new ContentValues();
	    // TODO: controllare appena possibile se il NOW() funziona
	    v.put(InterestTable.DATA_CANCELLAZIONE, "NOW()");
	    Log.d("Interest deleted", cat);
	    db.update(InterestTable.TABLE_NAME, v, InterestTable.CATEGORIA + "=" + cat, null);
	}
	
	public void restoreInterest(SQLiteDatabase db, String cat) {
		ContentValues v = new ContentValues();
	    v.putNull(InterestTable.DATA_CANCELLAZIONE);
	    // TODO: controllare appena possibile se il NOW() funziona
	    v.put(InterestTable.DATA_INSERIMENTO, "NOW()");
	    Log.d("Interest restored", cat);
	    db.update(InterestTable.TABLE_NAME, v, InterestTable.CATEGORIA + "=" + cat, null);
	}
	
	/*
	 * es. di inserimento: insertItinerary(db, "poi1,poi2,poi3", "4", "1200.35426", "3", "45.56236,9.14552");
	 * (dove db è il nome del database del tipo SQLiteDatabase)
	 */
	public void insertItinerary(SQLiteDatabase db, String poi, int popolar, double lung, int numpoi, String posiz, String datapref) {
		ContentValues v = new ContentValues();
		v.put(ItineraryTable.ELENCO_POI, poi);
		v.put(ItineraryTable.POPOLARITA, popolar);
		v.put(ItineraryTable.LUNGHEZZA, lung);
		v.put(ItineraryTable.NUM_POI, numpoi);
		v.put(ItineraryTable.POS_UTENTE, posiz);
		v.put(ItineraryTable.DATA_PREFERENZA, datapref);
		db.insert(ItineraryTable.TABLE_NAME, null, v);
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
	
	public void deleteIitnerary(SQLiteDatabase db, long id) {
	    Log.d("Itinerary deleted with id:", id+"");
	    db.delete(ItineraryTable.TABLE_NAME, ItineraryTable._ID + "=" + id, null);
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
