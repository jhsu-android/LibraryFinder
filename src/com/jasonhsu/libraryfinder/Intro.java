package com.jasonhsu.libraryfinder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Intro extends Activity {
	
	ConnectionDetector ConnectionDetector1;  // Connection detector class
	Boolean isInternetPresent = false;  // flag for Internet connection status
	TextView TextView1;
	String StringInternetStatus;
	
	Button ButtonCont; // Initializes button

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        addContinueButton();
        
        ConnectionDetector1 = new ConnectionDetector(getApplicationContext());
        
		// Check if Internet present
		isInternetPresent = ConnectionDetector1.isConnectingToInternet();
		TextView1 = (TextView) findViewById(R.id.textStatusInternet);
		if (!isInternetPresent) {
			StringInternetStatus = "\nWARNING: You are not connected to the Internet.  " +
					"Please connect to the Internet first.";
			TextView1.setText(StringInternetStatus);
			LinearLayout LinearLayout1;
			LinearLayout1 = (LinearLayout) findViewById (R.id.LinearLayoutIntro);
			ButtonCont = (Button) findViewById(R.id.buttonCont);
			LinearLayout1.removeView(ButtonCont);
		}
		else {
			StringInternetStatus = "\nInternet Connection: OK\n";
			TextView1.setText(StringInternetStatus);
		}
    }
    
    public void addContinueButton() { 
		final Context Context1 = this;
		// button = ButtonCont (Continue button) from res/layout/main.xml
		ButtonCont = (Button) findViewById(R.id.buttonCont);
		
		// Defines what happens when the Continue button is clicked
		ButtonCont.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// Run src/com.jasonhsu.libraryfinder/Select.java 
			    Intent Intent1 = new Intent(Context1, Select.class);
                startActivity(Intent1);   
			}
 
		});
    }
 
}
