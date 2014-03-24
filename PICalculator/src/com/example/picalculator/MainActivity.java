package com.example.picalculator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public class ReadPiTask extends AsyncTask<String, Void, String> {

		protected String doInBackground(String... precision) {
			String result = "";
			try {
				FileInputStream inputStream = new FileInputStream(Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/calculated pi (" + precision[0] + ").txt");
				if (inputStream != null) {
					InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
					BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

					char[] charString = new char[PI_LENGTH];
					bufferedReader.read(charString, 0, PI_LENGTH);

					inputStream.close();
					result = String.copyValueOf(charString);
				}
			} catch (FileNotFoundException e) {
				Toast.makeText(getApplicationContext(), getString(R.string.error) + " " + e.getMessage(),
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(), getString(R.string.error) + " " + e.getMessage(),
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			calculatedPiTextView_.setText(result);
		}
	}

	private static final int MAX_PROGRESS = 100;
	private static final int PI_LENGTH = 1000;;

	private Spinner precisionsSpinner_;
	private Button startButton_;
	private Button cancelButton_;
	private ProgressBar progressBar_;
	private TextView progressTextView_;
	private TextView calculatedPiTextView_;
	private TextView timeTextView_;

	private BroadcastReceiver calculateReceiver_ = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (bundle == null) {
				return;
			}
			int resultCode = bundle.getInt(PiCalculateService.RESULT_KEY);
			if (resultCode == Activity.RESULT_OK) {
				startButton_.setVisibility(Button.VISIBLE);
				cancelButton_.setVisibility(Button.GONE);
				precisionsSpinner_.setEnabled(true);
				progressBar_.setProgress(MAX_PROGRESS);

				String precision = bundle.getString(PiCalculateService.PRECISION_KEY);
				new ReadPiTask().execute(precision);

				boolean checkResult = bundle.getBoolean(PiCalculateService.CHECK_KEY);
				if (checkResult) {
					progressTextView_.setText(R.string.end_calculate_true);
				} else {
					progressTextView_.setText(R.string.end_calculate_false);
				}

				timeTextView_.setText(bundle.getString(PiCalculateService.TIME_KEY) + " "
						+ getString(R.string.time_sample));
				timeTextView_.setVisibility(TextView.VISIBLE);
			} else {
				ArrayAdapter<CharSequence> spinnerAdapter = (ArrayAdapter<CharSequence>) precisionsSpinner_
						.getAdapter();
				precisionsSpinner_.setSelection(spinnerAdapter.getPosition(bundle
						.getCharSequence(PiCalculateService.PRECISION_KEY)));

				int progress = bundle.getInt(PiCalculateService.PROGRESS_KEY, 0);
				progressBar_.setProgress(progress);
				progressTextView_.setText(String.valueOf(progress) + "%");
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		startButton_ = (Button) findViewById(R.id.start_button);
		cancelButton_ = (Button) findViewById(R.id.cancel_button);
		precisionsSpinner_ = (Spinner) findViewById(R.id.precision_spinner);
		progressBar_ = (ProgressBar) findViewById(R.id.progress);
		progressTextView_ = (TextView) findViewById(R.id.progress_label);
		calculatedPiTextView_ = (TextView) findViewById(R.id.pi_result);
		timeTextView_ = (TextView) findViewById(R.id.time);

		calculatedPiTextView_.setMovementMethod(new ScrollingMovementMethod());
		progressBar_.setMax(MAX_PROGRESS);
		startButton_.setVisibility(Button.VISIBLE);
		cancelButton_.setVisibility(Button.GONE);
		timeTextView_.setVisibility(TextView.GONE);

		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (PiCalculateService.class.getName().equals(service.service.getClassName())) {
				startButton_.setVisibility(Button.GONE);
				cancelButton_.setVisibility(Button.VISIBLE);
				progressTextView_.setText(R.string.in_progress);
			}
		}

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			int result = extras.getInt(PiCalculateService.RESULT_KEY, Activity.RESULT_OK);
			if (result == Activity.RESULT_CANCELED) {
				precisionsSpinner_.setEnabled(true);
				progressBar_.setProgress(MAX_PROGRESS);

				String precision = extras.getString(PiCalculateService.PRECISION_KEY);
				ArrayAdapter<CharSequence> spinnerAdapter = (ArrayAdapter<CharSequence>) precisionsSpinner_
						.getAdapter();
				precisionsSpinner_.setSelection(spinnerAdapter.getPosition(precision));
				precisionsSpinner_.setEnabled(true);

				new ReadPiTask().execute(precision);

				boolean checkResult = extras.getBoolean(PiCalculateService.CHECK_KEY);
				if (checkResult) {
					progressTextView_.setText(R.string.end_calculate_true);
				} else {
					progressTextView_.setText(R.string.end_calculate_false);
				}

				timeTextView_.setText(extras.getString(PiCalculateService.TIME_KEY) + " "
						+ getString(R.string.time_sample));
				timeTextView_.setVisibility(TextView.VISIBLE);
			} else {
				int progress = extras.getInt(PiCalculateService.PROGRESS_KEY, 0);
				if (progress != 0) {
					progressTextView_.setText(String.valueOf(progress) + "%");
					progressBar_.setProgress(progress);
				}

				String precision = extras.getString(PiCalculateService.PRECISION_KEY);
				if (precision != null) {
					ArrayAdapter<CharSequence> spinnerAdapter = (ArrayAdapter<CharSequence>) precisionsSpinner_
							.getAdapter();
					precisionsSpinner_.setSelection(spinnerAdapter.getPosition(precision));
					precisionsSpinner_.setEnabled(false);
				}
			}
		}
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
		startButton_.setVisibility(Button.GONE);
		timeTextView_.setVisibility(TextView.GONE);
		cancelButton_.setVisibility(Button.VISIBLE);
		precisionsSpinner_.setEnabled(false);
		calculatedPiTextView_.setText(R.string.pi_sample);

		Intent intent = new Intent(this, PiCalculateService.class);
		intent.putExtra(PiCalculateService.PRECISION_KEY, precisionsSpinner_.getSelectedItem().toString());
		startService(intent);
	}

	public void onCancelClicked(View view) {
		stopService(new Intent(this, PiCalculateService.class));
		startButton_.setVisibility(Button.VISIBLE);
		cancelButton_.setVisibility(Button.GONE);
		precisionsSpinner_.setEnabled(true);
		progressTextView_.setText(R.string.canceled_text);
	}
}
