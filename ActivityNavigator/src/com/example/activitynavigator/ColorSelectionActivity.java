/**
 * Activity Navigator
 * Copyright (c) 2014 com.example.activitynavigator
 *
 * Created: Feb 12, 2014 by Egor Teplyakov
 */
package com.example.activitynavigator;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

public class ColorSelectionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_color_selection);
	}

	public static void createIntent(int color, Context packageContext) {
		Intent intent = new Intent(packageContext, ShowColorActivity.class);
		intent.putExtra("color", color);
		packageContext.startActivity(intent);
	}

	public void onRedButtonClick(View view) {
		createIntent(R.color.red, this);
	}

	public void onGreenButtonClick(View view) {
		createIntent(R.color.green, this);
	}

	public void onBlueButtonClick(View view) {
		createIntent(R.color.blue, this);
	}

	public void onBlackButtonClick(View view) {
		createIntent(R.color.black, this);
	}

}
