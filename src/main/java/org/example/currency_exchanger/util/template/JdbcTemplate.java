package org.example.currency_exchanger.util.template;

import org.example.currency_exchanger.exception.DaoException;
import org.example.currency_exchanger.util.DatabaseConnectionPool;

import java.sql.*;

public final class JdbcTemplate {

    private JdbcTemplate() {
    }

    public static <T> T query(String query, StatementSetter setter, ResultSetExtractor<T> extractor) {
        Connection conn = DatabaseConnectionPool.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            setter.set(stmt);
            ResultSet rs = stmt.executeQuery();
            return extractor.extract(rs);
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            DatabaseConnectionPool.returnConnection(conn);
        }
    }

    public static void update(String sql, StatementSetter setter) {
        Connection conn = DatabaseConnectionPool.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            setter.set(stmt);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            DatabaseConnectionPool.returnConnection(conn);
        }
    }

    public static Long insert(String sql, StatementSetter setter) {
        Connection conn = DatabaseConnectionPool.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setter.set(stmt);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            return rs.next() ? rs.getLong(1) : 0L;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            DatabaseConnectionPool.returnConnection(conn);
        }
    }

}
