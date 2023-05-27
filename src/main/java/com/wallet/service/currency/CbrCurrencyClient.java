package com.wallet.service.currency;

import com.wallet.model.dto.cbr.CbrCurrencyDto;
import com.wallet.model.dto.cbr.CbrRatesDto;
import com.wallet.model.type.CurrencyType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CbrCurrencyClient {

    private static final List<String> currencyCodes = Stream.of(CurrencyType.values()).map(Enum::name).toList();
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter
        .ofPattern("dd/MM/yyyy")
        .withLocale(Locale.getDefault())
        .withZone(ZoneId.systemDefault());

    @Value("wallet.cbr-report.url")
    private String reportUrl;

    private final RestTemplate restTemplate;

    public List<CbrCurrencyDto> getCurrencyByDate(Instant date) {
        CbrRatesDto response = restTemplate.getForObject(
            String.format("%s?date_req=%s", reportUrl, dateFormatter.format(date)),
            CbrRatesDto.class
        );

        if (response == null) {
            return null;
        }
        List<CbrCurrencyDto> cbrCurrencies = new ArrayList<>();
        response.getCbrCurrencyDtos().forEach(currencyDto -> {
            currencyDto.setCurrencyValue(
                new BigDecimal(currencyDto.getValue().replace(",", "."))
            );
            if (currencyCodes.contains(currencyDto.getCharCode())) {
                cbrCurrencies.add(currencyDto);
            }
        });
        return cbrCurrencies;
    }
}
