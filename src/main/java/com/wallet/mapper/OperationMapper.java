package com.wallet.mapper;

import com.wallet.config.MapStructConfig;
import com.wallet.model.dto.operation.OperationRequestDto;
import com.wallet.model.dto.operation.OperationResponseDto;
import com.wallet.model.entity.Operation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = CategoryMapper.class)
public interface OperationMapper {

    @Mapping(target = "wallet", ignore = true)
    @Mapping(target = "category", ignore = true)
    Operation map(OperationRequestDto dto);

    @Mapping(target = "walletId", source = "wallet.id")
    @Mapping(target = "categoryDto", source = "category")
    OperationResponseDto map(Operation operation);
}
