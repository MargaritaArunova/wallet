package com.wallet.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wallet.exception.AlreadyExistsException;
import com.wallet.exception.AuthenticationException;
import com.wallet.mapper.PersonMapperImpl;
import com.wallet.model.dto.person.PersonCreateDto;
import com.wallet.model.entity.Person;
import com.wallet.repository.person.PersonRepository;
import com.wallet.service.mail.PersonNotificationService;
import com.wallet.service.person.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Optional;

@SpringBootTest(classes = PersonService.class)
public class PersonServiceTest {

    private PersonService personService;

    @SpyBean
    private PersonMapperImpl mapper;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private PersonNotificationService notificationService;

    @BeforeEach
    void setUp() {
        personService = new PersonService(
            personRepository,
            mapper,
            notificationService
        );
    }

    @Test
    void createPerson_newPersonTest() {
        // given
        var personDto = new PersonCreateDto()
            .setEmail("some-email");

        when(personRepository.findByEmail(personDto.getEmail()))
            .thenReturn(Optional.empty());

        // when
        personService.createPerson(personDto);

        // then
        verify(personRepository, times(1)).save(any());
        verify(notificationService, times(1)).notifyOnPersonCreate(personDto.getEmail());
    }

    @Test
    void createPerson_personAlreadyExistsTest() {
        // given
        var personDto = new PersonCreateDto()
            .setEmail("some-email");

        when(personRepository.findByEmail(personDto.getEmail()))
            .thenReturn(Optional.of(new Person()));

        // then
        assertThrows(AlreadyExistsException.class,
            () -> personService.createPerson(personDto)
        );
    }

    @Test
    void updatePerson_test() {
        // given
        var person = new Person()
            .setId(1L)
            .setEmail("some-email");

        // when
        personService.updatePerson(person);

        // then
        verify(personRepository, times(1)).save(person);
    }

    @Test
    void deletePersonByEmail_test() {
        // given
        var person = new Person()
            .setId(1L)
            .setEmail("some-email");
        when(personRepository.findByEmail(person.getEmail()))
            .thenReturn(Optional.of(person));


        // when
        personService.deletePersonByEmail(person.getEmail());

        // then
        verify(personRepository, times(1)).deleteById(person.getId());
    }

    @Test
    void getPersonById_test() {
        // given
        // In setUp


        // when
        personService.getPersonById(1L);

        // then
        verify(personRepository, times(1)).findById(1L);
    }

    @Test
    void getPersonByEmail_personExistsTest() {
        // given
        var person = new Person()
            .setId(1L)
            .setEmail("some-email");
        when(personRepository.findByEmail(person.getEmail()))
            .thenReturn(Optional.of(person));

        // when
        personService.getPersonByEmail(person.getEmail());

        // then
        verify(personRepository, times(1)).findByEmail(person.getEmail());
    }

    @Test
    void getPersonByEmail_personDoesNotExistTest() {
        // given
        when(personRepository.findByEmail(any()))
            .thenReturn(Optional.empty());

        // then
        assertThrows(AuthenticationException.class,
            () -> personService.getPersonByEmail(any())
        );
    }

    @Test
    void getPersonDtoByEmail_test() {
        // given
        var person = new Person()
            .setId(1L)
            .setEmail("some-email");
        when(personRepository.findByEmail(person.getEmail()))
            .thenReturn(Optional.of(person));

        // when
        personService.getPersonDtoByEmail(person.getEmail());

        // then
        verify(personRepository, times(1)).findByEmail(person.getEmail());
    }

    @Test
    void checkPersonByEmail_personExistsTest() {
        // given
        var person = new Person()
            .setId(1L)
            .setEmail("some-email");
        when(personRepository.findByEmail(person.getEmail()))
            .thenReturn(Optional.of(person));

        // when
        personService.checkPersonByEmail(person.getEmail());

        // then
        verify(personRepository, times(1)).findByEmail(person.getEmail());
    }

    @Test
    void checkPersonByEmail_personDoesNotExistTest() {
        // given
        when(personRepository.findByEmail(any()))
            .thenReturn(Optional.empty());

        // then
        assertThrows(AuthenticationException.class,
            () -> personService.checkPersonByEmail(any())
        );
    }
}
