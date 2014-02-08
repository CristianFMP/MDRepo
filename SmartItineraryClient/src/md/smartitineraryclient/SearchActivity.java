package md.smartitineraryclient;

import java.io.IOException;
import java.util.List;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class SearchActivity extends Activity implements LocationListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		// Show the Up button in the action bar.
		setupActionBar();
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
				// TODO Auto-generated catch block
				e.printStackTrace();
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
        if(!(pos.equals(""))){ // Posizione inserita manualmente
        	// TODO: controllo sull'indirizzo o sul luogo inserito dall'utente
        	Geocoder gc = new Geocoder(this);
        	List<Address> list = gc.getFromLocationName(pos, 1);
        	Address add = list.get(0);
        	// TODO: inviare qualcosa più precisa della locality
        	double lat = add.getLatitude();
        	double lng = add.getLongitude();
        	location.setLatitude(lat);
        	location.setLongitude(lng);
        	//String locality = add.getLocality();
        	//Toast.makeText(this, locality, Toast.LENGTH_LONG).show();
        }
        /** Trasforma la posizione da Location a String */
        pos = locationStringFromLocation(location);
		// TODO: informare l'utente di ciò che google localizzerà (toast oppure alert "intendevi ...?")
        Bundle bundle = new Bundle();
        bundle.putString("posizione", pos);
        bundle.putString("lunghezza", lun);
        bundle.putString("raggio", rag);
        
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