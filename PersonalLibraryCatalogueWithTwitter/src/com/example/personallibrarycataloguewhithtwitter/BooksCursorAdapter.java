package com.example.personallibrarycataloguewhithtwitter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BooksCursorAdapter extends CursorAdapter {

	public BooksCursorAdapter(Context context, Cursor cursor) {
		super(context, cursor);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return LayoutInflater.from(context).inflate(R.layout.book_list_item, parent, false);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView author = (TextView) view.findViewById(R.id.item_author);
		TextView title = (TextView) view.findViewById(R.id.item_title);
		ImageView cover = (ImageView) view.findViewById(R.id.item_cover);

		author.setText(cursor.getString(cursor.getColumnIndex(LibraryCatalogueDatabaseOpenHelper.Library.AUTHOR)));
		title.setText(cursor.getString(cursor.getColumnIndex(LibraryCatalogueDatabaseOpenHelper.Library.TITLE)));
		cover.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(LibraryCatalogueDatabaseOpenHelper.Library.COVER))));
	}
}
