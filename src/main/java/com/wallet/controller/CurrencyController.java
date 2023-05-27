package com.wallet.controller;

import com.wallet.model.dto.currency.CurrencyDto;
import com.wallet.service.currency.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequestMapping("/currency")
@RestController
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @Operation(summary = "Получение всех валют")
    @GetMapping
    public List<CurrencyDto> getAllCurrencies() {
        return currencyService.getAllCurrencies();
    }

    @Operation(summary = "Получение EUR, USD, GBP")
    @GetMapping("/daily")
    public List<CurrencyDto> getDailyCurrencies() {
        return currencyService.getDailyCurrencies();
    }
}
