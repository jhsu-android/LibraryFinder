// Source:
// http://www.androidhive.info/2012/08/android-working-with-google-places-and-maps-tutorial/

// Purpose: ConnectionDetector determines whether or not you are connected to the Internet.
// External inputs: none
// External outputs: true if connected, false if not connected

package com.jasonhsu.libraryfinder;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {
	 
    private Context _context;
 
    public ConnectionDetector(Context context) {
        this._context = context;
    }
 
    /**
     * Checking for all possible internet providers
     * **/
    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
        	NetworkInfo[] info = connectivity.getAllNetworkInfo();
        	if (info != null) {
        		for (int i = 0; i < info.length; i++) {
        			if (info[i].getState() == NetworkInfo.State.CONNECTED) {
        				return true;
        			}
        		}
        	}
        }
        return false;
    }
}
