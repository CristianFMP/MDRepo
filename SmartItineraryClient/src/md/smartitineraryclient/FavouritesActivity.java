package md.smartitineraryclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import md.smartitineraryclient.db.DatabaseHelper;
import md.smartitineraryclient.model.Interest;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.support.v4.app.NavUtils;

public class FavouritesActivity extends Activity {
	
	private List<Interest> catList;
	private ArrayList<String> list;
	private String cat = "";
	DatabaseHelper databaseHelper;
	private static final String TEXT1 = "text1";
	private static final String TEXT2 = "text2";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favourites);
		setupActionBar();
		
		/** Recupera gli interessi memorizzati in locale, e li mostra */
		catList = new ArrayList<Interest>();
		list = new ArrayList<String>();
		
		databaseHelper = new DatabaseHelper(this);
		Cursor c = databaseHelper.getAllInterests();
		try {
			while (c.moveToNext()) {
				//Long id = c.getLong(0);
				String categ = c.getString(1);
				String macroc = c.getString(2);
				String ins = c.getString(3);
				String canc = c.getString(4);
				if(canc==null) {
					list.add(categ);
					catList.add(new Interest(categ, macroc, ins, canc));
					cat += categ+",";
				}
			}
			if(cat.length()!=0){
				cat = cat.substring(0, cat.length()-1);
			}
			
		} finally {
			c.close();
		}
		updateCategoryList(updateListToItem(catList)); 
	}


	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.favourites, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			overridePendingTransition(0,0);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private List<Map<String, String>> updateListToItem(List<Interest> catList) {
		List<Map<String, String>> listItem = new ArrayList<Map<String, String>>();
		for (int i = 0; i < catList.size(); i++) {
			Interest tmpInterest = catList.get(i);
			String category = tmpInterest.getCategoria();
			String macrocat = tmpInterest.getMacrocategoria();
			Map<String, String> listItemMap = new HashMap<String, String>();
			listItemMap.put(TEXT1, category);
			listItemMap.put(TEXT2, macrocat); // per ora non viene mostrato
			listItem.add(listItemMap);
		}
		return listItem;
	}
	
	private void updateCategoryList(List<Map<String, String>> updateListToItem) {
		final List<Map<String, String>> rows = updateListToItem;		
		// Category ListView
		final String[] fromMapKey = new String[] {TEXT1, TEXT2};
	    final int[] toLayoutId = new int[] {android.R.id.text1, android.R.id.text2};
		ListView catListView = (ListView) findViewById(R.id.listView_categories);
		ListAdapter catAdapter = new SimpleAdapter(this, rows, android.R.layout.simple_list_item_2, fromMapKey, toLayoutId);
		catListView.setAdapter(catAdapter);
	}
	
	public void openInterests(View view) {
		Intent intent = new Intent(view.getContext(), ModCategoriesActivity.class);
		intent.putStringArrayListExtra("saved_categories", list);
		startActivityForResult(intent, 0);
		overridePendingTransition(0,0);
	}

}
