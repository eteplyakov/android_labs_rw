package com.example.personallibrarycataloguewhithtwitter;

import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import android.graphics.Bitmap;

public class MainActivity extends ListActivity {
	
	private static final int RESULT_CODE = 1;

	private ListView bookList_;
	private Dialog auth_dialog;
	private WebView web;
	SharedPreferences pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bookList_ = (ListView) findViewById(android.R.id.list);
		Cursor cursor = LibraryCatalogueDatabaseOpenHelper.getInstance(this).getAllBooks();
		bookList_.setAdapter(new BooksCursorAdapter(this, cursor));
		registerForContextMenu(bookList_);
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
							Cursor newCursor = LibraryCatalogueDatabaseOpenHelper.getInstance(
									MainActivity.this.getBaseContext()).getAllBooks();
							((BooksCursorAdapter) bookList_.getAdapter()).changeCursor(newCursor);
						}
					}).setNegativeButton(android.R.string.no, null).show();
			break;
		case R.id.share_menu:
			String message="";
			message+=getString(R.string.twit_begin)+" ";
			message+=LibraryCatalogueDatabaseOpenHelper.getInstance(MainActivity.this.getBaseContext()).getBookById(bookId).getTitle()+" ";
			message+=LibraryCatalogueDatabaseOpenHelper.getInstance(MainActivity.this.getBaseContext()).getBookById(bookId).getAuthor()+" ";
			message+=getString(R.string.twit_end);
			Intent twitIntent = new Intent(this, TwitterActivity.class);
			twitIntent.putExtra(TwitterActivity.STATUS_KEY, message);
			startActivity(twitIntent);
			break;
		}

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RESULT_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				Cursor newCursor = LibraryCatalogueDatabaseOpenHelper.getInstance(this).getAllBooks();
				((BooksCursorAdapter) bookList_.getAdapter()).changeCursor(newCursor);
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
