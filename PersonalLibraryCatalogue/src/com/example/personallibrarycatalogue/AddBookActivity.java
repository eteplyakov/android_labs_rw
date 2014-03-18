package com.example.personallibrarycatalogue;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class AddBookActivity extends Activity {

	private static final int SELECT_PICTURE = 1;
	private static final String IMAGE_TYPE = "image/*";
	private static final String BOOK_ID_KEY = "book_id";
	private static final String EDIT_MODE_KEY = "edit_mode";
	private static final String RESOURCE_PATH = "android.resource://com.example.personallibrarycatalogue/";
	private static final String IMAGE_URI_KEY = "slected_image";

	private Uri coverUri_;
	private boolean editMode_;
	private int bookId_;

	private ImageView bookCover_;
	private EditText author_;
	private EditText title_;
	private EditText description_;

	public static Intent makeIntentEdit(Context packageContext, int bookId) {
		Intent intent = new Intent(packageContext, AddBookActivity.class);
		intent.putExtra(BOOK_ID_KEY, bookId);
		intent.putExtra(EDIT_MODE_KEY, true);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_book);

		bookCover_ = (ImageView) findViewById(R.id.cover_image);
		author_ = (EditText) findViewById(R.id.author_input);
		title_ = (EditText) findViewById(R.id.title_input);
		description_ = (EditText) findViewById(R.id.description_input);

		Intent intent = getIntent();
		int bookId = intent.getIntExtra(BOOK_ID_KEY, 0);
		editMode_ = intent.getBooleanExtra(EDIT_MODE_KEY, false);
		if (editMode_) {
			this.setTitle(R.string.edit);
			LibraryCatalogueDatabaseOpenHelper dataBase = LibraryCatalogueDatabaseOpenHelper.getInstance(this);
			Book book = dataBase.getBookById(bookId);
			bookId_ = bookId;
			author_.setText(book.getAuthor());
			title_.setText(book.getTitle());
			bookCover_.setImageURI(Uri.parse(book.getCover()));
			coverUri_ = Uri.parse(book.getCover());
			description_.setText(book.getDescription());
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
		if (!TextUtils.isEmpty(author_.getText()) && !TextUtils.isEmpty(title_.getText())
				&& !TextUtils.isEmpty(description_.getText())) {
			LibraryCatalogueDatabaseOpenHelper dataBase = LibraryCatalogueDatabaseOpenHelper.getInstance(this);
			if (coverUri_ == null) {
				coverUri_ = Uri.parse(RESOURCE_PATH + R.drawable.book);
			}
			if (editMode_) {
				dataBase.updateBook(
						new Book(author_.getText().toString(), title_.getText().toString(), String.valueOf(coverUri_),
								description_.getText().toString()), bookId_);
			} else {
				dataBase.addBook(new Book(author_.getText().toString(), title_.getText().toString(), String
						.valueOf(coverUri_), description_.getText().toString()));
			}
			Intent returnIntent = new Intent();
			setResult(RESULT_OK, returnIntent);
			finish();
		} else {
			Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
		}
	}

	public void onCancelClicked(View view) {
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);
		finish();
	}

}
