/**
 * Preferences
 * Copyright (c) 2014 com.example.preferences
 *
 * Created: 14.02.2014 by alex
 */
package com.example.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PreferencesActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
	}
}