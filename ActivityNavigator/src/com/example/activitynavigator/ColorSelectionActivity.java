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

public class ColorSelectionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_color_selection);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.color_selection, menu);
		return true;
	}

	public void onRedButtonClick(View view) {
		Intent intent = new Intent(this, ShowColorActivity.class);
		intent.putExtra("color", R.color.red);
		startActivity(intent);
	}

	public void onGreenButtonClick(View view) {
		Intent intent = new Intent(this, ShowColorActivity.class);
		intent.putExtra("color", R.color.green);
		startActivity(intent);
	}

	public void onBlueButtonClick(View view) {
		Intent intent = new Intent(this, ShowColorActivity.class);
		intent.putExtra("color", R.color.blue);
		startActivity(intent);
	}

	public void onBlackButtonClick(View view) {
		Intent intent = new Intent(this, ShowColorActivity.class);
		intent.putExtra("color", R.color.black);
		startActivity(intent);
	}

}
