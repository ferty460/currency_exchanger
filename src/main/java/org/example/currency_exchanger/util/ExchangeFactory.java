package org.example.currency_exchanger.util;

import lombok.experimental.UtilityClass;
import org.example.currency_exchanger.dto.ExchangeDto;
import org.example.currency_exchanger.entity.Currency;
import org.example.currency_exchanger.entity.ExchangeRate;
import org.example.currency_exchanger.mapper.CurrencyMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;

@UtilityClass
public class ExchangeFactory {

    private static final BigDecimal BASE_RATE_VALUE = BigDecimal.ONE;
    private static final int SCALE = 2;

    private final CurrencyMapper currencyMapper = CurrencyMapper.INSTANCE;

    public static ExchangeDto createExchangeDto(Currency base, Currency target, BigDecimal rate, double amount) {
        return new ExchangeDto(
                currencyMapper.toDto(base),
                currencyMapper.toDto(target),
                rate,
                amount,
                convertAmount(rate, amount)
        );
    }

    public static ExchangeDto createExchangeFromExchangeRate(ExchangeRate exchangeRate, double amount, boolean isReverse) {
        Currency baseCurrency = isReverse ? exchangeRate.getTargetCurrency() : exchangeRate.getBaseCurrency();
        Currency targetCurrency = isReverse ? exchangeRate.getBaseCurrency() : exchangeRate.getTargetCurrency();

        BigDecimal inverseRate = BASE_RATE_VALUE.divide(exchangeRate.getRate(), SCALE, RoundingMode.HALF_UP);
        BigDecimal directRate = exchangeRate.getRate().setScale(SCALE, RoundingMode.HALF_UP);
        BigDecimal rate = isReverse ? inverseRate : directRate;

        return new ExchangeDto(
                currencyMapper.toDto(baseCurrency),
                currencyMapper.toDto(targetCurrency),
                rate,
                amount,
                convertAmount(rate, amount)
        );
    }

    public static ExchangeDto createCrossExchange(ExchangeRate usdToBase, ExchangeRate usdToTarget, double amount) {
        BigDecimal rate = usdToBase.getRate().divide(usdToTarget.getRate(), SCALE, RoundingMode.HALF_UP);

        return new ExchangeDto(
                currencyMapper.toDto(usdToBase.getTargetCurrency()),
                currencyMapper.toDto(usdToTarget.getTargetCurrency()),
                rate,
                amount,
                convertAmount(rate, amount)
        );
    }

    private BigDecimal convertAmount(BigDecimal rate, double amount) {
        return rate.multiply(BigDecimal.valueOf(amount));
    }

}
