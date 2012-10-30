// The process of obtaining the location is based on the example at
// http://www.hrupin.com/2011/04/android-gps-using-how-to-get-current-location-example

package com.jasonhsu.libraryfinder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class Select_Current extends Activity {
	// For checking the Internet connection
	ConnectionDetector ConnectionDetector1;  // Connection detector class
	Boolean isInternetPresent = false;  // flag for Internet connection status
	TextView TextViewInternet;
	String StringInternetStatus;
	
	// For checking location services
	private boolean gps_enabled = false;
	private boolean network_enabled = false;
	TextView TextViewLocationGPS, TextViewLocationNetwork, TextViewStatusLocation;
	private LocationManager LocationManager1;
	
	// Acquiring the current location
	private String provider;
	private LocationListener LocationListener1 = new MyLocationListener();
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	double LatDouble1, LongDouble1;
	String LatStr1, LongStr1;

	// For showing current location
	TextView TextViewLat, TextViewLong;
	
	// For moving on to ShowMap.java
	Button ButtonContinue; // Initializes button
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_current);
        
        // Check for Internet connection
        ConnectionDetector1 = new ConnectionDetector(getApplicationContext());
		isInternetPresent = ConnectionDetector1.isConnectingToInternet();
		TextViewInternet = (TextView) findViewById(R.id.textStatusInternetCurrent);
		if (isInternetPresent) {
			TextViewInternet.setText("Internet Status: OK");
		}
		
		// Check for Location service
		LocationManager1 = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		try {
			gps_enabled = LocationManager1.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} 
		catch (Exception ex) {			
		}
		try {
			network_enabled = LocationManager1.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} 
		catch (Exception ex) {			
		}
		
		// Check for location services
		TextViewLocationGPS = (TextView) findViewById(R.id.textStatusLocationGPS);
		TextViewLocationNetwork = (TextView) findViewById(R.id.textStatusLocationNetwork);
		TextViewStatusLocation = (TextView) findViewById(R.id.textStatusLocation);
		if (gps_enabled) {
			TextViewLocationGPS.setText ("GPS Status: working");
			TextViewStatusLocation.setText ("Location Status: working");
		}
		if (network_enabled) {
			TextViewLocationNetwork.setText ("Network Location Status: working");
			TextViewStatusLocation.setText ("Location Status: working");
		}
		
		// Get location
		Criteria Criteria1 = new Criteria();
		Criteria1.setAccuracy(Criteria.ACCURACY_FINE);
		provider = LocationManager1.getBestProvider(Criteria1, true);
		if (network_enabled) {
			LocationManager1.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, LocationListener1);
			addContinueButton();;
		}
		if (gps_enabled) {
			LocationManager1.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, LocationListener1);
			addContinueButton();
		}
		
    }
    
    class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			LocationManager1.removeUpdates(LocationListener1);
			LatDouble1 = location.getLatitude();
			LongDouble1 = location.getLongitude();
			LatStr1 = String.valueOf (LatDouble1);
			LongStr1 = String.valueOf (LongDouble1);
			TextViewLat = (TextView) findViewById (R.id.textLatCurrent);
			TextViewLong = (TextView) findViewById (R.id.textLngCurrent);
			TextViewLat.setText("Latitude: " + LatStr1 + " degrees");
			TextViewLong.setText("Longitude: " + LongStr1 + " degrees");
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
    }
    
    public void addContinueButton () {
    	final Context Context1 = this;
    	ButtonContinue = (Button) findViewById(R.id.buttonSelectCurrent);
    	ButtonContinue.setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View arg0) {
    			DataSave.LatDoubleStr = LatStr1;
    			DataSave.LongDoubleStr = LongStr1;
    			
    			Intent Intent1 = new Intent(Context1, ShowMap.class);
    			startActivity(Intent1);
    		}
    	});
    }

}
