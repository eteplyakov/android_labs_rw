package com.example.personallibrarycatalogue;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

public class AddBookActivity extends Activity {

	private static final int SELECT_PICTURE = 1;
	private static String IMAGE_TYPE = "image/*";
	private static String POSITION_KEY = "position";
	private static String EDIT_KEY = "edit";
	private static String RESOURCE_PATH = "android.resource://com.example.personallibrarycatalogue/";
	private static String IMAGE_URI_KEY = "slected_image";

	private Uri coverUri_;
	private boolean editMode_;
	private int position_;

	private ImageView bookCover_;
	private EditText author_;
	private EditText title_;
	private EditText description_;

	public static Intent makeIntentEdit(Context packageContext, int position) {
		Intent intent = new Intent(packageContext, AddBookActivity.class);
		intent.putExtra(POSITION_KEY, position);
		intent.putExtra(EDIT_KEY, true);
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
		int cursorPosition = intent.getIntExtra(POSITION_KEY, 0);
		editMode_ = intent.getBooleanExtra(EDIT_KEY, false);
		if (editMode_) {
			this.setTitle(R.string.edit);
			DataBaseHelper dataBase = new DataBaseHelper(this);
			Cursor cursor = dataBase.getAllBooks();
			cursor.moveToPosition(cursorPosition);
			position_ = cursor.getInt(0);
			author_.setText(cursor.getString(1));
			title_.setText(cursor.getString(2));
			bookCover_.setImageURI(Uri.parse(cursor.getString(3)));
			coverUri_ = Uri.parse(cursor.getString(3));
			description_.setText(cursor.getString(4));
			cursor.close();
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
		if (savedInstanceState.getString(IMAGE_URI_KEY) != null) {
			coverUri_ = Uri.parse(savedInstanceState.getString(IMAGE_URI_KEY));
			if (coverUri_ != null) {
				bookCover_.setImageURI(coverUri_);
			}
		}
	}

	public void coverImageClick(View view) {
		Intent intent = new Intent();
		intent.setType(IMAGE_TYPE);
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, getString(R.string.select_cover)), SELECT_PICTURE);
	}

	public void saveClick(View view) {
		if (author_.getText() != null && title_.getText() != null && description_.getText() != null
				&& !author_.getText().toString().isEmpty() && !title_.getText().toString().isEmpty()
				&& !description_.getText().toString().isEmpty()) {
			DataBaseHelper dataBase = new DataBaseHelper(this);
			if (coverUri_ == null) {
				coverUri_ = Uri.parse(RESOURCE_PATH + R.drawable.book);
			}
			if (editMode_) {
				dataBase.updateBook(
						new Book(author_.getText().toString(), title_.getText().toString(), String.valueOf(coverUri_),
								description_.getText().toString()), position_);
			} else {
				dataBase.addBook(new Book(author_.getText().toString(), title_.getText().toString(), String
						.valueOf(coverUri_), description_.getText().toString()));
			}
			Intent intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		} else {
			Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
		}
	}

	public void cancelClick(View view) {
		super.onBackPressed();
	}

}
