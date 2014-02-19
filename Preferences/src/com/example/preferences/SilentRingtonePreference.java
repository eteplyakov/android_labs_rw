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

	public static final int REQUEST_NOTIFICATION_TONE = 0;
	private List<Ringtone> mRingtones_;
	private String[] mEntries_, mValues_;
	private int mSelectedItem_;

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

	private void loadRingtones(final Context context) {
		final RingtoneManager manager = new RingtoneManager(context);
		manager.setType(RingtoneManager.TYPE_NOTIFICATION);
		final Cursor cur = manager.getCursor();
		cur.moveToFirst();
		final int count = cur.getCount();
		mRingtones_ = new ArrayList<Ringtone>(count);
		mEntries_ = new String[count];
		mValues_ = new String[count];
		for (int i = 0; i < count; i++) {
			final Ringtone ringtone = manager.getRingtone(i);
			mRingtones_.add(ringtone);
			mEntries_[i] = ringtone.getTitle(context);
			mValues_[i] = manager.getRingtoneUri(i).toString();
		}
		setEntries(mEntries_);
		setEntryValues(mValues_);
		cur.close();
	}

	public Ringtone getSelectedRingtone() {
		return mRingtones_.get(mSelectedItem_);
	}

	public int getSelectedItem() {
		return mSelectedItem_;
	}

	public void setSelectedItem(final int selected) {
		mSelectedItem_ = selected >= 0 && selected < mValues_.length ? selected : 0;
	}

}