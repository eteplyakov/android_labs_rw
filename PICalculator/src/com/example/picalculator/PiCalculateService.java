package com.example.picalculator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.apfloat.Apint;
import org.apfloat.ApintMath;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class PiCalculateService extends IntentService {

	private static final int NOTIFICATION_ID = 0;
	private static final int MAX_PROGRESS = 100;
	private static final String SERVICE_NAME = "PiCalculateService";
	private static final String THOUSAND = "000";
	private static final String MILLION = "000000";

	public static final String PRECISION_KEY = "precision";
	public static final String RESULT_KEY = "result";
	public static final String PROGRESS_KEY = "progress";
	public static final String TIME_KEY = "time";

	public static NotificationManager notifyManager_;
	public static android.support.v4.app.NotificationCompat.Builder builder_;

	private boolean stopFlag_;

	public PiCalculateService() {
		super(SERVICE_NAME);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		stopFlag_ = false;
		String precisionString = intent.getStringExtra(PRECISION_KEY);
		int precision = Integer.valueOf(precisionString.replace("K", THOUSAND).replace("M", MILLION));
		int radix = 10;
		notifyManager_ = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		builder_ = new NotificationCompat.Builder(this);
		builder_.setAutoCancel(true);
		builder_.setContentTitle(getString(R.string.app_name))
				.setContentText(getString(R.string.progress_text) + " " + precisionString)
				.setSmallIcon(R.drawable.ic_launcher);
		Intent notificationIntent = new Intent(this, MainActivity.class);
		notificationIntent.setAction(Intent.ACTION_MAIN);
		notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		builder_.setContentIntent(contentIntent);
		builder_.setProgress(MAX_PROGRESS, 0, false);
		notifyManager_.notify(NOTIFICATION_ID, builder_.build());
		Intent inProgressIntent = new Intent(getPackageName());
		inProgressIntent.putExtra(RESULT_KEY, Activity.RESULT_CANCELED);
		inProgressIntent.putExtra(PRECISION_KEY, precisionString);
		sendBroadcast(inProgressIntent);

		long time = System.currentTimeMillis();
		
		Apfloat sum = new Apfloat(0);
		for (int k = 0; k < radix; k++) {
			if (stopFlag_) {
				break;
			}
			int thrk = 3 * k;
			Apfloat a = ApintMath.factorial(6 * k);
			a = a.multiply(ApintMath.pow(new Apint(-1), k));

			Apfloat b = new Apfloat(545140134);
			b = b.multiply(new Apfloat(k));
			b = b.add(new Apfloat(13591409));

			Apfloat c = ApintMath.factorial(thrk);
			Apfloat d = ApintMath.factorial(k);
			d = ApfloatMath.pow(d, 3);
			Apfloat e = new Apfloat(640320);
			e = ApfloatMath.pow(e, (thrk));
			a = a.multiply(b);
			c = c.multiply(d).multiply(e);

			Apfloat div = a.divide(c.precision(precision));
			sum = sum.add(div);
			if (stopFlag_) {
				break;
			}
			
			Intent notificationIntentOnProgress = new Intent(this, MainActivity.class);
			notificationIntentOnProgress.setAction(Intent.ACTION_MAIN);
			notificationIntentOnProgress.addCategory(Intent.CATEGORY_LAUNCHER);
			notificationIntentOnProgress.putExtra(PRECISION_KEY, precisionString);
			notificationIntentOnProgress.putExtra(RESULT_KEY, Activity.RESULT_OK);
			notificationIntentOnProgress.putExtra(PROGRESS_KEY, (k + 1) * 10);
			PendingIntent contentIntentOnProgress = PendingIntent.getActivity(this, 0, notificationIntentOnProgress, PendingIntent.FLAG_UPDATE_CURRENT);
			builder_.setContentIntent(contentIntentOnProgress);
			
			builder_.setProgress(MAX_PROGRESS, (k + 1) * 10, false);
			notifyManager_.notify(0, builder_.build());
			inProgressIntent.putExtra(PROGRESS_KEY, (int) ((k + 1) * 10));
			sendBroadcast(inProgressIntent);
		}

		if (!stopFlag_) {
			builder_.setContentText(getString(R.string.saving_data)).setProgress(0, 0, false);
			notifyManager_.notify(NOTIFICATION_ID, builder_.build());

			Apfloat f = new Apfloat(10005, precision);
			f = ApfloatMath.sqrt(f);
			f = f.divide(new Apfloat(42709344 * 100L));
			Apfloat pi = ApfloatMath.pow(sum.multiply(f), -1);

			time = System.currentTimeMillis() - time;
			
			writeToFile(pi.toString(), precisionString);
			
			Intent notificationIntentOnFinish = new Intent(this, MainActivity.class);
			notificationIntentOnFinish.setAction(Intent.ACTION_MAIN);
			notificationIntentOnFinish.addCategory(Intent.CATEGORY_LAUNCHER);
			notificationIntentOnFinish.putExtra(RESULT_KEY, Activity.RESULT_CANCELED);
			notificationIntentOnFinish.putExtra(PRECISION_KEY, precisionString);
			notificationIntentOnFinish.putExtra(TIME_KEY, String.valueOf(time/1000));
			PendingIntent contentIntentOnFinish = PendingIntent.getActivity(this, 0, notificationIntentOnFinish, PendingIntent.FLAG_UPDATE_CURRENT);
			builder_.setContentIntent(contentIntentOnFinish);
			
			builder_.setContentText(getString(R.string.finish_text) + " " + precisionString).setProgress(0, 0, false);
			notifyManager_.notify(NOTIFICATION_ID, builder_.build());
			Intent finishIntent = new Intent(getPackageName());
			finishIntent.putExtra(PRECISION_KEY, precisionString);
			finishIntent.putExtra(RESULT_KEY, Activity.RESULT_OK);
			finishIntent.putExtra(TIME_KEY, String.valueOf(time/1000));
			sendBroadcast(finishIntent);
		} else {
			notifyManager_.cancel(NOTIFICATION_ID);
		}
	}

	@Override
	public void onDestroy() {
		stopFlag_ = true;
		super.onDestroy();
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