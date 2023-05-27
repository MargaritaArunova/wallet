package com.wallet.service.person;

import com.wallet.exception.AlreadyExistsException;
import com.wallet.exception.AuthenticationException;
import com.wallet.mapper.PersonMapper;
import com.wallet.model.dto.person.PersonCreateDto;
import com.wallet.model.dto.person.PersonDto;
import com.wallet.model.entity.Person;
import com.wallet.repository.person.PersonRepository;
import com.wallet.service.mail.PersonNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper mapper;

    private final PersonNotificationService notificationService;

    @Transactional
    public PersonDto createPerson(PersonCreateDto personDto) {
        personRepository.findByEmail(personDto.getEmail()).ifPresent(person -> {
            throw new AlreadyExistsException(
                "Person with email = '%s' already exists".formatted(person.getEmail())
            );
        });
        var person = new Person()
            .setEmail(personDto.getEmail())
            .setIncome(new BigDecimal(0))
            .setSpendings(new BigDecimal(0))
            .setBalance(new BigDecimal(0));
        person = personRepository.save(person);

        notificationService.notifyOnPersonCreate(personDto.getEmail());

        return mapper.map(person);
    }

    @Transactional
    public Person updatePerson(Person person) {
        return personRepository.save(person);
    }

    @Transactional
    public void deletePersonByEmail(String email) {
        var person = getPersonByEmail(email);
        personRepository.deleteById(person.getId());
    }

    @Transactional(readOnly = true)
    public Optional<Person> getPersonById(Long id) {
        return personRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Person getPersonByEmail(String email) {
        return personRepository.findByEmail(email).orElseThrow(
            () -> new AuthenticationException(email)
        );
    }

    @Transactional(readOnly = true)
    public PersonDto getPersonDtoByEmail(String email) {
        return mapper.map(getPersonByEmail(email));
    }

    @Transactional(readOnly = true)
    public void checkPersonByEmail(String email) {
        personRepository.findByEmail(email).orElseThrow(
            () -> new AuthenticationException(email)
        );
    }

}
