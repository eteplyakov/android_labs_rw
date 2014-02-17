package com.example.layoutsforlist;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

	private static final int LEFT_MARGIN = 48;
	private static final int TOP_MARGIN=25;

	ArrayList<Item> elementsArray = new ArrayList<Item>();
	Context context;

	public MyAdapter(Context context, ArrayList<Item> itemsArray) {
		if (itemsArray != null) {
			elementsArray = itemsArray;
		}
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return elementsArray.size();
	}

	@Override
	public Object getItem(int num) {
		// TODO Auto-generated method stub
		return elementsArray.get(num);
	}

	@Override
	public long getItemId(int itemId) {
		return itemId;
	}

	@Override
	public View getView(int position, View listView, ViewGroup arg2) {
		LayoutInflater inflater = LayoutInflater.from(context);
		if (listView == null) {
			listView = inflater.inflate(R.layout.list_view_item, arg2, false);
		}
		TextView title = (TextView) listView.findViewById(R.id.title);
		TextView details = (TextView) listView.findViewById(R.id.details);
		ImageView icon = (ImageView) listView.findViewById(R.id.xx);
		title.setText(elementsArray.get(position).title);
		details.setText(elementsArray.get(position).details);
		RelativeLayout.LayoutParams parameters = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		if ((position + 1) % 2 == 0) {
			details.setVisibility(View.GONE);
			parameters.setMargins(LEFT_MARGIN, TOP_MARGIN, 0, 0);
			title.setLayoutParams(parameters);
		}
		if ((position + 1) % 3 == 0) {
			icon.setVisibility(View.GONE);
			parameters.setMargins(LEFT_MARGIN, 0, 0, 0);
			title.setLayoutParams(parameters);
		}
		if (((position + 1) % 2 == 0) && ((position + 1) % 3 == 0)) {
			icon.setVisibility(View.GONE);
			details.setVisibility(View.GONE);
			parameters.setMargins(LEFT_MARGIN, TOP_MARGIN, 0, 0);
		}
		return listView;
	}

}
