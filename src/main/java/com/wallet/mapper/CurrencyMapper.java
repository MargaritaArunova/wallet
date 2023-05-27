package com.wallet.mapper;

import com.wallet.config.MapStructConfig;
import com.wallet.model.dto.currency.CurrencyDto;
import com.wallet.model.entity.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface CurrencyMapper {

    @Mapping(target = "ascending", ignore = true)
    CurrencyDto map(Currency currency);
}
