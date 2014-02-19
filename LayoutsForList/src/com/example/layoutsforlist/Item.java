package com.example.layoutsforlist;

public class Item {
	private String title_;
	private String details_;

	Item(String title, String details) {
		this.title_ = title;
		this.details_ = details;
	}

	public String getTitle() {
		return title_;
	}

	public void setTitle(String title) {
		this.title_ = title;
	}

	public String getDetails() {
		return details_;
	}

	public void setDetails(String details) {
		this.details_ = details;
	}

}
