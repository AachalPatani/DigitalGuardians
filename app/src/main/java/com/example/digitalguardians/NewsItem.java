package com.example.digitalguardians;

public class NewsItem {
    private String title;
    private String description;
    private String link;

    // Main constructor
    public NewsItem(String title, String description, String link) {
        this.title = title;
        this.description = description;
        this.link = link;
    }

    // Overloaded constructor (when description is not available)
    public NewsItem(String title, String link) {
        this(title, "No description available", link);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }
}

