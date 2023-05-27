package com.wallet.controller;

import com.wallet.model.dto.wallet.WalletCreateDto;
import com.wallet.model.dto.wallet.WalletDto;
import com.wallet.model.dto.wallet.WalletDtoResponse;
import com.wallet.service.wallet.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import javax.validation.Valid;

@RequestMapping("/wallet")
@RestController
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @Operation(summary = "Метод для создания кошелька")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public WalletDto postWallet(
        @Valid @RequestBody WalletCreateDto walletDto,
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
        return walletService.getWalletByIdAndPersonId(id, email);
    }

    @Operation(summary = "Метод для получения всех кошельков пользователя")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<WalletDtoResponse> getAllWalletsByPersonId(
        @RequestHeader("email") String email
    ) {
        return walletService.getWalletsByPerson(email);
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
