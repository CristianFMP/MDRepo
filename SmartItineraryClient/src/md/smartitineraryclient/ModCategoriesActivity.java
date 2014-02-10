package md.smartitineraryclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import md.smartitineraryclient.model.Category;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ModCategoriesActivity extends Activity {
	private static final String SERVICE_URL = "http://192.168.0.13:8080/SmartItineraryWebService/rest/category";
	@SuppressWarnings("unused")
	private static final String TAG = "ModCategoriesActivity";
	private Map<String, List<Category>> categories;
	private ArrayAdapter<String> adapter;
	ArrayAdapter<Category> listadapter;
	private List<Category> cats;
	private Spinner spMacroCats;
	ListView lv;

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
	
	// Riceve formato JSON, ne crea una rappresentazione utilizzando una Mappa
	public void handleResponse(String response) {
		categories = new HashMap<String, List<Category>>();	
		try {
			JSONArray jArray = new JSONArray(response);
			// TODO: handle JSONexceptions
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject tmpJObj = jArray.getJSONObject(i);
				String macro_cat = tmpJObj.getString("category");
				JSONArray subJArray = tmpJObj.getJSONArray("subCategories");
				List<Category> subCats = new ArrayList<Category>();
				for (int j = 0; j < subJArray.length(); j++) {
					subCats.add(new Category(subJArray.getString(j), false));
				}
				categories.put(macro_cat, subCats);
			}
			updateMacroCatsSpinner();
		} catch (JSONException e) {
			// TODO
			e.printStackTrace();
		}
	}
	
	// Gestione dimamica di Spinner e listview
	private void updateMacroCatsSpinner() {
		Set<String> macro_cats_set = categories.keySet();
		String[] macro_cats = new String[8];
		macro_cats_set.toArray(macro_cats);
		spMacroCats = (Spinner) findViewById(R.id.macro_categories_spinner);
		lv = (ListView) findViewById(R.id.category_list);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, macro_cats);		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spMacroCats.setAdapter(adapter);
		spMacroCats.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				String macro_category = spMacroCats.getItemAtPosition(pos).toString();
				cats = categories.get(macro_category);
				listadapter = new MyCustomAdapter(parent.getContext(), R.layout.rowcheckbox, cats);
				lv.setAdapter(listadapter);
				listadapter.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO
			}
		});
	}
	
	// Adapter customizzato per gestire oggetti Category ed i checkbox associati
	private class MyCustomAdapter extends ArrayAdapter<Category> {
		private List<Category> list;
		public MyCustomAdapter(Context context, int textViewResourceId, List<Category> list) {
			super(context, textViewResourceId, list);
			this.list = new ArrayList<Category>();
			this.list.addAll(list);
		}
		
		private class ViewHolder {
			TextView code;
			CheckBox name;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.rowcheckbox, null);
				holder = new ViewHolder();
				holder.code = (TextView) convertView.findViewById(android.R.id.text1);
				holder.name = (CheckBox) convertView.findViewById(R.id.checkbox);
				convertView.setTag(holder);
				holder.name.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						Category category = (Category) cb.getTag();
						Toast.makeText(getApplicationContext(), "Categoria " + cb.getText() + " risulta " + cb.isChecked(), Toast.LENGTH_LONG).show();
						category.setSelected(cb.isChecked());
					}
				});
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Category category = list.get(position);
			holder.code.setText(category.getCategory());
			holder.name.setText(category.getCategory());
			holder.name.setChecked(category.isSelected());
			holder.name.setTag(category);
			return convertView;
		}
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
