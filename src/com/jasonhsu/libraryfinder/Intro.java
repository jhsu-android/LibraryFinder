package com.jasonhsu.libraryfinder;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Intro extends Activity {
	
	Button ButtonCont; // Initializes button

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        addContinueButton();
    }
    
    public void addContinueButton() { 
		final Context Context1 = this;
		// button = ButtonCont (Continue button) from res/layout/main.xml
		ButtonCont = (Button) findViewById(R.id.buttonCont);
		
		// Defines what happens when the Continue button is clicked
		ButtonCont.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// Run src/com.jasonhsu.libraryfinder/SelectCereal.java 
			    Intent Intent1 = new Intent(Context1, Select.class);
                startActivity(Intent1);   
			}
 
		});
    }
 
}
