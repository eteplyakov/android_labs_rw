package com.example.multithreading;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.app.Activity;

public class MainActivity extends Activity {

	private class AsyncAddItem extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			for (int i = 1; i < 12; i++) {
				publishProgress(i);
				try {
					Thread.sleep(DELAY_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			threadButton_.setEnabled(false);
			asyncButton_.setEnabled(false);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			threadButton_.setEnabled(true);
			asyncButton_.setEnabled(true);
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Integer... value) {
			super.onProgressUpdate(value);
			if (value[0] == 11) {
				listAdapter_.remove((value[0] - 1) + "");
				listAdapter_.add(getString(R.string.added));
			} else {
				listAdapter_.remove((value[0] - 1) + "");
				listAdapter_.add(value[0] + "");
			}
		}
	}

	private static int DELAY_TIME = 1000;

	private ListView taskList_;
	private Button threadButton_;
	private Button asyncButton_;

	private ArrayAdapter<String> listAdapter_;
	private int count_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		threadButton_ = (Button) findViewById(R.id.thread);
		asyncButton_ = (Button) findViewById(R.id.async);
		taskList_ = (ListView) findViewById(R.id.task_list);

		listAdapter_ = new ArrayAdapter<String>(this, R.layout.list_item, R.id.title);
		taskList_.setAdapter(listAdapter_);
	}

	public void threadClick(View view) {
		count_ = 0;
		new Thread() {
			public void run() {
				while (count_++ < 11) {
					try {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								threadButton_.setEnabled(false);
								asyncButton_.setEnabled(false);
								if (count_ == 11) {
									listAdapter_.remove((count_ - 1) + "");
									listAdapter_.add(getString(R.string.added));
									threadButton_.setEnabled(true);
									asyncButton_.setEnabled(true);
								} else {
									listAdapter_.remove((count_ - 1) + "");
									listAdapter_.add(count_ + "");
								}
							}
						});
						Thread.sleep(DELAY_TIME);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	public void asyncClick(View view) {
		new AsyncAddItem().execute("");
	}
}
