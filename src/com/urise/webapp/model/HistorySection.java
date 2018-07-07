package com.urise.webapp.model;

import java.time.LocalDate;
import java.util.Objects;

public class HistorySection implements Section {
    private Link link;
    private String title;
    private LocalDate dateBegin;
    private LocalDate dateEnd;
    private String text;

    public HistorySection(Link link, String title, LocalDate dateBegin, LocalDate dateEnd, String text) {
        this.link = link;
        this.title = title;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
        this.text = text;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(LocalDate dateBegin) {
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
        HistorySection that = (HistorySection) o;
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
        return "HistorySection{" +
                "link=" + link +
                ", title='" + title + '\'' +
                ", dateBegin=" + dateBegin +
                ", dateEnd=" + dateEnd +
                ", text='" + text + '\'' +
                '}';
    }
}
