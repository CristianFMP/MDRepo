package md.smartitineraryclient;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.support.v4.app.NavUtils;

public class MapItineraryActivity extends Activity {

	Intent old_intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_itinerary);
		// Show the Up button in the action bar.
		setupActionBar();
		
		old_intent = getIntent();
		final String[] poiIdArr = old_intent.getStringExtra("poiIdList").split(",");
		String[] poiNameArr = old_intent.getStringExtra("poiNameList").split(",");
		String[] poiAddressArr = old_intent.getStringExtra("poiAddressList").split(",");
		String[] poiPopularityArr = old_intent.getStringExtra("poiPopularityList").split(",");
		String[] poiLatitudeArr = old_intent.getStringExtra("poiLatitudeList").split(",");
		String[] poiLongitudeArr = old_intent.getStringExtra("poiLongitudeList").split(",");
		ListView lv = (ListView) findViewById(R.id.poiList);
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
		getMenuInflater().inflate(R.menu.map_itinerary, menu);
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
		// TODO 
		
	}

}
