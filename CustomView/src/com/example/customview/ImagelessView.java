package com.example.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ImagelessView extends LinearLayout {

	private TextView textViewTop_;
	private TextView textViewBottom_;

	private OnClickListener onViewClick = new OnClickListener() {
		public void onClick(View v) {
			Toast toast = Toast.makeText(getContext(), getResources().getText(R.string.onclick_toast_message),
					Toast.LENGTH_SHORT);
			toast.show();
		}
	};

	public ImagelessView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.imagless_view, this, true);
		if (isInEditMode()) {
			return;
		}
		textViewTop_ = (TextView) findViewById(R.id.title);
		textViewBottom_ = (TextView) findViewById(R.id.details);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImagelessView);
		final int N = a.getIndexCount();
		for (int i = 0; i < N; ++i) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.ImagelessView_topLineText:
				textViewTop_.setText(a.getString(attr));
				break;
			case R.styleable.ImagelessView_bottomLineText:
				textViewBottom_.setText(a.getString(attr));
				break;
			case R.styleable.ImagelessView_onClick:
				this.setOnClickListener(onViewClick);
				break;
			}
		}
		a.recycle();
	}
}