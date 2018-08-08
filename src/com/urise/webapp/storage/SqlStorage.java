package com.urise.webapp.storage;


import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.ConnectionFactory;
import com.urise.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    private final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        SqlHelper.execute(connectionFactory, PreparedStatement::execute, "DELETE FROM resume");
    }

    @Override
    public Resume get(String uuid) {
        return SqlHelper.execute(connectionFactory, (ps) -> {
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, rs.getString("full_name"));
        }, "SELECT * FROM resume r WHERE r.uuid =?", uuid);
    }

    @Override
    public void update(Resume r) {
        SqlHelper.execute(connectionFactory, (ps) -> {
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(r.getUuid());
            }
            return true;
        }, "UPDATE resume SET full_name=? WHERE uuid=?", r.getFullName(), r.getUuid());

    }

    @Override
    public void save(Resume r) {
        SqlHelper.execute(connectionFactory,
                PreparedStatement::execute,
                "INSERT INTO resume (uuid, full_name) VALUES (?,?)",
                r.getUuid(),
                r.getFullName());
    }

    @Override
    public void delete(String uuid) {
        SqlHelper.execute(connectionFactory, (ps) -> {
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return true;
        }, "DELETE FROM resume WHERE uuid=?", uuid);
    }

    @Override
    public List<Resume> getAllSorted() {
        return SqlHelper.execute(connectionFactory, (ps) -> {
            ResultSet rs = ps.executeQuery();
            List<Resume> result = new ArrayList<>();
            while (rs.next()) {
                result.add(new Resume(rs.getString("uuid").trim(), rs.getString("full_name")));
            }
            return result;
        }, "SELECT * FROM resume r ORDER BY full_name");
    }

    @Override
    public int size() {
        return SqlHelper.execute(connectionFactory, (ps) -> {
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return 0;
            }
            return rs.getInt(1);
        }, "SELECT count(*) FROM resume r");
    }
}
