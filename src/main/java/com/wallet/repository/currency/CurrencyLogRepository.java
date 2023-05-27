package com.wallet.repository.currency;

import com.wallet.model.entity.CurrencyLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface CurrencyLogRepository extends JpaRepository<CurrencyLog, Long> {

    List<CurrencyLog> findFirst2ByOrderByIdDesc();

    Optional<CurrencyLog> findFirstByDateGreaterThanEqual(Instant date);
}
