package com.example.personallibrarycataloguewhithtwitter;

public class Book {

	private String author_;
	private String title_;
	private String cover_;
	private String description_;

	public Book(String author, String title, String cover, String description) {
		this.author_ = author;
		this.title_ = title;
		this.cover_ = cover;
		this.description_ = description;
	}

	public String getAuthor() {
		return this.author_;
	}

	public String getTitle() {
		return this.title_;
	}

	public String getCover() {
		return this.cover_;
	}

	public String getDescription() {
		return this.description_;
	}
}
