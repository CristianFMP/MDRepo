package md.smartitineraryclient;

import md.smartitineraryclient.database.*;
import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	private ItinerariesDataSource datasourceIt;
	private PreferencesDataSource datasourceP;
	private InterestsDataSource datasourceIn;

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	
	    datasourceIt = new ItinerariesDataSource(this);
	    datasourceIt.open();
	    
	    datasourceP = new PreferencesDataSource(this);
	    datasourceP.open();
	    
	    datasourceIn = new InterestsDataSource(this);
	    datasourceIn.open();
	    /*
	    List<Itinerary> valuesIt = datasourceIt.getAllItineraries();
	    
	    List<Preference> valuesP = datasourceP.getAllPreferences();
	    
	    List<Interest> valuesIn = datasourceIn.getAllInterests();
	
	    
	    // use the SimpleCursorAdapter to show the
	    // elements in a ListView
	    ArrayAdapter<Itinerary> adapterIt = new ArrayAdapter<Itinerary>(this, android.R.layout.simple_list_item_1, valuesIt);
	    setListAdapter(adapterIt);
	    
	    ArrayAdapter<Preference> adapterP = new ArrayAdapter<Preference>(this, android.R.layout.simple_list_item_1, valuesP);
	    setListAdapter(adapterP);
	    
	    ArrayAdapter<Interest> adapterIn = new ArrayAdapter<Interest>(this, android.R.layout.simple_list_item_1, valuesIn);
	    setListAdapter(adapterIn);
        */
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
    datasourceIt.open();
    datasourceP.open();
    datasourceIn.open();
    super.onResume();
  }

  @Override
  protected void onPause() {
    datasourceIt.close();
    datasourceP.close();
    datasourceIn.close();
    super.onPause();
  }

} 