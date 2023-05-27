package com.wallet.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wallet.mapper.CategoryMapperImpl;
import com.wallet.mapper.OperationMapperImpl;
import com.wallet.model.dto.operation.OperationRequestDto;
import com.wallet.model.entity.Category;
import com.wallet.model.entity.CurrencyLog;
import com.wallet.model.entity.Operation;
import com.wallet.model.entity.Person;
import com.wallet.model.entity.Wallet;
import com.wallet.model.type.CurrencyType;
import com.wallet.model.type.TransactionType;
import com.wallet.repository.operation.OperationRepository;
import com.wallet.service.category.CategoryService;
import com.wallet.service.currency.CurrencyLogService;
import com.wallet.service.operation.OperationService;
import com.wallet.service.person.PersonService;
import com.wallet.service.wallet.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest(classes = OperationService.class)
public class OperationServiceTest {

    private OperationService operationService;

    @SpyBean
    private CategoryMapperImpl categoryMapper;

    @SpyBean
    private OperationMapperImpl mapper;

    @MockBean
    private OperationRepository operationRepository;

    @MockBean
    private PersonService personService;

    @MockBean
    private WalletService walletService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private CurrencyLogService currencyLogService;

    @BeforeEach
    void setUp() {
        operationService = new OperationService(
            operationRepository,
            mapper,
            personService,
            walletService,
            categoryService,
            currencyLogService
        );
    }

    @Test
    void createOperation_incomeOperationTest() {
        // given
        var person = new Person()
            .setId(1L)
            .setEmail("some-mail")
            .setBalance(BigDecimal.valueOf(10000))
            .setIncome(BigDecimal.valueOf(10000))
            .setSpendings(BigDecimal.valueOf(0));
        var wallet = new Wallet()
            .setId(1L)
            .setPerson(person)
            .setCurrency(CurrencyType.RUB)
            .setBalance(BigDecimal.valueOf(10000))
            .setIncome(BigDecimal.valueOf(10000))
            .setSpendings(BigDecimal.valueOf(0));
        var category = new Category()
            .setId(1L)
            .setType(TransactionType.INCOME);

        doNothing().when(personService).checkPersonByEmail(any());
        when(walletService.getWalletById(wallet.getId()))
            .thenReturn(Optional.of(wallet));
        when(personService.getPersonById(person.getId()))
            .thenReturn(Optional.of(person));
        when(categoryService.getCategoryByIdAndType(category.getId(), TransactionType.INCOME))
            .thenReturn(Optional.of(category));
        when(walletService.getWalletById(wallet.getId()))
            .thenReturn(Optional.of(wallet));

        // when
        operationService.createOperation(
            wallet.getId(),
            new OperationRequestDto()
                .setWalletId(wallet.getId())
                .setCategoryId(category.getId())
                .setType(TransactionType.INCOME)
                .setBalance(BigDecimal.valueOf(1000)),
            person.getEmail()
        );

        // then
        verify(walletService, times(1)).updateWallet(
            wallet.setIncome(BigDecimal.valueOf(11000))
                .setBalance(BigDecimal.valueOf(11000))
        );
        verify(personService, times(1)).updatePerson(
            person.setIncome(BigDecimal.valueOf(11000))
                .setBalance(BigDecimal.valueOf(11000))
        );
        verify(operationRepository, times(1)).save(any());
    }

