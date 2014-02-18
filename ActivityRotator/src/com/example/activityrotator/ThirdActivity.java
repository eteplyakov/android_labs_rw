package com.example.activityrotator;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.EditText;

public class ThirdActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_third);
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		EditText editText1 = (EditText) findViewById(R.id.first_edit_text);
		EditText editText2 = (EditText) findViewById(R.id.second_edit_text);
		savedInstanceState.putString("first_edit_text", editText1.getText().toString());
		savedInstanceState.putString("second_edit_text", editText2.getText().toString());
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		EditText editText1 = (EditText) findViewById(R.id.first_edit_text);
		EditText editText2 = (EditText) findViewById(R.id.second_edit_text);
		editText1.setText(savedInstanceState.getString("first_edit_text"));
		editText2.setText(savedInstanceState.getString("second_edit_text"));
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}

}
