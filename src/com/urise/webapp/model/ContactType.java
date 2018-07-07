package com.urise.webapp.model;

public enum ContactType {
    TELEFON("телефон"),
    SKYPE("почта"),
    EMAIL("телефон"),
    LINKEDIN("Профиль LinkedIn"),
    GITHUB("Профиль GitHub"),
    STACKOVERFLOW("Профиль Stackoverflow");

    private String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
