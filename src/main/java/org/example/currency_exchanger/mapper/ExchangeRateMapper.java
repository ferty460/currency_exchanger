package org.example.currency_exchanger.mapper;

import org.example.currency_exchanger.dto.CurrencyDto;
import org.example.currency_exchanger.dto.ExchangeRateDto;
import org.example.currency_exchanger.entity.Currency;
import org.example.currency_exchanger.entity.ExchangeRate;
import org.example.currency_exchanger.exception.MappingException;

public final class ExchangeRateMapper implements EntityMapper<ExchangeRate, ExchangeRateDto> {

    private static final ExchangeRateMapper INSTANCE = new ExchangeRateMapper();

    private final EntityMapper<Currency, CurrencyDto> currencyMapper = CurrencyMapper.getInstance();

    private ExchangeRateMapper() {
    }

    @Override
    public ExchangeRateDto toDto(ExchangeRate entity) {
        if (entity == null) {
            throw new MappingException("Cannot convert null to ExchangeRateDto");
        }
        return new ExchangeRateDto(
                entity.getId(),
                currencyMapper.toDto(entity.getBaseCurrency()),
                currencyMapper.toDto(entity.getTargetCurrency()),
                entity.getRate()
        );
    }

    @Override
    public ExchangeRate toEntity(ExchangeRateDto dto) {
        if (dto == null) {
            throw new MappingException("Cannot convert null to ExchangeRate");
        }
        return new ExchangeRate(
                dto.id(),
                currencyMapper.toEntity(dto.baseCurrencyDto()),
                currencyMapper.toEntity(dto.targetCurrencyDto()),
                dto.rate()
        );
    }

    public static ExchangeRateMapper getInstance() {
        return INSTANCE;
    }

}
