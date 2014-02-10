package md.smartitineraryclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import md.smartitineraryclient.db.*;
import md.smartitineraryclient.util.GMapV2Direction;
import md.smartitineraryclient.util.GetDirectionsAsyncTask;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import android.os.Bundle;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;

public class MapItineraryActivity extends FragmentActivity {

	Intent old_intent;
	private GoogleMap mMap;
	String[] poiNameArr;
	String[] poiAddressArr;
	String[] poiPopularityArr;
	int itinPopularity;
	double itinLength;
	private static LatLng[] POIs_COORD;
	private int numPOI;
	private int width, height;
	private LatLngBounds latlngBounds;
	DatabaseHelper DbH;
	SQLiteDatabase db ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_itinerary);
		// Show the Up button in the action bar.
		setupActionBar();
		
		old_intent = getIntent();
		final String[] poiIdArr = old_intent.getStringExtra("poiIdList").split(",");
		poiNameArr = old_intent.getStringExtra("poiNameList").split(",");
		poiAddressArr = old_intent.getStringExtra("poiAddressList").split(",");
		poiPopularityArr = old_intent.getStringExtra("poiPopularityList").split(",");
		String[] poiLatitudeArr = old_intent.getStringExtra("poiLatitudeList").split(",");
		String[] poiLongitudeArr = old_intent.getStringExtra("poiLongitudeList").split(",");
		itinPopularity = old_intent.getIntExtra("itinPopularity", 0);
		itinLength = old_intent.getDoubleExtra("itinLength", 0);
		

		getScreenDimensions();
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		/** Per ogni POI dell'itinerario metto un marker con le relative info nel title */
		numPOI = poiIdArr.length;
		POIs_COORD = new LatLng[numPOI];
		for(int i=0; i< numPOI; i++){
			double lat = Double.parseDouble(poiLatitudeArr[i]);
			double lng = Double.parseDouble(poiLongitudeArr[i]);
			POIs_COORD[i] = new LatLng(lat, lng);
			String desc = poiAddressArr[i] + "\n (" + poiPopularityArr[i] + " check-ins)";
			mMap.addMarker(new MarkerOptions()
	        	.position(POIs_COORD[i])
	        	.title(poiNameArr[i])
	        	.snippet(desc)
	        	.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));
		}
		
		latlngBounds = createLatLngBoundsObject(POIs_COORD[0], POIs_COORD[numPOI-1]);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));
		
		/** Mostro l'itinerario come percorso stradale */
        for(int i=0; i<numPOI-1; i++){
        	findDirections( POIs_COORD[i].latitude, POIs_COORD[i].longitude,
					POIs_COORD[i+1].latitude, POIs_COORD[i+1].longitude, 
					GMapV2Direction.MODE_DRIVING );
        }
        
        /** Apro il database, redendolo scrivibile */
    	DbH = new DatabaseHelper(this);
    	db = DbH.getWritableDatabase();
	}


	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.map_itinerary, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
		    overridePendingTransition(0,0);
			return true;
		case R.id.action_add_fav:
			addToFav();
			return true;
		case R.id.action_itinerary_as_list:
			openSelectedItinerary();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void openSelectedItinerary() {
		Intent new_intent = new Intent(this, SelectedItineraryActivity.class);
		new_intent.putExtras(old_intent);
	    startActivity(new_intent);
	    overridePendingTransition(0,0);
	}

	private void addToFav() {
		String poi = "(";
		for(int i=0; i<numPOI; i++){
			poi +=  POIs_COORD[i].longitude + " " + POIs_COORD[i].latitude;
			poi += ",";
		}
		poi = poi.substring(0, poi.length()-1);
		poi += ")";
		String posutente = MainActivity.CurrentLocation(MainActivity.provider).toString();
		long id = DbH.insertItinerary(db, poi, itinPopularity, itinLength, numPOI, posutente);
		if(id != -1){
			Toast.makeText(this, "Itinerario aggiunto ai preferiti", Toast.LENGTH_LONG).show();
		}
		Log.d("Itinerario aggiunto ai preferiti", "id: " + id + ", elenco poi: " + poi + ", popolarità: " + itinPopularity + ", lungh: " + itinLength);
	}

	private void getScreenDimensions(){
		Display display = getWindowManager().getDefaultDisplay(); 
		Point size = new Point(); 
		display.getSize(size); 
		width = size.x; 
		height = size.y;
	}
	
	private LatLngBounds createLatLngBoundsObject(LatLng firstLocation, LatLng secondLocation) {
		if (firstLocation != null && secondLocation != null)
		{
			LatLngBounds.Builder builder = new LatLngBounds.Builder();    
			builder.include(firstLocation).include(secondLocation);
			
			return builder.build();
		}
		return null;
	}
	
	public void findDirections(double fromPositionDoubleLat, double fromPositionDoubleLong, double toPositionDoubleLat, double toPositionDoubleLong, String mode)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put(GetDirectionsAsyncTask.ORIGIN_LAT, String.valueOf(fromPositionDoubleLat));
		map.put(GetDirectionsAsyncTask.ORIGIN_LONG, String.valueOf(fromPositionDoubleLong));
		map.put(GetDirectionsAsyncTask.DESTINATION_LAT, String.valueOf(toPositionDoubleLat));
		map.put(GetDirectionsAsyncTask.DESTINATION_LONG, String.valueOf(toPositionDoubleLong));
		map.put(GetDirectionsAsyncTask.DIRECTIONS_MODE, mode);
		
		GetDirectionsAsyncTask asyncTask = new GetDirectionsAsyncTask(this);
		asyncTask.execute(map);	
	}
	
	public void handleGetDirectionsResult(ArrayList<LatLng> directionPoints) {
		PolylineOptions rectLine = new PolylineOptions().width(5).color(Color.DKGRAY);

		for(int i = 0 ; i < directionPoints.size() ; i++) {          
			rectLine.add(directionPoints.get(i));
		}
		mMap.addPolyline(rectLine);
		
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));

	}

}
