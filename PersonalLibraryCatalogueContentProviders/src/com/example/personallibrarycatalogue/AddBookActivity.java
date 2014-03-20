package com.example.personallibrarycatalogue;

import com.example.personallibrarycatalogue.LibraryCatalogueDatabaseOpenHelper.Library;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

public class AddBookActivity extends FragmentActivity implements LoaderCallbacks<Cursor> {

	static class BooksCursorLoader extends CursorLoader {

		public BooksCursorLoader(Context context) {
			super(context);
		}

		@Override
		public Cursor loadInBackground() {
			Uri books = Uri.parse(LibraryProvider.URI+ "/"
					+ AddBookActivity.bookId_);
			Cursor cursor = getContext().getContentResolver().query(books, null, null, null, null);
			return cursor;
		}
	}

	private static final int SELECT_PICTURE = 1;
	private static final int LOADER_ID = 0;
	private static final String IMAGE_TYPE = "image/*";
	private static final String BOOK_ID_KEY = "book_id";
	private static final String EDIT_MODE_KEY = "edit_mode";
	private static final String RESOURCE_PATH = "android.resource://com.example.personallibrarycatalogue/";
	private static final String IMAGE_URI_KEY = "slected_image";

	private static int bookId_;
	private Uri coverUri_;
	private boolean editMode_;

	private ImageView bookCover_;
	private EditText author_;
	private EditText title_;
	private EditText description_;
	private EditText year_;
	private EditText isbn_;

	public static Intent makeIntentEdit(Context packageContext, int bookId) {
		Intent intent = new Intent(packageContext, AddBookActivity.class);
		intent.putExtra(BOOK_ID_KEY, bookId);
		intent.putExtra(EDIT_MODE_KEY, true);
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
		year_.setText(cursor.getString(cursor.getColumnIndex(Library.YEAR)));
		isbn_.setText(cursor.getString(cursor.getColumnIndex(Library.ISBN)));
		coverUri_ = Uri.parse(cursor.getString(cursor.getColumnIndex(Library.COVER)));
		bookCover_.setImageURI(coverUri_);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		loader = null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_book);

		bookCover_ = (ImageView) findViewById(R.id.cover_image);
		author_ = (EditText) findViewById(R.id.author_input);
		title_ = (EditText) findViewById(R.id.title_input);
		description_ = (EditText) findViewById(R.id.description_input);
		year_ = (EditText) findViewById(R.id.year_input);
		isbn_ = (EditText) findViewById(R.id.isbn_input);

		Intent intent = getIntent();
		bookId_ = intent.getIntExtra(BOOK_ID_KEY, 0);
		editMode_ = intent.getBooleanExtra(EDIT_MODE_KEY, false);
		if (editMode_) {
			this.setTitle(R.string.edit);
			getSupportLoaderManager().initLoader(LOADER_ID, null, this);
		} else {
			this.setTitle(R.string.add_new_book);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case SELECT_PICTURE:
				coverUri_ = data.getData();
				bookCover_.setImageURI(coverUri_);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		if (coverUri_ != null) {
			savedInstanceState.putString(IMAGE_URI_KEY, coverUri_.toString());
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState.containsKey(IMAGE_URI_KEY)) {
			coverUri_ = Uri.parse(savedInstanceState.getString(IMAGE_URI_KEY));
			if (coverUri_ != null) {
				bookCover_.setImageURI(coverUri_);
			}
		}
	}

	public void coverImageClicked(View view) {
		Intent intent = new Intent();
		intent.setType(IMAGE_TYPE);
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, getString(R.string.select_cover)), SELECT_PICTURE);
	}

	public void onSaveClicked(View view) {

		if (isRightData()) {
			if (coverUri_ == null) {
				coverUri_ = Uri.parse(RESOURCE_PATH + R.drawable.book);
			}
			if (editMode_) {
				ContentValues values = new ContentValues();
				values.put(LibraryCatalogueDatabaseOpenHelper.Library.AUTHOR, author_.getText().toString());
				values.put(LibraryCatalogueDatabaseOpenHelper.Library.TITLE, title_.getText().toString());
				values.put(LibraryCatalogueDatabaseOpenHelper.Library.DESCRIPTION, description_.getText().toString());
				values.put(LibraryCatalogueDatabaseOpenHelper.Library.COVER, coverUri_.toString());
				values.put(LibraryCatalogueDatabaseOpenHelper.Library.YEAR, year_.getText().toString());
				values.put(LibraryCatalogueDatabaseOpenHelper.Library.ISBN, isbn_.getText().toString());
				getContentResolver().update(
						Uri.parse(LibraryProvider.URI + "/" + bookId_), values,
						null, null);
			} else {
				ContentValues values = new ContentValues();
				values.put(LibraryCatalogueDatabaseOpenHelper.Library.AUTHOR, author_.getText().toString());
				values.put(LibraryCatalogueDatabaseOpenHelper.Library.TITLE, title_.getText().toString());
				values.put(LibraryCatalogueDatabaseOpenHelper.Library.DESCRIPTION, description_.getText().toString());
				values.put(LibraryCatalogueDatabaseOpenHelper.Library.COVER, coverUri_.toString());
				values.put(LibraryCatalogueDatabaseOpenHelper.Library.YEAR, year_.getText().toString());
				values.put(LibraryCatalogueDatabaseOpenHelper.Library.ISBN, isbn_.getText().toString());
				getContentResolver().insert(LibraryProvider.CONTENT_URI, values);
			}
			Intent returnIntent = new Intent();
			setResult(RESULT_OK, returnIntent);
			finish();
		}
	}

	public void onCancelClicked(View view) {
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);
		finish();
	}

	private boolean isRightData() {
		boolean result = true;
		if (TextUtils.isEmpty(author_.getText())) {
			author_.setError(getString(R.string.error_blank));
			result = false;
		}
		if (TextUtils.isEmpty(title_.getText())) {
			title_.setError(getString(R.string.error_blank));
			result = false;
		}
		if (TextUtils.isEmpty(description_.getText())) {
			description_.setError(getString(R.string.error_blank));
			result = false;
		}
		if (!year_.getText().toString().matches("\\d{4}")) {
			if (!TextUtils.isEmpty(year_.getText())) {
				year_.setError(getString(R.string.error_year));
				result = false;
			}
		}
		return result;
	}
}
