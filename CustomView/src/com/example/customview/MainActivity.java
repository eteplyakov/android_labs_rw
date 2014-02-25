package com.example.customview;

import android.os.Bundle;
import android.widget.ListView;
import android.app.Activity;

public class MainActivity extends Activity {

	private static int COUNT_ITEMS = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ListItem[] items = new ListItem[COUNT_ITEMS];
		for (int i = 0; i < COUNT_ITEMS; i++) {
			items[i] = new ListItem(getString(R.string.image_item_top_line) + (i + 1),
					getString(R.string.image_item_bottom_line));
		}
		ListView listOfElements = (ListView) this.findViewById(R.id.list);
		listOfElements.setAdapter(new ListAdapter(this, items));
	}

}
