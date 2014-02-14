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
		makeIntent(this, FirstActivity.class);
	}

	public void secondActivityButtonClick(View view) {
		makeIntent(this, SecondActivity.class);
	}

	public void thirdActivityButtonClick(View view) {
		makeIntent(this, ThirdActivity.class);
	}

	public void fourthActivityButtonClick(View view) {
		makeIntent(this, FourthActivity.class);
	}
	private static void makeIntent(Context packageContext, Class<?> cls) {
		Intent intent = new Intent(packageContext, cls);
		packageContext.startActivity(intent);
	}

}
