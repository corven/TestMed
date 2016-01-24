package com.example.cos.testappmed.model;

public class News {
    private int id;
    private String text;
    private String title;
    private String imageURL;
    private String createdAt;
    private String source;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public News(int id, String title, String imageURL, String createdAt) {
        this.id = id;
        this.title = title;
        this.imageURL = imageURL;
        this.createdAt = createdAt;
    }

    public News(int id, String text, String imageURL, String createdAt, String source) {
        this.id = id;
        this.text = text;
        this.imageURL = imageURL;
        this.createdAt = createdAt;
        this.source = source;
    }

    public News(int id) {
        this.id = id;
    }
}