    @Test
    void createOperation_spendingOperationTest() {
        // given
        var person = new Person()
            .setId(1L)
            .setEmail("some-mail")
            .setBalance(BigDecimal.valueOf(5000))
            .setIncome(BigDecimal.valueOf(10000))
            .setSpendings(BigDecimal.valueOf(5000));
        var wallet = new Wallet()
            .setId(1L)
            .setPerson(person)
            .setCurrency(CurrencyType.RUB)
            .setBalance(BigDecimal.valueOf(5000))
            .setIncome(BigDecimal.valueOf(10000))
            .setSpendings(BigDecimal.valueOf(5000));
        var category = new Category()
            .setId(1L)
            .setType(TransactionType.SPENDING);

        doNothing().when(personService).checkPersonByEmail(any());
        when(walletService.getWalletById(wallet.getId()))
            .thenReturn(Optional.of(wallet));
        when(personService.getPersonById(person.getId()))
            .thenReturn(Optional.of(person));
        when(categoryService.getCategoryByIdAndType(category.getId(), TransactionType.SPENDING))
            .thenReturn(Optional.of(category));
        when(walletService.getWalletById(wallet.getId()))
            .thenReturn(Optional.of(wallet));

        // when
        operationService.createOperation(
            wallet.getId(),
            new OperationRequestDto()
                .setWalletId(wallet.getId())
                .setCategoryId(category.getId())
                .setType(TransactionType.SPENDING)
                .setBalance(BigDecimal.valueOf(5000)),
            person.getEmail()
        );

        // then
        verify(walletService, times(1)).updateWallet(
            wallet.setSpendings(BigDecimal.valueOf(10000))
                .setBalance(BigDecimal.valueOf(0))
        );
        verify(personService, times(1)).updatePerson(
            person.setSpendings(BigDecimal.valueOf(10000))
                .setBalance(BigDecimal.valueOf(0))
        );
        verify(operationRepository, times(1)).save(any());
    }

    @ParameterizedTest
    @EnumSource(CurrencyType.class)
    void createOperation_currencyOperationTest(CurrencyType currencyType) {
        // given
        var date = Instant.now();
        var person = new Person()
            .setId(1L)
            .setEmail("some-mail")
            .setBalance(BigDecimal.valueOf(10000))
            .setIncome(BigDecimal.valueOf(10000))
            .setSpendings(BigDecimal.valueOf(0));
        var wallet = new Wallet()
            .setId(1L)
            .setPerson(person)
            .setCurrency(currencyType)
            .setBalance(BigDecimal.valueOf(10000))
            .setIncome(BigDecimal.valueOf(10000))
            .setSpendings(BigDecimal.valueOf(0));
        var category = new Category()
            .setId(1L)
            .setType(TransactionType.INCOME);

        doNothing().when(personService).checkPersonByEmail(any());
        when(walletService.getWalletById(wallet.getId()))
            .thenReturn(Optional.of(wallet));
        when(personService.getPersonById(person.getId()))
            .thenReturn(Optional.of(person));
        when(categoryService.getCategoryByIdAndType(category.getId(), TransactionType.INCOME))
            .thenReturn(Optional.of(category));
        when(walletService.getWalletById(wallet.getId()))
            .thenReturn(Optional.of(wallet));
        when(currencyLogService.getFirstAfterDate(date))
            .thenReturn(
                new CurrencyLog()
                    .setUsd(BigDecimal.valueOf(10))
                    .setEur(BigDecimal.valueOf(20))
                    .setGbp(BigDecimal.valueOf(30))
                    .setChf(BigDecimal.valueOf(40))
                    .setJpy(BigDecimal.valueOf(50))
                    .setSek(BigDecimal.valueOf(60))
                    .setDate(date)
            );

        // when
        operationService.createOperation(
            wallet.getId(),
            new OperationRequestDto()
                .setWalletId(wallet.getId())
                .setCategoryId(category.getId())
                .setType(TransactionType.INCOME)
                .setBalance(BigDecimal.valueOf(1000))
                .setDate(date),
            person.getEmail()
        );

        // then
        verify(walletService, times(1)).updateWallet(any());
        verify(personService, times(1)).updatePerson(any());
        verify(operationRepository, times(1)).save(any());
    }

