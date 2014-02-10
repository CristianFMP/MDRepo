package md.smartitineraryclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import md.smartitineraryclient.db.DatabaseHelper;
import md.smartitineraryclient.model.Interest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SearchActivity extends Activity implements LocationListener {
	
	DatabaseHelper databaseHelper;
	
	// stringa contenente l'elenco di interessi da passare all'activity successiva
    private String cat = "";
	private static final String TAG = "SearchActivity";
	private static final String TEXT1 = "text1";
	private static final String TEXT2 = "text2";
	private List<Interest> catList;
	private ArrayList<String> list;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		// Show the Up button in the action bar.
		setupActionBar();
		
		/** Recupera gli interessi memorizzati in locale, e li mostra */
		// vedo spunto da handleresposnse di cristian
		catList = new ArrayList<Interest>();
		list = new ArrayList<String>();
		// esempio di categorie già checkate
		list.add("Airport");
		list.add("Airport Gate");
		databaseHelper = new DatabaseHelper(this);
		Cursor c = databaseHelper.getAllInterests();
		try {
			while (c.moveToNext()) {
				Long id = c.getLong(0);
				String categ = c.getString(1);
				String macroc = c.getString(2);
				String ins = c.getString(3);
				String canc = c.getString(4);
				Log.d(TAG, id + " " + categ + " " + macroc + " " + ins + " " + canc);
				if(canc==null) {
					list.add(categ);
					catList.add(new Interest(categ, macroc, ins, canc));
					cat += categ+",";
				}
			}
			if(cat.length()!=0){
				cat = cat.substring(0, cat.length()-1);
			}
			
		} finally {
			c.close();
		}
		updateCategoryList(updateListToItem(catList)); 
		
	}
	
	public void openInterests(View view) {
		Intent intent = new Intent(view.getContext(), ModCategoriesActivity.class);
		intent.putStringArrayListExtra("saved_categories", list);
		startActivityForResult(intent, 0);
		overridePendingTransition(0,0);
	}
	
	// TODO: mostrare testo indicante che non c'è ancora nessuna categoria negli interessi

	private void updateCategoryList(List<Map<String, String>> updateListToItem) {
		final List<Map<String, String>> rows = updateListToItem;		
		// Category ListView
		final String[] fromMapKey = new String[] {TEXT1, TEXT2};
	    final int[] toLayoutId = new int[] {android.R.id.text1, android.R.id.text2};
		ListView catListView = (ListView) findViewById(R.id.catList);
		ListAdapter catAdapter = new SimpleAdapter(this, rows, android.R.layout.test_list_item, fromMapKey, toLayoutId);
		catListView.setAdapter(catAdapter);
	}

	private List<Map<String, String>> updateListToItem(List<Interest> catList) {
		List<Map<String, String>> listItem = new ArrayList<Map<String, String>>();
		for (int i = 0; i < catList.size(); i++) {
			Interest tmpInterest = catList.get(i);
			String category = tmpInterest.getCategoria();
			String macrocat = tmpInterest.getMacrocategoria();
			Map<String, String> listItemMap = new HashMap<String, String>();
			listItemMap.put(TEXT1, category);
			listItemMap.put(TEXT2, macrocat); // per ora non viene mostrato
			listItem.add(listItemMap);
		}
		return listItem;
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			overridePendingTransition(0,0);
			return true;
		case R.id.action_result:
			try {
				openResult();
			} catch (IOException e) {
				Log.d(TAG, e.toString());
			}
			overridePendingTransition(0,0);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void openResult() throws IOException {
		final EditText posizione = (EditText)findViewById(R.id.editText_pos);
        final EditText lunghezza = (EditText)findViewById(R.id.editText_maxLength);
        final EditText raggio = (EditText)findViewById(R.id.editText_range);
        // Se non vengono inseriti parametri questi valori sono una stringa vuota ""
        String pos = posizione.getText().toString();
        String lun = lunghezza.getText().toString();
        String rag = raggio.getText().toString();
        Location location = MainActivity.mCurrentLocation; // Prende la posizione corrente
        /** Controlla se l'utente ha inserito la posizione, e sovrascrive pos */
        Geocoder gc = new Geocoder(this);
        List<Address> list;
        Address add = null;
        if(pos.equals("")) { 		// Posizione attuale
        	// Trasforma la posizione da Location a String
        	pos = locationStringFromLocation(location);
        	
	        String Pos[] = pos.split(" ");
	        double lat = Double.parseDouble(Pos[0].replace(",", "."));
	        double lng = Double.parseDouble(Pos[1].replace(",", "."));
	        list = gc.getFromLocation(lat, lng, 1);
	        add = list.get(0);
        } else { 					// Posizione inserita manualmente
        	list = gc.getFromLocationName(pos, 1);
        	add = list.get(0);
        	double lat = add.getLatitude();
        	double lng = add.getLongitude();
        	location.setLatitude(lat);
        	location.setLongitude(lng);
        	// Trasforma la posizione da Location a String
        	pos = locationStringFromLocation(location);
        }
        
        // Mostra tramite toast l'indirizzo sul quale avverà la ricerca
        //Log.d("Address found", add.toString());
        String address_found = "";
        for(int i=0; i<=add.getMaxAddressLineIndex(); i++){
        	if(i!=0){
        		address_found += "\n";
        	}
        	address_found += add.getAddressLine(i);
        }
        Toast.makeText(this, address_found, Toast.LENGTH_LONG).show();
        // TODO: eventualmente prossimamente si può chiedere all'utente se è corretto l'indirizzo trovato
        
        /** Controlla se l'utente ha inserito la lunghezza, e sovrascrive lun */
        if(lun.equals("")){
        	lun = "5";
        }
        /** Controlla se l'utente ha inserito il raggio, e sovrascrive rag */
        if(rag.equals("")){
        	rag = "1000";
        }
        
        cat = "Home (private),Coworking Space,Office,Bar,Pub,Restaurant"; // TODO: eliminare questo valore fisso - intera line
        // TODO: capire se crea problemi nel caso in cui non ci siano spazi da riampiazzare
        cat = cat.replace(" ", ".");
        pos = pos.replace(",", ".");
        pos = pos.replace(" ", ",");
        Bundle bundle = new Bundle();
        bundle.putString("posizione", pos);
        bundle.putString("lunghezza", lun);
        bundle.putString("raggio", rag);
        bundle.putString("interessi", cat);
        
		Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
        intent.putExtras(bundle);
	    startActivity(intent);
	}

	/** Trasforma la posizione da Location a String */
	public static String locationStringFromLocation(final Location location) {
		double lat = location.getLatitude();
		double lng = location.getLongitude();
        return Location.convert(lat, Location.FORMAT_DEGREES) + " " + Location.convert(lng, Location.FORMAT_DEGREES);
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		databaseHelper.close();
	}
	
	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
	
}