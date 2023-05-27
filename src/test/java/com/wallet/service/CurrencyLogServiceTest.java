package com.wallet.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wallet.model.entity.CurrencyLog;
import com.wallet.repository.currency.CurrencyLogRepository;
import com.wallet.service.currency.CurrencyLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.util.Optional;

@SpringBootTest(classes = CurrencyLogService.class)
public class CurrencyLogServiceTest {

    private CurrencyLogService currencyLogService;

    @MockBean
    private CurrencyLogRepository currencyLogRepository;

    @BeforeEach
    void setUp() {
        currencyLogService = new CurrencyLogService(currencyLogRepository);
    }

    @Test
    void getFirstAfterDate_test() {
        // given
        var date = Instant.now();
        when(currencyLogRepository.findFirstByDateGreaterThanEqual(date))
            .thenReturn(Optional.of(new CurrencyLog()));

        // when
        currencyLogService.getFirstAfterDate(date);

        // then
        verify(currencyLogRepository, times(1)).findFirstByDateGreaterThanEqual(date);
    }

}
