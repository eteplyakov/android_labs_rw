package com.example.personallibrarycatalogue;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

	private static int DATABASE_VERSION = 1;
	private static String DATABASE_NAME = "LibraryCatalogue";
	private static String TABLE_NAME = "library";
	private static String KEY_ID = "_id";
	private static String KEY_AUTHOR = "author";
	private static String KEY_TITLE = "title";
	private static String KEY_COVER = "cover";
	private static String KEY_DESCRIPTION = "description";

	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_AUTHOR
				+ " TEXT," + KEY_TITLE + " TEXT," + KEY_COVER + " TEXT," + KEY_DESCRIPTION + " TEXT)";
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void addBook(Book book) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_AUTHOR, book.getAuthor());
		values.put(KEY_TITLE, book.getTitle());
		values.put(KEY_COVER, book.getCover());
		values.put(KEY_DESCRIPTION, book.getDescription());
		db.insert(TABLE_NAME, null, values);
		db.close();
	}

	public void deleteBook(int position) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(position) });
		db.close();
	}

	public void updateBook(Book book, int position) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_AUTHOR, book.getAuthor());
		values.put(KEY_TITLE, book.getTitle());
		values.put(KEY_COVER, book.getCover());
		values.put(KEY_DESCRIPTION, book.getDescription());
		db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[] { String.valueOf(position) });
		db.close();
	}

	public Cursor getAllBooks() {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(TABLE_NAME,
				new String[] { KEY_ID, KEY_AUTHOR, KEY_TITLE, KEY_COVER, KEY_DESCRIPTION }, null, null, null, null,
				KEY_ID + " ASC");
		return cursor;
	}

	public Cursor getBookById(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(true, TABLE_NAME, new String[] { KEY_ID, KEY_AUTHOR, KEY_TITLE, KEY_COVER,
				KEY_DESCRIPTION }, KEY_ID + " = " + id, null, null, null, null, null);
		return cursor;
	}
}
