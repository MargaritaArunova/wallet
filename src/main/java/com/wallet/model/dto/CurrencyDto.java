package com.wallet.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Setter
@Accessors(chain = true)
public class CurrencyDto {

    private String code;

    private String symbol;

    private String fullDescription;

    private String shortDescription;

    private BigDecimal value;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean ascending;
}
