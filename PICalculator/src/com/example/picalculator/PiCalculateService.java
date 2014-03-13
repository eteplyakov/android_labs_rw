package com.example.picalculator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

import org.apfloat.*;
import org.apfloat.internal.ApfloatInternalException;

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
	private static final String THOUSAND ="000";
	private static final String MILLION = "000000";

	public static final String PRECISION_KEY = "precision";
	public static final String RESULT_KEY = "result";

	private boolean stopFlag_;

	public PiCalculateService() {
		super(SERVICE_NAME);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		stopFlag_ = false;
		String precisionString = intent.getStringExtra(PRECISION_KEY);
		int precision = Integer.valueOf(precisionString.replace("K", THOUSAND).replace("M", MILLION));
		
		NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setAutoCancel(true);
		builder.setContentTitle(getString(R.string.app_name))
				.setContentText(getString(R.string.progress_text) + " " + precisionString)
				.setSmallIcon(R.drawable.ic_launcher);
		Intent notificationIntent = new Intent(this, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		builder.setContentIntent(contentIntent);
		int progress = 0;
		double stepProgress = precision / MAX_PROGRESS;
		builder.setProgress(100, progress, false);
		notifyManager.notify(0, builder.build());
		Intent inProgressIntent = new Intent(getPackageName());
		inProgressIntent.putExtra(RESULT_KEY, Activity.RESULT_CANCELED);
		inProgressIntent.putExtra(PRECISION_KEY, precisionString);
		sendBroadcast(inProgressIntent);
		
		try {
			Apfloat sum = new Apfloat(0);
			for (int k = 0; k < 10; k++) {
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
				builder.setProgress(MAX_PROGRESS, (k + 1) * 10, false);
				notifyManager.notify(NOTIFICATION_ID, builder.build());
				sendBroadcast(inProgressIntent);
				if (stopFlag_) {
					break;
				}
			}
			if (!stopFlag_) {
				Apfloat f = new Apfloat(10005, precision);
				f = ApfloatMath.sqrt(f);
				f = f.divide(new Apfloat(42709344 * 100L));
				Apfloat pi = ApfloatMath.pow(sum.multiply(f), -1);
				writeToFile(pi.toString(), precisionString);
			}
		} catch (ApfloatInternalException ex) {
			final BigInteger TWO = BigInteger.valueOf(2);
			final BigInteger THREE = BigInteger.valueOf(3);
			final BigInteger FOUR = BigInteger.valueOf(4);
			final BigInteger SEVEN = BigInteger.valueOf(7);

			BigInteger q = BigInteger.ONE;
			BigInteger r = BigInteger.ZERO;
			BigInteger t = BigInteger.ONE;
			BigInteger k = BigInteger.ONE;
			BigInteger n = BigInteger.valueOf(3);
			BigInteger l = BigInteger.valueOf(3);

			BigInteger nn, nr;
			boolean first = true;

			File file = new File(Environment.getExternalStorageDirectory(), getString(R.string.file_name) + " ("
					+ precisionString + ").txt");
			FileWriter writer = null;
			try {
				writer = new FileWriter(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BufferedWriter out = new BufferedWriter(writer);
			int iteration = 0;
			while (true) {
				if (FOUR.multiply(q).add(r).subtract(t).compareTo(n.multiply(t)) == -1) {
					try {
						out.append(n.toString());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (first) {
						try {
							out.append(".");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						first = false;
					}
					nr = BigInteger.TEN.multiply(r.subtract(n.multiply(t)));
					n = BigInteger.TEN.multiply(THREE.multiply(q).add(r)).divide(t)
							.subtract(BigInteger.TEN.multiply(n));
					q = q.multiply(BigInteger.TEN);
					r = nr;
					try {
						out.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					nr = TWO.multiply(q).add(r).multiply(l);
					nn = q.multiply((SEVEN.multiply(k))).add(TWO).add(r.multiply(l)).divide(t.multiply(l));
					q = q.multiply(k);
					t = t.multiply(l);
					l = l.add(TWO);
					k = k.add(BigInteger.ONE);
					n = nn;
					r = nr;
				}
				sendBroadcast(inProgressIntent);
				if (iteration > stepProgress) {
					progress++;
					stepProgress += precision / MAX_PROGRESS;
					builder.setProgress(MAX_PROGRESS, progress, false);
					notifyManager.notify(NOTIFICATION_ID, builder.build());
				}
				if (stopFlag_ || iteration == precision) {
					break;
				}
				iteration++;
			}
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		if (!stopFlag_) {
			builder.setContentText(getString(R.string.finish_text) + " " + precisionString).setProgress(0, 0, false);
			notifyManager.notify(NOTIFICATION_ID, builder.build());
		} else {
			builder.setContentText(getString(R.string.canceled_text)).setProgress(0, 0, false);
			notifyManager.notify(NOTIFICATION_ID, builder.build());
		}
		Intent finishIntent = new Intent(getPackageName());
		finishIntent.putExtra(RESULT_KEY, Activity.RESULT_OK);
		sendBroadcast(finishIntent);
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
				File file = new File(root, "calculated pi ("+precision+").txt");
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
