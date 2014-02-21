package com.example.customview;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {

	private static int LARGE_TEXT_SIZE = 22;
	private static int SMALL_TEXT_SIZE = 16;
	private static int LEFT_MARGIN = 65;
	private static int TOP_MARGIN = 28;
	private ArrayList<ListItem> elementsArray_ = new ArrayList<ListItem>();
	private Context context_;

	public ListAdapter(Context context, ArrayList<ListItem> itemsArray) {
		if (itemsArray != null) {
			elementsArray_ = itemsArray;
		}
		this.context_ = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return elementsArray_.size();
	}

	@Override
	public Object getItem(int num) {
		// TODO Auto-generated method stub
		return elementsArray_.get(num);
	}

	@Override
	public long getItemId(int itemId) {
		return itemId;
	}

	@Override
	public View getView(int position, View listView, ViewGroup arg2) {
		RelativeLayout relativeLayout = new RelativeLayout(context_);
		TextView topLine = new TextView(context_);
		topLine.setText(elementsArray_.get(position).getTitle());
		TextView bottomLine = new TextView(context_);
		bottomLine.setText(elementsArray_.get(position).getDetails());
		ImageView image = new ImageView(context_);
		image.setImageResource(R.drawable.ic_launcher);
		topLine.setTextSize(LARGE_TEXT_SIZE);
		bottomLine.setTextSize(SMALL_TEXT_SIZE);
		RelativeLayout.LayoutParams parametersTopLine = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		parametersTopLine.leftMargin = LEFT_MARGIN;
		RelativeLayout.LayoutParams parametersBottomLine = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		parametersBottomLine.leftMargin = LEFT_MARGIN;
		parametersBottomLine.topMargin = TOP_MARGIN;
		relativeLayout.addView(topLine, parametersTopLine);
		relativeLayout.addView(bottomLine, parametersBottomLine);
		relativeLayout.addView(image);
		listView = relativeLayout;
		return listView;
	}

}