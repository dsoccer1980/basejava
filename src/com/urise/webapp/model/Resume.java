package com.urise.webapp.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


public class Resume implements Comparable<Resume>, Serializable {
    private static final long serialVersionUID = 1L;

    // Unique identifier
    private final String uuid;
    private String fullName;
    private Map<ContactType, String> contact;
    private Map<SectionType, Section> sections;

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Map<ContactType, String> getContact() {
        return new HashMap<>(getContact());
    }

    public void setContact(Map<ContactType, String> contact) {
        this.contact = contact;
    }

    public Map<SectionType, Section> getSections() {
        return new HashMap<>(sections);
    }

    public void setSections(Map<SectionType, Section> sections) {
        this.sections = sections;
    }

    @Override
    public String toString() {
        return "Resume{" +
                "uuid='" + uuid + '\'' +
                ", fullName='" + fullName + '\'' +
                ", \ncontact=" + contact +
                ", \nsections=" + sections +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return Objects.equals(uuid, resume.uuid) &&
                Objects.equals(fullName, resume.fullName) &&
                Objects.equals(contact, resume.contact) &&
                Objects.equals(sections, resume.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName, contact, sections);
    }

    @Override
    public int compareTo(Resume o) {
        int index = getFullName().compareTo(o.getFullName());
        if (index == 0) {
            return uuid.compareTo(o.getUuid());
        }
        return index;
    }
}
