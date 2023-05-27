package com.wallet.controller;

import com.wallet.model.dto.operation.OperationRequestDto;
import com.wallet.model.dto.operation.OperationResponseDto;
import com.wallet.service.operation.OperationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import javax.validation.Valid;

@RequestMapping("wallet/{walletId}/operation")
@RestController
@RequiredArgsConstructor
public class OperationController {

    private final OperationService operationService;

    @Operation(summary = "Получение операции")
    @GetMapping(value = "/{id}")
    @Transactional
    public OperationResponseDto getOperation(
        @PathVariable("id") Long id,
        @RequestHeader("email") String email
    ) {
        return operationService.getOperationDtoById(id, email);
    }

    @Operation(summary = "Получение всех операций")
    @GetMapping
    @Transactional
    public List<OperationResponseDto> getAllOperations(
        @PathVariable("walletId") Long walletId,
        @RequestHeader("email") String email
    ) {
        return operationService.getAllOperationsByWalletId(walletId, email);
    }

    @Operation(summary = "Создание новой операции")
    @PostMapping
    @Transactional
    public OperationResponseDto createOperation(
        @PathVariable("walletId") Long walletId,
        @Valid @RequestBody OperationRequestDto dto,
        @RequestHeader("email") String email) {
        return operationService.createOperation(walletId, dto, email);
    }

    @Operation(summary = "Удаление операции")
    @DeleteMapping(value = "/{id}")
    public void deleteOperation(
        @PathVariable("walletId") Long walletId,
        @PathVariable("id") Long id,
        @RequestHeader("email") String email
    ) {
        operationService.deleteOperationById(id, walletId, email);
    }

}
