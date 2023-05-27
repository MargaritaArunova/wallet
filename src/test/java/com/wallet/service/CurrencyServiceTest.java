package com.wallet.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wallet.mapper.CurrencyMapperImpl;
import com.wallet.model.entity.Currency;
import com.wallet.model.entity.CurrencyLog;
import com.wallet.model.type.CurrencyType;
import com.wallet.repository.currency.CurrencyLogRepository;
import com.wallet.repository.currency.CurrencyRepository;
import com.wallet.service.currency.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest(classes = CurrencyService.class)
public class CurrencyServiceTest {

    private static final List<String> dailyCurrenciesCodes = List.of("GBP", "EUR", "USD");

    private CurrencyService currencyService;

    @SpyBean
    private CurrencyMapperImpl mapper;

    @MockBean
    private CurrencyRepository currencyRepository;

    @MockBean
    private CurrencyLogRepository currencyLogRepository;

    @BeforeEach
    void setUp() {
        currencyService = new CurrencyService(
            currencyRepository,
            currencyLogRepository,
            mapper
        );
    }

    @ParameterizedTest
    @EnumSource(CurrencyType.class)
    void getCurrencyByCode_test(CurrencyType type) {
        // given
        when(currencyRepository.findByCode(any()))
            .thenReturn(new Currency());

        // when
        currencyService.getCurrencyByCode(type.toString());

        // then
        verify(currencyRepository, times(1)).findByCode(any());
    }

    @Test
    void getAllCurrencies_test() {
        // given
        when(currencyRepository.findAll())
            .thenReturn(List.of(new Currency()));

        // when
        currencyService.getAllCurrencies();

        // then
        verify(currencyRepository, times(1)).findAll();
    }

    @Test
    void getDailyCurrencies_ascendingTest() {
        // given
        var date = Instant.now();
        var currencyUsd = new Currency()
            .setId(1L)
            .setCode("USD")
            .setValue(BigDecimal.valueOf(1000));
        var currencyEur = new Currency()
            .setId(1L)
            .setCode("EUR")
            .setValue(BigDecimal.valueOf(1001));
        var currencyGbp = new Currency()
            .setId(1L)
            .setCode("GBP")
            .setValue(BigDecimal.valueOf(1002));

        when(currencyRepository.findCurrenciesByCodeIn(dailyCurrenciesCodes))
            .thenReturn(List.of(currencyUsd, currencyEur, currencyGbp));
        when(currencyLogRepository.findFirst2ByOrderByIdDesc())
            .thenReturn(List.of(
                new CurrencyLog()
                    .setId(2L)
                    .setDate(date)
                    .setUsd(BigDecimal.valueOf(1000))
                    .setEur(BigDecimal.valueOf(1001))
                    .setGbp(BigDecimal.valueOf(1002)),
                new CurrencyLog()
                    .setId(1L)
                    .setDate(date.minus(1, ChronoUnit.DAYS))
                    .setUsd(BigDecimal.valueOf(999))
                    .setEur(BigDecimal.valueOf(1000))
                    .setGbp(BigDecimal.valueOf(1000))
            ));
        var currencies = Stream.of(currencyUsd, currencyEur, currencyGbp)
            .map(mapper::map)
            .map(dto -> dto.setAscending(true))
            .collect(Collectors.toSet());

        // when
        var result = currencyService.getDailyCurrencies();

        // then
        verify(currencyLogRepository, times(1)).findFirst2ByOrderByIdDesc();
        verify(currencyRepository, times(1)).findCurrenciesByCodeIn(dailyCurrenciesCodes);

        assertThat(currencies).usingRecursiveComparison().isEqualTo(new HashSet<>(result));
    }

