package com.wallet.model.dto.person;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Accessors(chain = true)
@Schema(description = "Создание пользователя")
public class PersonCreateDto {

    @NotBlank
    private String email;
}
