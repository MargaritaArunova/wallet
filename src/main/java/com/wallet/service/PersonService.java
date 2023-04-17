package com.wallet.service;

import com.wallet.exception.AuthenticationException;
import com.wallet.mapper.PersonMapper;
import com.wallet.model.dto.PersonDto;
import com.wallet.model.entity.Person;
import com.wallet.repository.PersonRepository;
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
        return mapper.map(personRepository.findByEmail(email).orElseThrow(
                () -> new AuthenticationException(email)
        ));
    }

    @Transactional(readOnly = true)
    public void checkPersonByEmail(String email) {
        personRepository.findByEmail(email).orElseThrow(
                () -> new AuthenticationException(email)
        );
    }

    @Transactional
    public Person updatePerson(Person person) {
        return personRepository.save(person);
    }

    @Transactional
    public PersonDto createPerson(PersonDto personDto) {
        var checkPerson = personRepository.findByEmail(personDto.getEmail());
        if (checkPerson.isPresent()) {
            return mapper.map(checkPerson.get());
        }
        var person = mapper.map(personDto);
        person.setIncome(new BigDecimal(0));
        person.setSpendings(new BigDecimal(0));
        person.setBalance(new BigDecimal(0));
        return mapper.map(personRepository.save(person));
    }

    @Transactional
    public void deletePersonByEmail(String email) {
        var person = getPersonByEmail(email);
        personRepository.deleteById(person.getId());
    }

}
