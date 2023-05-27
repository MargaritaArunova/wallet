package com.wallet.model.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Schema
@AllArgsConstructor
public class ErrorResponseDto {

    private String code;

    @NotNull
    private String message;
}