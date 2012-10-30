package com.jasonhsu.libraryfinder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class ShowMap extends MapActivity {
	
	// Related to API keys
	static final String DEBUG_TAG = "YourApp";
	static final String DEBUG_SIG = "3:23:-52:-46:-100:103:123:21:76:-2:34:-83:-22:-20:34:-114:-76:0:103:19:";
	static final String DEBUG_KEY = "0u0qdTa16jZ3pIvkIx9B4-KD1mBGJfZvv6mYZOQ";
	static final String RELEASE_KEY = "0u0qdTa16jZ2ST9cMEEFLrQZvSZU5MBxuD74G8g";
	static String staticMapsApiKey = null;
	
	// For drawing the map
	private MapView MapView1;
	private MapController MapController1;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
		
		// Setting API key
		String api_key;
		api_key = getMapsApiKey (this);        
        Log.i ("ShowMap", api_key);
        
        // Draw map
        MapView1 = new MapView (this, api_key);
        setContentView(MapView1);
        MapView1.setClickable(true);
        MapController1 = MapView1.getController();
        
        // Center on desired location
        String LatStr1 = DataSave.LatDoubleStr;
        String LongStr1 = DataSave.LongDoubleStr;
        double LatDouble1 = Double.parseDouble(LatStr1);
        double LongDouble1 = Double.parseDouble(LongStr1);
        int LatInt1 = (int) (LatDouble1 * 1000000);
        int LongInt1 = (int) (LongDouble1 * 1000000);
        GeoPoint GeoPoint1 = new GeoPoint (LatInt1, LongInt1);
        MapController1.setCenter (GeoPoint1);
        
        // Get the appropriate level of zoom
        String StringRadius = DataSave.RadiusEntered;
    	double radius_miles = Double.parseDouble (StringRadius);
    	double radius_meters = radius_miles * 1609.34; 
    	
    	// radius: used for searching for places
    	// Fudge factor is used to avoid missing areas shown on the screen
    	// The user's screen may be rectangular.
    	// Zoom levels differ by a factor of 2.
    	double radius = radius_meters * 4; // Fudge factor due to rectangular screen
    	
    	double earth_circum = 40007863.0; // Meters
    	double d_lat_deg = earth_circum/360;  // Meters per degree of latitude
    	// Meters per degree of longitude
    	double d_long_deg = d_lat_deg * Math.cos (LongDouble1*Math.PI/180);
    	double d_lat_micro = d_lat_deg/1E6; // Meters per micro-degree of latitude
    	double d_long_micro = d_long_deg/1E6;
    	
    	MapView1.setBuiltInZoomControls(true); // Add zoom control
    	// Widest zoom: level 1; width is about 25,000 miles (Earth's circumference)
    	// and the represented distance from the center to the left or right edge is
    	// half that (12,5000 miles)
    	// Increasing the zoom factor by 1 reduces the search radius by a factor of 2
    	// Narrowest zoom: level 20
    	// NOTE: This script is designed to err on the side of showing more rather than
    	// showing less.  The user has the option of zooming in.
    	int zoom_factor;
    	zoom_factor = (int) (-3.333333 * Math.log10(radius_miles) + 14.666667);
    	MapController1.setZoom(zoom_factor);        
         
        // SEARCH FOR PLACES
    	// Borrowed from 
    	// http://p-xr.com/android-tutorial-how-to-parse-read-json-data-into-a-android-listview/
    	// http://www.androidhive.info/2012/01/android-json-parsing-tutorial/
    	
        
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	// Debug mode and release mode require different API keys.
	// The getMapsApiKey function selects the appropriate API key.
	// From the example at 
	// http://whereblogger.klaki.net/2009/10/choosing-android-maps-api-key-at-run.html
	public static String getMapsApiKey(Context context) {
		if (staticMapsApiKey == null) {
			try {
				staticMapsApiKey = RELEASE_KEY;
				Signature [] sigs = context.getPackageManager()
						.getPackageInfo(context.getPackageName(),
								PackageManager.GET_SIGNATURES)
								.signatures;
				for (int i = 0; i < sigs.length; i++) {
					MessageDigest sha = MessageDigest.getInstance("SHA-1");
					sha.update(sigs[i].toByteArray());
					byte [] digest = sha.digest();
					String str = "";
					for (int di = 0; di < digest.length; di++) {
						str += Byte.toString(digest[di])+":";
					}
					Log.i("ShowMap", "Got key sig: " + str);
					if (str.equals(DEBUG_SIG)) {
						Log.i (DEBUG_TAG, "Oh look, a debug signature!");
						staticMapsApiKey = DEBUG_KEY;
						break;
					}
		      }
		    } catch (NoSuchAlgorithmException e) {
		      e.printStackTrace();
		    } catch (NameNotFoundException e) {
		      e.printStackTrace();
		    }      
		  }
		  return staticMapsApiKey;
		}

}
