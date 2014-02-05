package md.smartitineraryclient.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class InterestsDataSource {
	
	// Database fields
		private SQLiteDatabase database;
		private SISQLiteHelper dbHelper;
		private String[] allColumns = { 
			SISQLiteHelper.INTER_COLUMN_ID,
			SISQLiteHelper.INTER_COLUMN_CATEGORIA,
			SISQLiteHelper.INTER_COLUMN_MACROCATEGORIA,
			SISQLiteHelper.INTER_COLUMN_DATA_INSERIMENTO,
			SISQLiteHelper.INTER_COLUMN_DATA_CANCELLAZIONE };

		public InterestsDataSource(Context context) {
			dbHelper = new SISQLiteHelper(context);
		}

		public void open() throws SQLException {
			database = dbHelper.getWritableDatabase();
		}

		public void close() {
			dbHelper.close();
		}

		public Interest createInterest(String categoria, String macrocategoria, String dataIns, String dataCanc) {
		    ContentValues values = new ContentValues();
		    values.put(SISQLiteHelper.INTER_COLUMN_CATEGORIA, categoria);
		    values.put(SISQLiteHelper.INTER_COLUMN_MACROCATEGORIA, macrocategoria);
		    values.put(SISQLiteHelper.INTER_COLUMN_DATA_INSERIMENTO, dataIns);
		    values.put(SISQLiteHelper.INTER_COLUMN_DATA_CANCELLAZIONE, dataCanc);
		    long insertId = database.insert(SISQLiteHelper.INTER_TABLE, null,
		        values);
		    Cursor cursor = database.query(SISQLiteHelper.INTER_TABLE,
		        allColumns, SISQLiteHelper.INTER_COLUMN_ID + " = " + insertId, null,
		        null, null, null);
		    cursor.moveToFirst();
		    Interest newInterest = cursorToInterest(cursor);
		    cursor.close();
		    return newInterest;
		  }
	  
		public void deleteInterest(Interest interest) {
		    long id = interest.getId();
		    ContentValues values = new ContentValues();
		    // TODO: controllare appena possibile se il NOW() funziona
		    values.put(SISQLiteHelper.INTER_COLUMN_DATA_CANCELLAZIONE, "NOW()");
		    System.out.println("Interest deleted with category: " + interest.getCategoria());
		    database.update(SISQLiteHelper.INTER_TABLE, values, "_id "+"="+id, null);
		}
		
		public void restoreInterest(Interest interest) {
		    long id = interest.getId();
		    ContentValues values = new ContentValues();
		    values.putNull(SISQLiteHelper.INTER_COLUMN_DATA_CANCELLAZIONE);
		    values.put(SISQLiteHelper.INTER_COLUMN_DATA_INSERIMENTO, "NOW()");
		    System.out.println("Interest restored with category: " + interest.getCategoria());
		    database.update(SISQLiteHelper.INTER_TABLE, values, "_id "+"="+id, null);
		}
		
		public List<Interest> getAllInterests() {
		    List<Interest> interests = new ArrayList<Interest>();

		    Cursor cursor = database.query(SISQLiteHelper.INTER_TABLE,
		        allColumns, null, null, null, null, null);

		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		    	Interest interest = cursorToInterest(cursor);
		    	interests.add(interest);
		    	cursor.moveToNext();
		    }
		    // make sure to close the cursor
		    cursor.close();
		    return interests;
		}
	  
		private Interest cursorToInterest(Cursor cursor) {
			Interest interest = new Interest();
			interest.setId(cursor.getLong(0));
			interest.setCategoria(cursor.getString(1));
			interest.setMacrocategoria(cursor.getString(2));
			interest.setDataInserimento(cursor.getString(3));
			return interest;
		}
}
