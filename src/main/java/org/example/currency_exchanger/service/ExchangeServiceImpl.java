package org.example.currency_exchanger.service;

import org.example.currency_exchanger.dao.CurrencyDao;
import org.example.currency_exchanger.dao.CurrencyDaoImpl;
import org.example.currency_exchanger.dao.ExchangeRateDao;
import org.example.currency_exchanger.dao.ExchangeRateDaoImpl;
import org.example.currency_exchanger.dto.ExchangeDto;
import org.example.currency_exchanger.entity.Currency;
import org.example.currency_exchanger.entity.ExchangeRate;
import org.example.currency_exchanger.exception.NotFoundException;
import org.example.currency_exchanger.util.ExchangeFactory;

import java.util.Optional;

public class ExchangeServiceImpl implements ExchangeService {

    private static final ExchangeService INSTANCE = new ExchangeServiceImpl();

    private final ExchangeRateDao exchangeRateDao = ExchangeRateDaoImpl.getInstance();
    private final CurrencyDao currencyDao = CurrencyDaoImpl.getInstance();

    private ExchangeServiceImpl() {
    }

    public static ExchangeService getInstance() {
        return INSTANCE;
    }

    @Override
    public ExchangeDto exchange(String baseCode, String targetCode, String stringedAmount) {
        double amount = Double.parseDouble(stringedAmount);

        Optional<Currency> baseCurrency = currencyDao.findByCode(baseCode);
        Optional<Currency> targetCurrency = currencyDao.findByCode(targetCode);
        if (baseCode.equals(targetCode) && baseCurrency.isPresent() && targetCurrency.isPresent()) {
            return ExchangeFactory.createExchangeDto(baseCurrency.get(), targetCurrency.get(), 1.0, amount);
        }

        Optional<ExchangeRate> directRate = exchangeRateDao.findByBaseCodeAndTargetCode(baseCode, targetCode);
        if (directRate.isPresent()) {
            return ExchangeFactory.createExchangeFromExchangeRate(directRate.get(), amount, false);
        }

        Optional<ExchangeRate> reverseRate = exchangeRateDao.findByBaseCodeAndTargetCode(targetCode, baseCode);
        if (reverseRate.isPresent()) {
            return ExchangeFactory.createExchangeFromExchangeRate(reverseRate.get(), amount, true);
        }

        Optional<ExchangeRate> usdToBase = exchangeRateDao.findByBaseCodeAndTargetCode("USD", baseCode);
        Optional<ExchangeRate> usdToTarget = exchangeRateDao.findByBaseCodeAndTargetCode("USD", targetCode);
        if (usdToBase.isPresent() && usdToTarget.isPresent()) {
            return ExchangeFactory.createCrossExchange(usdToBase.get(), usdToTarget.get(), amount);
        }

        throw new NotFoundException("Can't find exchange rate to exchange");
    }

}
