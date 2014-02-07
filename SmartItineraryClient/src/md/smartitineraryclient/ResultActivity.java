package md.smartitineraryclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import md.smartitineraryclient.Itinerary;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
	private static final String SERVICE_URL = "http://77.44.155.79:8080/SmartItineraryWebService/rest/itinerary";
	private static final String TAG = "MainActivity";
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
		
	private class WebServiceTask extends AsyncTask<String, Integer, String> {
		public static final int POST_TASK = 1;
		public static final int GET_TASK = 2;	
		private static final String TAG = "WebServiceTask";
		// connection timeout, in milliseconds (waiting to connect)
		private static final int CONN_TIMEOUT = 10000;
		// socket timeout, in milliseconds (waiting for data)
		private static final int SOCKET_TIMEOUT = 13000;
		private int taskType = GET_TASK;
		private Context mContext = null;
		private String processMessage = "Processing...";

		// params are used only for UrlEncoded POST requests
		private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		private ProgressDialog pDlg = null;

		public WebServiceTask(int taskType, Context mContext, String processMessage) {
			this.taskType = taskType;
			this.mContext = mContext;
			this.processMessage = processMessage;
		}
		
		private void showProgressDialog() {
			pDlg = new ProgressDialog(mContext);
			pDlg.setMessage(processMessage);
			pDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDlg.setCancelable(false);
			pDlg.show();
		}
		
		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}
		
		@Override
		protected String doInBackground(String... urls) {
			String url = urls[0];
			String result = "";

			HttpResponse response = doResponse(url);

			if (response == null) {
				return result;
			} else {
				try {
					result = inputStreamToString(response.getEntity().getContent());
				} catch (IllegalStateException e) {
					Log.e(TAG, e.getLocalizedMessage(), e);
				} catch (IOException e) {
					Log.e(TAG, e.getLocalizedMessage(), e);
				}
			}
			return result;
		}
		
		@Override
		protected void onPostExecute(String response) {
			handleResponse(response);
			pDlg.dismiss();
		}

		private String inputStreamToString(InputStream content) {
			String line = "";
			StringBuilder total = new StringBuilder();

			// Wrap a BufferedReader around the InputStream
			BufferedReader rd = new BufferedReader(new InputStreamReader(content));

			try {
				// Read response until the end
				while ((line = rd.readLine()) != null) {
					total.append(line);
				}
			} catch (IOException e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
			}
			// Return full string
			return total.toString();
		}

		private HttpResponse doResponse(String url) {
			// Use our connection and data timeouts as parameters for our
			// DefaultHttpClient
			HttpClient httpclient = new DefaultHttpClient(getHttpParams());
			HttpResponse response = null;

			try {
				switch (taskType) {
					case POST_TASK:
						HttpPost httppost = new HttpPost(url);
						// Add parameters
						httppost.setEntity(new UrlEncodedFormEntity(params));
						response = httpclient.execute(httppost);
						break;
					case GET_TASK:
						HttpGet httpget = new HttpGet(url);
						response = httpclient.execute(httpget);
						break;
				}
			} catch (Exception e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
			}
			return response;
		}

		private HttpParams getHttpParams() {
			HttpParams htpp = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
			HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);
			return htpp;
		}
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
