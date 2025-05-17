package org.example.currency_exchanger.dao;

import org.example.currency_exchanger.entity.Currency;
import org.example.currency_exchanger.entity.ExchangeRate;
import org.example.currency_exchanger.util.template.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDao implements CrudDao<ExchangeRate> {

    private static final ExchangeRateDao INSTANCE = new ExchangeRateDao();

    private static final String FIND_ALL_SQL = """
            SELECT exchange_rates.id,
                   base_currency_id,
                   target_currency_id,
                   rate
            FROM exchange_rates
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + "WHERE id = ?";

    private static final String SAVE_SQL = """
            INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate)
            VALUES (?, ?, ?);
            """;

    private static final String UPDATE_SQL = """
            UPDATE exchange_rates
            SET base_currency_id = ?,
                target_currency_id = ?,
                rate = ?
            WHERE id = ?;
            """;

    private static final String DELETE_SQL = "DELETE FROM exchange_rates WHERE id = ?";

    private ExchangeRateDao() {
    }

    public static ExchangeRateDao getInstance() {
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
                    stmt.setLong(1, entity.getBaseCurrency().getId());
                    stmt.setLong(2, entity.getTargetCurrency().getId());
                    stmt.setDouble(3, entity.getRate());
                    stmt.setLong(4, entity.getId());
                }
        );
    }

    @Override
    public void delete(Long id) {
        JdbcTemplate.update(
                DELETE_SQL,
                stmt -> {
                    stmt.setLong(1, id);
                }
        );
    }

    private ExchangeRate buildExchangeRate(ResultSet resultSet) throws SQLException {
        CurrencyDao currencyDao = CurrencyDao.getInstance();

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
