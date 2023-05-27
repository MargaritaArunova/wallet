package com.wallet.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wallet.model.dto.cbr.CbrCurrencyDto;
import com.wallet.model.entity.Currency;
import com.wallet.model.type.CurrencyType;
import com.wallet.repository.currency.CurrencyLogRepository;
import com.wallet.repository.currency.CurrencyRepository;
import com.wallet.service.currency.CbrCurrencyClient;
import com.wallet.service.currency.DownloadCurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest(classes = DownloadCurrencyService.class)
public class DownloadCurrencyServiceTest {

    private DownloadCurrencyService downloadCurrencyService;

    @MockBean
    private CbrCurrencyClient client;

    @MockBean
    private CurrencyRepository currencyRepository;

    @MockBean
    private CurrencyLogRepository currencyLogRepository;

    @BeforeEach
    void setUp() {
        downloadCurrencyService = new DownloadCurrencyService(
            client,
            currencyRepository,
            currencyLogRepository
        );
    }

    @ParameterizedTest
    @EnumSource(CurrencyType.class)
    void updateCurrenciesValues_test(CurrencyType type) {
        // given
        var currency = new Currency()
            .setId(1L)
            .setCode(type.toString())
            .setValue(BigDecimal.valueOf(1100));
        var cbrCurrency = new CbrCurrencyDto()
            .setCurrencyValue(BigDecimal.valueOf(1000))
            .setCharCode(type.toString());

        when(client.getCurrencyByDate(any()))
            .thenReturn(List.of(cbrCurrency));
        when(currencyRepository.findByCode(cbrCurrency.getCharCode()))
            .thenReturn(currency);

        // when
        downloadCurrencyService.updateCurrenciesValues();

        // then
        verify(currencyRepository, times(1)).findByCode(cbrCurrency.getCharCode());
        verify(currencyRepository, times(1)).save(
            currency.setValue(cbrCurrency.getCurrencyValue())
        );
        verify(currencyLogRepository, times(1)).save(any());
    }

}
