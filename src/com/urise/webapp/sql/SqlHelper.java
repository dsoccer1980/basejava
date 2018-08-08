package com.urise.webapp.sql;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.StorageException;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {

    public static <T> T execute(ConnectionFactory connectionFactory, SqlExecutor<T> sqlExecutor, String sql, String... parameters) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int index = 1;
            for (String parameter : parameters) {
                ps.setString(index++, parameter);
            }
            return sqlExecutor.execute(ps);
        } catch (PSQLException e) {
            throw new ExistStorageException(e);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

}
