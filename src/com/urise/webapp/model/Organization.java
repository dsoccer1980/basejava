package com.urise.webapp.model;

import java.time.LocalDate;
import java.util.Objects;

public class Organization {
    private Link link;
    private String title;
    private LocalDate dateBegin;
    private LocalDate dateEnd;
    private String text;

    public Organization(Link link, String title, LocalDate dateBegin, LocalDate dateEnd, String text) {
        Objects.requireNonNull(link, "link cannot be null");
        Objects.requireNonNull(title, "title cannot be null");
        Objects.requireNonNull(dateBegin, "dateBegin cannot be null");
        Objects.requireNonNull(dateEnd, "dateEnd cannot be null");
        this.link = link;
        this.title = title;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
        this.text = text;
    }

    public Organization(String titleLink, String link, String title, LocalDate dateBegin, LocalDate dateEnd, String text) {
        Objects.requireNonNull(title, "title cannot be null");
        Objects.requireNonNull(dateBegin, "dateBegin cannot be null");
        Objects.requireNonNull(dateEnd, "dateEnd cannot be null");
        this.link = new Link(titleLink, link);
        this.title = title;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
        this.text = text;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        Objects.requireNonNull(link, "link cannot be null");
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        Objects.requireNonNull(title, "title cannot be null");
        this.title = title;
    }

    public LocalDate getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(LocalDate dateBegin) {
        Objects.requireNonNull(dateBegin, "dateBegin cannot be null");
        this.dateBegin = dateBegin;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        Objects.requireNonNull(dateEnd, "dateEnd cannot be null");
        this.dateEnd = dateEnd;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return Objects.equals(link, that.link) &&
                Objects.equals(title, that.title) &&
                Objects.equals(dateBegin, that.dateBegin) &&
                Objects.equals(dateEnd, that.dateEnd) &&
                Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link, title, dateBegin, dateEnd, text);
    }

    @Override
    public String toString() {
        return "OrganizationSection{" +
                "link=" + link +
                ", title='" + title + '\'' +
                ", dateBegin=" + dateBegin +
                ", dateEnd=" + dateEnd +
                ", text='" + text + '\'' +
                '}';
    }
}
