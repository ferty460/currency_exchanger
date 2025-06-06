package org.example.currency_exchanger.dao;

import org.example.currency_exchanger.entity.Currency;
import org.example.currency_exchanger.entity.ExchangeRate;
import org.example.currency_exchanger.util.template.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDaoImpl implements ExchangeRateDao {

    private static final ExchangeRateDaoImpl INSTANCE = new ExchangeRateDaoImpl();

    private static final String FIND_ALL_SQL = """
            SELECT id,
                   base_currency_id,
                   target_currency_id,
                   rate
            FROM exchange_rates
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + "WHERE id = ?";

    private static final String FIND_BY_BASE_AND_TARGET_CODE_SQL = """
            SELECT er.id,
                   er.base_currency_id,
                   er.target_currency_id,
                   er.rate
            FROM exchange_rates er
            JOIN currencies bc ON er.base_currency_id = bc.id
            JOIN currencies tc ON er.target_currency_id = tc.id
            WHERE bc.code = ? AND tc.code = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate)
            VALUES (?, ?, ?);
            """;

    private static final String UPDATE_SQL = """
            UPDATE exchange_rates
            SET rate = ?
            WHERE id = ?;
            """;

    private static final String DELETE_SQL = "DELETE FROM exchange_rates WHERE id = ?;";

    private ExchangeRateDaoImpl() {
    }

    public static ExchangeRateDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<ExchangeRate> findById(Long id) {
        return JdbcTemplate.query(
                FIND_BY_ID_SQL,
                stmt -> stmt.setLong(1, id),
                rs -> rs.next() ? Optional.of(buildExchangeRate(rs)) : Optional.empty()
        );
    }

    @Override
    public Optional<ExchangeRate> findByBaseCodeAndTargetCode(String baseCode, String targetCode) {
        return JdbcTemplate.query(
                FIND_BY_BASE_AND_TARGET_CODE_SQL,
                stmt -> {
                    stmt.setString(1, baseCode);
                    stmt.setString(2, targetCode);
                },
                rs -> rs.next() ? Optional.of(buildExchangeRate(rs)) : Optional.empty()
        );
    }

    @Override
    public List<ExchangeRate> findAll() {
        return JdbcTemplate.query(
                FIND_ALL_SQL,
                stmt -> {},
                rs -> {
                    List<ExchangeRate> list = new ArrayList<>();
                    while (rs.next()) {
                        list.add(buildExchangeRate(rs));
                    }
                    return list;
                }
        );
    }

    @Override
    public ExchangeRate save(ExchangeRate entity) {
        long generatedId = JdbcTemplate.insert(
                SAVE_SQL,
                stmt -> {
                    stmt.setLong(1, entity.getBaseCurrency().getId());
                    stmt.setLong(2, entity.getTargetCurrency().getId());
                    stmt.setDouble(3, entity.getRate());
                }
        );

        entity.setId(generatedId);

        return entity;
    }

    @Override
    public void update(ExchangeRate entity) {
        JdbcTemplate.update(
                UPDATE_SQL,
                stmt -> {
                    stmt.setDouble(1, entity.getRate());
                    stmt.setLong(2, entity.getId());
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

    private ExchangeRate buildExchangeRate(ResultSet resultSet) throws SQLException {
        CurrencyDaoImpl currencyDao = CurrencyDaoImpl.getInstance();

        long baseCurrencyId = resultSet.getLong("base_currency_id");
        long targetCurrencyId = resultSet.getLong("target_currency_id");

        Currency baseCurrency = currencyDao.findById(baseCurrencyId).orElseThrow();
        Currency targetCurrency = currencyDao.findById(targetCurrencyId).orElseThrow();

        return new ExchangeRate(
                resultSet.getLong("id"),
                baseCurrency,
                targetCurrency,
                resultSet.getDouble("rate")
        );
    }

}
