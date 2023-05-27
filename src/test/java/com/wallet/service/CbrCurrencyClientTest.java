package com.wallet.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wallet.model.dto.cbr.CbrCurrencyDto;
import com.wallet.model.dto.cbr.CbrRatesDto;
import com.wallet.service.currency.CbrCurrencyClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@SpringBootTest(classes = CbrCurrencyClient.class)
public class CbrCurrencyClientTest {

    private CbrCurrencyClient cbrCurrencyClient;

    @MockBean
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        cbrCurrencyClient = new CbrCurrencyClient(
            restTemplate
        );
        ReflectionTestUtils.setField(cbrCurrencyClient, "reportUrl", "dummy");
    }

    @Test
    void getCurrencyByDate_test() {
        // given
        var date = ZonedDateTime.now()
            .withZoneSameInstant(ZoneId.of("Europe/Moscow"))
            .truncatedTo(ChronoUnit.DAYS)
            .toInstant();
        var cbrCurrency = new CbrCurrencyDto()
            .setCharCode("USD")
            .setValue(String.valueOf(1000));
        var cbrDefaultCurrency = new CbrCurrencyDto()
            .setCharCode("ABC")
            .setValue(String.valueOf(1000));
        var cbrRate = new CbrRatesDto()
            .setDate(date)
            .setCbrCurrencyDtos(List.of(cbrCurrency, cbrDefaultCurrency));

        when(restTemplate.getForObject(anyString(), eq(CbrRatesDto.class)))
            .thenReturn(cbrRate);

        // when
        var result = cbrCurrencyClient.getCurrencyByDate(date);

        // then
        verify(restTemplate, times(1)).getForObject(anyString(), eq(CbrRatesDto.class));
        assertThat(List.of(cbrCurrency.setCurrencyValue(BigDecimal.valueOf(1000))))
            .usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    void getCurrencyByDate_emptyResponseTest() {
        // given
        var date = ZonedDateTime.now()
            .withZoneSameInstant(ZoneId.of("Europe/Moscow"))
            .truncatedTo(ChronoUnit.DAYS)
            .toInstant();
        var cbrCurrency = new CbrCurrencyDto()
            .setCharCode("USD")
            .setValue(String.valueOf(1000));
        var cbrRate = new CbrRatesDto()
            .setDate(date)
            .setCbrCurrencyDtos(List.of(cbrCurrency));

        when(restTemplate.getForObject(anyString(), eq(CbrRatesDto.class)))
            .thenReturn(null);

        // when
        var result = cbrCurrencyClient.getCurrencyByDate(date);

        // then
        verify(restTemplate, times(1)).getForObject(anyString(), eq(CbrRatesDto.class));
        assertThat(result).isNull();
    }
}
