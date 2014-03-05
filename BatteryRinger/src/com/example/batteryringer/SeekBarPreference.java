package com.example.batteryringer;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.preference.DialogPreference;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.LinearLayout;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SeekBarPreference extends DialogPreference {

	private static final String androidns = "http://schemas.android.com/apk/res/android";

	private SeekBar seekBar_;
	private EditText valueText_;

	private int max_;
	private int value_;
	private int default_;

	OnSeekBarChangeListener seekBarListener = new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			value_ = progress;
			valueText_.setText(String.valueOf(progress));
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}
	};

	TextWatcher watcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			if (s.toString().equals("")) {
				value_ = 0;
				valueText_.setText("0");
			} else {
				if (s.length() > 4 || Integer.valueOf(s.toString()) > max_) {
					value_ = max_;
					valueText_.setText("" + max_);
				} else {
					value_ = Integer.valueOf(s.toString());
				}
			}
			seekBar_.setProgress(value_);
			valueText_.setSelection(valueText_.getText().length());
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
		}
	};

	public SeekBarPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		max_ = attrs.getAttributeIntValue(androidns, "max", 100);
		default_ = attrs.getAttributeIntValue(androidns, "defaultValue", 0);
	}

	@SuppressWarnings("deprecation") //disables certain compiler warnings about deprecated code
	@Override
	protected View onCreateDialogView() {
		LinearLayout.LayoutParams params;
		LinearLayout layout = new LinearLayout(getContext());
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setPadding(6, 6, 6, 6);

		valueText_ = new EditText(getContext());
		valueText_.setGravity(Gravity.CENTER_HORIZONTAL);
		valueText_.setTextSize(32);
		valueText_.setInputType(InputType.TYPE_CLASS_NUMBER);
		valueText_.addTextChangedListener(watcher);
		params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layout.addView(valueText_, params);

		seekBar_ = new SeekBar(getContext());
		seekBar_.setOnSeekBarChangeListener(seekBarListener);
		layout.addView(seekBar_, params);
		seekBar_.setMax(max_);
		seekBar_.setProgress(value_);
		return layout;
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		if (positiveResult) {
			if (shouldPersist()) {
				persistInt(value_);
			}
		} else {
			seekBar_.setProgress(getPersistedInt(value_));
		}
	}

	@Override
	protected void onBindDialogView(View v) {
		super.onBindDialogView(v);
		seekBar_.setMax(max_);
		value_ = getPersistedInt(default_);
		seekBar_.setProgress(value_);
	}
}