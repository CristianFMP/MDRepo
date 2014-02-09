package md.smartitineraryclient;

import md.smartitineraryclient.util.*;
import md.smartitineraryclient.util.Utilities.ErrorDialogFragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.*;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements LocationListener, GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener, LocationSource {
	
    private GoogleMap mMap;
    private LocationClient mLocationClient;
    static Location mCurrentLocation;
    private LocationManager locationManager;
    private String provider;
    private OnLocationChangedListener mListener;
    @SuppressWarnings("unused")
	private boolean gps_enabled,network_enabled;
    
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    
	    /*
         * Create a new location client, using the enclosing class to
         * handle callbacks.
         */
        mLocationClient = new LocationClient(this, this, this);
	    mLocationClient.connect();
	    
	    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	    
	    if(locationManager != null) {
            boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            
            if(!servicesConnected()) {
            	Toast.makeText(this, "Abilita i servizi Google Play Services sul device.", Toast.LENGTH_LONG).show();
            }
            
            if(gpsIsEnabled || networkIsEnabled) {
	            if(gpsIsEnabled) {
	                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 10F, this);
	            	provider = "LocationManager.GPS_PROVIDER";
	            	gps_enabled = true;
	            } // TODO: rimettere else e impostare un timeout per aggirare ricerca tramite gps dopo un po' che cerca
	            if(networkIsEnabled) {
	                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 10F, this);
	                provider = "LocationManager.NETWORK_PROVIDER";
	                network_enabled = true;
	            }
            } else {
            	Toast.makeText(this, "Abilita il GPS o la rete internet per rilevare la posizione.", Toast.LENGTH_LONG).show();
            }
            
        } else {
            //Show some generic error dialog because something must have gone wrong with location manager.
            System.err.println("Error with location manager.");

        }
	    
	    setUpMapIfNeeded();
  	}
	
	/**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
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
    private void setUpMap() 
    {
    	// Spot of the current position
        mMap.setMyLocationEnabled(true);
    }
     
    /** Check for Google Play services */
    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        // Google Play services was not available for some reason
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode,this,Utilities.CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(getSupportFragmentManager(), "Location Updates");
            }
            return false;
        }
    }
    
    
    /** Gets the coordinates of the current location */
    protected LatLng CurrentLocation(String provider) {
		LatLng current_location;
        if (mCurrentLocation != null) {
        	current_location = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        } else {
        	current_location = new LatLng(0, 0);
        }
		return current_location;
    }
    
    
    /** Handle lifecycle activities, connection and providers */
    
    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();
    }
    
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		
		setUpMapIfNeeded();
        
        if(locationManager != null)
        {
            mMap.setMyLocationEnabled(true);
        }
	}
	
	@Override
	protected void onPause() {
	    
	    if(locationManager != null)
        {
            locationManager.removeUpdates(this);
        }
         
        super.onPause();
	}
    
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	}
	
    @Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
    	/*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        Utilities.CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Utilities.showErrorDialog(connectionResult.getErrorCode());
        }
		
	}

	@Override
	public void onConnected(Bundle bundle) {
		mCurrentLocation = mLocationClient.getLastLocation();
		// Display the connection status
        //Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onDisconnected() {
		// Display the connection status
        Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
	}
    
    @Override
    public void onProviderDisabled(String provider) {
    	Log.d("Latitude","disable");
        Toast.makeText(this, "provider disabled", Toast.LENGTH_SHORT).show();
    }
 
    @Override
    public void onProviderEnabled(String provider) {
    	Log.d("Latitude","enable");
        //Toast.makeText(this, "provider enabled", Toast.LENGTH_SHORT).show();
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    	Log.d("Latitude","status");
        //Toast.makeText(this, "status changed", Toast.LENGTH_SHORT).show();
    }
	
    
    /** Menu's methods */
    
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
	    overridePendingTransition(0,0);
	}
	
	private void openFavourites() {
		Intent intent = new Intent(this, FavouritesActivity.class);
	    startActivity(intent);
	    overridePendingTransition(0,0);
	}

	@Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
    }
     
    @Override
    public void deactivate() {
        mListener = null;
    }
 
    @Override
    public void onLocationChanged(Location location) {
    	mCurrentLocation = location;
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

    
} 