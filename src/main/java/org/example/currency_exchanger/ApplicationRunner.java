package org.example.currency_exchanger;

import org.example.currency_exchanger.dto.CurrencyDto;
import org.example.currency_exchanger.dto.ExchangeRateDto;
import org.example.currency_exchanger.service.CurrencyService;
import org.example.currency_exchanger.service.CurrencyServiceImpl;
import org.example.currency_exchanger.service.ExchangeRateService;
import org.example.currency_exchanger.service.ExchangeRateServiceImpl;

public class ApplicationRunner {

    public static void main(String[] args) {
        ExchangeRateService service = new ExchangeRateServiceImpl();
        CurrencyService currencyService = new CurrencyServiceImpl();

        CurrencyDto currencyUSD = currencyService.getByCode("USD");
        CurrencyDto currencyEUR = currencyService.getByCode("EUR");

//        System.out.println(currencyUSD);
//        System.out.println(currencyEUR);

//        System.out.println(service.save(new ExchangeRateDto(1L, currencyUSD, currencyEUR, 3.0)));

        ExchangeRateDto exchangeRateDto = service.getByBaseCodeAndTargetCode("USD", "EUR");

        System.out.println(exchangeRateDto);

        service.update(new ExchangeRateDto(10L, currencyUSD, currencyEUR, 3.1));
    }

}
