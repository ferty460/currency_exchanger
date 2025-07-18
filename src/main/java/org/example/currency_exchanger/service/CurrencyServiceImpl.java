package org.example.currency_exchanger.service;

import org.example.currency_exchanger.context.ApplicationContext;
import org.example.currency_exchanger.dao.CurrencyDao;
import org.example.currency_exchanger.dto.CurrencyDto;
import org.example.currency_exchanger.entity.Currency;
import org.example.currency_exchanger.exception.DuplicateException;
import org.example.currency_exchanger.exception.NotFoundException;
import org.example.currency_exchanger.mapper.CurrencyMapper;
import org.example.currency_exchanger.util.validation.CurrencyValidator;
import org.example.currency_exchanger.util.validation.Validator;

import java.util.List;

public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyDao currencyDao = ApplicationContext.getContext().get(CurrencyDao.class);
    private final CurrencyMapper mapper = CurrencyMapper.INSTANCE;

    private final Validator<CurrencyDto> currencyValidator = new CurrencyValidator();

    @Override
    public List<CurrencyDto> getAll() {
        return mapper.toDtoList(currencyDao.findAll());
    }

    @Override
    public CurrencyDto getByCode(String code) {
        Currency currency = currencyDao.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Currency with code '" + code + "' is not found"));

        return mapper.toDto(currency);
    }

    @Override
    public CurrencyDto save(CurrencyDto currencyDto) {
        currencyValidator.validate(currencyDto);

        String code = currencyDto.code().trim().toUpperCase();
        String name = currencyDto.name().trim();
        String sign = currencyDto.sign().trim();

        if (currencyDao.findByCode(code).isPresent()) {
            throw new DuplicateException("Currency with code '" + code + "' already exists");
        }

        Currency currency = mapper.toEntity(
                new CurrencyDto(currencyDto.id(), code, name, sign)
        );

        return mapper.toDto(currencyDao.save(currency));
    }

}
