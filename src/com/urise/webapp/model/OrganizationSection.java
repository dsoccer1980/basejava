package com.urise.webapp.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OrganizationSection implements Section {

    private Map<Organization, List<OrganizationPosition>> section = new HashMap<>();

    public OrganizationSection(Map<Organization, List<OrganizationPosition>> section) {
        Objects.requireNonNull(section, "section cannot be null");
        this.section = section;
    }

    public Map<Organization, List<OrganizationPosition>> getSection() {
        return new HashMap<>(section);
    }

    public void setSection(Map<Organization, List<OrganizationPosition>> section) {
        Objects.requireNonNull(section, "section cannot be null");
        this.section = section;
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
