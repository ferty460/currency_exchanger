package org.example.currency_exchanger.dto;

public record ExchangeRateRequest(String base, String target, String rate) {
}
