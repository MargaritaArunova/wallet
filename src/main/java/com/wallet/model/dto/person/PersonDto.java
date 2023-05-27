package com.wallet.model.dto.person;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Accessors(chain = true)
@Schema(description = "Пользователь")
public class PersonDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    private String email;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal balance;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal income;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal spendings;
}
