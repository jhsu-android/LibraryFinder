package com.jasonhsu.libraryfinder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.content.pm.PackageInfo;
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
	
	// From DataSave
	//String StringLat = DataSave.LatIntStr;
	//String StringLong = DataSave.LongIntStr;
	//String StringRadius = DataSave.RadiusEntered;
	//int LatInt1 = Integer.parseInt(StringLat);
	//int LongInt1 = Integer.parseInt(StringLong);;
	//double radius_miles = Double.parseDouble (StringRadius);
	//double radius_meters = radius_miles * 1609.34; 
	//double radius = radius_meters * 1.73; // Fudge factor due to rectangular screen
	
	// Related to API keys
	static final String DEBUG_TAG = "YourApp";
	static final String DEBUG_SIG = "3:23:-52:-46:-100:103:123:21:76:-2:34:-83:-22:-20:34:-114:-76:0:103:19:";
	static final String DEBUG_KEY = "0u0qdTa16jZ3pIvkIx9B4-KD1mBGJfZvv6mYZOQ";
	static final String RELEASE_KEY = "0u0qdTa16jZ2ST9cMEEFLrQZvSZU5MBxuD74G8g";
	static String staticMapsApiKey = null;
	
	// For drawing the map
	private MapView MapView1;
	private MapController MapController1;
	private GeoPoint GeoPoint1;
	
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
        MapController1 = MapView1.getController();
        
        // Center on desired location
        String LatStr1 = DataSave.LatIntStr;
        String LongStr1 = DataSave.LongIntStr;
        double LatDouble1 = Double.parseDouble(LatStr1);
        double LongDouble1 = Double.parseDouble(LongStr1);
        int LatInt1 = (int) (LatDouble1 * 1000000);
        int LongInt1 = (int) (LongDouble1 * 1000000);
        GeoPoint GeoPoint1 = new GeoPoint (LatInt1, LongInt1);
        MapController1.setCenter (GeoPoint1);
        
        MapView1.setBuiltInZoomControls(true); // Add zoom control
        MapController1.setZoom(15); // Set zoom level 
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
