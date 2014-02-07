package md.smartitineraryclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class ResultActivity extends Activity {

	// TODO: set the ip of your *server* host
	private static final String SERVICE_URL = "http://159.149.177.116:8080/SmartItineraryWebService/rest/itinerary";
	private static final String TAG = "ResultActivity";
	private static final String TEXT1 = "text1";
	private static final String TEXT2 = "text2";
	ArrayList<Itinerary> itineraryList;
		
	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		setupActionBar();
		if (savedInstanceState == null) {		
			// url of the web service
			String url = SERVICE_URL + "/getItineraries";
			// web service calls must be executed in a separate thread
			WebServiceTask wst = new WebServiceTask(WebServiceTask.GET_TASK,
					this, "Retrieving Itineraries...");
			// show toast with reql url
			Context context = getApplicationContext();
			CharSequence text = url;
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			// execute the call
			wst.execute(new String[] { url });
			handleResponse(wst.getResponse());
		} else {
			itineraryList = savedInstanceState.getParcelableArrayList("key");
			updateItineraryList(convertToListItems(itineraryList));
		}
	}
		
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelableArrayList("key", itineraryList);
		super.onSaveInstanceState(outState);
	}
		
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		itineraryList = savedInstanceState.getParcelableArrayList("key");
	}

	public void handleResponse(String response) {
		try {
			itineraryList = new ArrayList<Itinerary>();
			JSONArray jArray = new JSONArray(response);
			
			// TODO: handle JSONexceptions
			
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject tmpJObj =  jArray.getJSONObject(i);
				// LineString ls = new LineString(tmpJObj.getString("poiLine"));
				List<Poi> poiList = new ArrayList<Poi>();
				JSONArray tmpJArr = tmpJObj.getJSONArray("pois");
				for (int j = 0; j < tmpJArr.length(); j++) {
					// Point point = new Point(tmpJArr.getJSONObject(j).getString("poi"));
					String id = tmpJArr.getJSONObject(j).getString("id");
					String name = tmpJArr.getJSONObject(j).getString("name");
					String address = tmpJArr.getJSONObject(j).getString("address");
					int popularity = tmpJArr.getJSONObject(j).getInt("popularity");
					double latitude = tmpJArr.getJSONObject(j).getDouble("latitude");
					double longitude = tmpJArr.getJSONObject(j).getDouble("longitude");
					poiList.add(new Poi(id, name, address, popularity, latitude, longitude));
				}
				int popularity = tmpJObj.getInt("popularity");
				double length = tmpJObj.getDouble("length");
				itineraryList.add(new Itinerary(poiList, popularity, length));
			}
			updateItineraryList(convertToListItems(itineraryList));
		} catch (Exception e) {
			Log.e(TAG, e.getLocalizedMessage(), e);
		}
	}
	
	private void updateItineraryList(List<Map<String, String>> listItem) {
		final List<Map<String, String>> rows = listItem;		
		// POI ListView
		final String[] fromMapKey = new String[] {TEXT1, TEXT2};
	    final int[] toLayoutId = new int[] {android.R.id.text1, android.R.id.text2};
		ListView itineraryListView = (ListView) findViewById(R.id.itineraryList);
		itineraryListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String poiList = rows.get(position).get("poiList");
				Intent intent = new Intent(view.getContext(), SelectedItineraryActivity.class);
				intent.putExtra("poiList", poiList);
				startActivityForResult(intent, 0);
			}
		});
		ListAdapter itineraryAdapter = new SimpleAdapter(this, rows, R.layout.rowlayout, fromMapKey, toLayoutId);
		// Assign adapter to ListView	
		itineraryListView.setAdapter(itineraryAdapter);
	}
	
	private List<Map<String, String>> convertToListItems(List<Itinerary> itineraryList) {
		List<Map<String, String>> listItem = new ArrayList<Map<String, String>>();
		for (int n = 0; n < itineraryList.size(); n++){
			Itinerary tmpItinerary = itineraryList.get(n);
			String title = "Itinerario da " + tmpItinerary.getPois().get(0).getName();
			String info = "Popolarita " + tmpItinerary.getPopularity() + ", " + tmpItinerary.getPois().size() + " points of interest, ";
			String poiList = "";
			for (int i = 0; i < tmpItinerary.getPois().size(); i++) {
				if (i == tmpItinerary.getPois().size() - 1)
					poiList += tmpItinerary.getPois().get(i).getId();
				else
					poiList += tmpItinerary.getPois().get(i).getId() + ",";
			}
			if (tmpItinerary.getLength() < 1000) {
				Map<String, String> listItemMap = new HashMap<String, String>();
				listItemMap.put(TEXT1, title);
				listItemMap.put(TEXT2, info + tmpItinerary.getLengthMeters() + " m");
				listItemMap.put("poiList", poiList);
				listItem.add(listItemMap);
			} else {
				Map<String, String> listItemMap = new HashMap<String, String>();
				listItemMap.put(TEXT1, title);
				listItemMap.put(TEXT2, info + tmpItinerary.getLengthKm() + " km");
				listItemMap.put("poiList", poiList);
				listItem.add(listItemMap);
			}
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
		getMenuInflater().inflate(R.menu.result, menu);
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
		}
		return super.onOptionsItemSelected(item);
	}
}
