package com.wallet.service;

import com.wallet.model.dto.CurrencyDto;
import com.wallet.model.entity.CurrencyLog;
import com.wallet.repository.CurrencyLogRepository;
import com.wallet.repository.CurrencyRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class DownloadCurrencyService {

    private final CbrCurrencyClient client;
    private final CurrencyRepository currencyRepository;
    private final CurrencyLogRepository currencyLogRepository;

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Moscow")
    void updateCurrenciesValues() {
        var cbrCurrencyDtos = client.getCurrencyByDate(Instant.now());

        List<CurrencyDto> currencyDtos = cbrCurrencyDtos.stream().map(
                cbrCurrencyDto -> new CurrencyDto()
                        .setValue(cbrCurrencyDto.getCurrencyValue())
                        .setCode(cbrCurrencyDto.getCharCode())
        ).toList();
        for (var currencyDto : currencyDtos) {
            currencyRepository.save(currencyRepository.findByCode(currencyDto.getCode())
                    .setValue(currencyDto.getValue()));
        }

        var currencyLog = new CurrencyLog().setDate(ZonedDateTime.now()
                .withZoneSameInstant(ZoneId.of("Europe/Moscow")).toInstant());
        for (CurrencyDto currency : currencyDtos) {
            switch (currency.getCode()) {
                case "USD" -> currencyLog.setUsd(currency.getValue());
                case "EUR" -> currencyLog.setEur(currency.getValue());
                case "CHF" -> currencyLog.setChf(currency.getValue());
                case "GBP" -> currencyLog.setGbp(currency.getValue());
                case "JPY" -> currencyLog.setJpy(currency.getValue());
                case "SEK" -> currencyLog.setSek(currency.getValue());
            }
        }
        currencyLogRepository.save(currencyLog);
    }
}
