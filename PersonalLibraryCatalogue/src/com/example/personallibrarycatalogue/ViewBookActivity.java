package com.example.personallibrarycatalogue;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

public class ViewBookActivity extends Activity {

	private static String BOOK_ID = "position";

	private ImageView bookCover_;
	private TextView author_;
	private TextView title_;
	private TextView description_;

	public static Intent makeIntentViewBook(Context packageContext, int bookId) {
		Intent intent = new Intent(packageContext, ViewBookActivity.class);
		intent.putExtra(BOOK_ID, bookId);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_book);

		bookCover_ = (ImageView) findViewById(R.id.cover_view);
		author_ = (TextView) findViewById(R.id.author_view);
		title_ = (TextView) findViewById(R.id.title_view);
		description_ = (TextView) findViewById(R.id.description_view);

		Intent intent = getIntent();
		int bookId = intent.getIntExtra(BOOK_ID, 0);

		DataBaseHelper dataBase = new DataBaseHelper(this);
		Cursor cursor = dataBase.getBookById(bookId);
		cursor.moveToFirst();

		author_.setText(cursor.getString(1));
		title_.setText(cursor.getString(2));
		bookCover_.setImageURI(Uri.parse(cursor.getString(3)));
		description_.setText(cursor.getString(4));
		setTitle(cursor.getString(1) + ": " + cursor.getString(2));
	}
}
