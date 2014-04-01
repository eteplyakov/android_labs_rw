package com.example.picalculator;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class PiResultsProvider extends ContentProvider{

	static final String PROVIDER_NAME = "com.example.picalculator.PiResults";
	static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/piresults");

	static final int PI_RESULTS = 1;
	static final int PI_RESULTS_ID = 2;

	private static HashMap<String, String> piResultsMap;

	static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "piresults", PI_RESULTS);
		uriMatcher.addURI(PROVIDER_NAME, "piresults/#", PI_RESULTS_ID);
	}
	
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(PICalculatorDatabaseOpenHelper.PiResults.TABLE_NAME);
		switch (uriMatcher.match(uri)) {
		case PI_RESULTS:
			queryBuilder.setProjectionMap(piResultsMap);
			break;
		case PI_RESULTS_ID:
			queryBuilder.appendWhere(PICalculatorDatabaseOpenHelper.PiResults.ID + "=" + uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		if (TextUtils.isEmpty(sortOrder)) {
			sortOrder = PICalculatorDatabaseOpenHelper.PiResults.PRECISION;
		}
		Cursor cursor = queryBuilder.query(PICalculatorDatabaseOpenHelper.getInstance(getContext())
				.getWritableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long row = PICalculatorDatabaseOpenHelper.getInstance(getContext()).getWritableDatabase()
				.insert(PICalculatorDatabaseOpenHelper.PiResults.TABLE_NAME, "", values);
		if (row > 0) {
			Uri newUri = ContentUris.withAppendedId(CONTENT_URI, row);
			getContext().getContentResolver().notifyChange(newUri, null);
			return newUri;
		}
		throw new SQLException("Fail to add a new record into " + uri);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;
		switch (uriMatcher.match(uri)) {
		case PI_RESULTS:
			count = PICalculatorDatabaseOpenHelper.getInstance(getContext()).getWritableDatabase()
					.delete(PICalculatorDatabaseOpenHelper.PiResults.TABLE_NAME, selection, selectionArgs);
			break;
		case PI_RESULTS_ID:
			count = PICalculatorDatabaseOpenHelper
					.getInstance(getContext())
					.getWritableDatabase()
					.delete(PICalculatorDatabaseOpenHelper.PiResults.TABLE_NAME,
							PICalculatorDatabaseOpenHelper.PiResults.ID + " = " + uri.getLastPathSegment()
									+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int count = 0;
		switch (uriMatcher.match(uri)) {
		case PI_RESULTS:
			count = PICalculatorDatabaseOpenHelper.getInstance(getContext()).getWritableDatabase()
					.update(PICalculatorDatabaseOpenHelper.PiResults.TABLE_NAME, values, selection, selectionArgs);
			break;
		case PI_RESULTS_ID:
			count = PICalculatorDatabaseOpenHelper
					.getInstance(getContext())
					.getWritableDatabase()
					.update(PICalculatorDatabaseOpenHelper.PiResults.TABLE_NAME,
							values,
							PICalculatorDatabaseOpenHelper.PiResults.ID + " = " + uri.getLastPathSegment()
									+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
