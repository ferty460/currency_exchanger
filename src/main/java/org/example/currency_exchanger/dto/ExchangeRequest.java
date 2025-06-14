package org.example.currency_exchanger.dto;

public record ExchangeRequest(String base, String target, String amount) {
}
