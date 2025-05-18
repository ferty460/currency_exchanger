package org.example.currency_exchanger.service;

import org.example.currency_exchanger.dao.CurrencyDao;
import org.example.currency_exchanger.dao.CurrencyDaoImpl;
import org.example.currency_exchanger.dto.CurrencyDto;
import org.example.currency_exchanger.entity.Currency;
import org.example.currency_exchanger.exception.NotFoundException;
import org.example.currency_exchanger.mapper.CurrencyMapper;
import org.example.currency_exchanger.mapper.EntityMapper;

import java.util.List;

public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyDao currencyDao = CurrencyDaoImpl.getInstance();
    private final EntityMapper<Currency, CurrencyDto> mapper = CurrencyMapper.getInstance();

    @Override
    public List<CurrencyDto> getAll() {
        return currencyDao.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public CurrencyDto getByCode(String code) {
        Currency currency = currencyDao.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Currency with code " + code + " not found"));

        return mapper.toDto(currency);
    }

    @Override
    public CurrencyDto save(CurrencyDto currencyDto) {
        Currency currency = mapper.toEntity(currencyDto);

        return mapper.toDto(currencyDao.save(currency));
    }

}
