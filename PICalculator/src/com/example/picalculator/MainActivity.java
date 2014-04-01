package com.example.picalculator;

import java.io.File;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

public class MainActivity extends FragmentActivity implements LoaderCallbacks<Cursor> {

	static class PiResultsCursorLoader extends CursorLoader {

		public PiResultsCursorLoader(Context context) {
			super(context);
		}

		@Override
		public Cursor loadInBackground() {
			Cursor cursor = getContext().getContentResolver().query(PiResultsProvider.CONTENT_URI, null, null, null,
					null);
			return cursor;
		}
	}

	private BroadcastReceiver calculateReceiver_ = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (bundle == null) {
				return;
			}
			int resultCode = bundle.getInt(PiCalculateService.RESULT_KEY);
			if (resultCode == Activity.RESULT_OK) {
				startButton_.setEnabled(true);
				precisionsSpinner_.setEnabled(true);
			}

		}
	};

	private static final int LOADER_ID = 0;

	private Spinner precisionsSpinner_;
	private Button startButton_;
	private ListView resultsList_;

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new PiResultsCursorLoader(this);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		((PiResultsCursorAdapter) resultsList_.getAdapter()).changeCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		((PiResultsCursorAdapter) resultsList_.getAdapter()).swapCursor(null);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		startButton_ = (Button) findViewById(R.id.start_button);
		precisionsSpinner_ = (Spinner) findViewById(R.id.precision_spinner);
		resultsList_ = (ListView) findViewById(R.id.results_list);

		resultsList_.setAdapter(new PiResultsCursorAdapter(this, null));
		registerForContextMenu(resultsList_);

		getSupportLoaderManager().initLoader(LOADER_ID, null, this);

		startButton_.setVisibility(Button.VISIBLE);

		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (PiCalculateService.class.getName().equals(service.service.getClassName())) {
				startButton_.setEnabled(false);
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(calculateReceiver_, new IntentFilter(getPackageName()));
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(calculateReceiver_);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		menu.setHeaderTitle(getString(R.string.context_menu_title));
		getMenuInflater().inflate(R.menu.context, menu);
		Cursor cursor = ((PiResultsCursorAdapter) resultsList_.getAdapter()).getCursor();
		cursor.moveToPosition(((AdapterContextMenuInfo) menuInfo).position);
		String status = cursor.getString(cursor.getColumnIndex(PICalculatorDatabaseOpenHelper.PiResults.STATUS));
		if (status.equals(PiCalculateService.STATUS_IN_PROGRESS)) {
			menu.findItem(R.id.recalculate_menu).setVisible(false);
			menu.findItem(R.id.share_menu).setVisible(false);
			menu.findItem(R.id.cancel_menu).setVisible(true);
		} else {
			if (status.equals(PiCalculateService.STATUS_DONE) || status.equals(PiCalculateService.STATUS_BAD)) {
				menu.findItem(R.id.recalculate_menu).setVisible(true);
				menu.findItem(R.id.share_menu).setVisible(true);
				menu.findItem(R.id.cancel_menu).setVisible(false);
				if (startButton_.isEnabled()) {
					menu.findItem(R.id.recalculate_menu).setEnabled(true);
				} else {
					menu.findItem(R.id.recalculate_menu).setEnabled(false);
				}
			} else {
				if (status.equals(PiCalculateService.STATUS_CANCELED)) {
					menu.findItem(R.id.recalculate_menu).setVisible(true);
					menu.findItem(R.id.share_menu).setVisible(false);
					menu.findItem(R.id.cancel_menu).setVisible(false);
					if (startButton_.isEnabled()) {
						menu.findItem(R.id.recalculate_menu).setEnabled(true);
					} else {
						menu.findItem(R.id.recalculate_menu).setEnabled(false);
					}
				}
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Cursor cursor = ((PiResultsCursorAdapter) resultsList_.getAdapter()).getCursor();
		cursor.moveToPosition(info.position);
		String precision = cursor.getString(cursor.getColumnIndex(PICalculatorDatabaseOpenHelper.PiResults.PRECISION));
		switch (item.getItemId()) {
		case R.id.recalculate_menu:
			Intent intent = new Intent(this, PiCalculateService.class);
			intent.putExtra(PiCalculateService.PRECISION_KEY, precision);
			startService(intent);
			precisionsSpinner_.setEnabled(false);
			startButton_.setEnabled(false);
			break;
		case R.id.cancel_menu:
			stopService(new Intent(this, PiCalculateService.class));
			startButton_.setEnabled(true);
			precisionsSpinner_.setEnabled(true);
			break;
		case R.id.share_menu:
			Intent shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"calculated pi (" + precision + ").txt")));
			startActivity(shareIntent);
			break;
		}
		return true;
	}

	public void onStartClicked(View view) {
		startButton_.setEnabled(false);
		precisionsSpinner_.setEnabled(false);

		Intent intent = new Intent(this, PiCalculateService.class);
		intent.putExtra(PiCalculateService.PRECISION_KEY, precisionsSpinner_.getSelectedItem().toString());
		startService(intent);

	}
}
