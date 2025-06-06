package org.example.currency_exchanger.service;

import org.example.currency_exchanger.dao.ExchangeRateDao;
import org.example.currency_exchanger.dao.ExchangeRateDaoImpl;
import org.example.currency_exchanger.dto.ExchangeDto;
import org.example.currency_exchanger.entity.Currency;
import org.example.currency_exchanger.entity.ExchangeRate;
import org.example.currency_exchanger.exception.NotFoundException;
import org.example.currency_exchanger.mapper.CurrencyMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ExchangeServiceImpl implements ExchangeService {

    private static final ExchangeService INSTANCE = new ExchangeServiceImpl();

    private final ExchangeRateDao exchangeRateDao = ExchangeRateDaoImpl.getInstance();
    private final CurrencyMapper currencyMapper = CurrencyMapper.INSTANCE;

    private ExchangeServiceImpl() {
    }

    public static ExchangeService getInstance() {
        return INSTANCE;
    }

    @Override
    public ExchangeDto exchange(String baseCode, String targetCode, String stringedAmount) {
        double amount = Double.parseDouble(stringedAmount);

        Optional<ExchangeRate> directRate = exchangeRateDao.findByBaseCodeAndTargetCode(baseCode, targetCode);
        if (directRate.isPresent()) {
            return createExchangeDto(directRate.get(), false, amount);
        }

        Optional<ExchangeRate> reverseRate = exchangeRateDao.findByBaseCodeAndTargetCode(targetCode, baseCode);
        if (reverseRate.isPresent()) {
            return createExchangeDto(reverseRate.get(), true, amount);
        }

        Optional<ExchangeRate> usdToBase = exchangeRateDao.findByBaseCodeAndTargetCode("USD", baseCode);
        Optional<ExchangeRate> usdToTarget = exchangeRateDao.findByBaseCodeAndTargetCode("USD", targetCode);

        if (usdToBase.isPresent() && usdToTarget.isPresent()) {
            return createCrossExchangeDto(usdToBase.get(), usdToTarget.get(), amount);
        }

        throw new NotFoundException("Can't find exchange rate to exchange");
    }

    private ExchangeDto createExchangeDto(ExchangeRate exchangeRate, boolean isReverse, double amount) {
        Currency baseCurrency = isReverse ? exchangeRate.getTargetCurrency() : exchangeRate.getBaseCurrency();
        Currency targetCurrency = isReverse ? exchangeRate.getBaseCurrency() : exchangeRate.getTargetCurrency();

        double rate = isReverse ? 1.0 / exchangeRate.getRate() : exchangeRate.getRate();
        double roundedRate = BigDecimal.valueOf(rate).setScale(2, RoundingMode.HALF_UP).doubleValue();

        return new ExchangeDto(
                currencyMapper.toDto(baseCurrency),
                currencyMapper.toDto(targetCurrency),
                roundedRate,
                amount,
                convertAmount(roundedRate, amount)
        );
    }

    private ExchangeDto createCrossExchangeDto(ExchangeRate usdToBase, ExchangeRate usdToTarget, double amount) {
        double rate = usdToBase.getRate() / usdToTarget.getRate();
        double roundedRate = BigDecimal.valueOf(rate).setScale(2, RoundingMode.HALF_UP).doubleValue();

        return new ExchangeDto(
                currencyMapper.toDto(usdToBase.getTargetCurrency()),
                currencyMapper.toDto(usdToTarget.getTargetCurrency()),
                roundedRate,
                amount,
                convertAmount(roundedRate, amount)
        );
    }

    private double convertAmount(double rate, double amount) {
        return amount * rate;
    }

}
