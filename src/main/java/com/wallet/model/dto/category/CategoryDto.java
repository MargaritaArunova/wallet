package com.wallet.model.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wallet.model.type.TransactionType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Accessors(chain = true)
public class CategoryDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    private String name;

    private TransactionType type;

    @NotBlank
    private String color;
}
