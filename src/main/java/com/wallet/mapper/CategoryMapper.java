package com.wallet.mapper;

import com.wallet.config.MapStructConfig;
import com.wallet.model.dto.category.CategoryDto;
import com.wallet.model.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface CategoryMapper {

    @Mapping(target = "personId", ignore = true)
    @Mapping(target = "operations", ignore = true)
    Category map(CategoryDto dto);

    CategoryDto map(Category category);
}
