package com.example.layoutsforlist;

public class Item {
	private String title_;
	private String details_;

	public Item(String title, String details) {
		this.title_ = title;
		this.details_ = details;
	}

	public String getTitle() {
		return title_;
	}

	public String getDetails() {
		return details_;
	}
}
