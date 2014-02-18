package com.example.activityrotator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void firstActivityButtonClick(View view) {
		Intent intent = new Intent(this, FirstActivity.class);
		startActivity(intent);
	}

	public void secondActivityButtonClick(View view) {
		Intent intent = new Intent(this, SecondActivity.class);
		startActivity(intent);
	}

	public void thirdActivityButtonClick(View view) {
		Intent intent = new Intent(this, ThirdActivity.class);
		startActivity(intent);
	}

	public void fourthActivityButtonClick(View view) {
		Intent intent = new Intent(this, FourthActivity.class);
		startActivity(intent);
	}

}
