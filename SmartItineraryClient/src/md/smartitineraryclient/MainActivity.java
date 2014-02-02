package md.smartitineraryclient;

import java.util.List;
import md.smartitineraryclient.database.*;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class MainActivity extends ListActivity {
	private ItinerariesDataSource datasource;
	private PreferencesDataSource datasourceP;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	
	    datasource = new ItinerariesDataSource(this);
	    datasource.open();
	    
	    datasourceP = new PreferencesDataSource(this);
	    datasourceP.open();
	
	    List<Itinerary> values = datasource.getAllItineraries();
	    
	    List<Preference> valuesP = datasourceP.getAllPreferences();
	
	    
	    // use the SimpleCursorAdapter to show the
	    // elements in a ListView
	    ArrayAdapter<Itinerary> adapter = new ArrayAdapter<Itinerary>(this, android.R.layout.simple_list_item_1, values);
	    setListAdapter(adapter);
	    
	    ArrayAdapter<Preference> adapterP = new ArrayAdapter<Preference>(this, android.R.layout.simple_list_item_1, valuesP);
	    setListAdapter(adapterP);
        
  	}
  
  	
/*
  // Will be called via the onClick attribute
  // of the buttons in main.xml
  public void onClick(View view) {
    @SuppressWarnings("unchecked")
    ArrayAdapter<Itinerary> adapter = (ArrayAdapter<Itinerary>) getListAdapter();
    Itinerary itinerary = null;
    switch (view.getId()) {
    case R.id.add:
      String[] comments = new String[] { "Cool", "Very nice", "Hate it" };
      int nextInt = new Random().nextInt(3);
      // save the new comment to the database
      itinerary = datasource.createItinerary(itinerary[nextInt]);
      adapter.add(itinerary);
      break;
    case R.id.delete:
      if (getListAdapter().getCount() > 0) {
    	  itinerary = (Itinerary) getListAdapter().getItem(0);
        datasource.deleteItinerary(itinerary);
        adapter.remove(itinerary);
      }
      break;
    }
    adapter.notifyDataSetChanged();
  }
*/
  @Override
  protected void onResume() {
    datasource.open();
    datasourceP.open();
    super.onResume();
  }

  @Override
  protected void onPause() {
    datasource.close();
    datasourceP.close();
    super.onPause();
  }

} 