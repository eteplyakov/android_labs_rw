package com.example.layoutsforlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class ItemsAdapter extends ArrayAdapter<Item> {

	public ItemsAdapter(Context context, Item[] objects) {
		super(context, 0, objects);
	}

	@Override
	public long getItemId(int itemId) {
		return itemId;
	}

	@Override
	public View getView(int position, View listView, ViewGroup arg2) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		if (listView == null) {
			listView = inflater.inflate(R.layout.list_view_item, arg2, false);
		}
		TextView title = (TextView) listView.findViewById(R.id.title);
		TextView details = (TextView) listView.findViewById(R.id.details);
		ImageView rightIcon = (ImageView) listView.findViewById(R.id.xx);
		ImageView leftIcon = (ImageView) listView.findViewById(R.id.image);
		title.setText(getItem(position).getTitle());
		details.setText(getItem(position).getDetails());
		RelativeLayout.LayoutParams whithoutOneElement = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		if ((position + 1) % 2 == 0) {
			whithoutOneElement.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			whithoutOneElement.addRule(RelativeLayout.RIGHT_OF, leftIcon.getId());
			whithoutOneElement.addRule(RelativeLayout.ALIGN_BOTTOM, leftIcon.getId());
			title.setLayoutParams(whithoutOneElement);
			details.setVisibility(View.GONE);
		}
		if ((position + 1) % 3 == 0) {
			whithoutOneElement.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			whithoutOneElement.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			whithoutOneElement.addRule(RelativeLayout.RIGHT_OF, leftIcon.getId());
			title.setLayoutParams(whithoutOneElement);
			rightIcon.setVisibility(View.GONE);
		}
		RelativeLayout.LayoutParams whithoutTwoElement = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		if (((position + 1) % 2 == 0) && ((position + 1) % 3 == 0)) {
			whithoutTwoElement.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			whithoutTwoElement.addRule(RelativeLayout.RIGHT_OF, leftIcon.getId());
			whithoutTwoElement.addRule(RelativeLayout.ALIGN_BOTTOM, leftIcon.getId());
			title.setLayoutParams(whithoutTwoElement);
		}
		return listView;
	}
}
