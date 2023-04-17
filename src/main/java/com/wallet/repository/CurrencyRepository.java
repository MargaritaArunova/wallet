package com.wallet.repository;

import com.wallet.model.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    List<Currency> findCurrenciesByCodeIn(List<String> currencyCodes);

    Currency findByCode(String code);
}
