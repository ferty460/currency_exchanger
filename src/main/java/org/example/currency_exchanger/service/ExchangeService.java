package org.example.currency_exchanger.service;

import org.example.currency_exchanger.dto.ExchangeDto;

public interface ExchangeService {

    ExchangeDto exchange(String baseCode, String targetCode, String amount);

}
