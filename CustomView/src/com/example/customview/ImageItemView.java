package com.example.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ImageItemView extends RelativeLayout {

	private TextView textViewTop_;
	private TextView textViewBottom_;
	private ImageView itemImage_;

	public ImageItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.image_view, this, true);
		if (isInEditMode()) {
			return;
		}
		textViewTop_ = (TextView) findViewById(R.id.title);
		textViewBottom_ = (TextView) findViewById(R.id.details);
		itemImage_ = (ImageView) findViewById(R.id.image);
	}

	public void setTitle(String text) {
		textViewTop_.setText(text);
	}

	public void setDetails(String text) {
		textViewBottom_.setText(text);
	}

	public void setImage(int imageId) {
		itemImage_.setImageResource(imageId);
	}
}