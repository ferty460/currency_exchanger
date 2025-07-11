package org.example.currency_exchanger.factory;

import org.example.currency_exchanger.dto.ExchangeDto;
import org.example.currency_exchanger.entity.Currency;
import org.example.currency_exchanger.entity.ExchangeRate;
import org.example.currency_exchanger.mapper.CurrencyMapper;
import org.example.currency_exchanger.service.ExchangeCalculator;

import java.math.BigDecimal;

public class ExchangeDtoFactory {

    private final CurrencyMapper currencyMapper = CurrencyMapper.INSTANCE;
    private final ExchangeCalculator calculator = new ExchangeCalculator();

    public ExchangeDto fromExchangeRate(ExchangeRate exchangeRate, double amount, boolean isReverse) {
        Currency base = isReverse ? exchangeRate.getTargetCurrency() : exchangeRate.getBaseCurrency();
        Currency target = isReverse ? exchangeRate.getBaseCurrency() : exchangeRate.getTargetCurrency();

        BigDecimal rate = calculator.calculateRate(exchangeRate, isReverse);
        BigDecimal resultAmount = calculator.convertAmount(rate, amount);

        return buildDto(base, target, rate, amount, resultAmount);
    }

    public ExchangeDto fromCrossRates(ExchangeRate usdToBase, ExchangeRate usdToTarget, double amount) {
        BigDecimal rate = calculator.calculateCrossRate(usdToBase, usdToTarget);
        BigDecimal resultAmount = calculator.convertAmount(rate, amount);

        return buildDto(
                usdToBase.getTargetCurrency(),
                usdToTarget.getTargetCurrency(),
                rate,
                amount,
                resultAmount
        );
    }

    public ExchangeDto fromRaw(Currency base, Currency target, BigDecimal rate, double amount) {
        BigDecimal resultAmount = calculator.convertAmount(rate, amount);
        return buildDto(base, target, rate, amount, resultAmount);
    }

    private ExchangeDto buildDto(Currency base, Currency target, BigDecimal rate, double amount, BigDecimal result) {
        return new ExchangeDto(
                currencyMapper.toDto(base),
                currencyMapper.toDto(target),
                rate,
                amount,
                result
        );
    }

}
