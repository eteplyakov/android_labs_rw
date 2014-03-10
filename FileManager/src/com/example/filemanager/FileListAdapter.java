package com.example.filemanager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FileListAdapter extends ArrayAdapter<File> {

	static class ViewHolderItem {
		TextView fileName_;
		TextView fileDescription_;
		ImageView fileIcon_;
		int position_;
	}

	private ThumbCache cache_;

	public FileListAdapter(Context context, int resource, File[] objects) {
		super(context, resource, objects);
		cache_ = new ThumbCache();
	}

	@Override
	public View getView(int position, View listView, ViewGroup parent) {

		ViewHolderItem viewHolder;
		if (listView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			listView = inflater.inflate(R.layout.file_list_item, parent, false);
			viewHolder = new ViewHolderItem();
			viewHolder.fileName_ = (TextView) listView.findViewById(R.id.file_name);
			viewHolder.fileDescription_ = (TextView) listView.findViewById(R.id.description);
			viewHolder.fileIcon_ = (ImageView) listView.findViewById(R.id.file_icon);
			listView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolderItem) listView.getTag();
		}
		if (getItem(position) != null) {
			viewHolder.fileName_.setText(getItem(position).getName());
			viewHolder.fileDescription_.setText(new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(new Date(getItem(
					position).lastModified())));
			viewHolder.position_ = position;
			new LoadImage(getContext(), viewHolder, cache_, position, getItem(position).getAbsolutePath()).execute("");
		}
		return listView;
	}
}
