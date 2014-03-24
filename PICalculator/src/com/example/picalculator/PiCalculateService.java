package com.example.picalculator;

import java.io.File;

import com.example.picalculator.Pi.onProgressListener;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;

public class PiCalculateService extends IntentService {

	public enum PrecisionsEnum {
		PRECISION_32K("61EFA066AA2C3E7AFC8A557673574C7C"),
		PRECISION_64K("FD50018108E062D62358EAB98D321428"),
		PRECISION_128K("DAB846C485F7BF1446FB26C24C28AB08"),
		PRECISION_256K("BAE1D0CB5680BEBE4E5A9C0C341381A0"),
		PRECISION_512K("22C6C559EA1EED739DD0DCC1CB952C39"),
		PRECISION_1M("9F04A51FA99821415C07A25EEB509896"),
		PRECISION_2M("CB7F9AC6E9B8E5D37D7150F8CDA6BDA6"),
		PRECISION_4M("C5AE4B2B65EC1701CA6EBEE11B4DC833"),
		PRECISION_8M("838537DC777AA065B35F39BEE22684C3"),
		PRECISION_16M("2B8548FFFC0C4D9AF002C917EE04F292");

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
	public static final String PROGRESS_KEY = "progress";
	public static final String TIME_KEY = "time";
	public static final String CHECK_KEY = "check";

	private NotificationManager notifyManager_;
	private android.support.v4.app.NotificationCompat.Builder builder_;

	private Pi pi_;
	public static Context appContext_;

	public PiCalculateService() {
		super(SERVICE_NAME);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		appContext_ = getApplicationContext();

		final String precisionString = intent.getStringExtra(PRECISION_KEY);
		int precision = Integer.valueOf(precisionString.replace("K", Pi.THOUSAND).replace("M", Pi.MILLION));

		final Intent notificationIntentOnProgress = new Intent(this, MainActivity.class);
		notificationIntentOnProgress.setAction(Intent.ACTION_MAIN);
		notificationIntentOnProgress.addCategory(Intent.CATEGORY_LAUNCHER);

		final Intent inProgressIntent = new Intent(getPackageName());
		inProgressIntent.putExtra(RESULT_KEY, Activity.RESULT_CANCELED);

		pi_ = new Pi(precision);
		pi_.setProgressListener(new onProgressListener() {

			@Override
			public void onEvent() {
				notificationIntentOnProgress.putExtra(PRECISION_KEY, precisionString);
				notificationIntentOnProgress.putExtra(RESULT_KEY, Activity.RESULT_OK);
				notificationIntentOnProgress.putExtra(PROGRESS_KEY, pi_.getProgress());

				PendingIntent contentIntentOnProgress = PendingIntent.getActivity(getApplicationContext(), 0,
						notificationIntentOnProgress, PendingIntent.FLAG_UPDATE_CURRENT);

				builder_.setContentIntent(contentIntentOnProgress);
				builder_.setProgress(MAX_PROGRESS, pi_.getProgress(), false);
				notifyManager_.notify(0, builder_.build());

				inProgressIntent.putExtra(PROGRESS_KEY, pi_.getProgress());
				sendBroadcast(inProgressIntent);
			}
		});

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

		inProgressIntent.putExtra(RESULT_KEY, Activity.RESULT_CANCELED);
		inProgressIntent.putExtra(PRECISION_KEY, precisionString);
		sendBroadcast(inProgressIntent);

		long time = System.currentTimeMillis();

		pi_.calculate();

		time = System.currentTimeMillis() - time;

		if (!pi_.getStopFlag()) {

			boolean checkResult = isMD5Checked(precisionString);

			Intent notificationIntentOnFinish = new Intent(this, MainActivity.class);
			notificationIntentOnFinish.setAction(Intent.ACTION_MAIN);
			notificationIntentOnFinish.addCategory(Intent.CATEGORY_LAUNCHER);
			notificationIntentOnFinish.putExtra(RESULT_KEY, Activity.RESULT_CANCELED);
			notificationIntentOnFinish.putExtra(PRECISION_KEY, precisionString);
			notificationIntentOnFinish.putExtra(TIME_KEY, String.valueOf(time / 1000));
			notificationIntentOnFinish.putExtra(CHECK_KEY, checkResult);

			PendingIntent contentIntentOnFinish = PendingIntent.getActivity(this, 0, notificationIntentOnFinish,
					PendingIntent.FLAG_UPDATE_CURRENT);

			builder_.setContentIntent(contentIntentOnFinish);
			builder_.setContentText(getString(R.string.finish_text) + " " + precisionString).setProgress(0, 0, false);
			notifyManager_.notify(NOTIFICATION_ID, builder_.build());

			Intent finishIntent = new Intent(getPackageName());
			finishIntent.putExtra(PRECISION_KEY, precisionString);
			finishIntent.putExtra(RESULT_KEY, Activity.RESULT_OK);
			finishIntent.putExtra(TIME_KEY, String.valueOf(time / 1000));
			finishIntent.putExtra(CHECK_KEY, checkResult);
			sendBroadcast(finishIntent);
		} else {
			notifyManager_.cancel(NOTIFICATION_ID);
		}
	}

	@Override
	public void onDestroy() {
		pi_.setStopFlag(true);
		super.onDestroy();
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
		
		for(PrecisionsEnum precisionMd5 :PrecisionsEnum.values()){
			if (precisionMd5.getSum().equalsIgnoreCase(md5)) {
				checkFlag = true;
				break;
			}
		}
		
		return checkFlag;
	}
}