    @Test
    void deleteOperationById_incomeTest() {
        // given
        var person = new Person()
            .setId(1L)
            .setEmail("some-mail")
            .setBalance(BigDecimal.valueOf(10000))
            .setIncome(BigDecimal.valueOf(10000))
            .setSpendings(BigDecimal.valueOf(0));
        var wallet = new Wallet()
            .setId(1L)
            .setPerson(person)
            .setCurrency(CurrencyType.RUB)
            .setBalance(BigDecimal.valueOf(10000))
            .setIncome(BigDecimal.valueOf(10000))
            .setSpendings(BigDecimal.valueOf(0));
        var category = new Category()
            .setId(1L)
            .setType(TransactionType.INCOME);
        var operation = new Operation()
            .setId(1L)
            .setCategory(category)
            .setWallet(wallet)
            .setDate(Instant.now())
            .setBalance(BigDecimal.valueOf(10000))
            .setType(TransactionType.INCOME);
        wallet.setOperations(List.of(operation));
        person.setWallets(List.of(wallet));

        doNothing().when(personService).checkPersonByEmail(any());
        when(operationRepository.findById(operation.getId()))
            .thenReturn(Optional.of(operation));
        when(walletService.getWalletById(wallet.getId()))
            .thenReturn(Optional.of(wallet));
        when(personService.getPersonById(person.getId()))
            .thenReturn(Optional.of(person));
        when(categoryService.getCategoryByIdAndType(category.getId(), TransactionType.INCOME))
            .thenReturn(Optional.of(category));
        when(walletService.getWalletById(wallet.getId()))
            .thenReturn(Optional.of(wallet));

        // when
        operationService.deleteOperationById(
            operation.getId(),
            wallet.getId(),
            person.getEmail()
        );

        // then
        verify(walletService, times(1)).updateWallet(
            wallet.setIncome(BigDecimal.valueOf(0))
                .setBalance(BigDecimal.valueOf(0))
        );
        verify(personService, times(1)).updatePerson(
            person.setIncome(BigDecimal.valueOf(0))
                .setBalance(BigDecimal.valueOf(0))
        );
        verify(operationRepository, times(1)).deleteById(operation.getId());
    }

    @Test
    void deleteOperationById_spendingTest() {
        // given
        var person = new Person()
            .setId(1L)
            .setEmail("some-mail")
            .setBalance(BigDecimal.valueOf(5000))
            .setIncome(BigDecimal.valueOf(10000))
            .setSpendings(BigDecimal.valueOf(5000));
        var wallet = new Wallet()
            .setId(1L)
            .setPerson(person)
            .setCurrency(CurrencyType.RUB)
            .setBalance(BigDecimal.valueOf(5000))
            .setIncome(BigDecimal.valueOf(10000))
            .setSpendings(BigDecimal.valueOf(5000));
        var category = new Category()
            .setId(1L)
            .setType(TransactionType.INCOME);
        var operation = new Operation()
            .setId(1L)
            .setCategory(category)
            .setWallet(wallet)
            .setDate(Instant.now())
            .setBalance(BigDecimal.valueOf(5000))
            .setType(TransactionType.SPENDING);
        wallet.setOperations(List.of(operation));
        person.setWallets(List.of(wallet));

        doNothing().when(personService).checkPersonByEmail(any());
        when(operationRepository.findById(operation.getId()))
            .thenReturn(Optional.of(operation));
        when(walletService.getWalletById(wallet.getId()))
            .thenReturn(Optional.of(wallet));
        when(personService.getPersonById(person.getId()))
            .thenReturn(Optional.of(person));
        when(categoryService.getCategoryByIdAndType(category.getId(), TransactionType.INCOME))
            .thenReturn(Optional.of(category));
        when(walletService.getWalletById(wallet.getId()))
            .thenReturn(Optional.of(wallet));

        // when
        operationService.deleteOperationById(
            operation.getId(),
            wallet.getId(),
            person.getEmail()
        );

        // then
        verify(walletService, times(1)).updateWallet(
            wallet.setSpendings(BigDecimal.valueOf(0))
                .setBalance(BigDecimal.valueOf(10000))
        );
        verify(personService, times(1)).updatePerson(
            person.setSpendings(BigDecimal.valueOf(0))
                .setBalance(BigDecimal.valueOf(10000))
        );
        verify(operationRepository, times(1)).deleteById(operation.getId());
    }

