package com.example.layoutsforlist;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends Activity {

	ListView listOfElements;
	ArrayList<Item> elementsArray = new ArrayList<Item>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		for(int i=0;i<12;i++){
			elementsArray.add(new Item(getString(R.string.title), getString(R.string.details)));
		}
		listOfElements = (ListView) this.findViewById(R.id.list);
		listOfElements.setAdapter(new MyAdapter(this, elementsArray));
	}

}
