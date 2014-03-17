package com.example.biglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemAdapter extends ArrayAdapter<Item> {

	static class ViewHolderItem {
		TextView itemNumber;
		TextView itemDescription;
		ImageView itemIcon;
	}

	public ItemAdapter(Context context, Item[] object) {
		super(context, 0, object);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolderItem viewHolder;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.list_view_item, parent, false);
			viewHolder = new ViewHolderItem();
			viewHolder.itemNumber = (TextView) convertView.findViewById(R.id.number);
			viewHolder.itemDescription = (TextView) convertView.findViewById(R.id.description);
			viewHolder.itemIcon = (ImageView) convertView.findViewById(R.id.left_image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolderItem) convertView.getTag();
		}
		if (getItem(position) != null) {
			viewHolder.itemNumber.setText(getItem(position).getNumberText());
			viewHolder.itemDescription.setText(getItem(position).getDescriptionId());
			viewHolder.itemIcon.setImageResource(getItem(position).getIconId());
		}
		return convertView;
	}
}