    @Test
    void deleteOperationById_currencyTest() {
        // given
        var person = new Person()
            .setId(1L)
            .setEmail("some-mail")
            .setBalance(BigDecimal.valueOf(10000))
            .setIncome(BigDecimal.valueOf(10000))
            .setSpendings(BigDecimal.valueOf(0));
        var wallet = new Wallet()
            .setId(1L)
            .setPerson(person)
            .setCurrency(CurrencyType.USD)
            .setBalance(BigDecimal.valueOf(10000))
            .setIncome(BigDecimal.valueOf(10000))
            .setSpendings(BigDecimal.valueOf(0));
        var category = new Category()
            .setId(1L)
            .setType(TransactionType.INCOME);
        var operation = new Operation()
            .setId(1L)
            .setCategory(category)
            .setWallet(wallet)
            .setDate(Instant.now())
            .setBalance(BigDecimal.valueOf(10000))
            .setType(TransactionType.INCOME);
        wallet.setOperations(List.of(operation));
        person.setWallets(List.of(wallet));

        doNothing().when(personService).checkPersonByEmail(any());
        when(operationRepository.findById(operation.getId()))
            .thenReturn(Optional.of(operation));
        when(walletService.getWalletById(wallet.getId()))
            .thenReturn(Optional.of(wallet));
        when(personService.getPersonById(person.getId()))
            .thenReturn(Optional.of(person));
        when(categoryService.getCategoryByIdAndType(category.getId(), TransactionType.INCOME))
            .thenReturn(Optional.of(category));
        when(walletService.getWalletById(wallet.getId()))
            .thenReturn(Optional.of(wallet));
        when(currencyLogService.getFirstAfterDate(operation.getDate()))
            .thenReturn(
                new CurrencyLog()
                    .setUsd(BigDecimal.valueOf(10))
                    .setEur(BigDecimal.valueOf(20))
                    .setGbp(BigDecimal.valueOf(30))
                    .setChf(BigDecimal.valueOf(40))
                    .setJpy(BigDecimal.valueOf(50))
                    .setSek(BigDecimal.valueOf(60))
                    .setDate(operation.getDate())
            );

        // when
        operationService.deleteOperationById(
            operation.getId(),
            wallet.getId(),
            person.getEmail()
        );

        // then
        verify(currencyLogService, times(1)).getFirstAfterDate(any());
        verify(walletService, times(1)).updateWallet(any());
        verify(personService, times(1)).updatePerson(any());
        verify(operationRepository, times(1)).deleteById(operation.getId());
    }

    @Test
    void getAllOperationsByWalletId_test() {
        // given
        var person = new Person()
            .setId(1L)
            .setEmail("some-mail")
            .setBalance(BigDecimal.valueOf(10000))
            .setIncome(BigDecimal.valueOf(10000))
            .setSpendings(BigDecimal.valueOf(0));
        var wallet = new Wallet()
            .setId(1L)
            .setPerson(person)
            .setCurrency(CurrencyType.RUB)
            .setBalance(BigDecimal.valueOf(10000))
            .setIncome(BigDecimal.valueOf(10000))
            .setSpendings(BigDecimal.valueOf(0));
        var category = new Category()
            .setId(1L)
            .setType(TransactionType.INCOME);
        var operation1 = new Operation()
            .setId(1L)
            .setCategory(category)
            .setWallet(wallet)
            .setDate(Instant.now())
            .setBalance(BigDecimal.valueOf(5000))
            .setType(TransactionType.INCOME);
        var operation2 = new Operation()
            .setId(2L)
            .setCategory(category)
            .setWallet(wallet)
            .setDate(Instant.now())
            .setBalance(BigDecimal.valueOf(5000))
            .setType(TransactionType.INCOME);
        wallet.setOperations(List.of(operation1, operation2));
        person.setWallets(List.of(wallet));

        doNothing().when(personService).checkPersonByEmail(any());
        when(operationRepository.findAllByWalletId(wallet.getId()))
            .thenReturn(List.of(operation1, operation2));
        when(walletService.getWalletById(wallet.getId()))
            .thenReturn(Optional.of(wallet));
        when(personService.getPersonById(person.getId()))
            .thenReturn(Optional.of(person));
        when(categoryService.getCategoryByIdAndType(category.getId(), TransactionType.INCOME))
            .thenReturn(Optional.of(category));
        when(walletService.getWalletById(wallet.getId()))
            .thenReturn(Optional.of(wallet));

        var operations = Stream.of(operation1, operation2)
            .map(mapper::map)
            .collect(Collectors.toSet());

        // when
        var result = operationService.getAllOperationsByWalletId(
            wallet.getId(),
            person.getEmail()
        );

        // then
        assertEquals(2, result.size());
        assertThat(operations).usingRecursiveComparison().isEqualTo(new HashSet<>(result));
    }

