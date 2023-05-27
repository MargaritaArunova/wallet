package com.wallet.model.dto.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wallet.model.type.CurrencyType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Accessors(chain = true)
@Schema(description = "Создание кошелька")
public class WalletCreateDto {

    @JsonProperty
    private Integer isHidden;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean limitExceeded;

    @NotBlank
    private String name;

    private CurrencyType currency;

    @Min(0)
    private BigDecimal amountLimit;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WalletCreateDto walletDto = (WalletCreateDto) o;
        return name.equals(walletDto.name)
            && currency == walletDto.currency
            && amountLimit.equals(walletDto.amountLimit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, currency, amountLimit);
    }
}
