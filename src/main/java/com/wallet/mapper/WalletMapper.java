package com.wallet.mapper;

import com.wallet.config.MapStructConfig;
import com.wallet.model.dto.CurrencyDto;
import com.wallet.model.dto.WalletDto;
import com.wallet.model.dto.WalletDtoResponse;
import com.wallet.model.entity.Wallet;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;

@Mapper(config = MapStructConfig.class)
public interface WalletMapper {

    @Mapping(target = "person", ignore = true)
    @Mapping(target = "operations", ignore = true)
    Wallet map(WalletDto dto);

    @Mapping(target = "limitExceeded", ignore = true)
    WalletDto map(Wallet wallet);

    @Mapping(target = "limitExceeded",
            expression = "java(wallet.getSpendings().compareTo(wallet.getAmountLimit()) >= 0)")
    WalletDtoResponse mapResponse(Wallet wallet, CurrencyDto currencyDto);

    @AfterMapping
    default void fillLimitExceeded(@MappingTarget WalletDto dto, Wallet wallet) {
        if (wallet.getAmountLimit().equals(BigDecimal.ZERO)) {
            dto.setLimitExceeded(false);
            return;
        }
        dto.setLimitExceeded(wallet.getSpendings().compareTo(wallet.getAmountLimit()) >= 0);
    }
}
