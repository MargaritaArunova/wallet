package com.wallet.service.currency;

import com.wallet.exception.NotFoundException;
import com.wallet.model.entity.CurrencyLog;
import com.wallet.repository.currency.CurrencyLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CurrencyLogService {

    private final CurrencyLogRepository currencyLogRepository;


    /*
     * Если за выбранную дату нет данных по курсу валют, то отдаётся самая ранняя дата из базы данных
     */
    public CurrencyLog getFirstAfterDate(Instant date) {
        return currencyLogRepository.findFirstByDateGreaterThanEqual(date).orElseThrow(() ->
            new NotFoundException(CurrencyLog.class, -1L)
        );
    }
}
