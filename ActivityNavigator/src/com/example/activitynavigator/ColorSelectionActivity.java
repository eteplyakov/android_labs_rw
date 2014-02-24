/**
 * Activity Navigator
 * Copyright (c) 2014 com.example.activitynavigator
 *
 * Created: Feb 12, 2014 by Egor Teplyakov
 */
package com.example.activitynavigator;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class ColorSelectionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_color_selection);
	}

	public void onRedButtonClick(View view) {
		startActivity(ShowColorActivity.makeIntentShowColor(this, R.color.red));
	}

	public void onGreenButtonClick(View view) {
		startActivity(ShowColorActivity.makeIntentShowColor(this, R.color.green));
	}

	public void onBlueButtonClick(View view) {
		startActivity(ShowColorActivity.makeIntentShowColor(this, R.color.blue));
	}

	public void onBlackButtonClick(View view) {
		startActivity(ShowColorActivity.makeIntentShowColor(this, R.color.black));
	}

}
