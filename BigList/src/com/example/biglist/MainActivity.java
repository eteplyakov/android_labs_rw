package com.example.biglist;

import java.util.Random;
import android.os.Bundle;
import android.widget.ListView;
import android.app.Activity;

public class MainActivity extends Activity {

	private static int COUNT_OF_ITEMS = 20000;
	private static int MAX_NUMBER_IMAGE = 4;
	private static int MAX_NUMBER_DESCRIPTION = 10;

	private static int randomImageId() {
		Random random = new Random();
		switch (random.nextInt(MAX_NUMBER_IMAGE)) {
		case 0:
			return R.drawable.image1;
		case 1:
			return R.drawable.image2;
		case 2:
			return R.drawable.image3;
		case 3:
			return R.drawable.image4;
		default:
			return R.drawable.image1;
		}
	}

	private static int randomDescriptionId() {
		Random random = new Random();
		switch (random.nextInt(MAX_NUMBER_DESCRIPTION)) {
		case 0:
			return R.string.description1;
		case 1:
			return R.string.description2;
		case 2:
			return R.string.description3;
		case 3:
			return R.string.description4;
		case 4:
			return R.string.description5;
		case 5:
			return R.string.description6;
		case 6:
			return R.string.description7;
		case 7:
			return R.string.description8;
		case 8:
			return R.string.description9;
		case 9:
			return R.string.description10;
		default:
			return R.string.description1;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Item[] items = new Item[COUNT_OF_ITEMS];
		for (int i = 0; i < COUNT_OF_ITEMS; i++) {
			items[i] = new Item(getString(R.string.item) + (i + 1), getString(R.string.description_begin) + (i + 1)
					+ " " + getString(randomDescriptionId()), randomImageId());
		}
		ListView listOfElements_ = (ListView) this.findViewById(R.id.list);
		listOfElements_.setAdapter(new ItemAdapter(this, items));
	}
}
