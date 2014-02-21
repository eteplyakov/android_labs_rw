package com.example.customview;

public class ListItem {
	private String title_;
	private String details_;

	ListItem(String title, String details) {
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
