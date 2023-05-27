package com.wallet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wallet.exception.NotFoundException;
import com.wallet.mapper.CurrencyMapperImpl;
import com.wallet.mapper.WalletMapperImpl;
import com.wallet.model.dto.wallet.WalletCreateDto;
import com.wallet.model.dto.wallet.WalletDto;
import com.wallet.model.entity.Person;
import com.wallet.model.entity.Wallet;
import com.wallet.model.type.CurrencyType;
import com.wallet.repository.wallet.WalletRepository;
import com.wallet.service.currency.CurrencyService;
import com.wallet.service.person.PersonService;
import com.wallet.service.wallet.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = WalletService.class)
public class WalletServiceTest {

    private WalletService walletService;

    @SpyBean
    private WalletMapperImpl mapper;

    @SpyBean
    private CurrencyMapperImpl currencyMapper;

    @MockBean
    private WalletRepository walletRepository;

    @MockBean
    private PersonService personService;

    @MockBean
    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        walletService = new WalletService(
            walletRepository,
            mapper,
            personService,
            currencyService,
            currencyMapper
        );
    }

    @Test
    void createWallet_test() {
        // given
        var person = new Person()
            .setId(1L)
            .setEmail("some-mail")
            .setBalance(BigDecimal.valueOf(10000))
            .setIncome(BigDecimal.valueOf(10000))
            .setSpendings(BigDecimal.valueOf(0));
        var walletDto = new WalletCreateDto()
            .setName("wallet")
            .setCurrency(CurrencyType.RUB)
            .setLimitExceeded(true)
            .setAmountLimit(BigDecimal.valueOf(1000))
            .setIsHidden(0);

        when(personService.getPersonByEmail(person.getEmail()))
            .thenReturn(person);

        // when
        walletService.createWallet(walletDto, person.getEmail());

        // then
        verify(walletRepository, times(1)).save(any());
    }

    @Test
    void updateWallet_test() {
        // given
        // In setUp

        // when
        walletService.updateWallet(any(Wallet.class));

        // then
        verify(walletRepository, times(1)).save(any());
    }

    @Test
    void updateWallet_byDtoTest() {
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
            .setName("wallet")
            .setCurrency(CurrencyType.RUB)
            .setBalance(BigDecimal.valueOf(1000))
            .setIncome(BigDecimal.valueOf(1500))
            .setSpendings(BigDecimal.valueOf(500))
            .setAmountLimit(BigDecimal.valueOf(1000))
            .setIsHidden(0);
        person.setWallets(List.of(wallet));

        var updateWalletDto = new WalletDto()
            .setName("wallet-1")
            .setAmountLimit(BigDecimal.valueOf(10000))
            .setIsHidden(1);

        when(personService.getPersonByEmail(person.getEmail()))
            .thenReturn(person);
        when(walletRepository.findByIdAndPersonId(wallet.getId(), person.getId()))
            .thenReturn(Optional.of(wallet));

        // when
        walletService.updateWallet(wallet.getId(), person.getEmail(), updateWalletDto);

        // then
        verify(walletRepository, times(1)).save(
            wallet
                .setName("wallet-1")
                .setAmountLimit(BigDecimal.valueOf(10000))
                .setIsHidden(0)
        );
    }

    @Test
    void deleteWallet_walletExistsTest() {
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
            .setName("wallet")
            .setCurrency(CurrencyType.RUB)
            .setBalance(BigDecimal.valueOf(1000))
            .setIncome(BigDecimal.valueOf(1500))
            .setSpendings(BigDecimal.valueOf(500))
            .setAmountLimit(BigDecimal.valueOf(1000))
            .setIsHidden(0);
        person.setWallets(List.of(wallet));

        when(personService.getPersonByEmail(person.getEmail()))
            .thenReturn(person);
        when(walletRepository.existsWalletByIdAndPersonId(wallet.getId(), person.getId()))
            .thenReturn(true);

        // when
        walletService.deleteWallet(wallet.getId(), person.getEmail());

        // then
        verify(walletRepository, times(1)).deleteByIdAndPersonId(
            wallet.getId(), person.getId()
        );
    }

    @Test
    void deleteWallet_walletDoesNotExistTest() {
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
            .setName("wallet")
            .setCurrency(CurrencyType.RUB)
            .setBalance(BigDecimal.valueOf(1000))
            .setIncome(BigDecimal.valueOf(1500))
            .setSpendings(BigDecimal.valueOf(500))
            .setAmountLimit(BigDecimal.valueOf(1000))
            .setIsHidden(0);
        person.setWallets(List.of(wallet));

        when(personService.getPersonByEmail(person.getEmail()))
            .thenReturn(person);
        when(walletRepository.existsWalletByIdAndPersonId(wallet.getId(), person.getId()))
            .thenReturn(false);

        // then
        assertThrows(NotFoundException.class,
            () -> walletService.deleteWallet(wallet.getId(), person.getEmail())
        );
    }

    @Test
    void getWalletById_test() {
        // given
        // In setUp

        // when
        walletService.getWalletById(any());

        // then
        verify(walletRepository, times(1)).findById(any());
    }

    @Test
    void getWalletByIdAndPersonId_walletDoesNotExistTest() {
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
            .setName("wallet")
            .setCurrency(CurrencyType.RUB)
            .setBalance(BigDecimal.valueOf(1000))
            .setIncome(BigDecimal.valueOf(1500))
            .setSpendings(BigDecimal.valueOf(500))
            .setAmountLimit(BigDecimal.valueOf(1000))
            .setIsHidden(0);
        person.setWallets(List.of(wallet));

        when(personService.getPersonByEmail(person.getEmail()))
            .thenReturn(person);
        when(walletRepository.findByIdAndPersonId(wallet.getId(), person.getId()))
            .thenReturn(Optional.of(wallet));

        // when
        var result = walletService.getWalletByIdAndPersonId(wallet.getId(), person.getEmail());

        // then
        assertEquals(mapper.map(wallet), result);
    }

    @Test
    void getWalletsByPerson_walletDoesNotExistTest() {
        // given
        var person = new Person()
            .setId(1L)
            .setEmail("some-mail")
            .setBalance(BigDecimal.valueOf(10000))
            .setIncome(BigDecimal.valueOf(10000))
            .setSpendings(BigDecimal.valueOf(0));
        var wallet1 = new Wallet()
            .setId(1L)
            .setPerson(person)
            .setName("wallet1")
            .setCurrency(CurrencyType.RUB)
            .setBalance(BigDecimal.valueOf(1000))
            .setIncome(BigDecimal.valueOf(1500))
            .setSpendings(BigDecimal.valueOf(500))
            .setAmountLimit(BigDecimal.valueOf(1000))
            .setIsHidden(0);
        var wallet2 = new Wallet()
            .setId(2L)
            .setPerson(person)
            .setName("wallet2")
            .setCurrency(CurrencyType.RUB)
            .setBalance(BigDecimal.valueOf(1000))
            .setIncome(BigDecimal.valueOf(1500))
            .setSpendings(BigDecimal.valueOf(500))
            .setAmountLimit(BigDecimal.valueOf(1000))
            .setIsHidden(0);
        person.setWallets(List.of(wallet1, wallet2));

        when(personService.getPersonByEmail(person.getEmail()))
            .thenReturn(person);
        when(walletRepository.findAllByPersonId(person.getId()))
            .thenReturn(person.getWallets());

        // when
        var result = walletService.getWalletsByPerson(person.getEmail());

        // then
        verify(currencyService, times(2)).getCurrencyByCode(any());
        assertEquals(2, result.size());
    }
}
