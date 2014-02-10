package md.smartitineraryclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import md.smartitineraryclient.db.DatabaseHelper;

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SelectedItineraryActivity extends Activity {
	
	private static final String TEXT1 = "text1";
	private static final String TEXT2 = "text2";
	
	String[] poiNameArr;
	String[] poiAddressArr;
	String[] poiPopularityArr;
	int itinPopularity;
	double itinLength;
	private static LatLng[] POIs_COORD;
	private int numPOI;
	DatabaseHelper DbH;
	SQLiteDatabase db ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selected_itinerary);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Intent intent = getIntent();
		final String[] poiIdArr = intent.getStringExtra("poiIdList").split(",");
		poiNameArr = intent.getStringExtra("poiNameList").split(",");
		poiAddressArr = intent.getStringExtra("poiAddressList").split(",");
		poiPopularityArr = intent.getStringExtra("poiPopularityList").split(",");
		String[] poiLatitudeArr = intent.getStringExtra("poiLatitudeList").split(",");
		String[] poiLongitudeArr = intent.getStringExtra("poiLongitudeList").split(",");
		itinPopularity = intent.getIntExtra("itinPopularity", 0);
		itinLength = intent.getDoubleExtra("itinLength", 0);
		
		numPOI = poiIdArr.length;
		POIs_COORD = new LatLng[numPOI];
		for(int i=0; i< numPOI; i++){
			double lat = Double.parseDouble(poiLatitudeArr[i]);
			double lng = Double.parseDouble(poiLongitudeArr[i]);
			POIs_COORD[i] = new LatLng(lat, lng);
		}
		
		
		ListView lv = (ListView) findViewById(R.id.poiList);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(view.getContext(), CommentsActivity.class);
				intent.putExtra("poiIdList", poiIdArr[position]);
				startActivityForResult(intent, 0);
				overridePendingTransition(0,0);
			}
		});
		final String[] fromMapKey = new String[] {TEXT1, TEXT2};
	    final int[] toLayoutId = new int[] {android.R.id.text1, android.R.id.text2};
		List<Map<String, String>> listItem = convertToListItems(poiNameArr, poiAddressArr, poiPopularityArr);
		ListAdapter adapter = new SimpleAdapter(this, listItem, R.layout.simple_list_item_1_custom, fromMapKey, toLayoutId);
		lv.setAdapter(adapter);
		
		/** Apro il database, redendolo scrivibile */
    	DbH = new DatabaseHelper(this);
    	db = DbH.getWritableDatabase();
	}


	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.selected_itinerary, menu);
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
		}
		return super.onOptionsItemSelected(item);
	}
	
	public List<Map<String, String>> convertToListItems(String[] names, String[] addresses, String[] popularities) {
		List<Map<String, String>> listItem = new ArrayList<Map<String, String>>();
		for (int i = 0; i < names.length; i++) {
			Map<String, String> listItemMap = new HashMap<String, String>();
			String title = names[i];
			String info = addresses[i] + "\n" + popularities[i] + " check-in effettuati"; 
			listItemMap.put(TEXT1, title);
			listItemMap.put(TEXT2, info);
			listItem.add(listItemMap);
		}
		return listItem;
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
}
