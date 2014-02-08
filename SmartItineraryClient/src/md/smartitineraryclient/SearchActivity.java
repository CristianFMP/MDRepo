package md.smartitineraryclient;

import java.io.IOException;
import java.util.List;

import md.smartitineraryclient.db.DatabaseHelper;
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
import android.widget.EditText;
import android.widget.Toast;

public class SearchActivity extends Activity implements LocationListener {
	
	//private ListView interests_listview;
	
	// stringa contenete la l'elenco di interessi da passare all'activity successiva
    private String cat = "Home.(private),Coworking.Space,Office"; // TODO
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		// Show the Up button in the action bar.
		setupActionBar();
		
		/** Recupera gli interessi memorizzati in locale, e li mostra */ // TODO: continuato in locale
		DatabaseHelper databaseHelper = new DatabaseHelper(this);
		Cursor c = databaseHelper.getAllInterests();
		try {
			while (c.moveToNext()) {
				Log.d("SmartItinerary", c.getLong(0) + " " + c.getString(1) + " " + c.getString(2) + " " + c.getString(3) + " " + c.getString(4));
			}
		} finally {
			c.close();
		}
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
				Log.d("IOException", e.toString());
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
        // TODO: eventualmente prossimamente
        
        /** Controlla se l'utente ha inserito la lunghezza, e sovrascrive lun */
        if(lun.equals("")){
        	lun = "5";
        }
        /** Controlla se l'utente ha inserito il raggio, e sovrascrive rag */
        if(rag.equals("")){
        	rag = "1000";
        }
        
        
        
        
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
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
}