package com.example.customview;

import java.util.ArrayList;

import android.os.Bundle;
import android.widget.ListView;
import android.app.Activity;

public class MainActivity extends Activity {

	private ListView listOfElements_;
	private ArrayList<ListItem> elementsArray_ = new ArrayList<ListItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		for (int i = 0; i < 20; i++) {
			elementsArray_.add(new ListItem(getString(R.string.image_item_top_line) + (i + 1),
					getString(R.string.image_item_bottom_line)));
		}
		listOfElements_ = (ListView) this.findViewById(R.id.list);
		listOfElements_.setAdapter(new ListAdapter(this, elementsArray_));
	}

}
