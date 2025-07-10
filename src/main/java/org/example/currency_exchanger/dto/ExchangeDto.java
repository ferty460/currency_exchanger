package org.example.currency_exchanger.dto;

import java.math.BigDecimal;

public record ExchangeDto(
        CurrencyDto baseCurrency,
        CurrencyDto targetCurrency,
        BigDecimal rate,
        Double amount,
        BigDecimal convertedAmount
) {
}