    @Test
    void getDailyCurrencies_descendingTest() {
        // given
        var date = Instant.now();
        var currencyUsd = new Currency()
            .setId(1L)
            .setCode("USD")
            .setValue(BigDecimal.valueOf(1000));
        var currencyEur = new Currency()
            .setId(1L)
            .setCode("EUR")
            .setValue(BigDecimal.valueOf(1001));
        var currencyGbp = new Currency()
            .setId(1L)
            .setCode("GBP")
            .setValue(BigDecimal.valueOf(1002));

        when(currencyRepository.findCurrenciesByCodeIn(dailyCurrenciesCodes))
            .thenReturn(List.of(currencyUsd, currencyEur, currencyGbp));
        when(currencyLogRepository.findFirst2ByOrderByIdDesc())
            .thenReturn(List.of(
                new CurrencyLog()
                    .setId(2L)
                    .setDate(date)
                    .setUsd(BigDecimal.valueOf(1000))
                    .setEur(BigDecimal.valueOf(1001))
                    .setGbp(BigDecimal.valueOf(1002)),
                new CurrencyLog()
                    .setId(1L)
                    .setDate(date.minus(1, ChronoUnit.DAYS))
                    .setUsd(BigDecimal.valueOf(1001))
                    .setEur(BigDecimal.valueOf(1002))
                    .setGbp(BigDecimal.valueOf(1003))
            ));
        var currencies = Stream.of(currencyUsd, currencyEur, currencyGbp)
            .map(mapper::map)
            .map(dto -> dto.setAscending(false))
            .collect(Collectors.toSet());

        // when
        var result = currencyService.getDailyCurrencies();

        // then
        verify(currencyLogRepository, times(1)).findFirst2ByOrderByIdDesc();
        verify(currencyRepository, times(1)).findCurrenciesByCodeIn(dailyCurrenciesCodes);

        assertThat(currencies).usingRecursiveComparison().isEqualTo(new HashSet<>(result));
    }

    @Test
    void getDailyCurrencies_defaultCurrencyTest() {
        // given
        var date = Instant.now();
        var currency = new Currency()
            .setId(1L)
            .setCode("ABC")
            .setValue(BigDecimal.valueOf(1000));

        when(currencyRepository.findCurrenciesByCodeIn(dailyCurrenciesCodes))
            .thenReturn(List.of(currency));
        when(currencyLogRepository.findFirst2ByOrderByIdDesc())
            .thenReturn(List.of(
                new CurrencyLog()
                    .setId(2L)
                    .setDate(date)
                    .setUsd(BigDecimal.valueOf(1000))
                    .setEur(BigDecimal.valueOf(1001))
                    .setGbp(BigDecimal.valueOf(1002)),
                new CurrencyLog()
                    .setId(1L)
                    .setDate(date.minus(1, ChronoUnit.DAYS))
                    .setUsd(BigDecimal.valueOf(1001))
                    .setEur(BigDecimal.valueOf(1002))
                    .setGbp(BigDecimal.valueOf(1003))
            ));
        var currencies = Stream.of(currency)
            .map(mapper::map)
            .map(dto -> dto.setAscending(false))
            .collect(Collectors.toSet());

        // when
        var result = currencyService.getDailyCurrencies();

        // then
        verify(currencyLogRepository, times(1)).findFirst2ByOrderByIdDesc();
        verify(currencyRepository, times(1)).findCurrenciesByCodeIn(dailyCurrenciesCodes);

        assertThat(currencies).usingRecursiveComparison().isEqualTo(new HashSet<>(result));
    }

    @Test
    void getDailyCurrencies_noCurrencyInfoTest() {
        // given
        var date = Instant.now();
        var currencyUsd = new Currency()
            .setId(1L)
            .setCode("USD")
            .setValue(BigDecimal.valueOf(1000));
        var currencyEur = new Currency()
            .setId(2L)
            .setCode("EUR")
            .setValue(BigDecimal.valueOf(1001));
        var currencyGbp = new Currency()
            .setId(3L)
            .setCode("GBP")
            .setValue(BigDecimal.valueOf(1002));

        when(currencyRepository.findCurrenciesByCodeIn(dailyCurrenciesCodes))
            .thenReturn(List.of(currencyUsd, currencyEur, currencyGbp));
        when(currencyLogRepository.findFirst2ByOrderByIdDesc())
            .thenReturn(List.of());
        var currencies = Stream.of(currencyUsd, currencyEur, currencyGbp)
            .map(mapper::map)
            .map(dto -> dto.setAscending(false))
            .collect(Collectors.toSet());

        // when
        var result = currencyService.getDailyCurrencies();

        // then
        verify(currencyLogRepository, times(1)).findFirst2ByOrderByIdDesc();
        verify(currencyRepository, times(1)).findCurrenciesByCodeIn(dailyCurrenciesCodes);

        assertThat(currencies).usingRecursiveComparison().isEqualTo(new HashSet<>(result));
    }
}
