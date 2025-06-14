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

    private final CurrencyMapper currencyMapper = CurrencyMapper.INSTANCE;

    public static ExchangeDto createExchangeDto(Currency base, Currency target, double rate, double amount) {
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

    public static ExchangeDto createCrossExchange(ExchangeRate usdToBase, ExchangeRate usdToTarget, double amount) {
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
