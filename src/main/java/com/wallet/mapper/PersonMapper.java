package com.wallet.mapper;

import com.wallet.config.MapStructConfig;
import com.wallet.model.dto.PersonDto;
import com.wallet.model.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface PersonMapper {

    @Mapping(target = "wallets", ignore = true)
    Person map(PersonDto dto);

    PersonDto map(Person person);
}
