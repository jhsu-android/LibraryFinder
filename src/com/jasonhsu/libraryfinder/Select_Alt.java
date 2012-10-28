package com.jasonhsu.libraryfinder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
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

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Select_Alt extends Activity {
	// For checking the Internet connection
	ConnectionDetector ConnectionDetector1;  // Connection detector class
	Boolean isInternetPresent = false;  // flag for Internet connection status
	TextView TextViewInternet;
	String StringInternetStatus;
	
	// For getting the location
	String LocationString;
    InputStream InputStream1 = null;
    String json_str = "";
    JSONObject JSONObject1 = null;
    ArrayList<HashMap<String, String>> ResultList = new ArrayList<HashMap<String, String>>();
    public static String KEY_ADDRESS = "formatted_address"; // Place area name
    public static String KEY_LAT = "lat";
    public static String KEY_LNG = "lng";
    
	int n_addresses;
	
	// For listing locations
	ListView ListView1;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_alt);
        
        // Check for Internet connection
        ConnectionDetector1 = new ConnectionDetector(getApplicationContext());
		isInternetPresent = ConnectionDetector1.isConnectingToInternet();
		TextViewInternet = (TextView) findViewById(R.id.textViewAltInstr);
		if (isInternetPresent) {
			TextViewInternet.setText(R.string.warning_noaddress);
		}
		
		// Get all possible addresses with Google Places API
		// Based on:
		// http://stackoverflow.com/questions/3574644/how-can-i-find-the-latitude-and-longitude-from-address
        LocationString = DataSave.LocationAlt;
        // String url1 = "http://maps.google.com/maps/api/geocode/json?sensor=false&address=" + LocationString;
        String url1 = "http://maps.google.com/maps/api/geocode/json?sensor=false&address=";
        String query1 = LocationString;
        String url2 = url1 + query1;
        
        // Get input data stream from the URL.
        try{
        	URI URI1 = new URI(url2.replace (' ', '+'));
        	HttpClient HttpClient1 = new DefaultHttpClient();
        	HttpPost HttpPost1 = new HttpPost(URI1);
        	HttpResponse Response1 = HttpClient1.execute(HttpPost1);
        	HttpEntity Entity1 = Response1.getEntity();
        	InputStream1 = Entity1.getContent();
        }
        catch(Exception e){
        	Log.e("Select_Alt", "Error in http connection "+e.toString());
        }
        
        // Convert the downloaded input stream into a string.
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
        catch(Exception e) {
        	Log.e("Select_Alt", "Error converting result "+e.toString());
        }

        Log.i ("CHECK 1", json_str);
        
        // Parse the data stream into a JSON object.  
        try {
        	JSONObject1 = new JSONObject(json_str);
        }
        catch (JSONException e) {
        	Log.e("Select_Alt", "Error parsing data "+e.toString());
        }
        
        // Collect the desired data from the JSON object and
        // place the information in an array.
        try {
        	JSONArray JSONresults = null;
        	JSONresults = JSONObject1.getJSONArray("results");
        	ResultList.clear();
        	for (int i = 0; i < JSONresults.length (); i++) {
        		JSONObject r = JSONresults.getJSONObject(i);
        		String FieldIndex = String.valueOf(i);
        		String FieldAddress = r.getString(KEY_ADDRESS);

        		JSONObject JSONgeometry = r.getJSONObject("geometry");
        		JSONObject JSONlocation = JSONgeometry.getJSONObject("location");
        		String FieldLat = JSONlocation.getString(KEY_LAT);
        		String FieldLng = JSONlocation.getString(KEY_LNG);

        		HashMap<String, String> HashMap1 = new HashMap<String, String>();
        		HashMap1.put ("index", FieldIndex);
        		HashMap1.put (KEY_ADDRESS, FieldAddress);
        		HashMap1.put (KEY_LAT, FieldLat);
        		HashMap1.put (KEY_LNG, FieldLng);

        		ResultList.add (HashMap1);
        	}
        }
        catch (Exception e) {
        	Log.e ("Select_Alt", "Failed to process JSONObject1");
        }
        
        // Display possible address in ListView
        ListView1 = (ListView) findViewById(R.id.listView1);
        HashMap<String, String> HashMap1 = new HashMap<String, String>();
        int list_length = ResultList.size();
        
		ListAdapter adapter = new SimpleAdapter(Select_Alt.this, ResultList,
                R.layout.list_addresses,
                new String[] { KEY_ADDRESS}, new int[] {
                        R.id.textViewAddress});
		ListView1.setAdapter(adapter);
		
        if (list_length > 0) {
        	TextViewInternet.setText(R.string.select_alt_instr);
        }
        
        ListView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				// Get value from selected list item
				String reference = ((TextView) arg1.findViewById(R.id.listView1)).getText().toString();
				
				// Intent
			}
        	
        });
     
    }
    

}
