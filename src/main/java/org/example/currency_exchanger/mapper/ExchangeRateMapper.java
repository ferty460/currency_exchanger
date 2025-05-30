package org.example.currency_exchanger.mapper;

import org.example.currency_exchanger.dto.ExchangeRateDto;
import org.example.currency_exchanger.entity.ExchangeRate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CurrencyMapper.class})
public interface ExchangeRateMapper {

    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);

    ExchangeRateDto toDto(ExchangeRate exchangeRate);
    ExchangeRate toEntity(ExchangeRateDto exchangeRateDto);

}
