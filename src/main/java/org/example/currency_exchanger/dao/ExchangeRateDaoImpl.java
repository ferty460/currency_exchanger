package org.example.currency_exchanger.dao;

import lombok.extern.slf4j.Slf4j;
import org.example.currency_exchanger.entity.Currency;
import org.example.currency_exchanger.entity.ExchangeRate;
import org.example.currency_exchanger.exception.NotFoundException;
import org.example.currency_exchanger.util.template.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ExchangeRateDaoImpl implements ExchangeRateDao {

    private static final ExchangeRateDaoImpl INSTANCE = new ExchangeRateDaoImpl();

    private static final String ID_COLUMN_NAME = "id";
    private static final String BASE_CURRENCY_ID_COLUMN_NAME = "base_currency_id";
    private static final String TARGET_CURRENCY_ID_COLUMN_NAME = "target_currency_id";
    private static final String RATE_COLUMN_NAME = "rate";

    private static final String FIND_ALL_SQL = """
            SELECT id, base_currency_id, target_currency_id, rate
            FROM exchange_rates
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + "WHERE id = ?";

    private static final String FIND_BY_BASE_AND_TARGET_CODE_SQL = """
            SELECT er.id, er.base_currency_id, er.target_currency_id, er.rate
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
                    stmt.setBigDecimal(3, entity.getRate());
                }
        );

        entity.setId(generatedId);

        log.info("Saved exchange rate {}", entity);
        return entity;
    }

    @Override
    public void update(ExchangeRate entity) {
        JdbcTemplate.update(
                UPDATE_SQL,
                stmt -> {
                    stmt.setBigDecimal(1, entity.getRate());
                    stmt.setLong(2, entity.getId());
                }
        );
        log.info("Exchange rate with id {} was updated: {}", entity.getId(), entity);
    }

    @Override
    public void delete(Long id) {
        JdbcTemplate.update(
                DELETE_SQL,
                stmt -> stmt.setLong(1, id)
        );
        log.info("Exchange rate with id {} was deleted", id);
    }

    private ExchangeRate buildExchangeRate(ResultSet resultSet) throws SQLException {
        CurrencyDaoImpl currencyDao = CurrencyDaoImpl.getInstance();

        long baseCurrencyId = resultSet.getLong(BASE_CURRENCY_ID_COLUMN_NAME);
        long targetCurrencyId = resultSet.getLong(TARGET_CURRENCY_ID_COLUMN_NAME);

        Currency baseCurrency = currencyDao.findById(baseCurrencyId).orElseThrow(
                () -> new NotFoundException("Base currency with id " + baseCurrencyId + " not found")
        );
        Currency targetCurrency = currencyDao.findById(targetCurrencyId).orElseThrow(
                () -> new NotFoundException("Target currency with id " + targetCurrencyId + " not found")
        );

        return new ExchangeRate(
                resultSet.getLong(ID_COLUMN_NAME),
                baseCurrency,
                targetCurrency,
                resultSet.getBigDecimal(RATE_COLUMN_NAME)
        );
    }

}
