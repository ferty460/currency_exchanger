package org.example.currency_exchanger.dto;

public record ExchangeRateDto(Long id, CurrencyDto baseCurrency, CurrencyDto targetCurrency, Double rate) {
}
