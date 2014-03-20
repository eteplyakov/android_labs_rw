package com.example.personallibrarycatalogue;

import com.example.personallibrarycatalogue.LibraryCatalogueDatabaseOpenHelper.Library;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

public class ViewBookActivity extends FragmentActivity implements LoaderCallbacks<Cursor> {

	static class BooksCursorLoader extends CursorLoader {

		public BooksCursorLoader(Context context) {
			super(context);
		}

		@Override
		public Cursor loadInBackground() {
			Uri books = Uri.parse(LibraryProvider.URI + "/"
					+ ViewBookActivity.bookId_);
			Cursor cursor = getContext().getContentResolver().query(books, null, null, null, null);
			return cursor;
		}
	}
	
	private static final String BOOK_ID = "position";
	private static final int LOADER_ID = 0;
	
	private static int bookId_;

	private ImageView bookCover_;
	private TextView author_;
	private TextView title_;
	private TextView description_;
	private TextView descriptionLabel_;
	private TextView year_;
	private TextView yearLabel_;
	private TextView isbn_;
	private TextView isbnLabel_;

	public static Intent makeIntentViewBook(Context packageContext, int bookId) {
		Intent intent = new Intent(packageContext, ViewBookActivity.class);
		intent.putExtra(BOOK_ID, bookId);
		return intent;
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return new BooksCursorLoader(this);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		cursor.moveToFirst();
		author_.setText(cursor.getString(cursor.getColumnIndex(Library.AUTHOR)));
		title_.setText(cursor.getString(cursor.getColumnIndex(Library.TITLE)));
		description_.setText(cursor.getString(cursor.getColumnIndex(Library.DESCRIPTION)));
		bookCover_.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(Library.COVER))));
		setTitle(cursor.getString(cursor.getColumnIndex(Library.AUTHOR)) + ": " + cursor.getString(cursor.getColumnIndex(Library.TITLE)));
		year_.setText(cursor.getString(cursor.getColumnIndex(Library.YEAR)));
		if(TextUtils.isEmpty(description_.getText().toString())){
			description_.setVisibility(TextView.GONE);
			descriptionLabel_.setVisibility(TextView.GONE);
		}
		if(TextUtils.isEmpty(year_.getText().toString())){
			year_.setVisibility(TextView.GONE);
			yearLabel_.setVisibility(TextView.GONE);
		}
		isbn_.setText(cursor.getString(cursor.getColumnIndex(Library.ISBN)));
		if(TextUtils.isEmpty(isbn_.getText().toString())){
			isbn_.setVisibility(TextView.GONE);
			isbnLabel_.setVisibility(TextView.GONE);
		}
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		loader=null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_book);

		bookCover_ = (ImageView) findViewById(R.id.cover_view);
		author_ = (TextView) findViewById(R.id.author_view);
		title_ = (TextView) findViewById(R.id.title_view);
		description_ = (TextView) findViewById(R.id.description_view);
		year_ = (TextView) findViewById(R.id.year_view);
		isbn_ = (TextView) findViewById(R.id.isbn_view);
		yearLabel_ = (TextView) findViewById(R.id.year_label);
		isbnLabel_ = (TextView) findViewById(R.id.isbn_label);
		descriptionLabel_ = (TextView) findViewById(R.id.description_label);

		Intent intent = getIntent();
		bookId_ = intent.getIntExtra(BOOK_ID, 0);
		
		getSupportLoaderManager().initLoader(LOADER_ID, null, this);
	}
}
