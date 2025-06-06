package org.example.currency_exchanger.dto;

public record ExchangeDto(
        CurrencyDto baseCurrency,
        CurrencyDto targetCurrency,
        Double rate,
        Double amount,
        Double convertedAmount
) {
}
