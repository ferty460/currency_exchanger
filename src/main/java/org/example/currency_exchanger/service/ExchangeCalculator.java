package org.example.currency_exchanger.service;

import org.example.currency_exchanger.entity.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeCalculator {

    private static final BigDecimal BASE_RATE = BigDecimal.ONE;
    private static final int SCALE = 2;

    public BigDecimal calculateRate(ExchangeRate rate, boolean isReverse) {
        return isReverse
                ? BASE_RATE.divide(rate.getRate(), SCALE, RoundingMode.HALF_UP)
                : rate.getRate().setScale(SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateCrossRate(ExchangeRate usdToBase, ExchangeRate usdToTarget) {
        return usdToBase.getRate().divide(usdToTarget.getRate(), SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal convertAmount(BigDecimal rate, double amount) {
        return rate.multiply(BigDecimal.valueOf(amount));
    }

}
