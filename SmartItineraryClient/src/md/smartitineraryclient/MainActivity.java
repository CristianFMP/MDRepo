package md.smartitineraryclient;

import md.smartitineraryclient.database.*;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.*;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements LocationListener, LocationSource {
	
	private ItinerariesDataSource datasourceIt;
	private InterestsDataSource datasourceIn;
	
    private GoogleMap mMap;
    SupportMapFragment sMapFragment;
    private OnLocationChangedListener mListener;
    private LocationManager locationManager;
    private String provider;
    LatLng current_location;

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    
	    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	    
	    if(locationManager != null)
        {
            boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
             
            if(gpsIsEnabled || networkIsEnabled) {
	            if(gpsIsEnabled) {
	                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 10F, this);
	            	provider = "LocationManager.GPS_PROVIDER";
	            }
	            if(networkIsEnabled) {
	                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 10F, this);
	                provider = "LocationManager.NETWORK_PROVIDER";
	            }
            } else {
            	Toast.makeText(this, "Abilita il GPS o la rete internet per rilevare la posizione.", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            //Show some generic error dialog because something must have gone wrong with location manager.
            System.err.println("Error with location manager.");

        }
     	
	    setUpMapIfNeeded();
	    
	    // Set the SQLite client DB
	    datasourceIt = new ItinerariesDataSource(this);
	    datasourceIt.open();
	    
	    datasourceIn = new InterestsDataSource(this);
	    datasourceIn.open();
	    
	    /*
	    List<Itinerary> valuesIt = datasourceIt.getAllItineraries();
	    
	    List<Interest> valuesIn = datasourceIn.getAllInterests();
	
	    
	    // use the SimpleCursorAdapter to show the
	    // elements in a ListView
	    ArrayAdapter<Itinerary> adapterIt = new ArrayAdapter<Itinerary>(this, android.R.layout.simple_list_item_1, valuesIt);
	    setListAdapter(adapterIt);
	    
	    
	    ArrayAdapter<Interest> adapterIn = new ArrayAdapter<Interest>(this, android.R.layout.simple_list_item_1, valuesIn);
	    setListAdapter(adapterIn);
        */
  	}
  
	
	@Override
	protected void onResume() {
		datasourceIt.open();
		datasourceIn.open();
		super.onResume();
		
		setUpMapIfNeeded();
        
        if(locationManager != null)
        {
            mMap.setMyLocationEnabled(true);
        }
	}
	
	@Override
	protected void onPause() {
		datasourceIt.close();
	    datasourceIn.close();
	    
	    if(locationManager != null)
        {
            locationManager.removeUpdates(this);
        }
         
        super.onPause();
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
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
		Intent intent = new Intent(this, SearchActivity.class);
	    startActivity(intent);
		
	}
	
	private void openFavourites() {
		Intent intent = new Intent(this, FavouritesActivity.class);
	    startActivity(intent);
		
	}

	/**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * 
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView
     * MapView}) will show a prompt for the user to install/update the Google Play services APK on
     * their device.
     * 
     * A user can return to this Activity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the Activity may not have been
     * completely destroyed during this process (it is likely that it would only be stopped or
     * paused), {@link #onCreate(Bundle)} may not be called again so we should call this method in
     * {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) 
        {
            
        	// Obtain the map from the FragmentManager
        	mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            
            // Check if we were successful in obtaining the map.
            if (mMap != null) 
            {
                setUpMap();
            }
            
            //This is how you register the LocationSource
            mMap.setLocationSource(this);
            
        }
        
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CurrentLocation(provider), 15));
    }
    
    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() 
    {
    	// Spot of the current position
        mMap.setMyLocationEnabled(true);
    }
     
    // Gets the coordinates of the current location 
    protected LatLng CurrentLocation(String provider) {
		LatLng current_location;
        Location location = locationManager.getLastKnownLocation(provider); // TODO: esce sempre null, e con il provider GPS crasha!
        if (location != null) {
        	current_location = new LatLng(location.getLatitude(), location.getLongitude());
        } else {
        	current_location = new LatLng(0, 0);
        }
		return current_location;
    }
    
    @Override
    public void activate(OnLocationChangedListener listener) 
    {
        mListener = listener;
    }
     
    @Override
    public void deactivate() 
    {
        mListener = null;
    }
 
    @Override
    public void onLocationChanged(Location location) 
    {
    	if( mListener != null )
        {
            mListener.onLocationChanged( location );
     
            LatLngBounds bounds = this.mMap.getProjection().getVisibleRegion().latLngBounds;
     
            if(!bounds.contains(new LatLng(location.getLatitude(), location.getLongitude())))
            {
                 //Move the camera to the user's location once it's available!
                 mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            }
        }
    	
    }
 
    @Override
    public void onProviderDisabled(String provider) 
    {
        Toast.makeText(this, "provider disabled", Toast.LENGTH_SHORT).show();
    }
 
    @Override
    public void onProviderEnabled(String provider) 
    {
        Toast.makeText(this, "provider enabled", Toast.LENGTH_SHORT).show();
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) 
    {
        Toast.makeText(this, "status changed", Toast.LENGTH_SHORT).show();
    }
	
} 