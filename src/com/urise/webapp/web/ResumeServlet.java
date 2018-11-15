package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.SqlStorage;
import com.urise.webapp.storage.Storage;
import com.urise.webapp.util.DateUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    private final Config config = Config.get();
    private Storage storage;

    @Override
    public void init() throws ServletException {
        super.init();
        storage = new SqlStorage(config.getDbUrl(), config.getDbUser(), config.getDbPassword());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");

        Resume resume;
        if (uuid != null) {
            resume = storage.get(uuid);
            resume.setFullName(fullName);
        } else {
            resume = new Resume(fullName);
        }

        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                resume.addContact(type, value);
            } else {
                resume.getContacts().remove(type);
            }
        }

        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                switch (type.name()) {
                    case "PERSONAL":
                    case "OBJECTIVE":
                        resume.addSection(type, new TextSection(value));
                        break;
                    case "ACHIEVEMENT":
                    case "QUALIFICATIONS":
                        resume.addSection(type, new ListSection(value.split("\n")));
                        break;
                    case "EXPERIENCE":
                    case "EDUCATION":
                        int amount = request.getParameterValues(type.name()).length;
                        List<Organization> organizations = new ArrayList<>();
                        for (int index = 0; index < amount; index++) {
                            String url = request.getParameter(type.name() + "url");
                            LocalDate startDate = DateUtil.parse(request.getParameter(type.name() + index + "startDate").trim());
                            LocalDate endDate = DateUtil.parse(request.getParameter(type.name() + index + "endDate").trim());
                            String title = request.getParameter(type.name() + index + "title");
                            String description = request.getParameter(type.name() + index + "description");
                            Organization.Position position = new Organization.Position(title, startDate, endDate, description);
                            organizations.add(new Organization(value, url, position));
                        }
                        resume.addSection(type, new OrganizationSection(organizations));
                }
            }
        }

        if (uuid == null) {
            storage.save(resume);
        } else {
            storage.update(resume);
        }
        response.sendRedirect("resume");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
            case "edit":
                r = storage.get(uuid);
                break;
            case "add":
                request.getRequestDispatcher(("/WEB-INF/jsp/add.jsp")
                ).forward(request, response);
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }

}