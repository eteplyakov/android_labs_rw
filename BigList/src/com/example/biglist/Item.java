package com.example.biglist;

public class Item {
	private String numberText_;
	private int descriptionId_;
	private int iconId_;

	Item(String numberText_, int descriptionId_, int iconId_) {
		this.numberText_ = numberText_;
		this.descriptionId_ = descriptionId_;
		this.iconId_ = iconId_;
	}

	public String getNumberText() {
		return numberText_;
	}

	public void setNumberText(String numberText) {
		this.numberText_ = numberText;
	}

	public int getDescriptionId() {
		return descriptionId_;
	}

	public void setDescriptionId(int descriptionId) {
		this.descriptionId_ = descriptionId;
	}

	public int getIconId() {
		return iconId_;
	}

	public void setIconId(int iconId) {
		this.iconId_ = iconId;
	}
}
