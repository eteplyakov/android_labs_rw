package com.example.preferences;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

public class PreferencesActivity extends PreferenceActivity {

	private static String LOUD_KEY = "loud";

	private ListPreference loud_;

	@SuppressWarnings("deprecation")
	// disables certain compiler warnings about deprecated code
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);

		loud_ = (ListPreference) findPreference(LOUD_KEY);

		if (!loud_.getSharedPreferences().getString(LOUD_KEY, "").equals("")) {
			loud_.setSummary(loud_.getEntry());
		}
		loud_.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				loud_.setSummary(newValue.toString());
				return true;
			}
		});
	}

	@SuppressWarnings("deprecation")
	// disables certain compiler warnings about deprecated code
	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
		super.onPreferenceTreeClick(preferenceScreen, preference);
		if (preference != null) {
			if (preference instanceof PreferenceScreen) {
				if (((PreferenceScreen) preference).getDialog() != null) {
					((PreferenceScreen) preference)
							.getDialog()
							.getWindow()
							.getDecorView()
							.setBackgroundDrawable(
									this.getWindow().getDecorView().getBackground().getConstantState().newDrawable());
				}
			}
		}
		return false;
	}
}