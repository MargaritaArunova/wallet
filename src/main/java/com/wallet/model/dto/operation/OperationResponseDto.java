package com.wallet.model.dto.operation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wallet.model.dto.category.CategoryDto;
import com.wallet.model.type.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Accessors(chain = true)
@Schema(description = "Операция")
public class OperationResponseDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long walletId;

    private TransactionType type;

    @Schema(description = "Категория операции")
    private CategoryDto categoryDto;

    private BigDecimal balance;

    private Instant date;
}

