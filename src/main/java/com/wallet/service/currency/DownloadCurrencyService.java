package com.wallet.service.currency;

import com.wallet.model.dto.currency.CurrencyDto;
import com.wallet.model.entity.CurrencyLog;
import com.wallet.repository.currency.CurrencyLogRepository;
import com.wallet.repository.currency.CurrencyRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@AllArgsConstructor
@Service
public class DownloadCurrencyService {

    private final CbrCurrencyClient client;
    private final CurrencyRepository currencyRepository;
    private final CurrencyLogRepository currencyLogRepository;

    @Scheduled(cron = "${wallet.cbr-report.cron}", zone = "Europe/Moscow")
    public void updateCurrenciesValues() {
        var currencyLog = new CurrencyLog().setDate(
            ZonedDateTime.now()
                .withZoneSameInstant(ZoneId.of("Europe/Moscow"))
                .truncatedTo(ChronoUnit.DAYS)
                .toInstant()
        );

        var cbrCurrencyDtos = client.getCurrencyByDate(Instant.now());
        cbrCurrencyDtos.forEach(cbrCurrencyDto -> {
            var currency = currencyRepository.findByCode(cbrCurrencyDto.getCharCode())
                .setValue(cbrCurrencyDto.getCurrencyValue());
            currencyRepository.save(currency);

            switch (cbrCurrencyDto.getCharCode()) {
                case "USD" -> currencyLog.setUsd(cbrCurrencyDto.getCurrencyValue());
                case "EUR" -> currencyLog.setEur(cbrCurrencyDto.getCurrencyValue());
                case "CHF" -> currencyLog.setChf(cbrCurrencyDto.getCurrencyValue());
                case "GBP" -> currencyLog.setGbp(cbrCurrencyDto.getCurrencyValue());
                case "JPY" -> currencyLog.setJpy(cbrCurrencyDto.getCurrencyValue());
                case "SEK" -> currencyLog.setSek(currency.getValue());
            }
        });
        currencyLogRepository.save(currencyLog);
    }
}
