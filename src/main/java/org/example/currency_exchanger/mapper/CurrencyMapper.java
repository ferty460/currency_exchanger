package org.example.currency_exchanger.mapper;

import org.example.currency_exchanger.dto.CurrencyDto;
import org.example.currency_exchanger.entity.Currency;
import org.example.currency_exchanger.exception.MappingException;

public final class CurrencyMapper implements EntityMapper<Currency, CurrencyDto> {

    private static final CurrencyMapper INSTANCE = new CurrencyMapper();

    private CurrencyMapper() {
    }

    public static CurrencyMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public CurrencyDto toDto(Currency entity) {
        if (entity == null) {
            throw new MappingException("Cannot convert null to CurrencyDto");
        }
        return new CurrencyDto(
                entity.getId(),
                entity.getCode(),
                entity.getFullName(),
                entity.getSign()
        );
    }

    @Override
    public Currency toEntity(CurrencyDto dto) {
        if (dto == null) {
            throw new MappingException("Cannot convert null to Currency");
        }
        return new Currency(
                dto.id(),
                dto.code(),
                dto.fullName(),
                dto.sign()
        );
    }

}
