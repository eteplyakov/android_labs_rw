package com.example.customview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ListAdapter extends ArrayAdapter<ListItem> {

	public ListAdapter(Context context, ListItem[] objects) {
		super(context, 0, objects);
	}

	@Override
	public View getView(int position, View listView, ViewGroup arg2) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		if (listView == null) {
			listView = inflater.inflate(R.layout.image_view_item, arg2, false);
		}
		ImageItemView item = (ImageItemView) listView.findViewById(R.id.item_id);
		item.setTitle(getItem(position).getTitle());
		item.setDetails(getItem(position).getDetails());
		return listView;
	}

}