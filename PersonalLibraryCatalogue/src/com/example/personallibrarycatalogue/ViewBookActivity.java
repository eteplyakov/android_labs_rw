package com.example.personallibrarycatalogue;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

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

		LibraryCatalogueDatabaseOpenHelper dataBase = LibraryCatalogueDatabaseOpenHelper.getInstance(this);
		Book book = dataBase.getBookById(bookId);

		author_.setText(book.getAuthor());
		title_.setText(book.getTitle());
		bookCover_.setImageURI(Uri.parse(book.getCover()));
		description_.setText(book.getDescription());
		setTitle(book.getAuthor() + ": " + book.getTitle());
	}
}
