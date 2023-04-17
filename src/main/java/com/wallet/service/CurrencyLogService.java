package com.wallet.service;

import com.wallet.exception.NotFoundException;
import com.wallet.model.entity.CurrencyLog;
import com.wallet.repository.CurrencyLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CurrencyLogService {
    private final CurrencyLogRepository currencyLogRepository;

    public CurrencyLog getCurrencyLogByDate(Instant date) {
        return currencyLogRepository.findByDate(date).orElseThrow(() ->
                new NotFoundException(CurrencyLog.class, -1L)
        );
    }

    public CurrencyLog getFirstAfterDate(Instant date) {
        return currencyLogRepository.findFirstByDateGreaterThanEqual(date).orElseThrow(() ->
                new NotFoundException(CurrencyLog.class, -1L)
        );
    }
}
