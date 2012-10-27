package com.jasonhsu.libraryfinder;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Intro extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.intro, menu);
        return true;
    }
}
