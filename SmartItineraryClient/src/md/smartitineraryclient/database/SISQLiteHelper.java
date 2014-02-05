package md.smartitineraryclient.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SISQLiteHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "smartitineraryDB.db";
    private static final int DATABASE_VERSION = 1;
    
    public SISQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/** METADATI DELLE TABELLE */
	// Itinerari
	public static final String ITIN_TABLE = "Itinerari";
  	public static final String ITIN_COLUMN_ID = "_id";
  	public static final String ITIN_COLUMN_ELENCO_POI= "elencoPOI";
  	public static final String ITIN_COLUMN_POPOLARITA = "popolarita";
  	public static final String ITIN_COLUMN_LUNGHEZZA = "lunghezza";
  	public static final String ITIN_COLUMN_N_POI = "numPOI";
  	public static final String ITIN_COLUMN_POS_UTENTE = "posizioneUtente";
	public static final String ITIN_COLUMN_DATETIME = "datetime";

	// Interessi
	public static final String INTER_TABLE = "Interessi";
  	public static final String INTER_COLUMN_ID = "_id";
  	public static final String INTER_COLUMN_CATEGORIA = "categoria";
  	public static final String INTER_COLUMN_MACROCATEGORIA = "macrocategoria";
  	public static final String INTER_COLUMN_DATA_INSERIMENTO = "dataInserimento";
	public static final String INTER_COLUMN_DATA_CANCELLAZIONE = "dataCancellazione";

    /** CODICE SQL DI CREAZIONE DELLE TABELLE */
	// Itinerari
    private static final String ITIN_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "  
      		+ ITIN_TABLE + " (" 
      		+ ITIN_COLUMN_ID + " integer primary key autoincrement, "
      		+ ITIN_COLUMN_ELENCO_POI + " text not null, "
      		+ ITIN_COLUMN_POPOLARITA + " integer not null, "
      		+ ITIN_COLUMN_LUNGHEZZA + " double not null, "
      		+ ITIN_COLUMN_N_POI + " integer not null, "
      		+ ITIN_COLUMN_POS_UTENTE + " text not null, "
    		+ ITIN_COLUMN_DATETIME + " datetime not null);";
    
    // Interessi
    static final String INTER_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
      		+ INTER_TABLE + " (" 
      		+ INTER_COLUMN_ID + " integer primary key autoincrement, "
      		+ INTER_COLUMN_CATEGORIA + " text not null, "
      		+ INTER_COLUMN_MACROCATEGORIA + " text not null, "
      		+ INTER_COLUMN_DATA_INSERIMENTO + " datetime not null, "
			+ INTER_COLUMN_DATA_CANCELLAZIONE + " datetime );";

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(ITIN_TABLE_CREATE);
		System.out.println(ITIN_TABLE_CREATE);
		database.execSQL(INTER_TABLE_CREATE);
		System.out.println(INTER_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SISQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + ITIN_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + INTER_TABLE);
		onCreate(db);
	}

} 
