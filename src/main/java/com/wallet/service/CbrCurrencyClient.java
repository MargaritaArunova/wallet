package com.wallet.service;

import com.wallet.model.dto.CbrCurrencyDto;
import com.wallet.model.dto.CbrRatesDto;
import com.wallet.model.type.CurrencyType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
public class CbrCurrencyClient {

    private static final List<String> currencyCodes =
            Stream.of(CurrencyType.values()).map(Enum::name).toList();
    private static final String URL = "https://cbr.ru/scripts/XML_daily.asp";
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    private final RestTemplate restTemplate = new RestTemplate();

    public List<CbrCurrencyDto> getCurrencyByDate(Instant date) {
        CbrRatesDto response = restTemplate.getForObject(
                String.format("%s?date_req=%s", URL, DATE_FORMATTER.format(date)),
                CbrRatesDto.class);

        if (response != null) {
            response.getCbrCurrencyDtos().forEach(currencyDto -> currencyDto.setCurrencyValue(
                    new BigDecimal(currencyDto.getValue().replace(",", "."))
            ));
            List<CbrCurrencyDto> currencyDtos = new ArrayList<>();
            for (var currencyDto : response.getCbrCurrencyDtos()) {
                if (currencyCodes.contains(currencyDto.getCharCode())) {
                    currencyDtos.add(currencyDto);
                }
            }
            return currencyDtos;
        }
        return null;
    }
}