    @Test
    void getOperationById_test() {
        // given
        var person = new Person()
            .setId(1L)
            .setEmail("some-mail")
            .setBalance(BigDecimal.valueOf(10000))
            .setIncome(BigDecimal.valueOf(10000))
            .setSpendings(BigDecimal.valueOf(0));
        var wallet = new Wallet()
            .setId(1L)
            .setPerson(person)
            .setCurrency(CurrencyType.RUB)
            .setBalance(BigDecimal.valueOf(10000))
            .setIncome(BigDecimal.valueOf(10000))
            .setSpendings(BigDecimal.valueOf(0));
        var category = new Category()
            .setId(1L)
            .setType(TransactionType.INCOME);
        var operation1 = new Operation()
            .setId(1L)
            .setCategory(category)
            .setWallet(wallet)
            .setDate(Instant.now())
            .setBalance(BigDecimal.valueOf(5000))
            .setType(TransactionType.INCOME);
        var operation2 = new Operation()
            .setId(2L)
            .setCategory(category)
            .setWallet(wallet)
            .setDate(Instant.now())
            .setBalance(BigDecimal.valueOf(5000))
            .setType(TransactionType.INCOME);
        wallet.setOperations(List.of(operation1, operation2));
        person.setWallets(List.of(wallet));

        doNothing().when(personService).checkPersonByEmail(any());
        when(operationRepository.findById(operation1.getId()))
            .thenReturn(Optional.of(operation1));
        when(walletService.getWalletById(wallet.getId()))
            .thenReturn(Optional.of(wallet));
        when(personService.getPersonById(person.getId()))
            .thenReturn(Optional.of(person));
        when(categoryService.getCategoryByIdAndType(category.getId(), TransactionType.INCOME))
            .thenReturn(Optional.of(category));
        when(walletService.getWalletById(wallet.getId()))
            .thenReturn(Optional.of(wallet));

        // when
        var result = operationService.getOperationById(
            operation1.getId(),
            person.getEmail()
        );

        // then
        assertThat(operation1).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    void getOperationDtoById_test() {
        // given
        var person = new Person()
            .setId(1L)
            .setEmail("some-mail")
            .setBalance(BigDecimal.valueOf(10000))
            .setIncome(BigDecimal.valueOf(10000))
            .setSpendings(BigDecimal.valueOf(0));
        var wallet = new Wallet()
            .setId(1L)
            .setPerson(person)
            .setCurrency(CurrencyType.RUB)
            .setBalance(BigDecimal.valueOf(10000))
            .setIncome(BigDecimal.valueOf(10000))
            .setSpendings(BigDecimal.valueOf(0));
        var category = new Category()
            .setId(1L)
            .setType(TransactionType.INCOME);
        var operation1 = new Operation()
            .setId(1L)
            .setCategory(category)
            .setWallet(wallet)
            .setDate(Instant.now())
            .setBalance(BigDecimal.valueOf(5000))
            .setType(TransactionType.INCOME);
        var operation2 = new Operation()
            .setId(2L)
            .setCategory(category)
            .setWallet(wallet)
            .setDate(Instant.now())
            .setBalance(BigDecimal.valueOf(5000))
            .setType(TransactionType.INCOME);
        wallet.setOperations(List.of(operation1, operation2));
        person.setWallets(List.of(wallet));

        doNothing().when(personService).checkPersonByEmail(any());
        when(operationRepository.findById(operation1.getId()))
            .thenReturn(Optional.of(operation1));
        when(walletService.getWalletById(wallet.getId()))
            .thenReturn(Optional.of(wallet));
        when(personService.getPersonById(person.getId()))
            .thenReturn(Optional.of(person));
        when(categoryService.getCategoryByIdAndType(category.getId(), TransactionType.INCOME))
            .thenReturn(Optional.of(category));
        when(walletService.getWalletById(wallet.getId()))
            .thenReturn(Optional.of(wallet));

        // when
        var result = operationService.getOperationDtoById(
            operation1.getId(),
            person.getEmail()
        );

        // then
        assertThat(mapper.map(operation1)).usingRecursiveComparison().isEqualTo(result);
    }
}
