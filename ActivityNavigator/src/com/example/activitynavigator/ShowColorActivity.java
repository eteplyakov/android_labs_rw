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
import android.view.Window;
import android.view.WindowManager;

public class ShowColorActivity extends Activity {

	private static final String KEY_COLOR = "color";

	public static Intent makeIntentShowColor(Context packageContext, int color) {
		Intent intent = new Intent(packageContext, ShowColorActivity.class);
		intent.putExtra(KEY_COLOR, color);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_show_color);
		Intent intent = getIntent();
		int color = intent.getIntExtra(KEY_COLOR, R.color.black);
		View view = this.getWindow().getDecorView();
		view.setBackgroundColor(getResources().getColor(color));
	}

	public void onBackButtonClick(View view) {
		super.onBackPressed();
	}

	public void onHomeButtonClick(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

}
