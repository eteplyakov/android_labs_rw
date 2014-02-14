package com.example.preferences;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent; 
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void setPreferencesButtonClick(View view) {
		Intent intent = new Intent(this, PreferencesActivity.class);
		startActivity(intent);
	}
}
