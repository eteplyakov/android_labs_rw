package com.example.personallibrarycatalogue;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends ListActivity {

	private ListView bookList_;
	private Cursor cursor_;
	private LibraryCatalogueDatabaseOpenHelper dataBase_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dataBase_ = LibraryCatalogueDatabaseOpenHelper.getInstance(this);
		bookList_ = (ListView) findViewById(android.R.id.list);
		setAdapter();
		registerForContextMenu(bookList_);
		bookList_.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				cursor_.moveToPosition(position);
				Intent intent = ViewBookActivity.makeIntentViewBook(view.getContext(),
						cursor_.getInt(cursor_.getColumnIndex(LibraryCatalogueDatabaseOpenHelper.Library.ID)));
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
		cursor_.moveToPosition(info.position);
		final int bookId = cursor_.getInt(cursor_.getColumnIndex(LibraryCatalogueDatabaseOpenHelper.Library.ID));
		switch (item.getItemId()) {
		case R.id.edit_menu:
			Intent intent = AddBookActivity.makeIntentEdit(this, bookId);
			startActivity(intent);
			break;
		case R.id.delete_menu:
			new AlertDialog.Builder(this).setTitle(R.string.delete).setMessage(R.string.delete_question)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int whichButton) {
							dataBase_.deleteBook(bookId);
							setAdapter();
						}
					}).setNegativeButton(android.R.string.no, null).show();
			break;
		}

		return true;
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
			Intent intent = new Intent(this, AddBookActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onAddBookClicked(View view) {
		Intent intent = new Intent(this, AddBookActivity.class);
		startActivity(intent);
	}
	
	public void setAdapter(){
		cursor_ = dataBase_.getAllBooks();
		bookList_.setAdapter(new BooksCursorAdapter(this, cursor_));
	}
}
