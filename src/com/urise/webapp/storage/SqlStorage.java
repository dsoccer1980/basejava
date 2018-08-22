package com.urise.webapp.storage;


import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.sql.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume", PreparedStatement::execute);
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.execute("" +
                        " SELECT uuid, full_name, " +
                        " c.type as contact_type, c.value as contact_value," +
                        " s.type as section_type, s.value as section_value" +
                        " FROM resume r " +
                        " LEFT JOIN contact c ON r.uuid = c.resume_uuid" +
                        " LEFT JOIN section s ON r.uuid = s.resume_uuid" +
                        " WHERE r.uuid =? ",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume resume = new Resume(uuid, rs.getString("full_name"));
                    do {
                        addContact(rs, resume);
                        addSection(rs, resume);
                    } while (rs.next());

                    return resume;
                });
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.transactionalExecute(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name=? WHERE uuid=?")) {
                        ps.setString(1, resume.getFullName());
                        ps.setString(2, resume.getUuid());
                        if (ps.executeUpdate() == 0) {
                            throw new NotExistStorageException(resume.getUuid());
                        }
                    }

                    try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid=?")) {
                        ps.setString(1, resume.getUuid());
                        ps.execute();
                    }

                    insertContacts(conn, resume);

            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM section WHERE resume_uuid=?")) {
                ps.setString(1, resume.getUuid());
                ps.execute();
            }

            insertSections(conn, resume);
                    return null;
                }
        );
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.transactionalExecute(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                        ps.setString(1, resume.getUuid());
                        ps.setString(2, resume.getFullName());
                        ps.execute();
                    }
                    insertContacts(conn, resume);
            insertSections(conn, resume);
                    return null;
                }
        );
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.<Void>execute("DELETE FROM resume WHERE uuid=?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.execute("" +
                        " SELECT uuid, full_name, " +
                        " c.type as contact_type, c.value as contact_value," +
                        " s.type as section_type, s.value as section_value" +
                        " FROM resume r " +
                        " LEFT JOIN contact c ON r.uuid = c.resume_uuid  " +
                        " LEFT JOIN section s ON r.uuid = s.resume_uuid  " +
                        " ORDER BY full_name, uuid",
                ps -> {
                    ResultSet rs = ps.executeQuery();
                    Map<String, Resume> map = new LinkedHashMap<>();
                    while (rs.next()) {
                        String uuid = rs.getString("uuid").trim();
                        Resume resume = map.get(uuid);
                        if (resume == null) {
                            resume = new Resume(uuid, rs.getString("full_name"));
                            map.put(uuid, resume);
                        }
                        addContact(rs, resume);
                        addSection(rs, resume);

                    }
                    return new ArrayList<>(map.values());
                });
    }

    public List<Resume> getAll() {
        Map<String, Resume> map = new HashMap<>();
        sqlHelper.execute("SELECT * FROM resume",
                ps -> {
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        String uuid = rs.getString("uuid").trim();
                        map.put(uuid, new Resume(uuid, rs.getString("full_name")));

                    }
                    return null;
                });
        sqlHelper.execute("SELECT type as contact_type, value as contact_value, resume_uuid FROM contact",
                ps -> {
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        String uuid = rs.getString("resume_uuid").trim();
                        Resume resume = map.get(uuid);
                        if (resume != null) {
                            addContact(rs, resume);
                        }
                    }
                    return null;
                });
        sqlHelper.execute("SELECT type as section_type, value as section_value, resume_uuid FROM section",
                ps -> {
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        String uuid = rs.getString("resume_uuid").trim();
                        Resume resume = map.get(uuid);
                        if (resume != null) {
                            addSection(rs, resume);
                        }
                    }
                    return null;
                });

        return new ArrayList<>(map.values());
    }

    @Override
    public int size() {
        return sqlHelper.execute("SELECT count(*) FROM resume r", ps -> {
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return 0;
            }
            return rs.getInt(1);
        });
    }

    private void insertContacts(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> e : resume.getContacts().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void addContact(ResultSet rs, Resume resume) throws SQLException {
        String type = rs.getString("contact_type");
        if (type != null) {
            String value = rs.getString("contact_value");
            ContactType contactType = ContactType.valueOf(type);
            resume.addContact(contactType, value);
        }
    }

    private void insertSections(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, Section> e : resume.getSections().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, e.getKey().name());
                switch (e.getKey()) {
                    case OBJECTIVE:
                    case PERSONAL:
                        TextSection textSection = (TextSection) e.getValue();
                        ps.setString(3, textSection.getContent());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        ListSection listSection = (ListSection) e.getValue();
                        String joinSectionItems = listSection.getItems()
                                .stream()
                                .map(Object::toString)
                                .collect(Collectors.joining("\n"));
                        ps.setString(3, joinSectionItems);
                }
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void addSection(ResultSet rs, Resume resume) throws SQLException {
        String type = rs.getString("section_type");
        if (type != null) {
            String value = rs.getString("section_value");
            SectionType sectionType = SectionType.valueOf(type);
            switch (sectionType) {
                case OBJECTIVE:
                case PERSONAL:
                    resume.addSection(sectionType, new TextSection(value));
                    break;
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    resume.addSection(sectionType, new ListSection(value.split("\n")));
            }
        }
    }
}
