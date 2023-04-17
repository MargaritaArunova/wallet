package com.wallet.controller;

import com.wallet.model.dto.WalletDto;
import com.wallet.model.dto.WalletDtoResponse;
import com.wallet.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/wallet")
@RestController
@RequiredArgsConstructor
@Validated
public class WalletController {

    private final WalletService walletService;

    @Operation(summary = "Метод для создания кошелька")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public WalletDto postWallet(
            @Valid @RequestBody WalletDto walletDto,
            @RequestHeader("email") String email
    ) {
        return walletService.createWallet(walletDto, email);
    }

    @Operation(summary = "Метод для получения кошелька")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WalletDto getWallet(
            @PathVariable("id") Long id,
            @RequestHeader("email") String email
    ) {
        return walletService.getWalletByPersonId(id, email);
    }

    @Operation(summary = "Метод для получения всех кошельков пользователя")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<WalletDtoResponse> getAllWalletsByPersonId(
            @RequestHeader("email") String email
    ) {
        return walletService.getWalletsByPersonId(email);
    }

    @Operation(summary = "Метод для обновления кошелька")
    @PutMapping(value = {"/{id}"})
    public WalletDto updateWallet(
            @PathVariable("id") Long walletId,
            @Valid @RequestBody WalletDto walletDto,
            @RequestHeader("email") String email
    ) {
        return walletService.updateWallet(walletId, email, walletDto);
    }

    @Operation(summary = "Метод для удаления")
    @DeleteMapping(value = "/{id}")
    public void deleteWallet(
            @PathVariable("id") Long id,
            @RequestHeader("email") String email
    ) {
        walletService.deleteWallet(id, email);
    }
}
