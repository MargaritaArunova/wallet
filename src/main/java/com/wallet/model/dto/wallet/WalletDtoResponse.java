package com.wallet.model.dto.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wallet.model.dto.currency.CurrencyDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Accessors(chain = true)
@Schema(description = "Кошелёк")
public class WalletDtoResponse {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty
    private Integer isHidden;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean limitExceeded;

    @NotBlank
    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private CurrencyDto currencyDto;

    @Min(0)
    private BigDecimal amountLimit;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal balance;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal income;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal spendings;
}
