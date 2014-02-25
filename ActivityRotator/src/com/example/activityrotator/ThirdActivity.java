package com.example.activityrotator;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class ThirdActivity extends Activity {

	private static String EDIT_TEXT_1="first_edit_text";
	private static String EDIT_TEXT_2="second_edit_text";
	
	private EditText editText1_;
	private EditText editText2_;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_third);
		editText1_ = (EditText) findViewById(R.id.first_edit_text);
		editText2_ = (EditText) findViewById(R.id.second_edit_text);
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putString(EDIT_TEXT_1, editText1_.getText().toString());
		savedInstanceState.putString(EDIT_TEXT_2, editText2_.getText().toString());
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		editText1_.setText(savedInstanceState.getString(EDIT_TEXT_1));
		editText2_.setText(savedInstanceState.getString(EDIT_TEXT_2));
	}

}
