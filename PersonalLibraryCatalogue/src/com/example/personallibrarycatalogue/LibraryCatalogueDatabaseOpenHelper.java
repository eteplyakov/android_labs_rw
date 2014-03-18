package com.example.personallibrarycatalogue;

import android.content.Context;
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
		String CREATE_TABLE = "CREATE TABLE " + Library.TABLE_NAME + "(" + Library.ID + " INTEGER PRIMARY KEY,"
				+ Library.AUTHOR + " TEXT, " + Library.TITLE + " TEXT, " + Library.COVER + " TEXT, "
				+ Library.DESCRIPTION + " TEXT, " + Library.YEAR + " TEXT, " + Library.ISBN + " TEXT)";
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion == 1 && newVersion == 2) {
			db.execSQL("ALTER TABLE library ADD COLUMN year TEXT;");
			db.execSQL("ALTER TABLE library ADD COLUMN isbn TEXT;");
			oldVersion++;
		} 
	}
}
