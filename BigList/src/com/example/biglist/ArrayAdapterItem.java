package com.example.biglist;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ArrayAdapterItem extends BaseAdapter {

	ArrayList<Item> elementsArray_ = new ArrayList<Item>();
	Context context_;

	static class ViewHolderItem {
		TextView itemNumber;
		TextView itemDescription;
		ImageView itemIcon;
	}

	public ArrayAdapterItem(Context context, ArrayList<Item> itemsArray) {
		if (itemsArray != null) {
			elementsArray_ = itemsArray;
		}
		this.context_ = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolderItem viewHolder;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context_);
			convertView = inflater.inflate(R.layout.list_view_item, parent, false);
			viewHolder = new ViewHolderItem();
			viewHolder.itemNumber = (TextView) convertView.findViewById(R.id.number);
			viewHolder.itemDescription = (TextView) convertView.findViewById(R.id.description);
			viewHolder.itemIcon = (ImageView) convertView.findViewById(R.id.left_image);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolderItem) convertView.getTag();
		}
		// assign values if the object is not null
		if (elementsArray_.get(position) != null) {
			// get the TextView from the ViewHolder and then set the text (item
			// name) and tag (item ID) values
			viewHolder.itemNumber.setText(elementsArray_.get(position).getNumberText());
			viewHolder.itemDescription.setText(elementsArray_.get(position).getDescriptionId());
			viewHolder.itemIcon.setImageResource(elementsArray_.get(position).getIconId());
		}

		return convertView;

	}

	@Override
	public int getCount() {
		return elementsArray_.size();
	}

	@Override
	public Object getItem(int position) {
		return elementsArray_.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
