package com.wallet.service;

import com.wallet.mapper.CurrencyMapper;
import com.wallet.model.dto.CurrencyDto;
import com.wallet.model.entity.Currency;
import com.wallet.repository.CurrencyLogRepository;
import com.wallet.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private static final List<String> dailyCurrenciesCodes = List.of("GBP", "EUR", "USD");

    private final CurrencyRepository currencyRepository;
    private final CurrencyLogRepository currencyLogRepository;
    private final CurrencyMapper mapper;

    @Transactional(readOnly = true)
    public Currency getCurrencyByCode(String code) {
        return currencyRepository.findByCode(code);
    }

    @Transactional(readOnly = true)
    public List<CurrencyDto> getAllCurrencies() {
        return currencyRepository.findAll()
                .stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CurrencyDto> getDailyCurrencies() {
        List<CurrencyDto> dailyCurrencies = new ArrayList<>();
        for (var currency : currencyRepository.findCurrenciesByCodeIn(dailyCurrenciesCodes)) {
            var currencyDto = mapper.map(currency);
            if (currencyLogRepository.count() >= 2) {
                var currencies = currencyLogRepository.findFirst2ByOrderByIdDesc();
                if (currencies.size() >= 2) {
                    var lastCurrencyLog = currencies.get(0);
                    var previousCurrencyLog = currencies.get(1);
                    boolean ascending = true;
                    switch (currency.getCode()) {
                        case "USD" -> ascending = lastCurrencyLog.getUsd().compareTo(previousCurrencyLog.getUsd()) >= 0;
                        case "EUR" -> ascending = lastCurrencyLog.getEur().compareTo(previousCurrencyLog.getEur()) >= 0;
                        case "GBP" -> ascending = lastCurrencyLog.getGbp().compareTo(previousCurrencyLog.getGbp()) >= 0;
                    }
                    currencyDto.setAscending(ascending);
                }
            } else {
                currencyDto.setAscending(false);
            }
            dailyCurrencies.add(currencyDto);
        }
        return dailyCurrencies;
    }
}
