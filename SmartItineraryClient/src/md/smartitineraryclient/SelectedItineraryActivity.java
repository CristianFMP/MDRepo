package md.smartitineraryclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SelectedItineraryActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selected_itinerary);
		// Show the Up button in the action bar.
		setupActionBar();
		Intent intent = getIntent();
		String[] poiIdArr = intent.getStringExtra("poiIdList").split(",");
		String[] poiNameArr = intent.getStringExtra("poiNameList").split(",");
		String[] poiAddressArr = intent.getStringExtra("poiAddressList").split(",");
		String[] poiPopularityArr = intent.getStringExtra("poiPopularityList").split(",");
		String[] poiLatitudeArr = intent.getStringExtra("poiLatitudeList").split(",");
		String[] poiLongitudeArr = intent.getStringExtra("poiLongitudeList").split(",");
		ListView lv = (ListView) findViewById(R.id.poiList);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
			}
		});
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, poiNameArr);
		lv.setAdapter(adapter);
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
		getMenuInflater().inflate(R.menu.selected_itinerary, menu);
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
