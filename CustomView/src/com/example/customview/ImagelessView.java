package com.example.customview;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImagelessView extends LinearLayout {
	
	private TextView textViewTop_;
	private TextView textViewBottom_;

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
				if (context.isRestricted()) {
					throw new IllegalStateException("The android:onClick attribute cannot "
							+ "be used within a restricted context");
				}

				final String handlerName = a.getString(attr);
				if (handlerName != null) {
					setOnClickListener(new OnClickListener() {
						private Method mHandler;

						public void onClick(View v) {
							if (mHandler == null) {
								try {
									mHandler = getContext().getClass().getMethod(handlerName, ImagelessView.class);
								} catch (NoSuchMethodException e) {
									int id = getId();
									String idText = id == NO_ID ? "" : " with id '"
											+ getContext().getResources().getResourceEntryName(id) + "'";
									throw new IllegalStateException("Could not find a method " + handlerName
											+ "(View) in the activity " + getContext().getClass()
											+ " for onClick handler" + " on view " + ImagelessView.this.getClass() + idText, e);
								}
							}

							try {
								mHandler.invoke(getContext(), ImagelessView.this);
							} catch (IllegalAccessException e) {
								throw new IllegalStateException("Could not execute non "
										+ "public method of the activity", e);
							} catch (InvocationTargetException e) {
								throw new IllegalStateException("Could not execute " + "method of the activity", e);
							}
						}
					});
				}
				break;
			}
		}
		a.recycle();
	}
}