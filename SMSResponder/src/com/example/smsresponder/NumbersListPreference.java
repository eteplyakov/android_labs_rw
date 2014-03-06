package com.example.smsresponder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Build;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class NumbersListPreference extends DialogPreference {

	private static String NUMBER_LIST_KEY = "numbers_to_answer";
	private static String SPLIT_CHAR = ";";

	private ListView numbersListView_;
	private TextView headText_;
	private ArrayAdapter<String> adapter_;

	public NumbersListPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setNegativeButtonText(null);
	}

	@Override
	protected View onCreateDialogView() {
		this.setPositiveButtonText(R.string.positive_button);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		LinearLayout layout = new LinearLayout(getContext());
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setPadding(10, 10, 10, 10);

		headText_ = new TextView(getContext());
		headText_.setTextSize(20);
		headText_.setGravity(Gravity.CENTER_HORIZONTAL);
		headText_.setText(R.string.number_list_empty);
		layout.addView(headText_, params);

		numbersListView_ = new ListView(getContext());
		params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layout.addView(numbersListView_, params);
		numbersListView_.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				menu.setHeaderTitle(getContext().getString(R.string.menu));
				menu.add(Menu.NONE, 0, 0, getContext().getString(R.string.delete_title)).setOnMenuItemClickListener(
						new OnMenuItemClickListener() {

							@Override
							public boolean onMenuItemClick(MenuItem item) {
								AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
								final int position = info.position;
								new AlertDialog.Builder(getContext()).setTitle(R.string.delete_title)
										.setMessage(R.string.delete_message)
										.setIcon(android.R.drawable.ic_dialog_alert)
										.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

											public void onClick(DialogInterface dialog, int whichButton) {
												SharedPreferences prefs = PreferenceManager
														.getDefaultSharedPreferences(getContext());
												String numbers = prefs.getString(NUMBER_LIST_KEY, "");
												Editor editor = prefs.edit();
												editor.putString(NUMBER_LIST_KEY,
														numbers.replace(adapter_.getItem(position) + SPLIT_CHAR, ""));
												editor.commit();
												bindList();
											}
										}).setNegativeButton(android.R.string.no, null).show();
								return true;
							}
						});
			}
		});
		bindList();
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			headText_.setTextColor(Color.WHITE);
		}
		return layout;
	}

	private void bindList() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
		String numbers = prefs.getString(NUMBER_LIST_KEY, "");
		String[] numbersArray = numbers.split(SPLIT_CHAR);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			adapter_ = new ArrayAdapter<String>(getContext(), R.layout.number_list_item_white, R.id.number_text_white,
					numbersArray);
		} else {
			adapter_ = new ArrayAdapter<String>(getContext(), R.layout.number_list_item, R.id.number_text, numbersArray);
		}
		numbersListView_.setAdapter(adapter_);
		if (numbers.equals("")) {
			numbersListView_.setVisibility(View.GONE);
			headText_.setVisibility(View.VISIBLE);
		} else {
			numbersListView_.setVisibility(View.VISIBLE);
			headText_.setVisibility(View.GONE);
		}
	}
}
