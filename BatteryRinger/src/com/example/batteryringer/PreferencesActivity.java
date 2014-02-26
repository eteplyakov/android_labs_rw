package com.example.batteryringer;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.RingtonePreference;

public class PreferencesActivity extends PreferenceActivity {

	private static String ENABLE_KEY = "enable";
	private static String RINGTONE_KEY = "ringtone";
	private static String LOUD_LEVEL_KEY = "loud_level";

	private CheckBoxPreference enable_;
	private RingtonePreference ringtone_;
	private SeekBarPreference loudLevel_;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);

		loudLevel_ = (SeekBarPreference) findPreference(LOUD_LEVEL_KEY);
		enable_ = (CheckBoxPreference) findPreference(ENABLE_KEY);
		ringtone_ = (RingtonePreference) findPreference(RINGTONE_KEY);

		visibleRingtone(enable_.isChecked());
		visibleLoudLevel(ringtone_.getSharedPreferences().getString(RINGTONE_KEY, ""));

		enable_.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				visibleRingtone(Boolean.valueOf(newValue.toString()));
				return true;
			}
		});
		ringtone_.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				visibleLoudLevel(newValue.toString());
				return true;
			}
		});
	}

	@SuppressWarnings("deprecation")
	private void visibleRingtone(boolean value) {
		PreferenceScreen preferenceScreen = getPreferenceScreen();
		if (value) {
			preferenceScreen.addPreference(ringtone_);
		} else {
			preferenceScreen.removePreference(ringtone_);
		}
	}

	@SuppressWarnings("deprecation")
	private void visibleLoudLevel(String value) {
		PreferenceScreen preferenceScreen = getPreferenceScreen();
		if (value.equals("")) {
			preferenceScreen.removePreference(loudLevel_);
		} else {
			preferenceScreen.addPreference(loudLevel_);
		}
	}
}