package md.smartitineraryclient.database;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PreferencesDataSource {
	
	// Database fields
	private SQLiteDatabase database;
	private SISQLiteHelper dbHelper;
	private String[] allColumns = { SISQLiteHelper.PREF_COLUMN_ID_ITIN,
		  SISQLiteHelper.PREF_COLUMN_NICKNAME_UTENTE,
		  SISQLiteHelper.PREF_COLUMN_POS_UTENTE,
		  SISQLiteHelper.PREF_COLUMN_DATETIME };

	public PreferencesDataSource(Context context) {
		dbHelper = new SISQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Preference createPreference(String nicknameUtente, String posUtente, String datetime) {
	    ContentValues values = new ContentValues();
	    values.put(SISQLiteHelper.PREF_COLUMN_NICKNAME_UTENTE, nicknameUtente);
	    values.put(SISQLiteHelper.PREF_COLUMN_POS_UTENTE, posUtente);
	    values.put(SISQLiteHelper.PREF_COLUMN_DATETIME, datetime);
	    long insertId = database.insert(SISQLiteHelper.PREF_TABLE, null,
	        values);
	    Cursor cursor = database.query(SISQLiteHelper.PREF_TABLE,
	        allColumns, SISQLiteHelper.PREF_COLUMN_ID_ITIN + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    Preference newPreference = cursorToPreference(cursor);
	    cursor.close();
	    return newPreference;
	  }
  
	public void deletePreference(Preference preference) {
	    long id = preference.getIdItinerario();
	    System.out.println("Preference deleted with id: " + id);
	    database.delete(SISQLiteHelper.PREF_TABLE, SISQLiteHelper.PREF_COLUMN_ID_ITIN
	        + " = " + id, null);
	}
	
	public List<Preference> getAllPreferences() {
	    List<Preference> preferences = new ArrayList<Preference>();

	    Cursor cursor = database.query(SISQLiteHelper.PREF_TABLE,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	Preference preference = cursorToPreference(cursor);
	    	preferences.add(preference);
	    	cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return preferences;
	}
  
	private Preference cursorToPreference(Cursor cursor) {
		Preference preference = new Preference();
		preference.setIdItinerario(cursor.getLong(0));
		preference.setNicknameUtente(cursor.getString(1));
		preference.setPosUtente(cursor.getString(2));
		preference.setDatetime(cursor.getString(3));
		return preference;
	}
}
