package com.example.picalculator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class PiCalculateService extends IntentService {

	public enum PrecisionsEnum {
		PRECISION_32K("9F6DFF902710728ADBFB1A3B0E1D4213"),
		PRECISION_64K("04E3E19A24E71F915A18414BFD1E5DE0"),
		PRECISION_128K("C6BFA46EE4943527542D83DCA5806039"),
		PRECISION_256K("23B01B069133FB926D908D6C8DFD4693"),
		PRECISION_512K("22AD11C887D8E25D5C143220B79E885E"),
		PRECISION_1M("0A72CE19825CDAC6A8C1C1D9898B0E58"),
		PRECISION_2M("C4A0043A9B320221FB650E1F9DF7CDB4"),
		PRECISION_4M("D22E5199684FD2CB022A18A5A76300CC"),
		PRECISION_8M("B5F7897792B1B19B018352F6E4DF390B"),
		PRECISION_16M("FF7B5518CD85DD9C22D1361CD6AE9BA7");

		private String sum;

		PrecisionsEnum(String sum) {
			this.sum = sum;
		}

		public String getSum() {
			return this.sum;
		}
	}

	private static final int NOTIFICATION_ID = 0;
	private static final int MAX_PROGRESS = 100;
	private static final String SERVICE_NAME = "PiCalculateService";

	public static final String PRECISION_KEY = "precision";
	public static final String RESULT_KEY = "result";
	
	public static final String STATUS_IN_PROGRESS = "in_progress";
	public static final String STATUS_DONE = "done";
	public static final String STATUS_CANCELED = "canceled";
	public static final String STATUS_BAD = "bad";

	private NotificationManager notifyManager_;
	private android.support.v4.app.NotificationCompat.Builder builder_;

	String precisionString_ = "";
	int presicionId_;
	boolean stopFlag_ = false;

	static {
		System.loadLibrary("piv2");
	}

	private native String calculatePi(int fft, String precision);

	public PiCalculateService() {
		super(SERVICE_NAME);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		precisionString_ = intent.getStringExtra(PRECISION_KEY);

		int fft = 0;
		if (precisionString_.equals("32K")) {
			fft = 8000;
		} else {
			if (precisionString_.equals("64K")) {
				fft = 16000;
			} else {
				if (precisionString_.equals("128K")) {
					fft = 20000;
				} else {
					if (precisionString_.equals("256K")) {
						fft = 40000;
					} else {
						if (precisionString_.equals("512K")) {
							fft = 80000;
						} else {
							if (precisionString_.equals("1M")) {
								fft = 160000;
							} else {
								if (precisionString_.equals("2M")) {
									fft = 320000;
								} else {
									if (precisionString_.equals("4M")) {
										fft = 640000;
									} else {
										if (precisionString_.equals("8M")) {
											fft = 1280000;
										} else {
											if (precisionString_.equals("16M")) {
												fft = 2560000;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		final Intent notificationIntentOnProgress = new Intent(this, MainActivity.class);
		notificationIntentOnProgress.setAction(Intent.ACTION_MAIN);
		notificationIntentOnProgress.addCategory(Intent.CATEGORY_LAUNCHER);

		notifyManager_ = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		builder_ = new NotificationCompat.Builder(this);

		builder_.setAutoCancel(true);
		builder_.setContentTitle(getString(R.string.app_name))
				.setContentText(getString(R.string.progress_text) + " " + precisionString_)
				.setSmallIcon(R.drawable.ic_launcher);

		Intent notificationIntent = new Intent(this, MainActivity.class);
		notificationIntent.setAction(Intent.ACTION_MAIN);
		notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		builder_.setContentIntent(contentIntent);
		builder_.setProgress(MAX_PROGRESS, 0, false);
		notifyManager_.notify(NOTIFICATION_ID, builder_.build());

		Cursor cursor = getApplicationContext().getContentResolver().query(PiResultsProvider.CONTENT_URI, null, null,
				null, null);
		cursor.moveToFirst();

		boolean updateFlag = false;

		for (int i = 0; i < cursor.getCount(); i++) {
			if (cursor.getString(cursor.getColumnIndex(PICalculatorDatabaseOpenHelper.PiResults.PRECISION)).equals(
					precisionString_)) {
				presicionId_ = cursor.getInt(cursor.getColumnIndex(PICalculatorDatabaseOpenHelper.PiResults.ID));
				updateFlag = true;
				break;
			}
			cursor.moveToNext();
		}

		cursor.close();

		ContentValues inProgressValues = new ContentValues();
		inProgressValues.put(PICalculatorDatabaseOpenHelper.PiResults.PRECISION, precisionString_);
		inProgressValues.put(PICalculatorDatabaseOpenHelper.PiResults.PROGRESS, "0");
		inProgressValues.put(PICalculatorDatabaseOpenHelper.PiResults.STATUS, STATUS_IN_PROGRESS);

		if (updateFlag) {
			getContentResolver().update(Uri.parse(PiResultsProvider.CONTENT_URI.toString() + "/" + presicionId_),
					inProgressValues, null, null);
		} else {
			Uri addedUri = getContentResolver().insert(PiResultsProvider.CONTENT_URI, inProgressValues);
			presicionId_ = Integer.valueOf(addedUri.getLastPathSegment());
		}

		String piResultTime = calculatePi(fft, precisionString_);

		if (!stopFlag_) {

			Intent notificationIntentOnFinish = new Intent(this, MainActivity.class);
			notificationIntentOnFinish.setAction(Intent.ACTION_MAIN);
			notificationIntentOnFinish.addCategory(Intent.CATEGORY_LAUNCHER);

			PendingIntent contentIntentOnFinish = PendingIntent.getActivity(this, 0, notificationIntentOnFinish,
					PendingIntent.FLAG_UPDATE_CURRENT);

			builder_.setContentIntent(contentIntentOnFinish);
			builder_.setContentText(getString(R.string.finish_text) + " " + precisionString_).setProgress(0, 0, false);
			notifyManager_.notify(NOTIFICATION_ID, builder_.build());

			ContentValues doneValues = new ContentValues();
			doneValues.put(PICalculatorDatabaseOpenHelper.PiResults.PRECISION, precisionString_);
			doneValues.put(PICalculatorDatabaseOpenHelper.PiResults.PROGRESS, "100");
			if(isMD5Checked(precisionString_)){
				doneValues.put(PICalculatorDatabaseOpenHelper.PiResults.STATUS, STATUS_DONE);
			} else {
				doneValues.put(PICalculatorDatabaseOpenHelper.PiResults.STATUS, STATUS_BAD);
			}
			
			doneValues.put(PICalculatorDatabaseOpenHelper.PiResults.TIME, piResultTime);

			getContentResolver().update(Uri.parse(PiResultsProvider.CONTENT_URI.toString() + "/" + presicionId_),
					doneValues, null, null);

		} else {
			ContentValues doneValues = new ContentValues();
			doneValues.put(PICalculatorDatabaseOpenHelper.PiResults.STATUS, STATUS_CANCELED);
			getContentResolver().update(Uri.parse(PiResultsProvider.CONTENT_URI.toString() + "/" + presicionId_),
					doneValues, null, null);

			notifyManager_.cancel(NOTIFICATION_ID);
		}
		Intent doneIntent = new Intent(getPackageName());
		doneIntent.putExtra(RESULT_KEY, Activity.RESULT_OK);
		sendBroadcast(doneIntent);
	}

	@Override
	public void onDestroy() {
		stopFlag_ = true;
		super.onDestroy();
	}

	public void callback(final int value) {
		if (!stopFlag_) {
			new Thread() {

				@Override
				public void run() {
					super.run();
					Intent notificationIntentOnProgress = new Intent(getApplicationContext(), MainActivity.class);
					notificationIntentOnProgress.setAction(Intent.ACTION_MAIN);
					notificationIntentOnProgress.addCategory(Intent.CATEGORY_LAUNCHER);

					PendingIntent contentIntentOnProgress = PendingIntent.getActivity(getApplicationContext(), 0,
							notificationIntentOnProgress, PendingIntent.FLAG_UPDATE_CURRENT);

					builder_.setContentIntent(contentIntentOnProgress);
					builder_.setProgress(MAX_PROGRESS, value, false);
					notifyManager_.notify(0, builder_.build());

					ContentValues progressValue = new ContentValues();
					progressValue.put(PICalculatorDatabaseOpenHelper.PiResults.PROGRESS, String.valueOf(value));

					getContentResolver().update(Uri.parse(PiResultsProvider.CONTENT_URI.toString() + "/" + presicionId_),
							progressValue, null, null);
				}
				
			}.start();
			
		}
	}

	public int getStopFlag() {
		if (stopFlag_) {
			return 1;
		} else {
			return 0;
		}
	}

	private boolean isMD5Checked(String precision) {
		File root = Environment.getExternalStorageDirectory();
		File file = new File(root, "calculated pi (" + precision + ").txt");
		String md5 = null;
		try {
			md5 = ChecksumUtils.calculateMD5ChecksumForFile(file.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}

		boolean checkFlag = false;

		for (PrecisionsEnum precisionMd5 : PrecisionsEnum.values()) {
			if (precisionMd5.getSum().equalsIgnoreCase(md5)) {
				checkFlag = true;
				break;
			}
		}

		return checkFlag;
	}

	private void writeToFile(String data, String precision) {
		try {

			File root = Environment.getExternalStorageDirectory();
			if (root.canWrite()) {
				File file = new File(root, "calculated pi (" + precision + ").txt");
				FileWriter writer = new FileWriter(file);
				BufferedWriter out = new BufferedWriter(writer);
				out.write(data);
				out.close();
			}
		} catch (IOException e) {
			Log.e("Error", "Could not write file " + e.getMessage());
		}
	}
}