package com.urise.webapp.model;

import java.util.Objects;

public class Organization {
    private String title;
    private String link;

    public Organization(String title, String link) {
        Objects.requireNonNull(title, "title cannot be null");
        this.title = title;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        Objects.requireNonNull(title, "title cannot be null");
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}