package org.example.currency_exchanger.service;

import org.example.currency_exchanger.dao.CurrencyDao;
import org.example.currency_exchanger.dao.CurrencyDaoImpl;
import org.example.currency_exchanger.dto.CurrencyDto;
import org.example.currency_exchanger.entity.Currency;
import org.example.currency_exchanger.exception.NotFoundException;
import org.example.currency_exchanger.mapper.CurrencyMapper;

import java.util.List;

public class CurrencyServiceImpl implements CurrencyService {

    private static final CurrencyService INSTANCE = new CurrencyServiceImpl();

    private final CurrencyDao currencyDao = CurrencyDaoImpl.getInstance();
    private final CurrencyMapper mapper = CurrencyMapper.INSTANCE;

    private CurrencyServiceImpl() {
    }

    public static CurrencyService getInstance() {
        return INSTANCE;
    }

    @Override
    public List<CurrencyDto> getAll() {
        return mapper.toDtoList(currencyDao.findAll());
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
