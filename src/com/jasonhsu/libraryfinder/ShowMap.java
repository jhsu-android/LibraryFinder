package com.jasonhsu.libraryfinder;

import android.os.Bundle;

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
	
	// For drawing the map
	private MapView MapView1;
	private MapController MapController1;
	private GeoPoint GeoPoint1;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		// Start map
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_map);
        DrawMap ();
    
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void DrawMap () {
		MapView1 = (MapView)findViewById(R.id.mapview1);
		MapController1 = MapView1.getController();
		//GeoPoint GeoPoint1 = new GeoPoint (LatInt1, LongInt1);
		//MapController1.setCenter (GeoPoint1);
		MapView1.setBuiltInZoomControls(true); // Add zoom control
		MapController1.setZoom(15); // Set zoom level
	}
}
