package com.javaapp;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PersonRepository implements PanacheRepository<Person> {
    public boolean existsById(Long id) {
        return find("id", id).firstResult() != null;
    }
}
