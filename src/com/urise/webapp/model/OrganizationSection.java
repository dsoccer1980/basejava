package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrganizationSection implements Section {
    private List<Organization> list;

    public OrganizationSection(List<Organization> list) {
        Objects.requireNonNull(list, "list cannot be null");
        this.list = list;
    }

    public List<Organization> getList() {
        return new ArrayList<>(list);
    }

    public void setList(List<Organization> list) {
        Objects.requireNonNull(list, "list cannot be null");
        this.list = list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationSection that = (OrganizationSection) o;
        return Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }

    @Override
    public String toString() {
        return "OrganizationSection{" +
                "list=" + list +
                '}';
    }
}
