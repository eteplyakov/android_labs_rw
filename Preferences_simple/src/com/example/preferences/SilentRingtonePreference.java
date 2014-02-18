package com.example.preferences;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.AttributeSet;
import android.app.AlertDialog.Builder;

public class SilentRingtonePreference extends ListPreference implements OnPreferenceClickListener {
	private List<Ringtone> mRingtones;
	private String[] mEntries, mValues;
	private int mSelectedItem;
	public static final int REQUEST_NOTIFICATION_TONE = 0;

	public SilentRingtonePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		loadRingtones(getContext());
		setOnPreferenceClickListener(this);
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		return true;
	}

	@Override
	protected void onPrepareDialogBuilder(final Builder builder) {
		builder.setSingleChoiceItems(getEntries(), getSelectedItem(), new OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				setSelectedItem(which);
				final Ringtone ringtone = getSelectedRingtone();
				if (ringtone.isPlaying()) {
					ringtone.stop();
				}
			}
		});
	}

	public Ringtone getSelectedRingtone() {
		return mRingtones.get(mSelectedItem);
	}

	public int getSelectedItem() {
		return mSelectedItem;
	}

	public void setSelectedItem(final int selected) {
		mSelectedItem = selected >= 0 && selected < mValues.length ? selected : 0;
	}

	private void loadRingtones(final Context context) {
		final RingtoneManager manager = new RingtoneManager(context);
		manager.setType(RingtoneManager.TYPE_NOTIFICATION);
		final Cursor cur = manager.getCursor();
		cur.moveToFirst();
		final int count = cur.getCount();
		mRingtones = new ArrayList<Ringtone>(count);
		mEntries = new String[count];
		mValues = new String[count];
		for (int i = 0; i < count; i++) {
			final Ringtone ringtone = manager.getRingtone(i);
			mRingtones.add(ringtone);
			mEntries[i] = ringtone.getTitle(context);
			mValues[i] = manager.getRingtoneUri(i).toString();
		}
		setEntries(mEntries);
		setEntryValues(mValues);
		cur.close();
	}
}