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
import android.util.AttributeSet;
import android.app.AlertDialog.Builder;

public class SilentRingtonePreference extends ListPreference {

	private List<Ringtone> ringtones_;
	private String[] entries_;
	private String[] values_;
	private int selectedItem_;
	private String value_;

	public SilentRingtonePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		loadRingtones(getContext());
	}

	@Override
	protected void onPrepareDialogBuilder(final Builder builder) {
		builder.setSingleChoiceItems(getEntries(), getSelectedItem(), new OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				setSelectedItem(which);
			}
		});
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		if (positiveResult) {
			value_ = entries_[getSelectedItem()].toString();
			if (callChangeListener(value_)) {
				persistString(value_);
			}
		} else {
			setValue(getPersistedString(value_));
		}
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
		setValue(restoreValue ? getPersistedString(value_) : (String) defaultValue);
	}

	public void setValue(String value) {
		value_ = value;
		for (int i = 0; i < entries_.length; i++) {
			if (entries_[i].equals(value_)) {
				selectedItem_ = i;
			} else {
				selectedItem_ = 0;
			}
		}
	}

	private void loadRingtones(final Context context) {
		final RingtoneManager manager = new RingtoneManager(context);
		manager.setType(RingtoneManager.TYPE_NOTIFICATION);
		final Cursor cur = manager.getCursor();
		cur.moveToFirst();
		final int count = cur.getCount();
		ringtones_ = new ArrayList<Ringtone>(count);
		entries_ = new String[count];
		values_ = new String[count];
		for (int i = 0; i < count; i++) {
			final Ringtone ringtone = manager.getRingtone(i);
			ringtones_.add(ringtone);
			entries_[i] = ringtone.getTitle(context);
			values_[i] = manager.getRingtoneUri(i).toString();
		}
		setEntries(entries_);
		setEntryValues(values_);
		cur.close();
	}

	public Ringtone getSelectedRingtone() {
		return ringtones_.get(selectedItem_);
	}

	public int getSelectedItem() {
		return selectedItem_;
	}

	public String getSelectedValue() {
		if (value_.equals(null)) {
			return "";
		} else {
			return value_;
		}
	}

	public void setSelectedItem(final int selected) {
		selectedItem_ = selected >= 0 && selected < values_.length ? selected : 0;
	}
}