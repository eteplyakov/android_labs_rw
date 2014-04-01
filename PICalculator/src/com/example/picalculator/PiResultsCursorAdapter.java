package com.example.picalculator;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PiResultsCursorAdapter extends CursorAdapter {

	public PiResultsCursorAdapter(Context context, Cursor c) {
		super(context, c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView precisionName = (TextView) view.findViewById(R.id.precision_name);
		TextView piResultStatus = (TextView) view.findViewById(R.id.pi_result_status);

		precisionName.setText(cursor.getString(cursor
				.getColumnIndex(PICalculatorDatabaseOpenHelper.PiResults.PRECISION)));
		if (cursor.getString(cursor.getColumnIndex(PICalculatorDatabaseOpenHelper.PiResults.STATUS)).equals(
				PiCalculateService.STATUS_DONE)) {
			piResultStatus.setText(context.getString(R.string.done) + " "
					+ cursor.getString(cursor.getColumnIndex(PICalculatorDatabaseOpenHelper.PiResults.TIME)) + " "
					+ context.getString(R.string.time_sample));
			precisionName.setBackgroundColor(context.getResources().getColor(R.color.green));
		} else {
			if (cursor.getString(cursor.getColumnIndex(PICalculatorDatabaseOpenHelper.PiResults.STATUS)).equals(
					PiCalculateService.STATUS_IN_PROGRESS)) {
				piResultStatus.setText(context.getString(R.string.in_progress) + " ("
						+ cursor.getString(cursor.getColumnIndex(PICalculatorDatabaseOpenHelper.PiResults.PROGRESS))
						+ context.getString(R.string.in_progress_end) + ")");
				precisionName.setBackgroundColor(context.getResources().getColor(R.color.yellow));
			} else {
				if (cursor.getString(cursor.getColumnIndex(PICalculatorDatabaseOpenHelper.PiResults.STATUS)).equals(
						PiCalculateService.STATUS_CANCELED)) {
					piResultStatus.setText(context.getString(R.string.canceled_text));
					precisionName.setBackgroundColor(context.getResources().getColor(R.color.red));
				} else {
					if (cursor.getString(cursor.getColumnIndex(PICalculatorDatabaseOpenHelper.PiResults.STATUS)).equals(
							PiCalculateService.STATUS_BAD)) {
						piResultStatus.setText(context.getString(R.string.done) + " "
								+ cursor.getString(cursor.getColumnIndex(PICalculatorDatabaseOpenHelper.PiResults.TIME)) + " "
								+ context.getString(R.string.time_sample));
						precisionName.setBackgroundColor(context.getResources().getColor(R.color.red));
					}
				}
			}
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return LayoutInflater.from(context).inflate(R.layout.results_list_item, parent, false);
	}

}
