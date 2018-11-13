package com.urise.webapp.model;

import java.time.format.DateTimeFormatter;
import java.util.List;


public enum SectionType {
    PERSONAL("Личные качества") {
        @Override
        protected String toHtml0(Section section) {
            return toHtmlTextSection(section);
        }
    },
    OBJECTIVE("Позиция") {
        @Override
        protected String toHtml0(Section value) {
            return toHtmlTextSection(value);
        }
    },
    ACHIEVEMENT("Достижения") {
        @Override
        protected String toHtml0(Section value) {
            return toHtmlListSection(value);
        }
    },
    QUALIFICATIONS("Квалификация") {
        @Override
        protected String toHtml0(Section value) {
            return toHtmlListSection(value);
        }
    },
    EXPERIENCE("Опыт работы") {
        @Override
        protected String toHtml0(Section value) {
            return toHtmlOrganizationSection(value);
        }
    },
    EDUCATION("Образование") {
        @Override
        protected String toHtml0(Section value) {
            return toHtmlOrganizationSection(value);
        }
    };

    private String title;

    SectionType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    protected String toHtml0(Section value) {
        return title + ": " + value;
    }

    public String toHtml(Section value) {
        return (value == null) ? "" : toHtml0(value);
    }

    protected String toHtmlTextSection(Section value) {
        return this.title + ": " + ((TextSection) value).getContent();
    }

    protected String toHtmlListSection(Section value) {
        List<String> items = ((ListSection) value).getItems();
        StringBuilder result = new StringBuilder();
        for (String item : items) {
            result.append("<LI>");
            result.append(item);
            result.append("</LI>");
        }
        return "<H3>" + this.title + "</H3>" + result;
    }

    protected String toHtmlOrganizationSection(Section value) {
        List<Organization> items = ((OrganizationSection) value).getSection();
        StringBuilder result = new StringBuilder();
        for (Organization item : items) {
            result.append("<a href=\"");
            result.append(item.getHomePage().getUrl());
            result.append("\">");
            result.append(item.getHomePage().getName());
            result.append("</a>");
            List<Organization.Position> positions = item.getPositions();
            for (Organization.Position position : positions) {
                result.append("<BR>");
                result.append(position.getDateBegin().format(DateTimeFormatter.ofPattern("MM/yyyy")));
                result.append(" - ");
                result.append(position.getDateEnd().format(DateTimeFormatter.ofPattern("MM/yyyy")));
                result.append(position.getTitle());
                result.append("<BR>");
                result.append(position.getText());
                result.append("<BR>");
            }
            result.append("<BR>");
        }
        return "<H3>" + this.title + "</H3>" + result;
    }
}

