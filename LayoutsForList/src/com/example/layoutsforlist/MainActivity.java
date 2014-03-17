package com.example.layoutsforlist;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends Activity {

	private static int LIST_COUNT = 12;

	private ListView listOfElements_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Item[] items = new Item[LIST_COUNT];
		for (int i = 0; i < LIST_COUNT; i++) {
			items[i] = new Item(getString(R.string.title), getString(R.string.details));
		}
		listOfElements_ = (ListView) this.findViewById(R.id.list);
		listOfElements_.setAdapter(new ItemsAdapter(this, items));
	}

}
