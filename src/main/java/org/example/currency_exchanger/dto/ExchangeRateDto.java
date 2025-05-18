package org.example.currency_exchanger.dto;

public record ExchangeRateDto(Long id, CurrencyDto baseCurrencyDto, CurrencyDto targetCurrencyDto, Double rate) {
}
