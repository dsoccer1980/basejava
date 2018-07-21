package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OrganizationSection extends Section {
    private static final long serialVersionUID = 1L;

    private List<Organization> section = new ArrayList<>();

    public OrganizationSection(List<Organization> section) {
        this.section = section;
    }

    public OrganizationSection(Organization... section) {
        this(Arrays.asList(section));
    }

    public List<Organization> getSection() {
        return section;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationSection that = (OrganizationSection) o;
        return Objects.equals(section, that.section);
    }

    @Override
    public int hashCode() {
        return Objects.hash(section);
    }

    @Override
    public String toString() {
        return "OrganizationSection{" +
                "section=" + section +
                '}';
    }
}
