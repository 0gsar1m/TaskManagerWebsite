package com.emir.mytasks.mytasks_backend.dto;

public class MotivationQuoteResponse {

    private String text;
    private String author;

    public MotivationQuoteResponse() {
    }

    public MotivationQuoteResponse(String text, String author) {
        this.text = text;
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
