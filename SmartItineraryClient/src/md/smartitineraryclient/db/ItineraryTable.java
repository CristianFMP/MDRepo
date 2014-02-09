package md.smartitineraryclient.db;

import android.provider.BaseColumns;

public interface ItineraryTable extends BaseColumns {
	String TABLE_NAME = "itinerari";
	 
	String ELENCO_POI = "elencoPOI";
	String POPOLARITA = "popolarita";
	String LUNGHEZZA = "lunghezza";
	String NUM_POI = "numPOI";
	String POS_UTENTE = "posizioneUtente";
	String DATA_PREFERENZA = "dataPreferenza";
	
 
	String[] COLUMNS = new String[]
	{ _ID, ELENCO_POI, POPOLARITA, LUNGHEZZA, NUM_POI, POS_UTENTE, DATA_PREFERENZA };
}
