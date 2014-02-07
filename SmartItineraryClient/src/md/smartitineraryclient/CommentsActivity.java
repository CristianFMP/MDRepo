package md.smartitineraryclient;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class CommentsActivity extends Activity {
	private static final String SERVICE_URL = "http://159.149.177.116:8080/SmartItineraryWebService/rest/comment";
	private static final String TAG = "CommentsActivity";
	private static final String TEXT1 = "text1";
	private static final String TEXT2 = "text2";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments);
		// Show the Up button in the action bar.
		setupActionBar();
		String url = SERVICE_URL + "/getComments";
		// Intent intent = getIntent();
		// se si usa POST verificare di inviare anche la/e stringa/e di parametri oltre all'url
		// String param = intent.getStringExtra("poiId");
		String param = "https://foursquare.com/v/1-world-financial-center/4b95354af964a520249534e3";
		// web service calls must be executed in a separate thread
		WebServiceTask wst = new WebServiceTask(WebServiceTask.POST_TASK,
				this, "Retrieving Comments...");
		// show toast with real url
		Context context = getApplicationContext();
		CharSequence text = url;
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		// execute the call
		wst.execute(new String[] { url, param });
		String response = wst.getResponse();
		handleResponse(response);
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
		getMenuInflater().inflate(R.menu.comments, menu);
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

	public void handleResponse(String response) {
		try {
			List<Comment> commentList = new ArrayList<Comment>();
			JSONArray jArray = new JSONArray(response);
			// TODO: handle JSONexceptions
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject tmpJObj = jArray.getJSONObject(i);
				String comment = tmpJObj.getString("comment");
				Long timestamp = tmpJObj.getLong("timestamp");
				commentList.add(new Comment(comment, new Timestamp(timestamp)));
			}
			updateCommentList(updateListToItem(commentList));
		} catch (Exception e) {
			Log.e(TAG, e.getLocalizedMessage(), e);
		}
	}

	private void updateCommentList(List<Map<String, String>> updateListToItem) {
		final List<Map<String, String>> rows = updateListToItem;		
		// Comment ListView
		final String[] fromMapKey = new String[] {TEXT1, TEXT2};
	    final int[] toLayoutId = new int[] {android.R.id.text1, android.R.id.text2};
		ListView commentListView = (ListView) findViewById(R.id.commentList);
		ListAdapter commentAdapter = new SimpleAdapter(this, rows, android.R.layout.simple_list_item_2, fromMapKey, toLayoutId);
		commentListView.setAdapter(commentAdapter);
	}

	private List<Map<String, String>> updateListToItem(List<Comment> commentList) {
		List<Map<String, String>> listItem = new ArrayList<Map<String, String>>();
		for (int i = 0; i < commentList.size(); i++) {
			Comment tmpComment = commentList.get(i);
			String comment = tmpComment.getComment();
			String date = DateFormat.getDateTimeInstance().format(commentList.get(i).getDate());
			Map<String, String> listItemMap = new HashMap<String, String>();
			listItemMap.put(TEXT1, comment);
			listItemMap.put(TEXT2, date);
			listItem.add(listItemMap);
		}
		return listItem;
	}
}
