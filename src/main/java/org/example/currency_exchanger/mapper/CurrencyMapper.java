package org.example.currency_exchanger.mapper;

import org.example.currency_exchanger.dto.CurrencyDto;
import org.example.currency_exchanger.entity.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CurrencyMapper {

    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    CurrencyDto toDto(Currency currency);
    Currency toEntity(CurrencyDto currencyDto);
    List<CurrencyDto> toDtoList(List<Currency> currencies);

}
