package com.javaapp.Service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import com.javaapp.Person;
import com.javaapp.PersonRepository;
import com.javaapp.exception.DuplicateException;
import com.javaapp.exception.InvalidException;
import com.javaapp.exception.NotFoundException;

import io.opentelemetry.instrumentation.annotations.WithSpan;

import org.jboss.logging.Logger;

@ApplicationScoped
public class PersonService {

    public static final Logger logger = Logger.getLogger(PersonService.class);

    @Inject
    PersonRepository personRepository;

 
    public List<Person> getAllPersonsFn() {
        logger.info("Fetching all persons");
        return personRepository.listAll();
    }

    @WithSpan
    public Person getPersonByIdFn(Long id) {
        logger.info("Fetching person with ID" + id);
        Person person = personRepository.findById(id);
        if (person == null) {
            logger.warn("Person with ID");
            throw new NotFoundException("Person not found");
        }
        return person;
    }

  
    @Transactional
    @WithSpan
    public Person createPersonFn(Person person) {
        validatePerson(person);
        if (personRepository.find("username", person.username).firstResult() != null) {
            logger.error("Username is already taken");
            throw new DuplicateException("Username is already taken");
        }
        if (personRepository.find("email", person.email).firstResult() != null) {
            logger.error("Email is already taken");
            throw new DuplicateException("Email is already taken");
        }
        personRepository.persist(person);
        logger.info("Person created");
        return person;
    }

    
    @Transactional
    @WithSpan
    public Person updatePersonFn(Long id, Person person) {
        validatePerson(person);
        Person existingPerson = personRepository.findById(id);
        if (existingPerson == null) {
            logger.warn("Person with ID not found");
            throw new NotFoundException("Person not found");
        }
        existingPerson.username = person.username;
        existingPerson.email = person.email;
        existingPerson.firstName = person.firstName;
        existingPerson.lastName = person.lastName;
        personRepository.persist(existingPerson);
        logger.info("Person updated");
        return existingPerson;
    }


    @Transactional
    @WithSpan
    public void deletePersonFn(Long id) {
        Person person = personRepository.findById(id);
        if (person == null) {
            logger.warn("Person with ID not found");
            throw new NotFoundException("Person not found");
        }
        personRepository.delete(person);
        logger.info("Person deleted with ID" + id);
    }

    private void validatePerson(Person person) {
        if (person.username == null || person.username.trim().isEmpty()) {
            logger.error("Username cannot be blank");
            throw new InvalidException("Username cannot be blank");
        }
        if (person.email == null || person.email.trim().isEmpty()) {
            logger.error("Email cannot be blank");
            throw new InvalidException("Email cannot be blank");
        }
        if (!person.email.contains("@") || !person.email.endsWith(".com")) {
            logger.error("Invalid email format");
            throw new InvalidException("Email should be valid");
        }
    }
}