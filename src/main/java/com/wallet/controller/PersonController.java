package com.wallet.controller;

import com.wallet.model.dto.PersonDto;
import com.wallet.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/person")
@RestController
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @Operation(summary = "Метод для получения пользователя")
    @GetMapping
    public PersonDto getUser(@RequestHeader("email") String email) {
        return personService.getPersonDtoByEmail(email);
    }

    @Operation(summary = "Метод для создания пользователя",
            description = "Для создания пользователя нужно передать существующую почту")
    @PostMapping
    public PersonDto createUser(@Valid @RequestBody PersonDto userDto) {
        return personService.createPerson(userDto);
    }

    @Operation(summary = "Метод для удаления пользователя")
    @DeleteMapping
    public void deleteUser(@RequestHeader("email") String email) {
        personService.deletePersonByEmail(email);
    }
}
