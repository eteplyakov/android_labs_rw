package com.example.picalculator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.apfloat.Apint;
import org.apfloat.ApintMath;

import android.os.Environment;
import android.util.Log;

public class Pi {

	public interface onProgressListener {
		public void onEvent();
	}

	public static final String THOUSAND = "000";
	public static final String MILLION = "000000";
	private static final int RADIX = 10;

	private onProgressListener listener_;

	private int precision_;
	private int progress_;
	private boolean stopFlag_;

	public Pi(int precision) {
		this.precision_ = precision;
		this.progress_ = 0;
		this.stopFlag_ = false;
	}

	public Apfloat calculate() {
		// following method from http://stackoverflow.com/questions/21561914/error-calculating-pi-using-the-chudnovsky-algorithm-java
		Apfloat sum = new Apfloat(0);
		for (int k = 0; k < RADIX; k++) {
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

			Apfloat div = a.divide(c.precision(precision_));
			sum = sum.add(div);

			if (stopFlag_) {
				break;
			}
			progress_ += 10;
			listener_.onEvent();
		}

		Apfloat f = new Apfloat(10005, precision_);
		f = ApfloatMath.sqrt(f);
		f = f.divide(new Apfloat(42709344 * 100L));
		Apfloat pi = ApfloatMath.pow(sum.multiply(f), -1);
		
		writeToFile(pi.toString(), precision_);
		
		return pi;
	}

	public void setStopFlag(boolean flag) {
		stopFlag_ = flag;
	}
	
	public boolean getStopFlag() {
		return stopFlag_;
	}
	
	public int getProgress() {
		return progress_;
	}

	public void setProgressListener(onProgressListener eventListener) {
		listener_ = eventListener;
	}

	private void writeToFile(String data, int precision) {
		try {
			String.valueOf(precision).replace(THOUSAND, "K").replace(MILLION, "M");
			
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
