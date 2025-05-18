package org.example.currency_exchanger.service;

import org.example.currency_exchanger.dto.ExchangeRateDto;

import java.util.List;

public interface ExchangeRateService {

    List<ExchangeRateDto> getAll();

    ExchangeRateDto getByBaseCodeAndTargetCode(String baseCode, String targetCode);

    ExchangeRateDto save(ExchangeRateDto exchangeRateDto);

    void update(ExchangeRateDto exchangeRateDto);

}
