package com.example.smsresponder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class NumbersListPreference extends DialogPreference {

	private static String NUMBER_LIST_KEY = "numbers_to_answer";
	private static String SPLIT_CHAR = ";";
	private static String NULL_STRING="null";
	
	private EditText newNumberEditText_;
	private Button addButton_;
	private ListView numbersListView_;
	private Context context_;
	private ArrayAdapter<String> adapter_;

	private static boolean checkString(String string) {
		if (string == null || string.length() == 0)
			return false;
		int i = 0;
		if (string.charAt(0) == '-') {
			if (string.length() == 1) {
				return false;
			}
			i = 1;
		}
		char c;
		for (; i < string.length(); i++) {
			c = string.charAt(i);
			if (!(c >= '0' && c <= '9')) {
				return false;
			}
		}
		return true;
	}
	
	public NumbersListPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context_ = context;
	}
	

	@Override
	protected View onCreateDialogView() {
		this.setPositiveButtonText(R.string.positive_button);
		LayoutInflater inflater = LayoutInflater.from(context_);
		View layout = inflater.inflate(R.layout.number_list, null);
		addButton_ = (Button) layout.findViewById(R.id.add_button);
		addButton_.setOnClickListener(handler);
		newNumberEditText_ = (EditText) layout.findViewById(R.id.add_number_text);
		numbersListView_ = (ListView) layout.findViewById(R.id.numbers_list);
		numbersListView_.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		numbersListView_.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context_);
				deleteDialog.setTitle(R.string.delete_title);
				deleteDialog.setMessage(R.string.delete_message + adapter_.getItem(position) + "?");
				final int positionToRemove = position;
				deleteDialog.setNegativeButton(R.string.negative_button, null);
				deleteDialog.setPositiveButton(R.string.positive_button, new AlertDialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context_);
						String numbers = prefs.getString(NUMBER_LIST_KEY, NULL_STRING);
						Editor editor = prefs.edit();
						editor.putString(NUMBER_LIST_KEY, numbers.replace(adapter_.getItem(positionToRemove) + SPLIT_CHAR, ""));
						editor.commit();
						bindList();
					}
				});
				deleteDialog.show();
			}
		});
		bindList();
		return layout;
	}

	View.OnClickListener handler = new View.OnClickListener() {
		public void onClick(View v) {
			if (checkString(newNumberEditText_.getText().toString())) {
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context_);
				String numbers = prefs.getString(NUMBER_LIST_KEY, NULL_STRING);
				Editor editor = prefs.edit();
				if (!numbers.equals(NULL_STRING)) {
					editor.putString(NUMBER_LIST_KEY, numbers + newNumberEditText_.getText() + SPLIT_CHAR);
				} else {
					editor.putString(NUMBER_LIST_KEY, newNumberEditText_.getText().toString() + SPLIT_CHAR);
				}
				editor.commit();
				newNumberEditText_.setText("");
				bindList();
			}
		}
	};

	private void bindList() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context_);
		String numbers = prefs.getString(NUMBER_LIST_KEY, NULL_STRING);
		if (!numbers.equals(NULL_STRING)) {
			String[] numbersArray = numbers.split(SPLIT_CHAR);
			adapter_ = new ArrayAdapter<String>(context_, R.layout.number_list_item, R.id.number_text, numbersArray);
			numbersListView_.setAdapter(adapter_);
		}
	}
}
