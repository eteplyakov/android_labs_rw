/**
 * Activity Navigator
 * Copyright (c) 2014 com.example.activitynavigator
 *
 * Created: Feb 12, 2014 by Egor Teplyakov
 */
package com.example.activitynavigator;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ShowColor extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);	//disable activity title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,	//enable fullscreen
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_show_color);
		Intent intent = getIntent();
		int color = intent.getIntExtra("color",-1);
		View view = this.getWindow().getDecorView();
	    view.setBackgroundColor(getResources().getColor(color)); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_color, menu);
		return true;
	}
	
	public void onBackButtonClick(View view){
		super.onBackPressed();	//imitation pressing the "back" hardware button
    }
	public void onHomeButtonClick(View view){
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);	//clear stack of activities
		startActivity(intent);
    }

}
