package md.smartitineraryclient;

// import md.smartitineraryclient.database.*;
import android.app.Activity;
import android.os.Bundle;
import android.view.*;

public class MainActivity extends Activity {
	// private ItinerariesDataSource datasourceIt;
	// private InterestsDataSource datasourceIn;

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    
	    /*
	    datasourceIt = new ItinerariesDataSource(this);
	    datasourceIt.open();
	    
	    datasourceIn = new InterestsDataSource(this);
	    datasourceIn.open();
	    
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
	  @Override
	  protected void onResume() {
	    datasourceIt.open();
	    datasourceIn.open();
	    super.onResume();
	  }
	
	  @Override
	  protected void onPause() {
	    datasourceIt.close();
	    datasourceIn.close();
	    super.onPause();
	  }
	*/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_search:
	            openSearch();
	            return true;
	        case R.id.action_favourites:
	            openFavourites();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	private void openSearch() {
		// TODO Auto-generated method stub
		
	}
	
	private void openFavourites() {
		// TODO Auto-generated method stub
		
	}
	
} 