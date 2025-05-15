package org.example.currency_exchanger.dao;

import org.example.currency_exchanger.entity.Currency;
import org.example.currency_exchanger.exception.DaoException;
import org.example.currency_exchanger.util.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDao implements Dao<Currency> {

    private static final CurrencyDao INSTANCE = new CurrencyDao();

    private static final String FIND_ALL_SQL = """
            SELECT id,
                   code,
                   fullName,
                   sign
            FROM currencies
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + " WHERE id = ?;";

    private static final String SAVE_SQL = """
            INSERT INTO currencies(code, fullName, sign)
            VALUES (?, ?, ?);
            """;

    private static final String UPDATE_SQL = """
            UPDATE currencies
            SET code = ?,
                fullName = ?,
                sign = ?
            WHERE id = ?;
            """;

    private static final String DELETE_SQL = """
            DELETE FROM currencies WHERE id = ?;
            """;

    public CurrencyDao() {
    }

    public static CurrencyDao getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<Currency> findById(Long id) {
        Connection conn = DatabaseConnectionPool.getConnection();

        try (PreparedStatement preparedStatement = conn.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            Currency currency = null;
            if (resultSet.next()) {
                currency = buildCurrency(resultSet);
            }

            return Optional.ofNullable(currency);
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            DatabaseConnectionPool.returnConnection(conn);
        }
    }

    @Override
    public List<Currency> findAll() {
        Connection conn = DatabaseConnectionPool.getConnection();

        try (PreparedStatement prepareStatement = conn.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = prepareStatement.executeQuery();
            List<Currency> currencies = new ArrayList<>();

            while (resultSet.next()) {
                currencies.add(buildCurrency(resultSet));
            }

            return currencies;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            DatabaseConnectionPool.returnConnection(conn);
        }
    }

    @Override
    public Currency save(Currency entity) {
        Connection conn = DatabaseConnectionPool.getConnection();

        try (PreparedStatement prepareStatement = conn
                .prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatement.setString(1, entity.getCode());
            prepareStatement.setString(2, entity.getFullName());
            prepareStatement.setString(3, entity.getSign());

            prepareStatement.executeUpdate();

            ResultSet generatedKeys = prepareStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong(1));
            }

            return entity;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            DatabaseConnectionPool.returnConnection(conn);
        }
    }

    @Override
    public void update(Currency entity) {
        Connection conn = DatabaseConnectionPool.getConnection();

        try (PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, entity.getCode());
            preparedStatement.setString(2, entity.getFullName());
            preparedStatement.setString(3, entity.getSign());
            preparedStatement.setLong(4, entity.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            DatabaseConnectionPool.returnConnection(conn);
        }
    }

    @Override
    public void delete(Long id) {
        Connection conn = DatabaseConnectionPool.getConnection();

        try (PreparedStatement preparedStatement = conn.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseConnectionPool.returnConnection(conn);
        }
    }

    private Currency buildCurrency(ResultSet resultSet) throws SQLException  {
        return new Currency(
                resultSet.getLong("id"),
                resultSet.getString("code"),
                resultSet.getString("fullName"),
                resultSet.getString("sign")
        );
    }

}
