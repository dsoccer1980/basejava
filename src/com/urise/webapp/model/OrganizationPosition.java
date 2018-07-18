package com.urise.webapp.model;

import java.time.LocalDate;
import java.util.Objects;

public class OrganizationPosition {
    private String title;
    private LocalDate dateBegin;
    private LocalDate dateEnd;
    private String text;

    public OrganizationPosition(String title, LocalDate dateBegin, LocalDate dateEnd, String text) {
        Objects.requireNonNull(title, "title cannot be null");
        Objects.requireNonNull(dateBegin, "dateBegin cannot be null");
        this.title = title;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
        this.text = text;
    }

    public OrganizationPosition(String titleLink, String link, String title, LocalDate dateBegin, LocalDate dateEnd, String text) {
        Objects.requireNonNull(title, "title cannot be null");
        Objects.requireNonNull(dateBegin, "dateBegin cannot be null");
        Objects.requireNonNull(dateEnd, "dateEnd cannot be null");
        this.title = title;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
        this.text = text;
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
        OrganizationPosition that = (OrganizationPosition) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(dateBegin, that.dateBegin) &&
                Objects.equals(dateEnd, that.dateEnd) &&
                Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, dateBegin, dateEnd, text);
    }

    @Override
    public String toString() {
        return "OrganizationPosition{" +
                "title='" + title + '\'' +
                ", dateBegin=" + dateBegin +
                ", dateEnd=" + dateEnd +
                ", text='" + text + '\'' +
                '}';
    }
}
