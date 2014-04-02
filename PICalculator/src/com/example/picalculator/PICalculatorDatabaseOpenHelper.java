package com.example.picalculator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PICalculatorDatabaseOpenHelper extends SQLiteOpenHelper {
	
	public static class PiResults {
		public static final String TABLE_NAME = "pi_results";
		public static final String ID = "_id";
		public static final String PRECISION = "precision";
		public static final String TIME = "time";
		public static final String STATUS = "status";
		public static final String PROGRESS = "progress";
	}
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "PiResults";

	private static PICalculatorDatabaseOpenHelper instance_;

	public static PICalculatorDatabaseOpenHelper getInstance(Context context) {
		if (instance_ == null) {
			instance_ = new PICalculatorDatabaseOpenHelper(context.getApplicationContext());
		}
		return instance_;
	}
	
	private PICalculatorDatabaseOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE = "CREATE TABLE " + PiResults.TABLE_NAME + "(" + PiResults.ID + " INTEGER PRIMARY KEY,"
				+ PiResults.PRECISION + " TEXT, " + PiResults.TIME + " TEXT, " + PiResults.STATUS + " TEXT, "
				+ PiResults.PROGRESS + " TEXT)";
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS pi_results");
		onCreate(db);
	}

}
