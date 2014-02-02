package md.smartitineraryclient.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SISQLiteHelper extends SQLiteOpenHelper {
	
	/** METADATI DELLE TABELLE */
	// Itinerari
	public static final String ITIN_TABLE = "Itinerari";
  	public static final String ITIN_COLUMN_ID = "_id";
  	public static final String ITIN_COLUMN_NICKNAME_UTENTE = "nicknameUtente";
  	public static final String ITIN_COLUMN_ELENCO_POI= "elencoPOI";
  	public static final String ITIN_COLUMN_POPOLARITA = "popolarita";
  	public static final String ITIN_COLUMN_LUNGHEZZA = "lunghezza";
  	public static final String ITIN_COLUMN_N_POI = "numPOI";
  	
  	// Preferenze
  	public static final String PREF_TABLE = "Preferenze";
	public static final String PREF_COLUMN_ID_ITIN = "_idItinerario";
	public static final String PREF_COLUMN_NICKNAME_UTENTE = "nicknameUtente";
	public static final String PREF_COLUMN_POS_UTENTE = "posizioneUtente";
	public static final String PREF_COLUMN_DATETIME = "datetime";
  	//...
  	private static final String DATABASE_NAME = "smartitineraryDB.db";
    private static final int DATABASE_VERSION = 1;
  

    /** CODICE SQL DI CREAZIONE DELLE TABELLE */
    private static final String ITIN_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "  // Itinerari
      		+ ITIN_TABLE + " (" 
      		+ ITIN_COLUMN_ID + " integer not null, "
      		+ ITIN_COLUMN_NICKNAME_UTENTE + " text not null, "
      		+ ITIN_COLUMN_ELENCO_POI + " text not null, "
      		+ ITIN_COLUMN_POPOLARITA + " integer not null, "
      		+ ITIN_COLUMN_LUNGHEZZA + " double not null, "
      		+ ITIN_COLUMN_N_POI + " integer not null, "
      		+ "primary key(" + ITIN_COLUMN_ID + ", " + ITIN_COLUMN_NICKNAME_UTENTE + ")"
      		+ ");";
    

	public SISQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(ITIN_TABLE_CREATE);
		System.out.println(ITIN_TABLE_CREATE);
	}

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(SISQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + ITIN_TABLE);
    onCreate(db);
  }

} 
