package org.example.currency_exchanger.service;

import org.example.currency_exchanger.factory.ExchangeDtoFactory;
import org.example.currency_exchanger.dao.CurrencyDao;
import org.example.currency_exchanger.dao.CurrencyDaoImpl;
import org.example.currency_exchanger.dao.ExchangeRateDao;
import org.example.currency_exchanger.dao.ExchangeRateDaoImpl;
import org.example.currency_exchanger.dto.ExchangeDto;
import org.example.currency_exchanger.entity.Currency;
import org.example.currency_exchanger.entity.ExchangeRate;
import org.example.currency_exchanger.exception.NotFoundException;

import java.math.BigDecimal;
import java.util.Optional;

public class ExchangeServiceImpl implements ExchangeService {

    private static final ExchangeService INSTANCE = new ExchangeServiceImpl();

    private static final String USD_CODE = "USD";
    private static final BigDecimal BASE_RATE = BigDecimal.ONE;

    private final ExchangeRateDao exchangeRateDao = ExchangeRateDaoImpl.getInstance();
    private final CurrencyDao currencyDao = CurrencyDaoImpl.getInstance();
    private final ExchangeDtoFactory exchangeDtoFactory = new ExchangeDtoFactory();

    public static ExchangeService getInstance() {
        return INSTANCE;
    }

    private ExchangeServiceImpl() {
    }

    @Override
    public ExchangeDto exchange(String baseCode, String targetCode, String stringedAmount) {
        double amount = Double.parseDouble(stringedAmount);

        Currency baseCurrency = getCurrencyOrThrow(baseCode);
        Currency targetCurrency = getCurrencyOrThrow(targetCode);

        if (baseCurrency.getCode().equals(targetCurrency.getCode())) {
            return exchangeDtoFactory.fromRaw(baseCurrency, targetCurrency, BASE_RATE, amount);
        }

        return findExchangeRate(baseCode, targetCode, amount)
                .orElseThrow(() -> new NotFoundException("Can't find exchange rate to exchange"));
    }

    private Currency getCurrencyOrThrow(String code) {
        return currencyDao.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Currency not found: " + code));
    }

    private Optional<ExchangeDto> findExchangeRate(String baseCode, String targetCode, double amount) {
        return exchangeRateDao.findByBaseCodeAndTargetCode(baseCode, targetCode)
                .map(rate -> exchangeDtoFactory.fromExchangeRate(rate, amount, false))
                .or(() -> exchangeRateDao.findByBaseCodeAndTargetCode(targetCode, baseCode)
                        .map(rate -> exchangeDtoFactory.fromExchangeRate(rate, amount, true)))
                .or(() -> tryCrossExchange(baseCode, targetCode, amount));
    }

    private Optional<ExchangeDto> tryCrossExchange(String baseCode, String targetCode, double amount) {
        Optional<ExchangeRate> usdToBase = exchangeRateDao.findByBaseCodeAndTargetCode(USD_CODE, baseCode);
        Optional<ExchangeRate> usdToTarget = exchangeRateDao.findByBaseCodeAndTargetCode(USD_CODE, targetCode);

        if (usdToBase.isPresent() && usdToTarget.isPresent()) {
            return Optional.of(exchangeDtoFactory.fromCrossRates(usdToBase.get(), usdToTarget.get(), amount));
        }

        return Optional.empty();
    }

}
