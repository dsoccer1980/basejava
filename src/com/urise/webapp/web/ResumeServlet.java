package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.SqlStorage;
import com.urise.webapp.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String uuid = request.getParameter("uuid");
        List<Resume> resumes;
        response.getWriter().write("<TABLE border=1>");
        response.getWriter().write("<TR>");
        response.getWriter().write("<TD><B>uuid</B></TD>");
        response.getWriter().write("<TD><B>fullname</B></TD>");
        response.getWriter().write("</TR>");

        if (uuid == null) {
            resumes = storage.getAllSorted();
            for (Resume resume : resumes) {
                showResume(response, resume);
            }
        } else {
            Resume resume = storage.get(uuid);
            if (resume == null) {
                throw new NotExistStorageException(uuid);
            }
            showResume(response, resume);
        }

        response.getWriter().write("</TABLE>");
    }

    private void showResume(HttpServletResponse response, Resume resume) throws IOException {
        response.getWriter().write("<TR>");
        response.getWriter().write("<TD>" + resume.getUuid() + "</TD>");
        response.getWriter().write("<TD>" + resume.getFullName() + "</TD>");
        response.getWriter().write("</TR>");
    }
}