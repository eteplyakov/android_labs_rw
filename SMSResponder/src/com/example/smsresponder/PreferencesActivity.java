package com.example.smsresponder;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.widget.Toast;

public class PreferencesActivity extends PreferenceActivity {

	static final String ADD_NUMBER_KEY = "add_number";
	static final String NUMBER_LIST_KEY = "numbers_to_answer";
	static final String SPLIT_CHAR = ";";

	private EditTextPreference addNumber_;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		addNumber_ = (EditTextPreference) findPreference(ADD_NUMBER_KEY);
		addNumber_.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				if (checkString(newValue.toString())) {
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
					String numbers = prefs.getString(NUMBER_LIST_KEY, "");
					Editor editor = prefs.edit();
					if (!numbers.equals("")) {
						editor.putString(NUMBER_LIST_KEY, numbers + newValue.toString() + SPLIT_CHAR);
					} else {
						editor.putString(NUMBER_LIST_KEY, newValue.toString() + SPLIT_CHAR);
					}
					editor.commit();
					Toast.makeText(getApplicationContext(), R.string.number_added, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), R.string.add_error, Toast.LENGTH_SHORT).show();
				}
				return false;
			}
		});
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
		super.onPreferenceTreeClick(preferenceScreen, preference);
		if (preference != null)
			if (preference instanceof PreferenceScreen)
				if (((PreferenceScreen) preference).getDialog() != null)
					((PreferenceScreen) preference)
							.getDialog()
							.getWindow()
							.getDecorView()
							.setBackgroundDrawable(
									this.getWindow().getDecorView().getBackground().getConstantState().newDrawable());
		return false;
	}

	private boolean checkString(String string) {
		if (string == null || string.length() == 0)
			return false;
		int i = 0;
		if (string.charAt(0) == '-') {
			if (string.length() == 1) {
				return false;
			}
			i = 1;
		}
		char c;
		for (; i < string.length(); i++) {
			c = string.charAt(i);
			if (!(c >= '0' && c <= '9')) {
				return false;
			}
		}
		return true;
	}
}