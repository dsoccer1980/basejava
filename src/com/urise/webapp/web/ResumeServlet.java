package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.SqlStorage;
import com.urise.webapp.storage.Storage;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ResumeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        final Config config = Config.get();

        Storage storage = new SqlStorage(config.getDbUrl(), config.getDbUser(), config.getDbPassword());
        List<Resume> allSorted = storage.getAllSorted();
        for (Resume resume : allSorted) {
            response.getWriter().write(resume.getUuid() + "  " + resume.getFullName());
            response.getWriter().write("<BR>");
            Map<ContactType, String> contacts = resume.getContacts();
            if (!contacts.isEmpty()) {
                response.getWriter().write("Contacts:<BR>");
                for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                    response.getWriter().write(entry.getKey() + ": " + entry.getValue());
                    response.getWriter().write("<BR>");
                }
            }
            Map<SectionType, Section> sections = resume.getSections();
            if (!sections.isEmpty()) {
                response.getWriter().write("Sections:<BR>");
                for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
                    SectionType key = entry.getKey();
                    switch (key) {
                        case OBJECTIVE:
                        case PERSONAL:
                            response.getWriter().write(key + ": " + ((TextSection) entry.getValue()).getContent());
                            break;
                        case ACHIEVEMENT:
                        case QUALIFICATIONS:
                            response.getWriter().write(key + ": " + ((ListSection) entry.getValue()).getItems());
                    }
                    response.getWriter().write("<BR>");
                }
            }
            response.getWriter().write("<BR><BR>");
        }
    }
}