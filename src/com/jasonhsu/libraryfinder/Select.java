package com.jasonhsu.libraryfinder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class Select extends Activity {
	EditText text_radius;
	double radius_double;
	boolean SelectedLocationCurrent;
	Button ButtonSubmit; // Initializes button
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select);
        addSubmitButton();
    }
    
    public void addSubmitButton() { 
		final Context Context1 = this;
		ButtonSubmit = (Button) findViewById(R.id.buttonSubmit);
		ButtonSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				RadioButton ButtonCurrent = (RadioButton) findViewById(R.id.radioCurrent);
				EditText text_radius = (EditText) findViewById (R.id.editRadius);
				radius_double = Double.parseDouble(text_radius.getText().toString());
				
				if (ButtonCurrent.isChecked()) {
				    Intent Intent1 = new Intent(Context1, Select_Current.class);
	                startActivity(Intent1);   
				}
				else {
					EditText EditTextLocation = (EditText)findViewById(R.id.editAddress);
					String LocationEntered = EditTextLocation.getText().toString();
					DataSave.LocationAlt = LocationEntered;
					DataSave.RadiusEntered = Double.toString(radius_double);
					Intent Intent1 = new Intent(Context1, Select_Alt.class);
	                startActivity(Intent1);
				}
				
			}
 
		});
    }
    
}
