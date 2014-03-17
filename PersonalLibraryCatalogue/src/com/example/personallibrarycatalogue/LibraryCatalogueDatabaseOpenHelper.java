package com.example.personallibrarycatalogue;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LibraryCatalogueDatabaseOpenHelper extends SQLiteOpenHelper {

	public static class Library {
		public static final String TABLE_NAME = "library";
		public static final String ID = "_id";
		public static final String AUTHOR = "author";
		public static final String TITLE = "title";
		public static final String COVER = "cover";
		public static final String DESCRIPTION = "description";
		public static final String YEAR = "year";
		public static final String ISBN = "ISBN";
	}

	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "LibraryCatalogue";

	private static LibraryCatalogueDatabaseOpenHelper instance_;

	public static LibraryCatalogueDatabaseOpenHelper getInstance(Context context) {
		if (instance_ == null) {
			instance_ = new LibraryCatalogueDatabaseOpenHelper(context.getApplicationContext());
		}
		return instance_;
	}

	private LibraryCatalogueDatabaseOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE = "CREATE TABLE " + Library.TABLE_NAME + "("
				+ Library.ID + " INTEGER PRIMARY KEY,"
				+ Library.AUTHOR + " TEXT, "
				+ Library.TITLE + " TEXT, "
				+ Library.COVER + " TEXT, "
				+ Library.DESCRIPTION + " TEXT, "
				+ Library.YEAR + " TEXT, "
				+ Library.ISBN + " TEXT)";
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS library");
		onCreate(db);
	}

	public void addBook(Book book) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Library.AUTHOR, book.getAuthor());
		values.put(Library.TITLE, book.getTitle());
		values.put(Library.COVER, book.getCover());
		values.put(Library.DESCRIPTION, book.getDescription());
		db.insert(Library.TABLE_NAME, null, values);
		db.close();
	}

	public void deleteBook(int bookId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(Library.TABLE_NAME, Library.ID + " = ?", new String[] { String.valueOf(bookId) });
		db.close();
	}

	public void updateBook(Book book, int bookId) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Library.AUTHOR, book.getAuthor());
		values.put(Library.TITLE, book.getTitle());
		values.put(Library.COVER, book.getCover());
		values.put(Library.DESCRIPTION, book.getDescription());
		db.update(Library.TABLE_NAME, values, Library.ID + " = ?", new String[] { String.valueOf(bookId) });
		db.close();
	}

	public Cursor getAllBooks() {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(Library.TABLE_NAME, new String[] { Library.ID, Library.AUTHOR, Library.TITLE,
				Library.COVER, Library.DESCRIPTION }, null, null, null, null, Library.AUTHOR + " ASC");
		return cursor;
	}

	public Book getBookById(int bookId) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(true, Library.TABLE_NAME, new String[] { Library.ID, Library.AUTHOR, Library.TITLE,
				Library.COVER, Library.DESCRIPTION }, Library.ID + " = ?", new String[] { String.valueOf(bookId) },
				null, null, null, null);
		cursor.moveToFirst();
		Book book = new Book(cursor.getString(cursor.getColumnIndex(Library.AUTHOR)), cursor.getString(cursor
				.getColumnIndex(Library.TITLE)), cursor.getString(cursor.getColumnIndex(Library.COVER)),
				cursor.getString(cursor.getColumnIndex(Library.DESCRIPTION)));
		cursor.close();
		return book;
	}
}
