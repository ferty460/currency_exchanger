package org.example.currency_exchanger.service;

import org.example.currency_exchanger.dto.CurrencyDto;

import java.util.List;

public interface CurrencyService {

    List<CurrencyDto> getAll();

    CurrencyDto getByCode(String code);

    CurrencyDto save(CurrencyDto currencyDto);

}
