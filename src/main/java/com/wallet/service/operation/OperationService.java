package com.wallet.service.operation;

import com.wallet.exception.NotFoundException;
import com.wallet.mapper.OperationMapper;
import com.wallet.model.dto.operation.OperationRequestDto;
import com.wallet.model.dto.operation.OperationResponseDto;
import com.wallet.model.entity.Category;
import com.wallet.model.entity.Operation;
import com.wallet.model.entity.Person;
import com.wallet.model.entity.Wallet;
import com.wallet.model.type.CurrencyType;
import com.wallet.model.type.TransactionType;
import com.wallet.repository.operation.OperationRepository;
import com.wallet.service.category.CategoryService;
import com.wallet.service.currency.CurrencyLogService;
import com.wallet.service.person.PersonService;
import com.wallet.service.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationService {

    private final OperationRepository operationRepository;
    private final OperationMapper mapper;

    private final PersonService personService;
    private final WalletService walletService;
    private final CategoryService categoryService;
    private final CurrencyLogService currencyLogService;


    @Transactional
    public OperationResponseDto createOperation(Long walletId, OperationRequestDto dto, String email) {
        validatePerson(email);

        var operation = mapper.map(dto);
        var wallet = walletService.getWalletById(walletId)
            .orElseThrow(() -> new NotFoundException(Wallet.class, walletId));
        var person = personService.getPersonById(wallet.getPerson().getId())
            .orElseThrow(() -> new NotFoundException(Person.class, wallet.getPerson().getId()));
        var category = categoryService.getCategoryByIdAndType(dto.getCategoryId(), dto.getType())
            .orElseThrow(() -> new NotFoundException(Category.class, dto.getCategoryId()));
        operation.setWallet(wallet).setCategory(category);

        BigDecimal nominal = operation.getBalance();
        if (wallet.getCurrency() != CurrencyType.RUB) {
            nominal = convertCurrency(nominal, operation.getDate(), wallet.getCurrency());
        }
        if (operation.getType() == TransactionType.INCOME) {
            wallet.setIncome(wallet.getIncome().add(operation.getBalance()))
                .setBalance(wallet.getBalance().add(operation.getBalance()));

            person.setIncome(person.getIncome().add(nominal))
                .setBalance(person.getBalance().add(nominal));
        } else {
            wallet.setSpendings(wallet.getSpendings().add(operation.getBalance()))
                .setBalance(wallet.getBalance().subtract(operation.getBalance()));

            person.setSpendings(person.getSpendings().add(nominal))
                .setBalance(person.getBalance().subtract(nominal));
        }
        walletService.updateWallet(wallet);
        personService.updatePerson(person);
        return mapper.map(operationRepository.save(operation));
    }

    @Transactional
    public void deleteOperationById(Long id, Long walletId, String email) {
        validatePerson(email);

        var operation = getOperationById(id, email);
        var wallet = walletService.getWalletById(walletId)
            .orElseThrow(() -> new NotFoundException(Wallet.class, walletId));
        var person = personService.getPersonById(wallet.getPerson().getId())
            .orElseThrow(() -> new NotFoundException(Person.class, wallet.getPerson().getId()));

        updateStatisticsAfterDeletion(person, wallet, operation);
        walletService.updateWallet(wallet);
        personService.updatePerson(person);

        operationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<OperationResponseDto> getAllOperationsByWalletId(Long walletId, String email) {
        validatePerson(email);

        var operations = operationRepository.findAllByWalletId(walletId);
        return operations.stream().map(mapper::map).toList();
    }

    @Transactional(readOnly = true)
    public Operation getOperationById(Long id, String email) {
        validatePerson(email);

        return operationRepository.findById(id).orElseThrow(
            () -> new NotFoundException(Operation.class, id)
        );
    }

    @Transactional(readOnly = true)
    public OperationResponseDto getOperationDtoById(Long id, String email) {
        return mapper.map(getOperationById(id, email));
    }

    private void updateStatisticsAfterDeletion(Person person, Wallet wallet, Operation operation) {
        BigDecimal nominal = operation.getBalance();
        if (wallet.getCurrency() != CurrencyType.RUB) {
            nominal = convertCurrency(nominal, operation.getDate(), wallet.getCurrency());
        }
        if (operation.getType() == TransactionType.INCOME) {
            wallet.setIncome(wallet.getIncome().subtract(operation.getBalance()))
                .setBalance(wallet.getBalance().subtract(operation.getBalance()));

            person.setIncome(person.getIncome().subtract(nominal))
                .setBalance(person.getBalance().subtract(nominal));
        } else {
            wallet.setSpendings(wallet.getSpendings().subtract(operation.getBalance()))
                .setBalance(wallet.getBalance().add(operation.getBalance()));

            person.setSpendings(person.getSpendings().subtract(nominal))
                .setBalance(person.getBalance().add(nominal));
        }
    }

    private BigDecimal convertCurrency(BigDecimal nominal, Instant date, CurrencyType type) {
        var currencyLog = currencyLogService.getFirstAfterDate(date);
        switch (type.name()) {
            case "USD" -> nominal = nominal.multiply(currencyLog.getUsd());
            case "EUR" -> nominal = nominal.multiply(currencyLog.getEur());
            case "CHF" -> nominal = nominal.multiply(currencyLog.getChf());
            case "GBP" -> nominal = nominal.multiply(currencyLog.getGbp());
            case "JPY" -> nominal = nominal.multiply(currencyLog.getJpy());
            case "SEK" -> nominal = nominal.multiply(currencyLog.getSek());
        }
        return nominal;
    }

    private void validatePerson(String email) {
        personService.checkPersonByEmail(email);
    }
}
