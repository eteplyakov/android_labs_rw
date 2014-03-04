package com.example.personallibrarycatalogue;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
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

public class MainActivity extends Activity {

	private ListView bookList_;
	private Cursor cursor_;
	private DataBaseHelper dataBase_;
	private BooksCursorAdapter cursorAdapter_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dataBase_ = new DataBaseHelper(this);
		cursor_ = dataBase_.getAllBooks();
		if(cursor_.getCount()==0){
			setContentView(R.layout.activity_no_book);
		} else {
			setContentView(R.layout.activity_main);
			cursorAdapter_ = new BooksCursorAdapter(this, cursor_);
			bookList_ = (ListView) findViewById(R.id.book_list);
			bookList_.setAdapter(cursorAdapter_);
			registerForContextMenu(bookList_);
			bookList_.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					cursor_.moveToPosition(position);
					Intent intent = ViewBookActivity.makeIntentViewBook(view.getContext(), cursor_.getInt(0));
					startActivity(intent);
				}
			});
		}
		
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		menu.setHeaderTitle(getString(R.string.item_menu_title));
		menu.add(Menu.NONE, 0, 0, getString(R.string.edit));
		menu.add(Menu.NONE, 1, 1, getString(R.string.delete));
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		final int position = info.position;
		switch (item.getItemId()) {
		case 0:
			cursorAdapter_.getItem(position);
			Intent intent = AddBookActivity.makeIntentEdit(this, position);
			startActivity(intent);
			break;
		case 1:
			new AlertDialog.Builder(this).setTitle(R.string.delete).setMessage(R.string.delete_question)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int whichButton) {
							cursor_.moveToPosition(position);
							int itemId = cursor_.getInt(0);
							dataBase_.deleteBook(itemId);
							Bundle tempBundle = new Bundle();
							onCreate(tempBundle);
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
	
	public void addBookClick(View view){
		Intent intent = new Intent(this, AddBookActivity.class);
		startActivity(intent);
	}
}
