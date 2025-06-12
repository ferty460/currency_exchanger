package org.example.currency_exchanger.dao;

import org.example.currency_exchanger.entity.Currency;
import org.example.currency_exchanger.util.template.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDaoImpl implements CurrencyDao {

    private static final CurrencyDaoImpl INSTANCE = new CurrencyDaoImpl();

    private static final String FIND_ALL_SQL = """
            SELECT id,
                   code,
                   full_name,
                   sign
            FROM currencies
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + " WHERE id = ?;";

    private static final String FIND_BY_CODE_SQL = FIND_ALL_SQL + " WHERE code = ?;";

    private static final String SAVE_SQL = """
            INSERT INTO currencies(code, full_name, sign)
            VALUES (?, ?, ?);
            """;

    private static final String UPDATE_SQL = """
            UPDATE currencies
            SET code = ?,
                full_name = ?,
                sign = ?
            WHERE id = ?;
            """;

    private static final String DELETE_SQL = "DELETE FROM currencies WHERE id = ?;";

    private CurrencyDaoImpl() {
    }

    public static CurrencyDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<Currency> findById(Long id) {
        return JdbcTemplate.query(
                FIND_BY_ID_SQL,
                stmt -> stmt.setLong(1, id),
                rs -> rs.next() ? Optional.of(buildCurrency(rs)) : Optional.empty()
        );
    }

    @Override
    public Optional<Currency> findByCode(String code) {
        return JdbcTemplate.query(
                FIND_BY_CODE_SQL,
                stmt -> stmt.setString(1, code),
                rs -> rs.next() ? Optional.of(buildCurrency(rs)) : Optional.empty()
        );
    }

    @Override
    public List<Currency> findAll() {
        return JdbcTemplate.query(
                FIND_ALL_SQL,
                stmt -> {},
                rs -> {
                    List<Currency> list = new ArrayList<>();
                    while (rs.next()) {
                        list.add(buildCurrency(rs));
                    }
                    return list;
                }
        );
    }

    @Override
    public Currency save(Currency entity) {
        long generatedId = JdbcTemplate.insert(
                SAVE_SQL,
                stmt -> {
                    stmt.setString(1, entity.getCode());
                    stmt.setString(2, entity.getName());
                    stmt.setString(3, entity.getSign());
                }
        );

        entity.setId(generatedId);

        return entity;
    }

    @Override
    public void update(Currency entity) {
        JdbcTemplate.update(
                UPDATE_SQL,
                stmt -> {
                    stmt.setString(1, entity.getCode());
                    stmt.setString(2, entity.getName());
                    stmt.setString(3, entity.getSign());
                    stmt.setLong(4, entity.getId());
                }
        );
    }

    @Override
    public void delete(Long id) {
        JdbcTemplate.update(
                DELETE_SQL,
                stmt -> stmt.setLong(1, id)
        );
    }

    private Currency buildCurrency(ResultSet resultSet) throws SQLException  {
        return new Currency(
                resultSet.getLong("id"),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign")
        );
    }

}
