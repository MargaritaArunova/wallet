package com.wallet.service.wallet;

import com.wallet.exception.NotFoundException;
import com.wallet.mapper.CurrencyMapper;
import com.wallet.mapper.WalletMapper;
import com.wallet.model.dto.wallet.WalletCreateDto;
import com.wallet.model.dto.wallet.WalletDto;
import com.wallet.model.dto.wallet.WalletDtoResponse;
import com.wallet.model.entity.Wallet;
import com.wallet.repository.wallet.WalletRepository;
import com.wallet.service.currency.CurrencyService;
import com.wallet.service.person.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper mapper;

    private final PersonService personService;
    private final CurrencyService currencyService;
    private final CurrencyMapper currencyMapper;

    @Transactional
    public WalletDto createWallet(WalletCreateDto walletDto, String email) {
        var person = personService.getPersonByEmail(email);
        var wallet = mapper.map(walletDto)
            .setPerson(person)
            .setIncome(new BigDecimal(0))
            .setBalance(new BigDecimal(0))
            .setSpendings(new BigDecimal(0));
        return mapper.map(walletRepository.save(wallet));
    }

    @Transactional
    public Wallet updateWallet(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @Transactional
    public WalletDto updateWallet(Long id, String email, WalletDto walletDto) {
        var person = personService.getPersonByEmail(email);
        var wallet = walletRepository.findByIdAndPersonId(id, person.getId())
            .orElseThrow(() -> new NotFoundException(Wallet.class, id));

        wallet.setAmountLimit(walletDto.getAmountLimit())
            .setIsHidden(walletDto.getIsHidden())
            .setName(walletDto.getName());
        return mapper.map(walletRepository.save(wallet));
    }

    @Transactional
    public void deleteWallet(Long id, String email) {
        var person = personService.getPersonByEmail(email);

        if (walletRepository.existsWalletByIdAndPersonId(id, person.getId())) {
            walletRepository.deleteByIdAndPersonId(id, person.getId());
        } else {
            throw new NotFoundException(Wallet.class, id);
        }
    }

    @Transactional(readOnly = true)
    public Optional<Wallet> getWalletById(Long id) {
        return walletRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public WalletDto getWalletByIdAndPersonId(Long id, String email) {
        var person = personService.getPersonByEmail(email);

        return walletRepository.findByIdAndPersonId(id, person.getId())
                .map(mapper::map)
                .orElseThrow(() -> new NotFoundException(Wallet.class, id));
    }

    @Transactional(readOnly = true)
    public List<WalletDtoResponse> getWalletsByPerson(String email) {
        var person = personService.getPersonByEmail(email);
        var wallets = walletRepository.findAllByPersonId(person.getId());

        List<WalletDtoResponse> walletDtoResponses = new ArrayList<>();
        for (Wallet wallet : wallets) {
            var currency = currencyService.getCurrencyByCode(wallet.getCurrency().toString());
            walletDtoResponses.add(mapper.mapResponse(wallet, currencyMapper.map(currency)));
        }
        return walletDtoResponses;
    }
}
