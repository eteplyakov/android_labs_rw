package com.example.biglist;

import java.util.ArrayList;
import java.util.Random;

import android.os.Bundle;
import android.widget.ListView;
import android.app.Activity;

public class MainActivity extends Activity {
	private static final int COUNT_OF_ITEMS = 20000;
	private static final int MAX_NUMBER_IMAGE = 4;
	private static final int MAX_NUMBER_DESCRIPTION = 10;
	private static final String ITEM_STRING_BEGIN = "Item #";
	private ListView listOfElements_;
	private ArrayList<Item> elementsArray_ = new ArrayList<Item>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		int imageNumber = 0;
		int descriprionNumber = 0;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Random random = new Random();
		for (int i = 0; i < COUNT_OF_ITEMS; i++) {
			switch (random.nextInt(MAX_NUMBER_IMAGE)) {
			case 0:
				imageNumber = R.drawable.image1;
				break;
			case 1:
				imageNumber = R.drawable.image2;
				break;
			case 2:
				imageNumber = R.drawable.image3;
				break;
			case 3:
				imageNumber = R.drawable.image4;
				break;
			default:
				imageNumber = R.drawable.image1;
				break;
			}
			switch (random.nextInt(MAX_NUMBER_DESCRIPTION)) {
			case 0:
				descriprionNumber = R.string.description1;
				break;
			case 1:
				descriprionNumber = R.string.description2;
				break;
			case 2:
				descriprionNumber = R.string.description3;
				break;
			case 3:
				descriprionNumber = R.string.description4;
				break;
			case 4:
				descriprionNumber = R.string.description5;
				break;
			case 5:
				descriprionNumber = R.string.description6;
				break;
			case 6:
				descriprionNumber = R.string.description7;
				break;
			case 7:
				descriprionNumber = R.string.description8;
				break;
			case 8:
				descriprionNumber = R.string.description9;
				break;
			case 9:
				descriprionNumber = R.string.description10;
				break;
			}
			elementsArray_.add(new Item(ITEM_STRING_BEGIN+(i + 1), descriprionNumber, imageNumber));
		}
		listOfElements_ = (ListView) this.findViewById(R.id.list);
		listOfElements_.setAdapter(new ArrayAdapterItem(this, elementsArray_));
	}
}
