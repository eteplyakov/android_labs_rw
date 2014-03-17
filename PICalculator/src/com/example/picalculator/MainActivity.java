package com.example.picalculator;

import java.io.BufferedReader;
import java.io.File;
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
import android.content.res.ColorStateList;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final int MAX_PROGRESS = 100;

	private Spinner precisions_;
	private Button startButton_;
	private Button cancelButton_;
	private ProgressBar progressBar_;
	private TextView progressLabel_;
	private TextView calculatedPi_;
	private TextView time_;

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
					progressBar_.setProgress(MAX_PROGRESS);
					String precision = bundle.getString(PiCalculateService.PRECISION_KEY);
					calculatedPi_.setText(readFromFile(precision));
					if (isMD5Checked(precision)) {
						progressLabel_.setText(R.string.end_calculate_true);
					} else {
						progressLabel_.setText(R.string.end_calculate_false);
					}
					time_.setText(bundle.getString(PiCalculateService.TIME_KEY) + " " + getString(R.string.time_sample));
					time_.setVisibility(TextView.VISIBLE);
				} else {
					ArrayAdapter<CharSequence> spinnerAdapter = (ArrayAdapter<CharSequence>) precisions_.getAdapter();
					precisions_.setSelection(spinnerAdapter.getPosition(bundle
							.getCharSequence(PiCalculateService.PRECISION_KEY)));
					int progress = bundle.getInt(PiCalculateService.PROGRESS_KEY, 0);
					progressBar_.setProgress(progress);
					progressLabel_.setText(String.valueOf(progress) + "%");
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
		progressBar_ = (ProgressBar) findViewById(R.id.progress);
		progressLabel_ = (TextView) findViewById(R.id.progress_label);
		calculatedPi_ = (TextView) findViewById(R.id.pi_result);
		time_ = (TextView) findViewById(R.id.time);

		calculatedPi_.setMovementMethod(new ScrollingMovementMethod());
		progressBar_.setMax(MAX_PROGRESS);
		startButton_.setVisibility(Button.VISIBLE);
		cancelButton_.setVisibility(Button.GONE);
		time_.setVisibility(TextView.GONE);

		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (PiCalculateService.class.getName().equals(service.service.getClassName())) {
				startButton_.setVisibility(Button.GONE);
				cancelButton_.setVisibility(Button.VISIBLE);
			}
		}
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			int result = extras.getInt(PiCalculateService.RESULT_KEY, Activity.RESULT_OK);
			if (result == Activity.RESULT_CANCELED) {
				precisions_.setEnabled(true);
				progressBar_.setProgress(MAX_PROGRESS);
				String precision = extras.getString(PiCalculateService.PRECISION_KEY);
				ArrayAdapter<CharSequence> spinnerAdapter = (ArrayAdapter<CharSequence>) precisions_.getAdapter();
				precisions_.setSelection(spinnerAdapter.getPosition(precision));
				calculatedPi_.setText(readFromFile(precision));
				precisions_.setEnabled(true);
				if (isMD5Checked(precision)) {
					progressLabel_.setText(R.string.end_calculate_true);
				} else {
					progressLabel_.setText(R.string.end_calculate_false);
				}
				time_.setText(extras.getString(PiCalculateService.TIME_KEY) + " " + getString(R.string.time_sample));
				time_.setVisibility(TextView.VISIBLE);
			} else {
				int progress = extras.getInt(PiCalculateService.PROGRESS_KEY, 0);
				if (progress != 0) {
					progressLabel_.setText(String.valueOf(progress) + "%");
					progressBar_.setProgress(progress);
				}

				String precision = extras.getString(PiCalculateService.PRECISION_KEY);
				if (precision != null) {
					ArrayAdapter<CharSequence> spinnerAdapter = (ArrayAdapter<CharSequence>) precisions_.getAdapter();
					precisions_.setSelection(spinnerAdapter.getPosition(precision));
					precisions_.setEnabled(false);
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
		time_.setVisibility(TextView.GONE);
		cancelButton_.setVisibility(Button.VISIBLE);
		precisions_.setEnabled(false);
		calculatedPi_.setText(R.string.pi_sample);
		Intent intent = new Intent(this, PiCalculateService.class);
		intent.putExtra(PiCalculateService.PRECISION_KEY, precisions_.getSelectedItem().toString());
		startService(intent);
		File dir = new File(Environment.getExternalStorageDirectory().toString() + "/cash_pi");
		String[] children = dir.list();
		for (int i = 0; i < children.length; i++) {
			new File(dir, children[i]).delete();
		}
	}

	public void onCancelClicked(View view) {
		stopService(new Intent(this, PiCalculateService.class));
		startButton_.setVisibility(Button.VISIBLE);
		cancelButton_.setVisibility(Button.GONE);
		precisions_.setEnabled(true);
		progressLabel_.setText(R.string.canceled_text);
	}

	private String readFromFile(String precision) {
		String result = "";
		try {
			FileInputStream inputStream = new FileInputStream(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/calculated pi (" + precision + ").txt");
			if (inputStream != null) {
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				char[] charString = new char[1000];
				bufferedReader.read(charString, 0, 1000);
				inputStream.close();
				result = String.copyValueOf(charString);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private boolean isMD5Checked(String precision) {
		File root = Environment.getExternalStorageDirectory();
		File file = new File(root, "calculated pi (" + precision + ").txt");
		String md5 = null;
		try {
			md5 = MD5CheckSum.getMD5Checksum(file.getAbsolutePath());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] sums = getResources().getStringArray(R.array.precisions_md5);
		boolean checkFlag = false;
		for (int i = 0; i < sums.length; i++) {
			if (sums[i].equalsIgnoreCase(md5)) {
				checkFlag = true;
				break;
			}
		}
		return checkFlag;

	}

}
