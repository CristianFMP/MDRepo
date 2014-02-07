package md.smartitineraryclient;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class CommentsActivity extends Activity {
	private static final String SERVICE_URL = "http://159.149.177.116:8080/SmartItineraryWebService/rest/comment";
	private static final String TAG = "CommentsActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments);
		// Show the Up button in the action bar.
		setupActionBar();
		String url = SERVICE_URL + "/getComments";
		// web service calls must be executed in a separate thread
		WebServiceTask wst = new WebServiceTask(WebServiceTask.POST_TASK,
				this, "Retrieving Comments...");
		// show toast with reql url
		Context context = getApplicationContext();
		CharSequence text = url;
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		// execute the call
		wst.execute(new String[] { url });
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
		// TODO Auto-generated method stub
		
	}
}
