package com.example.mibrary;

public class Book {
    private String title;
    private String author;
    private String description;
    private String videoUrl;

    public Book(String title, String author, String description, String videoUrl) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
