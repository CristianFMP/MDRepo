package md.smartitineraryclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SelectedItineraryActivity extends Activity {
	private static final String TEXT1 = "text1";
	private static final String TEXT2 = "text2";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selected_itinerary);
		// Show the Up button in the action bar.
		setupActionBar();
		Intent intent = getIntent();
		final String[] poiIdArr = intent.getStringExtra("poiIdList").split(",");
		String[] poiNameArr = intent.getStringExtra("poiNameList").split(",");
		String[] poiAddressArr = intent.getStringExtra("poiAddressList").split(",");
		String[] poiPopularityArr = intent.getStringExtra("poiPopularityList").split(",");
		// String[] poiLatitudeArr = intent.getStringExtra("poiLatitudeList").split(",");
		// String[] poiLongitudeArr = intent.getStringExtra("poiLongitudeList").split(",");
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
}
