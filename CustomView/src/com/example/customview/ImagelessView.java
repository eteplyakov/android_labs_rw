package com.example.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;

public class ImagelessView extends View {

	private Context context_;
	private String title_;
	private String details_;

	public ImagelessView(Context context, AttributeSet attrs) {
		super(context);
		this.context_ = context;
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImagelessView);
		final int N = a.getIndexCount();
		for (int i = 0; i < N; ++i) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.ImagelessView_topLineText:
				this.title_ = a.getString(attr);
				break;
			case R.styleable.ImagelessView_bottomLineText:
				this.details_ = a.getString(attr);
				break;
			case R.styleable.ImagelessView_onClick:
				setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Toast toast = Toast.makeText(context_, getResources().getText(R.string.onclick_toast_message),
								Toast.LENGTH_SHORT);
						toast.show();
					}
				});
				break;
			}
		}
		a.recycle();
	}

	@SuppressLint("DrawAllocation")
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		LinearLayout layout = new LinearLayout(context_);
		layout.setOrientation(LinearLayout.VERTICAL);
		TextView textViewTop = new TextView(context_);
		TextView textViewBottom = new TextView(context_);
		textViewTop.setText(title_);
		textViewBottom.setText(details_);
		textViewTop.setTextSize(22);
		textViewBottom.setTextSize(16);
		layout.addView(textViewTop);
		layout.addView(textViewBottom);
		layout.measure(canvas.getWidth(), canvas.getHeight());
		layout.layout(0, 0, canvas.getWidth(), canvas.getHeight());
		layout.draw(canvas);
	}

}