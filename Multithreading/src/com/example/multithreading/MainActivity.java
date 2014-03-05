package com.example.multithreading;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.app.Activity;

public class MainActivity extends Activity {

	private static class AsyncAddItem extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			for (int i = 1; i <= END_VALUE; i++) {
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
			currentActivity_.threadButton_.setEnabled(false);
			currentActivity_.asyncButton_.setEnabled(false);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			currentActivity_.threadButton_.setEnabled(true);
			currentActivity_.asyncButton_.setEnabled(true);
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Integer... value) {
			super.onProgressUpdate(value);
			if (value[0] == END_VALUE) {
				currentActivity_.listAdapter_.remove((value[0] - 1) + "");
				currentActivity_.listAdapter_.add(currentActivity_.getString(R.string.added));
			} else {
				currentActivity_.listAdapter_.remove((value[0] - 1) + "");
				currentActivity_.listAdapter_.add(value[0] + "");
			}
		}
	}

	private static int DELAY_TIME = 1000;
	private static int END_VALUE = 11;

	private static MainActivity currentActivity_;

	private ListView taskList_;
	private Button threadButton_;
	private Button asyncButton_;

	private AsyncAddItem asyncAddItem_;
	private ArrayAdapter<String> listAdapter_;
	private int count_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		threadButton_ = (Button) findViewById(R.id.thread);
		asyncButton_ = (Button) findViewById(R.id.async);
		taskList_ = (ListView) findViewById(R.id.task_list);

		MainActivity oldObject = (MainActivity) getLastNonConfigurationInstance();
		if (oldObject != null) {
			listAdapter_ = oldObject.listAdapter_;
			threadButton_.setEnabled(oldObject.threadButton_.isEnabled());
			asyncButton_.setEnabled(oldObject.asyncButton_.isEnabled());
		} else {
			listAdapter_ = new ArrayAdapter<String>(this, R.layout.list_item, R.id.title);
		}
		taskList_.setAdapter(listAdapter_);
		currentActivity_ = this;
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return this;
	}

	public void threadClick(View view) {
		count_ = 0;
		new Thread() {
			public void run() {
				while (count_++ < END_VALUE) {
					try {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								currentActivity_.threadButton_.setEnabled(false);
								currentActivity_.asyncButton_.setEnabled(false);
								if (count_ == END_VALUE) {
									listAdapter_.remove((count_ - 1) + "");
									listAdapter_.add(getString(R.string.added));
									currentActivity_.threadButton_.setEnabled(true);
									currentActivity_.asyncButton_.setEnabled(true);
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
		asyncAddItem_ = new AsyncAddItem();
		asyncAddItem_.execute("");
	}
}
