package com.example.biglist;

public class Item {
	private String numberText_;
	private String description_;
	private int iconId_;

	Item(String numberText, String description, int iconId) {
		this.numberText_ = numberText;
		this.description_ = description;
		this.iconId_ = iconId;
	}

	public String getNumberText() {
		return numberText_;
	}

	public String getDescriptionId() {
		return description_;
	}

	public int getIconId() {
		return iconId_;
	}
}
