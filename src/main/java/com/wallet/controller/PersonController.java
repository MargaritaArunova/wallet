package com.wallet.controller;

import com.wallet.model.dto.person.PersonCreateDto;
import com.wallet.model.dto.person.PersonDto;
import com.wallet.service.person.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public PersonDto createUser(@Valid @RequestBody PersonCreateDto personCreateDto) {
        return personService.createPerson(personCreateDto);
    }

    @Operation(summary = "Метод для удаления пользователя")
    @DeleteMapping
    public void deleteUser(@RequestHeader("email") String email) {
        personService.deletePersonByEmail(email);
    }
}
