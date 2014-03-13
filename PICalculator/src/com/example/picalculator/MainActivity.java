package com.example.picalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class MainActivity extends Activity {

	public static final String START_KEY = "start";

	private Spinner precisions_;
	private Button startButton_;
	private Button cancelButton_;
	private String currentPrecision_;

	private BroadcastReceiver calculateReceiver_ = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				int resultCode = bundle.getInt(PiCalculateService.RESULT_KEY);
				if (resultCode == Activity.RESULT_OK) {
					startButton_.setVisibility(Button.VISIBLE);
					cancelButton_.setVisibility(Button.GONE);
					precisions_.setEnabled(true);
				} else {
					precisions_.setEnabled(false);
					startButton_.setVisibility(Button.GONE);
					cancelButton_.setVisibility(Button.VISIBLE);
					ArrayAdapter<CharSequence> spinnerAdapter = (ArrayAdapter<CharSequence>) precisions_.getAdapter();
					precisions_.setSelection(spinnerAdapter.getPosition(bundle.getCharSequence(PiCalculateService.PRECISION_KEY)));
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		startButton_ = (Button) findViewById(R.id.start_button);
		cancelButton_ = (Button) findViewById(R.id.cancel_button);
		precisions_ = (Spinner) findViewById(R.id.precision_spinner);
		
		startButton_.setVisibility(Button.VISIBLE);
		cancelButton_.setVisibility(Button.GONE);
		currentPrecision_ = precisions_.getSelectedItem().toString();
		precisions_.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				currentPrecision_ = precisions_.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(calculateReceiver_, new IntentFilter(getPackageName()));
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(calculateReceiver_);
	}

	public void onStartClicked(View view) {
		Intent intent = new Intent(this, PiCalculateService.class);
		intent.putExtra(PiCalculateService.PRECISION_KEY, currentPrecision_);
		startService(intent);
	}

	public void onCancelClicked(View view) {
		stopService(new Intent(this, PiCalculateService.class));
	}

}
