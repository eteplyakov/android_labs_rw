package com.example.batteryringer;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
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

		changeRingtonePreferenceVisibility(enable_.isChecked());
		changeSeekBarPreferenceVisibility(ringtone_.getSharedPreferences().getString(RINGTONE_KEY, ""));
		Ringtone ringtone = RingtoneManager.getRingtone(getBaseContext(),
				Uri.parse(ringtone_.getSharedPreferences().getString(RINGTONE_KEY, "")));
		ringtone_.setSummary(ringtone.getTitle(getBaseContext()));
		enable_.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				changeRingtonePreferenceVisibility(Boolean.valueOf(newValue.toString()));
				return true;
			}
		});
		ringtone_.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				changeSeekBarPreferenceVisibility(newValue.toString());
				if (newValue.toString().equals("")) {
					ringtone_.setSummary(R.string.silent);
				} else {
					Ringtone ringtone = RingtoneManager.getRingtone(getBaseContext(), Uri.parse(newValue.toString()));
					ringtone_.setSummary(ringtone.getTitle(getBaseContext()));
				}
				return true;
			}
		});
	}

	@SuppressWarnings("deprecation")
	private void changeRingtonePreferenceVisibility(boolean isVisible) {
		PreferenceScreen preferenceScreen = getPreferenceScreen();
		if (isVisible) {
			preferenceScreen.addPreference(ringtone_);
			preferenceScreen.addPreference(loudLevel_);
		} else {
			preferenceScreen.removePreference(ringtone_);
			preferenceScreen.removePreference(loudLevel_);
		}
	}

	@SuppressWarnings("deprecation")
	private void changeSeekBarPreferenceVisibility(String isVisible) {
		PreferenceScreen preferenceScreen = getPreferenceScreen();
		if (isVisible.equals("")) {
			preferenceScreen.removePreference(loudLevel_);
		} else {
			preferenceScreen.addPreference(loudLevel_);
		}
	}
}