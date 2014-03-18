package com.example.preferences;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.preference.ListPreference;
import android.util.AttributeSet;

public class SilentRingtonePreference extends ListPreference {

	public SilentRingtonePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		loadRingtones(getContext());
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		if (positiveResult) {
			persistString(getEntry().toString());
			setSummary(getEntry().toString());
		}
	}

	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		
		String currentValue = getPersistedString("");
		if (!currentValue.equals("")) {
			for (int i = 0; i < getEntries().length; i++) {
				if (getEntries()[i].equals(currentValue)) {
					setValueIndex(i);
					break;
				}
			}
		}
		super.onPrepareDialogBuilder(builder);
	}

	private void loadRingtones(Context context) {
		final RingtoneManager manager = new RingtoneManager(context);
		manager.setType(RingtoneManager.TYPE_NOTIFICATION);
		final Cursor cur = manager.getCursor();
		cur.moveToFirst();
		List<Ringtone> ringtones = new ArrayList<Ringtone>(cur.getCount());
		String[] entries = new String[cur.getCount()];
		String[] values = new String[cur.getCount()];
		for (int i = 0; i < cur.getCount(); i++) {
			final Ringtone ringtone = manager.getRingtone(i);
			ringtones.add(ringtone);
			entries[i] = ringtone.getTitle(context);
			values[i] = manager.getRingtoneUri(i).toString();
		}
		setEntries(entries);
		setEntryValues(values);
		cur.close();
	}

}