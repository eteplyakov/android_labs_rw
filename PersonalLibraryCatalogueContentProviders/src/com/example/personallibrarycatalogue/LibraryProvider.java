package com.example.personallibrarycatalogue;

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

public class LibraryProvider extends ContentProvider {

	static final String PROVIDER_NAME = "com.example.personallibrarycatalogue.Books";
	static final String URI = "content://" + PROVIDER_NAME + "/books";
	static final Uri CONTENT_URI = Uri.parse(URI);

	static final int BOOKS = 1;
	static final int BOOKS_ID = 2;

	private static HashMap<String, String> BooksMap;

	static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "books", BOOKS);
		uriMatcher.addURI(PROVIDER_NAME, "books/#", BOOKS_ID);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(LibraryCatalogueDatabaseOpenHelper.Library.TABLE_NAME);
		switch (uriMatcher.match(uri)) {
		case BOOKS:
			queryBuilder.setProjectionMap(BooksMap);
			break;
		case BOOKS_ID:
			queryBuilder.appendWhere(LibraryCatalogueDatabaseOpenHelper.Library.ID + "=" + uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		if (TextUtils.isEmpty(sortOrder)) {
			sortOrder = LibraryCatalogueDatabaseOpenHelper.Library.AUTHOR;
		}
		Cursor cursor = queryBuilder.query(LibraryCatalogueDatabaseOpenHelper.getInstance(getContext())
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
		long row = LibraryCatalogueDatabaseOpenHelper.getInstance(getContext()).getWritableDatabase()
				.insert(LibraryCatalogueDatabaseOpenHelper.Library.TABLE_NAME, "", values);
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
		case BOOKS:
			count = LibraryCatalogueDatabaseOpenHelper.getInstance(getContext()).getWritableDatabase()
					.delete(LibraryCatalogueDatabaseOpenHelper.Library.TABLE_NAME, selection, selectionArgs);
			break;
		case BOOKS_ID:
			count = LibraryCatalogueDatabaseOpenHelper
					.getInstance(getContext())
					.getWritableDatabase()
					.delete(LibraryCatalogueDatabaseOpenHelper.Library.TABLE_NAME,
							LibraryCatalogueDatabaseOpenHelper.Library.ID + " = " + uri.getLastPathSegment()
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
		case BOOKS:
			count = LibraryCatalogueDatabaseOpenHelper.getInstance(getContext()).getWritableDatabase()
					.update(LibraryCatalogueDatabaseOpenHelper.Library.TABLE_NAME, values, selection, selectionArgs);
			break;
		case BOOKS_ID:
			count = LibraryCatalogueDatabaseOpenHelper
					.getInstance(getContext())
					.getWritableDatabase()
					.update(LibraryCatalogueDatabaseOpenHelper.Library.TABLE_NAME,
							values,
							LibraryCatalogueDatabaseOpenHelper.Library.ID + " = " + uri.getLastPathSegment()
									+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
