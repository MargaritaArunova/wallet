package com.wallet.service.currency;

import com.wallet.mapper.CurrencyMapper;
import com.wallet.model.dto.currency.CurrencyDto;
import com.wallet.model.entity.Currency;
import com.wallet.model.entity.CurrencyLog;
import com.wallet.repository.currency.CurrencyLogRepository;
import com.wallet.repository.currency.CurrencyRepository;
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
        List<CurrencyLog> currencies = currencyLogRepository.findFirst2ByOrderByIdDesc();

        currencyRepository.findCurrenciesByCodeIn(dailyCurrenciesCodes).forEach(currency -> {
            var currencyDto = mapper.map(currency).setAscending(false);
            if (currencies.size() == 2) {
                var lastLog = currencies.get(0);
                var previousLog = currencies.get(1);
                switch (currency.getCode()) {
                    case "USD" -> currencyDto.setAscending(lastLog.getUsd().compareTo(previousLog.getUsd()) >= 0);
                    case "EUR" -> currencyDto.setAscending(lastLog.getEur().compareTo(previousLog.getEur()) >= 0);
                    case "GBP" -> currencyDto.setAscending(lastLog.getGbp().compareTo(previousLog.getGbp()) >= 0);
                }
            }
            dailyCurrencies.add(currencyDto);
        });

        return dailyCurrencies;
    }
}
