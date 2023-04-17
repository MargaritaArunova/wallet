package com.wallet.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wallet.model.type.CurrencyType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@Accessors(chain = true)
@Schema(description = "Кошелёк")
public class WalletDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty
    private Integer isHidden;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean limitExceeded;

    @NotBlank
    private String name;

    private CurrencyType currency;

    @Min(0)
    private BigDecimal amountLimit;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal balance;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal income;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal spendings;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WalletDto walletDto = (WalletDto) o;
        return name.equals(walletDto.name) && currency == walletDto.currency && amountLimit.equals(walletDto.amountLimit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, currency, amountLimit);
    }
}
