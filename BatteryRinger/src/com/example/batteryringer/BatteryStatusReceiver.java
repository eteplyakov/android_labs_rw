package com.example.batteryringer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class BatteryStatusReceiver extends BroadcastReceiver {

	static final String ENABLE_KEY = "enable";
	static final String RINGTONE_KEY = "ringtone";
	static final String LOUD_LEVEL_KEY = "loud_level";
	static final int MAX_VOLUME = 3000;
	static final int DEFAULT_VOLUME = 100;

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		if (prefs.getBoolean(ENABLE_KEY, false)) {
			Uri ringtoneUri = Uri.parse(prefs.getString(RINGTONE_KEY, ""));
			if (ringtoneUri != null) {
				int soundVolume = prefs.getInt(LOUD_LEVEL_KEY, DEFAULT_VOLUME);
				float volume = (float) (1 - (Math.log(MAX_VOLUME - soundVolume) / Math.log(MAX_VOLUME)));
				MediaPlayer player = new MediaPlayer();
				try {
					player.setDataSource(context, ringtoneUri);
					player.prepare();
					player.setVolume(volume, volume);
					player.start();
				} catch (Exception e) {
					Toast.makeText(context, context.getText(R.string.error), Toast.LENGTH_LONG).show();
				}
				player.start();
			}
		}
	}
}
