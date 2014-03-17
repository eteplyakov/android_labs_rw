package com.example.personallibrarycatalogue;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends FragmentActivity implements LoaderCallbacks<Cursor> {

	static class BooksCursorLoader extends CursorLoader {

		public BooksCursorLoader(Context context) {
			super(context);
		}

		@Override
		public Cursor loadInBackground() {
			Uri books = Uri.parse("content://com.example.personallibrarycatalogue.Books/books");
			Cursor cursor = getContext().getContentResolver().query(books, null, null, null, null);
			return cursor;
		}
	}

	private static final int RESULT_CODE = 1;

	private ListView bookList_;

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return new BooksCursorLoader(this);
	}

	@Override
	public void onLoadFinished(android.support.v4.content.Loader<Cursor> arg0, Cursor arg1) {
		((BooksCursorAdapter) bookList_.getAdapter()).changeCursor(arg1);
	}

	@Override
	public void onLoaderReset(android.support.v4.content.Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bookList_ = (ListView) findViewById(android.R.id.list);
		bookList_.setAdapter(new BooksCursorAdapter(this, null));
		registerForContextMenu(bookList_);
		getSupportLoaderManager().initLoader(0, null, this);
		bookList_.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Cursor cursor = ((BooksCursorAdapter) ((ListView) parent).getAdapter()).getCursor();
				cursor.moveToPosition(position);
				Intent intent = ViewBookActivity.makeIntentViewBook(view.getContext(),
						cursor.getInt(cursor.getColumnIndex(LibraryCatalogueDatabaseOpenHelper.Library.ID)));
				startActivity(intent);
			}
		});
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		menu.setHeaderTitle(getString(R.string.item_menu_title));
		getMenuInflater().inflate(R.menu.context, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Cursor cursor = ((BooksCursorAdapter) (bookList_).getAdapter()).getCursor();
		cursor.moveToPosition(info.position);
		final int bookId = cursor.getInt(cursor.getColumnIndex(LibraryCatalogueDatabaseOpenHelper.Library.ID));
		switch (item.getItemId()) {
		case R.id.edit_menu:
			Intent intent = AddBookActivity.makeIntentEdit(this, bookId);
			startActivityForResult(intent, RESULT_CODE);
			break;
		case R.id.delete_menu:
			new AlertDialog.Builder(this).setTitle(R.string.delete).setMessage(R.string.delete_question)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int whichButton) {
							LibraryCatalogueDatabaseOpenHelper.getInstance(MainActivity.this.getBaseContext())
									.deleteBook(bookId);
							getSupportLoaderManager().getLoader(0).forceLoad();
						}
					}).setNegativeButton(android.R.string.no, null).show();
			break;
		}

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RESULT_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				getSupportLoaderManager().getLoader(0).forceLoad();
				
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_book:
			onAddBookClicked(findViewById(R.id.add_book_button));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onAddBookClicked(View view) {
		Intent intent = new Intent(this, AddBookActivity.class);
		startActivityForResult(intent, RESULT_CODE);
	}

	
}
