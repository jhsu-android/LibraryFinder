package com.jasonhsu.libraryfinder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

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
	
	// For getting information on places
	public static String KEY_INDEX = "index"; // ID of the place
	public static String KEY_REFERENCE = "reference"; // ID of the place
	public static String KEY_NAME = "name"; // name of the place
	public static String KEY_ADDRESS = "vicinity"; // Place area name
	public static String KEY_LAT = "lat";
	public static String KEY_LNG = "lng";
	ArrayList<HashMap<String, String>> ResultList = new ArrayList<HashMap<String, String>>();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
		
		// STEP 1: Setting API key
		String api_key;
		api_key = getMapsApiKey (this);        
        
        // STEP 2: Draw map
        MapView1 = new MapView (this, api_key);
        setContentView(MapView1);
        MapView1.setClickable(true);
        MapController1 = MapView1.getController();
        
        // STEP 3: Center on desired location
        String LatStr1 = DataSave.LatDoubleStr;
        String LongStr1 = DataSave.LongDoubleStr;
        double LatDouble1 = Double.parseDouble(LatStr1);
        double LongDouble1 = Double.parseDouble(LongStr1);
        int LatInt1 = (int) (LatDouble1 * 1000000);
        int LongInt1 = (int) (LongDouble1 * 1000000);
        GeoPoint GeoPoint1 = new GeoPoint (LatInt1, LongInt1);
        MapController1.setCenter (GeoPoint1);
        
        // STEP 4: Get the appropriate level of zoom
        String StringRadius = DataSave.RadiusEntered;
    	double radius_miles = Double.parseDouble (StringRadius);
    	double radius_meters = radius_miles * 1609.34; 
    	
    	// radius: used for searching for places
    	// Fudge factor is used to avoid missing areas shown on the screen
    	// The user's screen may be rectangular.
    	// Zoom levels differ by a factor of 2.
    	double radius = radius_meters * 2; // Fudge factor due to rectangular screen
    	
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
         
        // STEP 5: SEARCH FOR PLACES
    	// STEP 5A: Get the URL
    	// Borrowed from 
    	// http://p-xr.com/android-tutorial-how-to-parse-read-json-data-into-a-android-listview/
    	// http://www.androidhive.info/2012/01/android-json-parsing-tutorial/
    	// URL PARAMETERS
    	String API_KEY = "AIzaSyCRLa4LQZWNQBcjCYcIVYA45i9i8zfClqc";
    	String types = "library";
    	String URL1 = "https://maps.googleapis.com/maps/api/place/search/json?";
    	URL1 = URL1 + "key=" + API_KEY;
    	URL1 = URL1 + "&location=" + LatDouble1 + "," + LongDouble1;
    	URL1 = URL1 + "&radius=" + String.valueOf(radius);
    	URL1 = URL1 + "&sensor=false";
    	URL1 = URL1 + "&types=" + types;
    	
    	Log.i ("ShowMap - Part 5A", URL1);
    			
    	// URL for location 55402 and radius of 5 miles
    	// https://maps.googleapis.com/maps/api/place/search/json?key=AIzaSyCRLa4LQZWNQBcjCYcIVYA45i9i8zfClqc&location=44.975922,-93.272186&radius=12926.5&sensor=false&types=library
    			
    	InputStream InputStream1 = null;
    	String json_str = "";
    	JSONObject JSONObject1 = null;
    			
    	// Step 5B: Get input data stream from the URL.
    	try{
    		HttpClient HttpClient1 = new DefaultHttpClient();
    		HttpPost HttpPost1 = new HttpPost(URL1);
    		HttpResponse Response1 = HttpClient1.execute(HttpPost1);
    		HttpEntity Entity1 = Response1.getEntity();
    		InputStream1 = Entity1.getContent(); 
    	}
    	catch(Exception e){
    		Log.e("ShowMap - Step 5B", "Error in http connection "+e.toString());
    	}
    			
    	// STEP 5C: Convert the downloaded input stream into a string. 
    	try{
    		BufferedReader Reader1 = new BufferedReader(new InputStreamReader(InputStream1,"iso-8859-1"),8);
    		StringBuilder Builder1 = new StringBuilder();
    		String line = null;
    		while ((line = Reader1.readLine()) != null) {
    			Builder1.append(line + "\n");
    		}
    		InputStream1.close();
    		json_str = Builder1.toString();
    	}
    			
    	catch(Exception e){
    		Log.e("ShowMap - Step 5C", "Error converting result "+e.toString());
    	}
    			
    	Log.i ("ShowMap - Step 5C", json_str);
    			
    	// STEP 5D: Parse the data stream into a JSON object.  
    	try {
    		JSONObject1 = new JSONObject(json_str);
    	}
    	catch (JSONException e) {
    		Log.e("ShowMap - Step 5D", "Error parsing data "+e.toString());
    	}
    			
    			
    	// STEP 5E: Collect the desired data from the JSON object and
    	// place the information in an array.
    	// Borrowed from
    	// http://www.androidhive.info/2012/01/android-json-parsing-tutorial/
    	try {
    		JSONArray JSONresults = null;
    		JSONresults = JSONObject1.getJSONArray("results"); 
    		ResultList.clear();
    				
    		for (int i = 0; i < JSONresults.length (); i++) {	
    			JSONObject r = JSONresults.getJSONObject(i);
    			String FieldIndex = String.valueOf(i);
    			String FieldName = r.getString(KEY_NAME);
    			String FieldAddress = r.getString(KEY_ADDRESS);
    					
    			JSONObject JSONgeometry = r.getJSONObject("geometry");
    			JSONObject JSONlocation = JSONgeometry.getJSONObject("location");
    			String FieldLat = JSONlocation.getString(KEY_LAT);
    			String FieldLng = JSONlocation.getString(KEY_LNG);
    					
    			HashMap<String, String> HashMap1 = new HashMap<String, String>();
    			HashMap1.put ("index", FieldIndex);
    			HashMap1.put (KEY_NAME, FieldName);
    			HashMap1.put (KEY_ADDRESS, FieldAddress);
    			HashMap1.put (KEY_LAT, FieldLat);
    			HashMap1.put (KEY_LNG, FieldLng);
    					
    			ResultList.add (HashMap1);
    		}
    				
    	}
    	catch (Exception e) {
    		Log.e ("ShowMap - Step 5E", "Failed to process JSONObject1");
    	}
        
    	// STEP 6: Draw markers for each found place
		Drawable MarkerPlaces = getResources().getDrawable(R.drawable.marker);
		MarkerPlaces.setBounds( (int) (-MarkerPlaces.getIntrinsicWidth()/2),
				(int) (-MarkerPlaces.getIntrinsicHeight()/2),
				(int) (MarkerPlaces.getIntrinsicWidth()/2),
				(int) (MarkerPlaces.getIntrinsicHeight()/2)
				);
		PlacesFound PlacesFound1 = new PlacesFound (MarkerPlaces);
		MapView1.getOverlays().add(PlacesFound1);
    	
	}
	
	// Based on the example on pages 608-610 in the book _Pro Android 4_
	class PlacesFound extends ItemizedOverlay {
		private ArrayList<OverlayItem> places =
				new ArrayList<OverlayItem>();
		private GeoPoint center = null;
		public PlacesFound (Drawable marker_local) {
			super(marker_local);
			// TODO Auto-generated constructor stub
			try {
		        HashMap<String, String> HashMap1 = new HashMap<String, String>();
		    	int list_length = ResultList.size();
		    	for (int i = 0; i < list_length; i++) {
		    		HashMap1 = ResultList.get(i);
		    		String Name = HashMap1.get(KEY_NAME);
		    		String Address = HashMap1.get(KEY_ADDRESS);
		    		String LatStr = HashMap1.get(KEY_LAT);
		    		String LngStr = HashMap1.get(KEY_LNG);
		    		double LatDouble = Double.valueOf(LatStr);
		    		double LngDouble = Double.valueOf(LngStr);
		    		int LatInt = (int)(LatDouble*1000000);
		    		int LngInt = (int)(LngDouble*1000000);
		    		GeoPoint GeoPointPlace = new GeoPoint (LatInt, LngInt);
		    		places.add(new OverlayItem (GeoPointPlace, Name, Address));
		    		populate();
		    	}
			}
			catch (Exception e) {
				Log.e ("ShowMap - PlacesFound", "ERROR: Could not extract places data");
			}
				
		}			

		@Override
		protected OverlayItem createItem(int arg0) {
			// TODO Auto-generated method stub
			return places.get(arg0);
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return places.size();
		}
			
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
