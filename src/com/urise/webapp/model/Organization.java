package com.urise.webapp.model;

import com.urise.webapp.util.LocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.urise.webapp.util.DateUtil.NOW;
import static com.urise.webapp.util.DateUtil.of;

@XmlAccessorType(XmlAccessType.FIELD)
public class Organization implements Serializable {
    private static final long serialVersionUID = 1L;

    private Link homePage;
    private List<Position> positions = new ArrayList<>();

    public Organization() {
    }

    public Organization(Link homePage, List<Position> positions) {
        this.homePage = homePage;
        this.positions = positions;
    }

    public Organization(String name, String url, Position... positions) {
        this(new Link(name, url), Arrays.asList(positions));
    }

    public Link getHomePage() {
        return homePage;
    }

    public List<Position> getPositions() {
        return new ArrayList<>(positions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return Objects.equals(homePage, that.homePage) &&
                Objects.equals(positions, that.positions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homePage, positions);
    }

    @Override
    public String toString() {
        return "Organization{" +
                "homePage=" + homePage +
                ", positions=" + positions +
                '}';
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Position implements Serializable {
        private static final long serialVersionUID = 1L;

        private String title;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate dateBegin;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate dateEnd;
        private String text;

        public Position(String title, LocalDate dateBegin, LocalDate dateEnd, String text) {
            Objects.requireNonNull(title, "title cannot be null");
            Objects.requireNonNull(dateBegin, "dateBegin cannot be null");
            Objects.requireNonNull(dateEnd, "dateEnd cannot be null");
            this.title = title;
            this.dateBegin = dateBegin;
            this.dateEnd = dateEnd;
            this.text = text;
        }

        public Position() {

        }

        public Position(String title, int startYear, Month startMonth, int endYear, Month endMonth, String text) {
            this(title, of(startYear, startMonth), of(endYear, endMonth), text);
        }

        public Position(String title, int startYear, Month startMonth, String text) {
            this(title, of(startYear, startMonth), NOW, text);
        }

        public String getTitle() {
            return title;
        }

        public LocalDate getDateBegin() {
            return dateBegin;
        }

        public LocalDate getDateEnd() {
            return dateEnd;
        }

        public String getText() {
            return text;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return Objects.equals(title, position.title) &&
                    Objects.equals(dateBegin, position.dateBegin) &&
                    Objects.equals(dateEnd, position.dateEnd) &&
                    Objects.equals(text, position.text);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, dateBegin, dateEnd, text);
        }

        @Override
        public String toString() {
            return "Position{" +
                    "title='" + title + '\'' +
                    ", dateBegin=" + dateBegin +
                    ", dateEnd=" + (dateEnd.equals(NOW) ? "Сейчас" : dateEnd) +
                    ", text='" + text + '\'' +
                    '}';
        }
    }
}
