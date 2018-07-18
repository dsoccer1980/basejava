package com.urise.webapp.model;

import java.util.List;
import java.util.Objects;

public class ListSection implements Section {
    private List<String> items;

    public ListSection(List<String> items) {
        Objects.requireNonNull(items, "items cannot be null");
        this.items = items;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        Objects.requireNonNull(items, "items cannot be null");
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListSection that = (ListSection) o;
        return Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }

    @Override
    public String toString() {
        return items.toString();
    }
}