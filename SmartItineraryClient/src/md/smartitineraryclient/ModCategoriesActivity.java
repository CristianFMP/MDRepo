package md.smartitineraryclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.json.JSONException;
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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class ModCategoriesActivity extends Activity {
	private static final String SERVICE_URL = "http://192.168.0.18:8080/SmartItineraryWebService/rest/category";
	private static final String TAG = "ModCategoriesActivity";
	private Map<String, List<String>> categories;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mod_categories);
		// Show the Up button in the action bar.
		setupActionBar();
		String url = SERVICE_URL + "/getCategories";
		WebServiceTask wst = new WebServiceTask(WebServiceTask.GET_TASK, this, "Retrieving Categories...");
		// show toast with real url
		Context context = getApplicationContext();
		CharSequence text = url;
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		// execute the call
		wst.execute(new String[] { url });
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
		getMenuInflater().inflate(R.menu.mod_categories, menu);
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
		case R.id.action_favourites:
            openFavourites();
            return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void openFavourites() {
		Intent intent = new Intent(this, FavouritesActivity.class);
	    startActivity(intent);
	    overridePendingTransition(0,0);
	}
	
	public void handleResponse(String response) {
		categories = new HashMap<String, List<String>>();
		List<String> subCats = new ArrayList<String>();
		try {
			JSONArray jArray = new JSONArray(response);
			// TODO: handle JSONexceptions
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject tmpJObj = jArray.getJSONObject(i);
				String macro_cat = tmpJObj.getString("category");
				JSONArray subJArray = tmpJObj.getJSONArray("subCategories");
				for (int j = 0; j < subJArray.length(); j++) {
					subCats.add(subJArray.getString(j));
				}
				categories.put(macro_cat, subCats);
			}
			Log.d(TAG, categories.size()+"");
			updateMacroCatsSpinner();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void updateMacroCatsSpinner() {
		Set<String> macro_cats_set = categories.keySet();
		String[] macro_cats = new String[8];
		macro_cats_set.toArray(macro_cats);
		List<String> list = Arrays.asList(macro_cats);
		for (String s : list)
			Log.d(TAG, s);
		Spinner spMacroCats = (Spinner) findViewById(R.id.macro_categories_spinner);
		ArrayAdapter<String> mcAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, macro_cats);
		spMacroCats.setAdapter(mcAdapter);
	}

	private class WebServiceTask extends AsyncTask<String, Integer, String> {
		public static final int POST_TASK = 1;
		public static final int GET_TASK = 2;	
		private static final String TAG = "WebServiceTask";
		// connection timeout, in milliseconds (waiting to connect)
		private static final int CONN_TIMEOUT = 3000;
		// socket timeout, in milliseconds (waiting for data)
		private static final int SOCKET_TIMEOUT = 50000;
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
		protected String doInBackground(String... params) {
			String url = params[0];
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
}
